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

package org.netbeans.modules.j2ee.ddloaders.multiview;

import org.netbeans.modules.j2ee.dd.api.ejb.Ejb;
import org.netbeans.modules.j2ee.dd.api.ejb.EntityAndSession;
import org.netbeans.modules.xml.multiview.ui.SectionNodeInnerPanel;
import org.netbeans.modules.xml.multiview.ui.SectionNodeView;

/**
 * @author pfiala
 */
public class BeanEnvironmentNode extends EjbSectionNode {

    BeanEnvironmentNode(SectionNodeView sectionNodeView, Ejb ejb) {
        super(sectionNodeView, false, ejb, Utils.getBundleMessage("LBL_BeanEnvironment"), Utils.ICON_BASE_MISC_NODE);
        addChild(new EjbReferencesNode(sectionNodeView, ejb));
        addChild(new EnvironmentEntriesNode(sectionNodeView, ejb));
        addChild(new ResourceReferencesNode(sectionNodeView, ejb));
        addChild(new ResourceEnvironmentReferencesNode(sectionNodeView, ejb));
        if (ejb instanceof EntityAndSession) {
            addChild(new SecurityRoleReferencesNode(sectionNodeView, (EntityAndSession) ejb));
        }
    }

    protected SectionNodeInnerPanel createNodeInnerPanel() {
        return null;
    }
}
