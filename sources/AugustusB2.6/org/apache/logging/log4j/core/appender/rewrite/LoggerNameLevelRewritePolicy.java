// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rewrite;

import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Locale;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.util.HashMap;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.Level;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "LoggerNameLevelRewritePolicy", category = "Core", elementType = "rewritePolicy", printObject = true)
public class LoggerNameLevelRewritePolicy implements RewritePolicy
{
    private final String loggerName;
    private final Map<Level, Level> map;
    
    @PluginFactory
    public static LoggerNameLevelRewritePolicy createPolicy(@PluginAttribute("logger") final String loggerNamePrefix, @PluginElement("KeyValuePair") final KeyValuePair[] levelPairs) {
        final Map<Level, Level> newMap = new HashMap<Level, Level>(levelPairs.length);
        for (final KeyValuePair keyValuePair : levelPairs) {
            newMap.put(getLevel(keyValuePair.getKey()), getLevel(keyValuePair.getValue()));
        }
        return new LoggerNameLevelRewritePolicy(loggerNamePrefix, newMap);
    }
    
    private static Level getLevel(final String name) {
        return Level.getLevel(name.toUpperCase(Locale.ROOT));
    }
    
    private LoggerNameLevelRewritePolicy(final String loggerName, final Map<Level, Level> map) {
        this.loggerName = loggerName;
        this.map = map;
    }
    
    @Override
    public LogEvent rewrite(final LogEvent event) {
        if (event.getLoggerName() == null || !event.getLoggerName().startsWith(this.loggerName)) {
            return event;
        }
        final Level sourceLevel = event.getLevel();
        final Level newLevel = this.map.get(sourceLevel);
        if (newLevel == null || newLevel == sourceLevel) {
            return event;
        }
        final LogEvent result = new Log4jLogEvent.Builder(event).setLevel(newLevel).build();
        return result;
    }
}
