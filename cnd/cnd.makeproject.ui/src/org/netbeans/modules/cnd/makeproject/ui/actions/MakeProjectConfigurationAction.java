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
package org.netbeans.modules.cnd.makeproject.ui.actions;

import javax.swing.Action;
import org.netbeans.api.project.Project;
import org.netbeans.modules.cnd.makeproject.ui.SetConfigurationAction;
import org.openide.util.NbBundle;

/**
 *
 */
public class MakeProjectConfigurationAction extends ContextAwareWrapperAction {
    

    @Override
    protected Action createDelegateAction(Project[] projects) {
        return projects.length == 1 ? new SetConfigurationAction(projects[0]) : null;
    }

    @Override
    protected boolean supportMultipleProjects() {
        return false;
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage( SetConfigurationAction.class, "LBL_SetConfigurationAction_Name");//NOI18N
    }
    
}
