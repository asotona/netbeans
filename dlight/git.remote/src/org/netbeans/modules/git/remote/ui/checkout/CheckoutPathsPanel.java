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

/*
 * CheckoutPathsPanel.java
 *
 * Created on Oct 18, 2010, 3:28:45 PM
 */

package org.netbeans.modules.git.remote.ui.checkout;

import javax.swing.JPanel;
import org.netbeans.modules.git.remote.ui.repository.RevisionDialog;

/**
 *
 */
public class CheckoutPathsPanel extends JPanel {
    private final RevisionDialog revisionPanel;

    /** Creates new form CheckoutPathsPanel */
    public CheckoutPathsPanel (RevisionDialog revisionPanel) {
        this.revisionPanel = revisionPanel;
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        org.netbeans.modules.git.remote.ui.repository.RevisionDialog revisionPickerDialog1 = this.revisionPanel;

        setToolTipText(org.openide.util.NbBundle.getMessage(CheckoutPathsPanel.class, "CheckoutPathsPanel.cbUpdateIndex.TTtext")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(cbUpdateIndex, org.openide.util.NbBundle.getMessage(CheckoutPathsPanel.class, "CheckoutPathsPanel.cbUpdateIndex.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbUpdateIndex)
                    .addComponent(revisionPickerDialog1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbUpdateIndex)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(revisionPickerDialog1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    final javax.swing.JCheckBox cbUpdateIndex = new javax.swing.JCheckBox();
    // End of variables declaration//GEN-END:variables

}
