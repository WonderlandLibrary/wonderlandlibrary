// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core;

import org.apache.logging.log4j.util.EnglishEnums;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;

public interface Filter extends LifeCycle
{
    public static final Filter[] EMPTY_ARRAY = new Filter[0];
    public static final String ELEMENT_TYPE = "filter";
    
    Result getOnMismatch();
    
    Result getOnMatch();
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object p0);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object p0, final Object p1);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t);
    
    Result filter(final LogEvent event);
    
    public enum Result
    {
        ACCEPT, 
        NEUTRAL, 
        DENY;
        
        public static Result toResult(final String name) {
            return toResult(name, null);
        }
        
        public static Result toResult(final String name, final Result defaultResult) {
            return EnglishEnums.valueOf(Result.class, name, defaultResult);
        }
    }
}
