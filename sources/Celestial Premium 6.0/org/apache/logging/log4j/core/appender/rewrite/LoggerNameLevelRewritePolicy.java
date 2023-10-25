/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rewrite;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.util.KeyValuePair;

@Plugin(name="LoggerNameLevelRewritePolicy", category="Core", elementType="rewritePolicy", printObject=true)
public class LoggerNameLevelRewritePolicy
implements RewritePolicy {
    private final String loggerName;
    private final Map<Level, Level> map;

    @PluginFactory
    public static LoggerNameLevelRewritePolicy createPolicy(@PluginAttribute(value="logger") String loggerNamePrefix, @PluginElement(value="KeyValuePair") KeyValuePair[] levelPairs) {
        HashMap<Level, Level> newMap = new HashMap<Level, Level>(levelPairs.length);
        for (KeyValuePair keyValuePair : levelPairs) {
            newMap.put(LoggerNameLevelRewritePolicy.getLevel(keyValuePair.getKey()), LoggerNameLevelRewritePolicy.getLevel(keyValuePair.getValue()));
        }
        return new LoggerNameLevelRewritePolicy(loggerNamePrefix, newMap);
    }

    private static Level getLevel(String name) {
        return Level.getLevel((String)name.toUpperCase(Locale.ROOT));
    }

    private LoggerNameLevelRewritePolicy(String loggerName, Map<Level, Level> map) {
        this.loggerName = loggerName;
        this.map = map;
    }

    @Override
    public LogEvent rewrite(LogEvent event) {
        if (event.getLoggerName() == null || !event.getLoggerName().startsWith(this.loggerName)) {
            return event;
        }
        Level sourceLevel = event.getLevel();
        Level newLevel = this.map.get((Object)sourceLevel);
        if (newLevel == null || newLevel == sourceLevel) {
            return event;
        }
        Log4jLogEvent result = new Log4jLogEvent.Builder(event).setLevel(newLevel).build();
        return result;
    }
}

