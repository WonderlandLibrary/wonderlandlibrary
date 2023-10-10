// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.Supplier;

public interface LogBuilder
{
    public static final LogBuilder NOOP = new LogBuilder() {};
    
    default LogBuilder withMarker(final Marker marker) {
        return this;
    }
    
    default LogBuilder withThrowable(final Throwable throwable) {
        return this;
    }
    
    default LogBuilder withLocation() {
        return this;
    }
    
    default LogBuilder withLocation(final StackTraceElement location) {
        return this;
    }
    
    default void log(final CharSequence message) {
    }
    
    default void log(final String message) {
    }
    
    default void log(final String message, final Object... params) {
    }
    
    default void log(final String message, final Supplier<?>... params) {
    }
    
    default void log(final Message message) {
    }
    
    default void log(final Supplier<Message> messageSupplier) {
    }
    
    default void log(final Object message) {
    }
    
    default void log(final String message, final Object p0) {
    }
    
    default void log(final String message, final Object p0, final Object p1) {
    }
    
    default void log(final String message, final Object p0, final Object p1, final Object p2) {
    }
    
    default void log(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
    }
    
    default void log(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
    }
    
    default void log(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
    }
    
    default void log(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
    }
    
    default void log(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
    }
    
    default void log(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
    }
    
    default void log(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
    }
    
    default void log() {
    }
}
