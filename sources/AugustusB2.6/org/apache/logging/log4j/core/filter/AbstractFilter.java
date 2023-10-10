// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.AbstractLifeCycle;

public abstract class AbstractFilter extends AbstractLifeCycle implements Filter
{
    protected final Result onMatch;
    protected final Result onMismatch;
    
    protected AbstractFilter() {
        this(null, null);
    }
    
    protected AbstractFilter(final Result onMatch, final Result onMismatch) {
        this.onMatch = ((onMatch == null) ? Result.NEUTRAL : onMatch);
        this.onMismatch = ((onMismatch == null) ? Result.DENY : onMismatch);
    }
    
    @Override
    protected boolean equalsImpl(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equalsImpl(obj)) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final AbstractFilter other = (AbstractFilter)obj;
        return this.onMatch == other.onMatch && this.onMismatch == other.onMismatch;
    }
    
    @Override
    public Result filter(final LogEvent event) {
        return Result.NEUTRAL;
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        return Result.NEUTRAL;
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
        return Result.NEUTRAL;
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
        return Result.NEUTRAL;
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0) {
        return this.filter(logger, level, marker, msg, new Object[] { p0 });
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1) {
        return this.filter(logger, level, marker, msg, new Object[] { p0, p1 });
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2) {
        return this.filter(logger, level, marker, msg, new Object[] { p0, p1, p2 });
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3) {
        return this.filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3 });
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        return this.filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3, p4 });
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        return this.filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3, p4, p5 });
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        return this.filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3, p4, p5, p6 });
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        return this.filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7 });
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        return this.filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8 });
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        return this.filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8, p9 });
    }
    
    @Override
    public final Result getOnMatch() {
        return this.onMatch;
    }
    
    @Override
    public final Result getOnMismatch() {
        return this.onMismatch;
    }
    
    @Override
    protected int hashCodeImpl() {
        final int prime = 31;
        int result = super.hashCodeImpl();
        result = 31 * result + ((this.onMatch == null) ? 0 : this.onMatch.hashCode());
        result = 31 * result + ((this.onMismatch == null) ? 0 : this.onMismatch.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
    
    public abstract static class AbstractFilterBuilder<B extends AbstractFilterBuilder<B>>
    {
        public static final String ATTR_ON_MISMATCH = "onMismatch";
        public static final String ATTR_ON_MATCH = "onMatch";
        @PluginBuilderAttribute("onMatch")
        private Result onMatch;
        @PluginBuilderAttribute("onMismatch")
        private Result onMismatch;
        
        public AbstractFilterBuilder() {
            this.onMatch = Result.NEUTRAL;
            this.onMismatch = Result.DENY;
        }
        
        public Result getOnMatch() {
            return this.onMatch;
        }
        
        public Result getOnMismatch() {
            return this.onMismatch;
        }
        
        public B setOnMatch(final Result onMatch) {
            this.onMatch = onMatch;
            return this.asBuilder();
        }
        
        public B setOnMismatch(final Result onMismatch) {
            this.onMismatch = onMismatch;
            return this.asBuilder();
        }
        
        public B asBuilder() {
            return (B)this;
        }
    }
}
