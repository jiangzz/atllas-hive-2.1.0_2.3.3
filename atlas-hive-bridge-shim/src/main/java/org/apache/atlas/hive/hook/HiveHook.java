/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.atlas.hive.hook;


import org.apache.atlas.plugin.classloader.AtlasPluginClassLoader;
import org.apache.hadoop.hive.ql.hooks.ExecuteWithHookContext;
import org.apache.hadoop.hive.ql.hooks.HookContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hive hook used for atlas entity registration.
 */
public class HiveHook implements ExecuteWithHookContext {
    private static final Logger LOG = LoggerFactory.getLogger(HiveHook.class);

    private static final String ATLAS_PLUGIN_TYPE = "hive";
    private static final String ATLAS_HIVE_HOOK_IMPL_CLASSNAME = "org.apache.atlas.hive.hook.HiveHook";

    private AtlasPluginClassLoader atlasPluginClassLoader = null;
    private ExecuteWithHookContext hiveHookImpl = null;

    public HiveHook() {
        this.initialize();
    }

    @Override
    public void run(final HookContext hookContext) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("==> HiveHook.run({})", hookContext);
        }

        try {
            activatePluginClassLoader();
             hiveHookImpl.run(hookContext);
       }catch (Throwable e){
         LOG.error("Ops! HOOK ERROR,you can ignore it...");
        } finally {
            deactivatePluginClassLoader();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("<== HiveHook.run({})", hookContext);
        }
    }

    private void initialize() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("==> HiveHook.initialize()");
        }

        try {
            atlasPluginClassLoader = AtlasPluginClassLoader.getInstance(ATLAS_PLUGIN_TYPE, this.getClass());

            @SuppressWarnings("unchecked")
            Class<ExecuteWithHookContext> cls = (Class<ExecuteWithHookContext>) Class
                    .forName(ATLAS_HIVE_HOOK_IMPL_CLASSNAME, true, atlasPluginClassLoader);

            activatePluginClassLoader();

            hiveHookImpl = cls.newInstance();
        }
        catch (Exception excp) {
            LOG.error("Error instantiating Atlas hook implementation", excp);
        } finally {
            deactivatePluginClassLoader();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("<== HiveHook.initialize()");
        }
    }

    private void activatePluginClassLoader() {
        if (atlasPluginClassLoader != null) {
            atlasPluginClassLoader.activate();
        }
    }

    private void deactivatePluginClassLoader() {
        if (atlasPluginClassLoader != null) {
            atlasPluginClassLoader.deactivate();
        }
    }
}
