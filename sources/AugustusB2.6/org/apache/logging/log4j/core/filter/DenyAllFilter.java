// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "DenyAllFilter", category = "Core", elementType = "filter", printObject = true)
@PerformanceSensitive({ "allocation" })
public final class DenyAllFilter extends AbstractFilter
{
    private DenyAllFilter(final Filter.Result onMatch, final Filter.Result onMismatch) {
        super(onMatch, onMismatch);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
        return Filter.Result.DENY;
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
        return Filter.Result.DENY;
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        return Filter.Result.DENY;
    }
    
    @Override
    public Filter.Result filter(final LogEvent event) {
        return Filter.Result.DENY;
    }
    
    private Filter.Result filter(final Marker marker) {
        return Filter.Result.DENY;
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0) {
        return Filter.Result.DENY;
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1) {
        return Filter.Result.DENY;
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2) {
        return this.filter(marker);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3) {
        return Filter.Result.DENY;
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        return Filter.Result.DENY;
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        return Filter.Result.DENY;
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        return Filter.Result.DENY;
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        return Filter.Result.DENY;
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        return Filter.Result.DENY;
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        return Filter.Result.DENY;
    }
    
    @Override
    public String toString() {
        return "DenyAll";
    }
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    public static class Builder extends AbstractFilterBuilder<Builder> implements org.apache.logging.log4j.core.util.Builder<DenyAllFilter>
    {
        @Override
        public DenyAllFilter build() {
            return new DenyAllFilter(this.getOnMatch(), this.getOnMismatch(), null);
        }
    }
}
