/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.netbeans.modules.git.remote.ui.history;

import java.awt.Component;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import org.netbeans.api.diff.DiffController;
import org.netbeans.modules.git.remote.cli.GitException;
import org.netbeans.modules.git.remote.client.GitProgressSupport;
import org.netbeans.modules.git.remote.ui.diff.DiffStreamSource;
import org.netbeans.modules.git.remote.utils.GitUtils;
import org.netbeans.modules.versioning.core.api.VCSFileProxy;
import org.netbeans.modules.versioning.util.NoContentPanel;
import org.openide.ErrorManager;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.WeakListeners;

/**
 * Shows Search History results in a table with Diff pane below it.
 * 
 */
class DiffResultsView implements AncestorListener, PropertyChangeListener {

    protected final SearchHistoryPanel parent;

    protected DiffTreeTable treeView;
    private final JSplitPane    diffView;
    
    protected GitProgressSupport            currentTask;
    
    protected DiffController            currentDiff;
    private int                     currentDifferenceIndex;
    protected int                     currentIndex;
    private boolean                 dividerSet;
    protected List<RepositoryRevision> results;
    private static final RequestProcessor rp = new RequestProcessor("GitRemoteDiff", 1, true);  // NOI18N
    protected static final Logger LOG = Logger.getLogger(DiffResultsView.class.getName());
    private final PropertyChangeListener list;
    private Node[] selectedNodes;
    private final Set<RepositoryRevision> revisionsToRefresh = new HashSet<>(2);
    private int lastDividerLoc;

    public DiffResultsView (SearchHistoryPanel parent, List<RepositoryRevision> results) {
        this.parent = parent;
        this.results = results;
        treeView = new DiffTreeTable(parent);
        treeView.setResults(results);
        treeView.addAncestorListener(this);

        diffView = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        diffView.setTopComponent(treeView);
        setBottomComponent(new NoContentPanel(NbBundle.getMessage(DiffResultsView.class, "MSG_DiffPanel_NoRevisions"))); // NOI18N
        list = WeakListeners.propertyChange(this, null);
    }

    @Override
    public void ancestorAdded(AncestorEvent event) {
        ExplorerManager em = ExplorerManager.find(treeView);
        em.addPropertyChangeListener(this);
        if (dividerSet) {
            if (lastDividerLoc != 0) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run () {
                        diffView.setDividerLocation(lastDividerLoc);
                    }
                });
            }
        } else {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    dividerSet = true;
                    diffView.setDividerLocation(0.33);
                }
            });
        }
    }

    @Override
    public void ancestorMoved(AncestorEvent event) {
    }

    @Override
    public void ancestorRemoved(AncestorEvent event) {
        if (dividerSet) {
            lastDividerLoc = diffView.getDividerLocation();
        }
        ExplorerManager em = ExplorerManager.find(treeView);
        em.removePropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (ExplorerManager.PROP_SELECTED_NODES.equals(evt.getPropertyName())) {
            assert EventQueue.isDispatchThread();
            selectedNodes = (Node[]) evt.getNewValue();
            currentDifferenceIndex = 0;
            if (selectedNodes.length == 0) {
                showDiffError(NbBundle.getMessage(DiffResultsView.class, "MSG_DiffPanel_NoRevisions")); // NOI18N
                parent.refreshComponents(false);
                return;
            }
            else if (selectedNodes.length > 2) {
                showDiffError(NbBundle.getMessage(DiffResultsView.class, "MSG_DiffPanel_TooManyRevisions")); // NOI18N
                parent.refreshComponents(false);
                return;
            }
            revisionsToRefresh.clear();

            // invoked asynchronously becase treeView.getSelection() may not be ready yet
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    showDiff();
                }
            };
            EventQueue.invokeLater(runnable);
        } else if (RepositoryRevision.PROP_EVENTS_CHANGED.equals(evt.getPropertyName())) {
            if (evt.getSource() instanceof RepositoryRevision) {
                RepositoryRevision revision = (RepositoryRevision) evt.getSource();
                revision.removePropertyChangeListener(RepositoryRevision.PROP_EVENTS_CHANGED, list);
                if (revisionsToRefresh.contains(revision) && selectedNodes != null && selectedNodes.length > 0) {
                    showDiff();
                }
            }
        }
    }
    
    private void showDiff () {
        RepositoryRevision container1 = selectedNodes[0].getLookup().lookup(RepositoryRevision.class);
        RepositoryRevision.Event r1 = selectedNodes[0].getLookup().lookup(RepositoryRevision.Event.class);
        boolean error = false;
        boolean loading = false;
        try {
            currentIndex = treeView.getSelection()[0];
            if (selectedNodes.length == 1) {
                if (container1 != null) {
                    container1.removePropertyChangeListener(RepositoryRevision.PROP_EVENTS_CHANGED, list);
                    container1.addPropertyChangeListener(RepositoryRevision.PROP_EVENTS_CHANGED, list);
                    if (container1.expandEvents()) {
                        revisionsToRefresh.add(container1);
                        loading = true;
                    } else {
                        container1.removePropertyChangeListener(RepositoryRevision.PROP_EVENTS_CHANGED, list);
                    }
                    if (showContainerDiff(container1, onSelectionshowLastDifference)) {
                        loading = false;
                    }
                } else if (r1 != null) {
                    showRevisionDiff(r1, onSelectionshowLastDifference);
                }
            } else if (selectedNodes.length == 2) {
                RepositoryRevision.Event revOlder = null;
                if (container1 != null) {
                    /**
                        * both repository revision events must be acquired from a container, not through a Lookup as before,
                        * since only two containers (and no rev-event) are present in the lookup
                        */
                    RepositoryRevision container2 = selectedNodes[1].getLookup().lookup(RepositoryRevision.class);
                    if (container2 == null) {
                        error = true;
                    } else {
                        container1.removePropertyChangeListener(RepositoryRevision.PROP_EVENTS_CHANGED, list);
                        container1.addPropertyChangeListener(RepositoryRevision.PROP_EVENTS_CHANGED, list);
                        container2.removePropertyChangeListener(RepositoryRevision.PROP_EVENTS_CHANGED, list);
                        container2.addPropertyChangeListener(RepositoryRevision.PROP_EVENTS_CHANGED, list);
                        if (container1.expandEvents() || container2.expandEvents()) {
                            loading = true;
                            revisionsToRefresh.add(container1);
                            revisionsToRefresh.add(container2);
                        } else {
                            container1.removePropertyChangeListener(RepositoryRevision.PROP_EVENTS_CHANGED, list);
                            container2.removePropertyChangeListener(RepositoryRevision.PROP_EVENTS_CHANGED, list);
                        }
                        r1 = getEventForRoots(container1, null);
                        revOlder = getEventForRoots(container2, r1 == null ? null : r1.getFile());
                        if (r1 != null && revOlder != null) {
                            loading = false;
                        }
                    }
                } else {
                    revOlder = (RepositoryRevision.Event) selectedNodes[1].getLookup().lookup(RepositoryRevision.Event.class);
                }
                if (r1 == null || revOlder == null || revOlder.getFile() == null) {
                    error = true;
                } else {
                    showDiff(r1.getLogInfoHeader().getRepositoryRoot(), revOlder, r1, false);
                }
            }
        } catch (Exception e) {
            error = true;
        }
        
        if (loading) {
            showDiffError(NbBundle.getMessage(DiffResultsView.class, "MSG_DiffPanel_LoadingDiff")); //NOI18N
            parent.refreshComponents(false);
        } else if (error) {
            showDiffError(NbBundle.getMessage(DiffResultsView.class, "MSG_DiffPanel_IllegalSelection")); // NOI18N
            parent.refreshComponents(false);
        } else {
            revisionsToRefresh.clear();
        }
    }

    protected void showDiffError (final String s) {
        Runnable inAWT = new Runnable() {
            @Override
            public void run() {
                setBottomComponent(new NoContentPanel(s));
            }
        };
        if (EventQueue.isDispatchThread()) {
            inAWT.run();
        } else {
            EventQueue.invokeLater(inAWT);
        }
    }

    protected final void setBottomComponent(Component component) {
        assert EventQueue.isDispatchThread();
        int dl = diffView.getDividerLocation();
        diffView.setBottomComponent(component);
        diffView.setDividerLocation(dl);
    }

    protected GitProgressSupport createShowDiffTask(RepositoryRevision.Event revision1, RepositoryRevision.Event revision2, boolean showLastDifference) {
        return new ShowDiffTask(revision1, revision2, showLastDifference);
    }

    protected void showDiff (VCSFileProxy repositoryRoot, RepositoryRevision.Event revision1, RepositoryRevision.Event revision2, boolean showLastDifference) {
        synchronized(this) {
            cancelBackgroundTasks();
            currentTask = createShowDiffTask(revision1, revision2, showLastDifference);
            currentTask.start(rp, repositoryRoot, NbBundle.getMessage(DiffResultsView.class, "LBL_SearchHistory_Diffing")); //NOI18N
        }
    }

    synchronized void cancelBackgroundTasks () {
        if (currentTask != null) {
            currentTask.cancel();
        }
    }

    private boolean onSelectionshowLastDifference;

    protected void setDiffIndex(int idx, boolean showLastDifference) {
        currentIndex = idx;
        onSelectionshowLastDifference = showLastDifference;
        treeView.setSelection(idx);
    }

    protected void showRevisionDiff(RepositoryRevision.Event rev, boolean showLastDifference) {
        if (rev.getFile() == null) {
            return;
        }
        showDiff(rev.getLogInfoHeader().getRepositoryRoot(), null, rev, showLastDifference);
    }

    protected boolean showContainerDiff(RepositoryRevision container, boolean showLastDifference) {
        boolean initialized = container.isEventsInitialized();
        RepositoryRevision.Event[] revs = container.getEvents();
        
        RepositoryRevision.Event newest = getEventForRoots(container, null);
        if(newest == null) {
            newest = revs[0];   
        }
        if (newest == null && !initialized) {
            return false;
        } else {
            showRevisionDiff(newest, showLastDifference);
            return true;
        }
    }

    private RepositoryRevision.Event getEventForRoots (RepositoryRevision container, VCSFileProxy preferedFile) {
        RepositoryRevision.Event event = null;
        RepositoryRevision.Event[] revs;
        if (container.isEventsInitialized()) {
            revs = container.getEvents();
        } else {
            revs = container.getDummyEvents();
        }

        //try to get the root
        VCSFileProxy[] roots = parent.getRoots();
        outer:
        for(RepositoryRevision.Event evt : revs) {
            if (preferedFile == null) {
                for(VCSFileProxy root : roots) {
                    if (root.equals(evt.getFile())) {
                        event = evt;
                        break outer;
                    } else if (similarPaths(root, evt.getFile())) {
                        event = evt;
                    }
                }
            } else {
                if (preferedFile.equals(evt.getFile())) {
                    event = evt;
                    break;
                } else if (similarPaths(preferedFile, evt.getFile())) {
                    event = evt;
                }
            }
        }

        return event;
    }
    
    void onNextButton() {
        if (currentDiff != null) {
            if (++currentDifferenceIndex >= currentDiff.getDifferenceCount()) {
                if (++currentIndex >= treeView.getRowCount()) {
                    currentIndex = 0;
                }
                setDiffIndex(currentIndex, false);
            } else {
                currentDiff.setLocation(DiffController.DiffPane.Modified, DiffController.LocationType.DifferenceIndex, currentDifferenceIndex);
                parent.updateActions();
            }
        } else {
            if (++currentIndex >= treeView.getRowCount()) {
                currentIndex = 0;
            }
            setDiffIndex(currentIndex, false);
        }
    }

    void onPrevButton() {
        if (currentDiff != null) {
            if (--currentDifferenceIndex < 0) {
                if (--currentIndex < 0) {
                    currentIndex = treeView.getRowCount() - 1;
                }
                setDiffIndex(currentIndex, true);
            } else if (currentDifferenceIndex < currentDiff.getDifferenceCount()) {
                currentDiff.setLocation(DiffController.DiffPane.Modified, DiffController.LocationType.DifferenceIndex, currentDifferenceIndex);
                parent.updateActions();
            }
        } else {
            if (--currentIndex < 0) {
                currentIndex = treeView.getRowCount() - 1;
            }
            setDiffIndex(currentIndex, true);
        }
    }

    boolean isNextEnabled() {
        if (currentDiff != null) {
            return currentIndex < treeView.getRowCount() - 1 || currentDifferenceIndex < currentDiff.getDifferenceCount() - 1;
        } else {
            return false;
        }
    }

    boolean isPrevEnabled() {
        return currentIndex > 0 || currentDifferenceIndex > 0;
    }
    
    /**
     * Selects given revision in the view as if done by the user.
     *
     * @param events revision to select
     */
    void select (RepositoryRevision.Event... events) {
        treeView.requestFocusInWindow();
        treeView.setSelection(events);
    }

    void select(RepositoryRevision container) {
        treeView.requestFocusInWindow();
        treeView.setSelection(container);
    }

    void refreshResults (List<RepositoryRevision> res) {
        results = res;
        treeView.refreshResults(res);
    }

    private boolean similarPaths (VCSFileProxy referenceFile, VCSFileProxy file) {
        return referenceFile.getName().equals(file.getName())
                || referenceFile.getPath().equalsIgnoreCase(null);
    }

    private class ShowDiffTask extends GitProgressSupport {
        
        private VCSFileProxy file1;
        private VCSFileProxy baseFile1;
        private String revision1;
        private final boolean showLastDifference;
        private final RepositoryRevision.Event event2;
        private DiffStreamSource s1;
        private DiffStreamSource s2;

        public ShowDiffTask(RepositoryRevision.Event event1, RepositoryRevision.Event event2, boolean showLastDifference) {
            this.event2 = event2;
            if (event1 != null) {
                revision1 = event1.getLogInfoHeader().getLog().getRevision();
                file1 = event1.getOriginalFile();
                baseFile1 = event1.getFile();
            }
            this.showLastDifference = showLastDifference;
        }

        @Override
        public void perform () {
            showDiffError(NbBundle.getMessage(DiffResultsView.class, "MSG_DiffPanel_LoadingDiff")); //NOI18N
            if (revision1 == null) {
                try {
                    revision1 = event2.getLogInfoHeader().getAncestorCommit(event2.getOriginalFile(), getClient(), GitUtils.NULL_PROGRESS_MONITOR);
                    file1 = event2.getOriginalFile();
                    baseFile1 = event2.getFile();
                } catch (GitException ex) {
                    LOG.log(Level.INFO, null, ex);
                    showDiffError(NbBundle.getMessage(DiffResultsView.class, "MSG_DiffPanel_LoadingDiff")); //NOI18N
                    return;
                }
            }
            if (isCanceled()) {
                return;
            }
            String title1, title2;
            title1 = revision1 == null ? null : revision1.substring(0, 7);
            title2 = event2.getLogInfoHeader().getLog().getRevision() == null ? null : event2.getLogInfoHeader().getLog().getRevision().substring(0, 7);
            s1 = new DiffStreamSource(file1, baseFile1, revision1, file1.getName() + (title1 == null ? "" : (" (" + title1 + ")"))); //NOI18N
            s2 = new DiffStreamSource(event2.getFile(), event2.getFile(), event2.getLogInfoHeader().getLog().getRevision(), 
                    event2.getFile().getName() + (title2 == null ? "" : (" (" + title2 + ")"))); //NOI18N

            // it's enqueued at ClientRuntime queue and does not return until previous request handled
            s1.getMIMEType();  // triggers s1.init()
            if (isCanceled()) {
                showDiffError(NbBundle.getMessage(DiffResultsView.class, "MSG_DiffPanel_NoRevisions")); // NOI18N
                return;
            }

            s2.getMIMEType();  // triggers s2.init()
            if (isCanceled()) {
                showDiffError(NbBundle.getMessage(DiffResultsView.class, "MSG_DiffPanel_NoRevisions")); // NOI18N
                return;
            }

            if (currentTask != this) {
                return;
            }

            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (isCanceled()) {
                            showDiffError(NbBundle.getMessage(DiffResultsView.class, "MSG_DiffPanel_NoRevisions")); // NOI18N
                            return;
                        }
                        final DiffController view = DiffController.createEnhanced(s1, s2);
                        if (currentTask == ShowDiffTask.this) {
                            currentDiff = view;
                            setBottomComponent(currentDiff.getJComponent());
                            final int dl = diffView.getDividerLocation();
                            if (!setLocation(view)) {
                                view.addPropertyChangeListener(new PropertyChangeListener() {
                                    @Override
                                    public void propertyChange(PropertyChangeEvent evt) {
                                        view.removePropertyChangeListener(this);
                                        setLocation(view);
                                        parent.updateActions();
                                    }
                                });
                            }
                            parent.refreshComponents(false);
                            EventQueue.invokeLater(new Runnable () {
                                @Override
                                public void run() {
                                    diffView.setDividerLocation(dl);
                                }
                            });
                        }
                    } catch (IOException e) {
                        ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, e);
                    }
                }
            });
        }

        @Override
        public boolean cancel () {
            if (s1 != null) {
                s1.close();
            }
            if (s2 != null) {
                s2.close();
            }
            return super.cancel();
        }

        private boolean setLocation (DiffController view) {
            boolean locationSet = false;
            if (view == currentDiff && view.getDifferenceCount() > 0) {
                locationSet = true;
                currentDifferenceIndex = showLastDifference ? view.getDifferenceCount() - 1 : 0;
                view.setLocation(DiffController.DiffPane.Base, DiffController.LocationType.DifferenceIndex, currentDifferenceIndex);
            }
            return locationSet;
        }
    }
    
    public JComponent getComponent() {
        return diffView;
    }
}


