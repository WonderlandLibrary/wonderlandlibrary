/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.filter;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.core.helpers.KeyValuePair;
import org.apache.logging.log4j.message.Message;

@Plugin(name="DynamicThresholdFilter", category="Core", elementType="filter", printObject=true)
public final class DynamicThresholdFilter
extends AbstractFilter {
    private Map<String, Level> levelMap = new HashMap<String, Level>();
    private Level defaultThreshold = Level.ERROR;
    private final String key;

    private DynamicThresholdFilter(String key, Map<String, Level> pairs, Level defaultLevel, Filter.Result onMatch, Filter.Result onMismatch) {
        super(onMatch, onMismatch);
        if (key == null) {
            throw new NullPointerException("key cannot be null");
        }
        this.key = key;
        this.levelMap = pairs;
        this.defaultThreshold = defaultLevel;
    }

    public String getKey() {
        return this.key;
    }

    @Override
    public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object ... params) {
        return this.filter(level);
    }

    @Override
    public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        return this.filter(level);
    }

    @Override
    public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return this.filter(level);
    }

    @Override
    public Filter.Result filter(LogEvent event) {
        return this.filter(event.getLevel());
    }

    private Filter.Result filter(Level level) {
        String value = ThreadContext.get(this.key);
        if (value != null) {
            Level ctxLevel = this.levelMap.get(value);
            if (ctxLevel == null) {
                ctxLevel = this.defaultThreshold;
            }
            return level.isAtLeastAsSpecificAs(ctxLevel) ? this.onMatch : this.onMismatch;
        }
        return Filter.Result.NEUTRAL;
    }

    public Map<String, Level> getLevelMap() {
        return this.levelMap;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("key=").append(this.key);
        sb.append(", default=").append((Object)this.defaultThreshold);
        if (this.levelMap.size() > 0) {
            sb.append("{");
            boolean first = true;
            for (Map.Entry<String, Level> entry : this.levelMap.entrySet()) {
                if (!first) {
                    sb.append(", ");
                    first = false;
                }
                sb.append(entry.getKey()).append("=").append((Object)entry.getValue());
            }
            sb.append("}");
        }
        return sb.toString();
    }

    @PluginFactory
    public static DynamicThresholdFilter createFilter(@PluginAttribute(value="key") String key, @PluginElement(value="Pairs") KeyValuePair[] pairs, @PluginAttribute(value="defaultThreshold") String levelName, @PluginAttribute(value="onMatch") String match, @PluginAttribute(value="onMismatch") String mismatch) {
        Filter.Result onMatch = Filter.Result.toResult(match);
        Filter.Result onMismatch = Filter.Result.toResult(mismatch);
        HashMap<String, Level> map = new HashMap<String, Level>();
        for (KeyValuePair pair : pairs) {
            map.put(pair.getKey(), Level.toLevel(pair.getValue()));
        }
        Level level = Level.toLevel(levelName, Level.ERROR);
        return new DynamicThresholdFilter(key, map, level, onMatch, onMismatch);
    }
}

