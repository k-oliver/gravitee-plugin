/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.plugin.core.internal;

import io.gravitee.plugin.core.api.ClassLoaderFactory;
import io.gravitee.plugin.core.api.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author David BRASSELY (brasseld at gmail.com)
 */
public class ClassLoaderFactoryImpl implements ClassLoaderFactory {

    protected final Logger LOGGER = LoggerFactory.getLogger(ClassLoaderFactoryImpl.class);

    private final Map<String, URLClassLoader> pluginClassLoaderCache = new HashMap<>();

    @Override
    public URLClassLoader getOrCreatePluginClassLoader(Plugin plugin, ClassLoader parent) {
        URLClassLoader cl;

        try {
            cl = pluginClassLoaderCache.get(plugin.id());
            if (null == cl)  {
                cl = new URLClassLoader(plugin.dependencies(), parent);
                pluginClassLoaderCache.put(plugin.id(), cl);
            }

            LOGGER.debug("Created plugin ClassLoader for {} with classpath {}",
                    plugin.id(), plugin.dependencies());

            return cl;
        }
        catch (Throwable t) {
            LOGGER.error("Unexpected error while creating plugin classloader", t);
            return null;
        }
    }
}
