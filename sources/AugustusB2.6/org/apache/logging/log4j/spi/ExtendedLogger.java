// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.util.Supplier;
import org.apache.logging.log4j.util.MessageSupplier;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public interface ExtendedLogger extends Logger
{
    boolean isEnabled(final Level level, final Marker marker, final Message message, final Throwable t);
    
    boolean isEnabled(final Level level, final Marker marker, final CharSequence message, final Throwable t);
    
    boolean isEnabled(final Level level, final Marker marker, final Object message, final Throwable t);
    
    boolean isEnabled(final Level level, final Marker marker, final String message, final Throwable t);
    
    boolean isEnabled(final Level level, final Marker marker, final String message);
    
    boolean isEnabled(final Level level, final Marker marker, final String message, final Object... params);
    
    boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0);
    
    boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1);
    
    boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2);
    
    boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3);
    
    boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4);
    
    boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5);
    
    boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6);
    
    boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7);
    
    boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8);
    
    boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9);
    
    void logIfEnabled(final String fqcn, final Level level, final Marker marker, final Message message, final Throwable t);
    
    void logIfEnabled(final String fqcn, final Level level, final Marker marker, final CharSequence message, final Throwable t);
    
    void logIfEnabled(final String fqcn, final Level level, final Marker marker, final Object message, final Throwable t);
    
    void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Throwable t);
    
    void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message);
    
    void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object... params);
    
    void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object p0);
    
    void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1);
    
    void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2);
    
    void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3);
    
    void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4);
    
    void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5);
    
    void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6);
    
    void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7);
    
    void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8);
    
    void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9);
    
    void logMessage(final String fqcn, final Level level, final Marker marker, final Message message, final Throwable t);
    
    void logIfEnabled(final String fqcn, final Level level, final Marker marker, final MessageSupplier msgSupplier, final Throwable t);
    
    void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Supplier<?>... paramSuppliers);
    
    void logIfEnabled(final String fqcn, final Level level, final Marker marker, final Supplier<?> msgSupplier, final Throwable t);
}
