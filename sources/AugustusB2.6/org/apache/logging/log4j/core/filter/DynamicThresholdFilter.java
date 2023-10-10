// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import java.util.Iterator;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import java.util.Objects;
import org.apache.logging.log4j.core.impl.ContextDataInjectorFactory;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.util.HashMap;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.util.Map;
import org.apache.logging.log4j.core.ContextDataInjector;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "DynamicThresholdFilter", category = "Core", elementType = "filter", printObject = true)
@PerformanceSensitive({ "allocation" })
public final class DynamicThresholdFilter extends AbstractFilter
{
    private Level defaultThreshold;
    private final String key;
    private final ContextDataInjector injector;
    private Map<String, Level> levelMap;
    
    @PluginFactory
    public static DynamicThresholdFilter createFilter(@PluginAttribute("key") final String key, @PluginElement("Pairs") final KeyValuePair[] pairs, @PluginAttribute("defaultThreshold") final Level defaultThreshold, @PluginAttribute("onMatch") final Filter.Result onMatch, @PluginAttribute("onMismatch") final Filter.Result onMismatch) {
        final Map<String, Level> map = new HashMap<String, Level>();
        for (final KeyValuePair pair : pairs) {
            map.put(pair.getKey(), Level.toLevel(pair.getValue()));
        }
        final Level level = (defaultThreshold == null) ? Level.ERROR : defaultThreshold;
        return new DynamicThresholdFilter(key, map, level, onMatch, onMismatch);
    }
    
    private DynamicThresholdFilter(final String key, final Map<String, Level> pairs, final Level defaultLevel, final Filter.Result onMatch, final Filter.Result onMismatch) {
        super(onMatch, onMismatch);
        this.defaultThreshold = Level.ERROR;
        this.injector = ContextDataInjectorFactory.createInjector();
        this.levelMap = new HashMap<String, Level>();
        Objects.requireNonNull(key, "key cannot be null");
        this.key = key;
        this.levelMap = pairs;
        this.defaultThreshold = defaultLevel;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equalsImpl(obj)) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final DynamicThresholdFilter other = (DynamicThresholdFilter)obj;
        return Objects.equals(this.defaultThreshold, other.defaultThreshold) && Objects.equals(this.key, other.key) && Objects.equals(this.levelMap, other.levelMap);
    }
    
    private Filter.Result filter(final Level level, final ReadOnlyStringMap contextMap) {
        final String value = contextMap.getValue(this.key);
        if (value != null) {
            Level ctxLevel = this.levelMap.get(value);
            if (ctxLevel == null) {
                ctxLevel = this.defaultThreshold;
            }
            return level.isMoreSpecificThan(ctxLevel) ? this.onMatch : this.onMismatch;
        }
        return Filter.Result.NEUTRAL;
    }
    
    @Override
    public Filter.Result filter(final LogEvent event) {
        return this.filter(event.getLevel(), event.getContextData());
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        return this.filter(level, this.currentContextData());
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
        return this.filter(level, this.currentContextData());
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
        return this.filter(level, this.currentContextData());
    }
    
    private ReadOnlyStringMap currentContextData() {
        return this.injector.rawContextData();
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0) {
        return this.filter(level, this.currentContextData());
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1) {
        return this.filter(level, this.currentContextData());
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2) {
        return this.filter(level, this.currentContextData());
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3) {
        return this.filter(level, this.currentContextData());
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        return this.filter(level, this.currentContextData());
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        return this.filter(level, this.currentContextData());
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        return this.filter(level, this.currentContextData());
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        return this.filter(level, this.currentContextData());
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        return this.filter(level, this.currentContextData());
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        return this.filter(level, this.currentContextData());
    }
    
    public String getKey() {
        return this.key;
    }
    
    public Map<String, Level> getLevelMap() {
        return this.levelMap;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCodeImpl();
        result = 31 * result + ((this.defaultThreshold == null) ? 0 : this.defaultThreshold.hashCode());
        result = 31 * result + ((this.key == null) ? 0 : this.key.hashCode());
        result = 31 * result + ((this.levelMap == null) ? 0 : this.levelMap.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("key=").append(this.key);
        sb.append(", default=").append(this.defaultThreshold);
        if (this.levelMap.size() > 0) {
            sb.append('{');
            boolean first = true;
            for (final Map.Entry<String, Level> entry : this.levelMap.entrySet()) {
                if (!first) {
                    sb.append(", ");
                    first = false;
                }
                sb.append(entry.getKey()).append('=').append(entry.getValue());
            }
            sb.append('}');
        }
        return sb.toString();
    }
}
