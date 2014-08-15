/*
 *  Copyright 2014 Jeroen van der Wal
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.isisaddons.module.security.dom.permission;

public enum ApplicationPermissionType {
    /**
     * The permission grants the ability to view/use the {@link org.isisaddons.module.security.dom.feature.ApplicationFeature}.
     *
     * <p>
     * The {@link org.isisaddons.module.security.dom.permission.ApplicationPermissionMode} determines whether the
     * permission is to only view or also to use the {@link org.isisaddons.module.security.dom.feature.ApplicationFeature}.
     * </p>
     */
    ALLOW,
    /**
     * The permission prevents the ability to view/use the {@link org.isisaddons.module.security.dom.feature.ApplicationFeature}.
     *
     * <p>
     * The {@link org.isisaddons.module.security.dom.permission.ApplicationPermissionMode} determines whether the
     * permission is to only view or also to use the {@link org.isisaddons.module.security.dom.feature.ApplicationFeature}.
     * </p>
     */
    VETO
}
