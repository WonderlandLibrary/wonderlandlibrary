// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "loggers", category = "Core")
public final class LoggersPlugin
{
    private LoggersPlugin() {
    }
    
    @PluginFactory
    public static Loggers createLoggers(@PluginElement("Loggers") final LoggerConfig[] loggers) {
        final ConcurrentMap<String, LoggerConfig> loggerMap = new ConcurrentHashMap<String, LoggerConfig>();
        LoggerConfig root = null;
        for (final LoggerConfig logger : loggers) {
            if (logger != null) {
                if (logger.getName().isEmpty()) {
                    if (root != null) {
                        throw new IllegalStateException("Configuration has multiple root loggers. There can be only one.");
                    }
                    root = logger;
                }
                loggerMap.put(logger.getName(), logger);
            }
        }
        return new Loggers(loggerMap, root);
    }
}
