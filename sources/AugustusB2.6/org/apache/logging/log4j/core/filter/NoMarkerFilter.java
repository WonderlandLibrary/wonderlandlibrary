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

@Plugin(name = "NoMarkerFilter", category = "Core", elementType = "filter", printObject = true)
@PerformanceSensitive({ "allocation" })
public final class NoMarkerFilter extends AbstractFilter
{
    private NoMarkerFilter(final Filter.Result onMatch, final Filter.Result onMismatch) {
        super(onMatch, onMismatch);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
        return this.filter(marker);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
        return this.filter(marker);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        return this.filter(marker);
    }
    
    @Override
    public Filter.Result filter(final LogEvent event) {
        return this.filter(event.getMarker());
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0) {
        return this.filter(marker);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1) {
        return this.filter(marker);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2) {
        return this.filter(marker);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3) {
        return this.filter(marker);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        return this.filter(marker);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        return this.filter(marker);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        return this.filter(marker);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        return this.filter(marker);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        return this.filter(marker);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        return this.filter(marker);
    }
    
    private Filter.Result filter(final Marker marker) {
        return (null == marker) ? this.onMatch : this.onMismatch;
    }
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    public static class Builder extends AbstractFilterBuilder<Builder> implements org.apache.logging.log4j.core.util.Builder<NoMarkerFilter>
    {
        @Override
        public NoMarkerFilter build() {
            return new NoMarkerFilter(this.getOnMatch(), this.getOnMismatch(), null);
        }
    }
}
