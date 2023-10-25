/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFileWatcher;
import org.apache.logging.log4j.core.config.ConfigurationListener;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import org.apache.logging.log4j.core.util.Source;
import org.apache.logging.log4j.core.util.Throwables;
import org.apache.logging.log4j.core.util.Watcher;
import org.apache.logging.log4j.status.StatusLogger;

public class WatcherFactory {
    private static Logger LOGGER = StatusLogger.getLogger();
    private static PluginManager pluginManager = new PluginManager("Watcher");
    private static volatile WatcherFactory factory;
    private final Map<String, PluginType<?>> plugins;

    private WatcherFactory(List<String> packages) {
        pluginManager.collectPlugins(packages);
        this.plugins = pluginManager.getPlugins();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static WatcherFactory getInstance(List<String> packages) {
        if (factory == null) {
            PluginManager pluginManager = WatcherFactory.pluginManager;
            synchronized (pluginManager) {
                if (factory == null) {
                    factory = new WatcherFactory(packages);
                }
            }
        }
        return factory;
    }

    public Watcher newWatcher(Source source, Configuration configuration, Reconfigurable reconfigurable, List<ConfigurationListener> configurationListeners, long lastModifiedMillis) {
        if (source.getFile() != null) {
            return new ConfigurationFileWatcher(configuration, reconfigurable, configurationListeners, lastModifiedMillis);
        }
        String name = source.getURI().getScheme();
        PluginType<?> pluginType = this.plugins.get(name);
        if (pluginType != null) {
            return WatcherFactory.instantiate(name, pluginType.getPluginClass(), configuration, reconfigurable, configurationListeners, lastModifiedMillis);
        }
        LOGGER.info("No Watcher plugin is available for protocol '{}'", name);
        return null;
    }

    public static <T extends Watcher> T instantiate(String name, Class<T> clazz, Configuration configuration, Reconfigurable reconfigurable, List<ConfigurationListener> listeners, long lastModifiedMillis) {
        Objects.requireNonNull(clazz, "No class provided");
        try {
            Constructor<T> constructor = clazz.getConstructor(Configuration.class, Reconfigurable.class, List.class, Long.TYPE);
            return (T)((Watcher)constructor.newInstance(configuration, reconfigurable, listeners, lastModifiedMillis));
        }
        catch (NoSuchMethodException ex) {
            throw new IllegalArgumentException("No valid constructor for Watcher plugin " + name, ex);
        }
        catch (InstantiationException | LinkageError e) {
            throw new IllegalArgumentException(e);
        }
        catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        catch (InvocationTargetException e) {
            Throwables.rethrow(e.getCause());
            throw new InternalError("Unreachable");
        }
    }
}

