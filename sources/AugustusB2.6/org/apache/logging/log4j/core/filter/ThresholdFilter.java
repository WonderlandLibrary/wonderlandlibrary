// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ThresholdFilter", category = "Core", elementType = "filter", printObject = true)
@PerformanceSensitive({ "allocation" })
public final class ThresholdFilter extends AbstractFilter
{
    private final Level level;
    
    private ThresholdFilter(final Level level, final Filter.Result onMatch, final Filter.Result onMismatch) {
        super(onMatch, onMismatch);
        this.level = level;
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level testLevel, final Marker marker, final String msg, final Object... params) {
        return this.filter(testLevel);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level testLevel, final Marker marker, final Object msg, final Throwable t) {
        return this.filter(testLevel);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level testLevel, final Marker marker, final Message msg, final Throwable t) {
        return this.filter(testLevel);
    }
    
    @Override
    public Filter.Result filter(final LogEvent event) {
        return this.filter(event.getLevel());
    }
    
    private Filter.Result filter(final Level testLevel) {
        return testLevel.isMoreSpecificThan(this.level) ? this.onMatch : this.onMismatch;
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
    
    public Level getLevel() {
        return this.level;
    }
    
    @Override
    public String toString() {
        return this.level.toString();
    }
    
    @PluginFactory
    public static ThresholdFilter createFilter(@PluginAttribute("level") final Level level, @PluginAttribute("onMatch") final Filter.Result match, @PluginAttribute("onMismatch") final Filter.Result mismatch) {
        final Level actualLevel = (level == null) ? Level.ERROR : level;
        final Filter.Result onMatch = (match == null) ? Filter.Result.NEUTRAL : match;
        final Filter.Result onMismatch = (mismatch == null) ? Filter.Result.DENY : mismatch;
        return new ThresholdFilter(actualLevel, onMatch, onMismatch);
    }
}
