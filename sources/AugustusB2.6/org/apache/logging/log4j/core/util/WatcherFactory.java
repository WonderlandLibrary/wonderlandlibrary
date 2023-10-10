// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.status.StatusLogger;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import org.apache.logging.log4j.core.config.ConfigurationFileWatcher;
import org.apache.logging.log4j.core.config.ConfigurationListener;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.config.Configuration;
import java.util.List;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import org.apache.logging.log4j.Logger;

public class WatcherFactory
{
    private static Logger LOGGER;
    private static PluginManager pluginManager;
    private static volatile WatcherFactory factory;
    private final Map<String, PluginType<?>> plugins;
    
    private WatcherFactory(final List<String> packages) {
        WatcherFactory.pluginManager.collectPlugins(packages);
        this.plugins = WatcherFactory.pluginManager.getPlugins();
    }
    
    public static WatcherFactory getInstance(final List<String> packages) {
        if (WatcherFactory.factory == null) {
            synchronized (WatcherFactory.pluginManager) {
                if (WatcherFactory.factory == null) {
                    WatcherFactory.factory = new WatcherFactory(packages);
                }
            }
        }
        return WatcherFactory.factory;
    }
    
    public Watcher newWatcher(final Source source, final Configuration configuration, final Reconfigurable reconfigurable, final List<ConfigurationListener> configurationListeners, final long lastModifiedMillis) {
        if (source.getFile() != null) {
            return new ConfigurationFileWatcher(configuration, reconfigurable, configurationListeners, lastModifiedMillis);
        }
        final String name = source.getURI().getScheme();
        final PluginType<?> pluginType = this.plugins.get(name);
        if (pluginType != null) {
            return instantiate(name, pluginType.getPluginClass(), configuration, reconfigurable, configurationListeners, lastModifiedMillis);
        }
        WatcherFactory.LOGGER.info("No Watcher plugin is available for protocol '{}'", name);
        return null;
    }
    
    public static <T extends Watcher> T instantiate(final String name, final Class<T> clazz, final Configuration configuration, final Reconfigurable reconfigurable, final List<ConfigurationListener> listeners, final long lastModifiedMillis) {
        Objects.requireNonNull(clazz, "No class provided");
        try {
            final Constructor<T> constructor = clazz.getConstructor(Configuration.class, Reconfigurable.class, List.class, Long.TYPE);
            return constructor.newInstance(configuration, reconfigurable, listeners, lastModifiedMillis);
        }
        catch (NoSuchMethodException ex) {
            throw new IllegalArgumentException("No valid constructor for Watcher plugin " + name, ex);
        }
        catch (LinkageError | InstantiationException linkageError) {
            final Throwable t;
            final Throwable e = t;
            throw new IllegalArgumentException(e);
        }
        catch (IllegalAccessException e2) {
            throw new IllegalStateException(e2);
        }
        catch (InvocationTargetException e3) {
            Throwables.rethrow(e3.getCause());
            throw new InternalError("Unreachable");
        }
    }
    
    static {
        WatcherFactory.LOGGER = StatusLogger.getLogger();
        WatcherFactory.pluginManager = new PluginManager("Watcher");
    }
}
