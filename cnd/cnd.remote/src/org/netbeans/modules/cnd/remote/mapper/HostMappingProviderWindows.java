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

package org.netbeans.modules.cnd.remote.mapper;

import java.util.Map;
import org.netbeans.modules.cnd.api.toolchain.PlatformTypes;
import org.netbeans.modules.cnd.api.utils.PlatformInfo;
import org.netbeans.modules.nativeexecution.api.ExecutionEnvironment;

/**
 *
 */
/*package*/ final class HostMappingProviderWindows implements HostMappingProvider {

   
    @Override
    public boolean isApplicable(PlatformInfo hostPlatform, PlatformInfo otherPlatform) {
        return PlatformTypes.PLATFORM_WINDOWS == hostPlatform.getPlatform()
                && hostPlatform.isLocalhost(); // Windows is only supported as client platform
    }

    @Override
    public Map<String, String> findMappings(ExecutionEnvironment execEnv, ExecutionEnvironment otherExecEnv) {
        return WindowsSupport.findMappings(execEnv, otherExecEnv, null, false);
    }

    
}
