// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "LevelRangeFilter", category = "Core", elementType = "filter", printObject = true)
@PerformanceSensitive({ "allocation" })
public final class LevelRangeFilter extends AbstractFilter
{
    private final Level maxLevel;
    private final Level minLevel;
    
    @PluginFactory
    public static LevelRangeFilter createFilter(@PluginAttribute("minLevel") final Level minLevel, @PluginAttribute("maxLevel") final Level maxLevel, @PluginAttribute("onMatch") final Filter.Result match, @PluginAttribute("onMismatch") final Filter.Result mismatch) {
        final Level actualMinLevel = (minLevel == null) ? Level.ERROR : minLevel;
        final Level actualMaxLevel = (maxLevel == null) ? Level.ERROR : maxLevel;
        final Filter.Result onMatch = (match == null) ? Filter.Result.NEUTRAL : match;
        final Filter.Result onMismatch = (mismatch == null) ? Filter.Result.DENY : mismatch;
        return new LevelRangeFilter(actualMinLevel, actualMaxLevel, onMatch, onMismatch);
    }
    
    private LevelRangeFilter(final Level minLevel, final Level maxLevel, final Filter.Result onMatch, final Filter.Result onMismatch) {
        super(onMatch, onMismatch);
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
    }
    
    private Filter.Result filter(final Level level) {
        return level.isInRange(this.minLevel, this.maxLevel) ? this.onMatch : this.onMismatch;
    }
    
    @Override
    public Filter.Result filter(final LogEvent event) {
        return this.filter(event.getLevel());
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        return this.filter(level);
    }
    
    public Level getMinLevel() {
        return this.minLevel;
    }
    
    @Override
    public String toString() {
        return this.minLevel.toString();
    }
}
