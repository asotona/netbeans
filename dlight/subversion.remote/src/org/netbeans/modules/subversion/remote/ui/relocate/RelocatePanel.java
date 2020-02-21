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
 * RelocatePanel.java
 *
 * Created on 03 March 2007, 11:53
 */

package org.netbeans.modules.subversion.remote.ui.relocate;

import javax.swing.JTextField;

/**
 *
 */
public class RelocatePanel extends javax.swing.JPanel {
    
    /** Creates new form RelocatePanel */
    public RelocatePanel() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tfWorkingCopy = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tfCurrentURL = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfNewURL = new javax.swing.JTextField();

        jLabel1.setLabelFor(tfWorkingCopy);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(RelocatePanel.class, "RelocatePanel.jLabel1.text")); // NOI18N

        tfWorkingCopy.setEditable(false);

        jLabel2.setLabelFor(tfCurrentURL);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(RelocatePanel.class, "RelocatePanel.jLabel2.text")); // NOI18N

        tfCurrentURL.setEditable(false);

        jLabel3.setLabelFor(tfNewURL);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(RelocatePanel.class, "RelocatePanel.jLabel3.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfCurrentURL, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(tfWorkingCopy, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(tfNewURL, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfWorkingCopy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfCurrentURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tfNewURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(RelocatePanel.class, "RelocatePanel.jLabel1.AccessibleContext.accessibleDescription")); // NOI18N
        tfWorkingCopy.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(RelocatePanel.class, "RelocatePanel.tfWorkingCopy.AccessibleContext.accessibleName")); // NOI18N
        tfWorkingCopy.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(RelocatePanel.class, "RelocatePanel.tfWorkingCopy.AccessibleContext.accessibleDescription")); // NOI18N
        jLabel2.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(RelocatePanel.class, "RelocatePanel.jLabel2.AccessibleContext.accessibleDescription")); // NOI18N
        tfCurrentURL.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(RelocatePanel.class, "RelocatePanel.tfCurrentURL.AccessibleContext.accessibleName")); // NOI18N
        tfCurrentURL.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(RelocatePanel.class, "RelocatePanel.tfCurrentURL.AccessibleContext.accessibleDescription")); // NOI18N
        jLabel3.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(RelocatePanel.class, "RelocatePanel.jLabel3.AccessibleContext.accessibleDescription")); // NOI18N
        tfNewURL.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(RelocatePanel.class, "RelocatePanel.tfNewURL.AccessibleContext.accessibleName")); // NOI18N
        tfNewURL.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(RelocatePanel.class, "RelocatePanel.tfNewURL.AccessibleContext.accessibleDescription")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField tfCurrentURL;
    private javax.swing.JTextField tfNewURL;
    private javax.swing.JTextField tfWorkingCopy;
    // End of variables declaration//GEN-END:variables
    
    public JTextField getWorkingCopy() {
        return tfWorkingCopy;
    }
    
    public JTextField getCurrentURL() {
        return tfCurrentURL;
    }
    
    public JTextField getNewURL() {
        return tfNewURL;
    }
}
