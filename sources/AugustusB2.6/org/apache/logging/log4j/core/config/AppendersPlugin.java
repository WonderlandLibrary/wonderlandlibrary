// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "appenders", category = "Core")
public final class AppendersPlugin
{
    private AppendersPlugin() {
    }
    
    @PluginFactory
    public static ConcurrentMap<String, Appender> createAppenders(@PluginElement("Appenders") final Appender[] appenders) {
        final ConcurrentMap<String, Appender> map = new ConcurrentHashMap<String, Appender>(appenders.length);
        for (final Appender appender : appenders) {
            map.put(appender.getName(), appender);
        }
        return map;
    }
}
