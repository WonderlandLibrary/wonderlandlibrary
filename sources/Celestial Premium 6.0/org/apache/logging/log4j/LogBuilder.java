/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.Supplier;

public interface LogBuilder {
    public static final LogBuilder NOOP = new LogBuilder(){};

    default public LogBuilder withMarker(Marker marker) {
        return this;
    }

    default public LogBuilder withThrowable(Throwable throwable) {
        return this;
    }

    default public LogBuilder withLocation() {
        return this;
    }

    default public LogBuilder withLocation(StackTraceElement location) {
        return this;
    }

    default public void log(CharSequence message) {
    }

    default public void log(String message) {
    }

    default public void log(String message, Object ... params) {
    }

    default public void log(String message, Supplier<?> ... params) {
    }

    default public void log(Message message) {
    }

    default public void log(Supplier<Message> messageSupplier) {
    }

    default public void log(Object message) {
    }

    default public void log(String message, Object p0) {
    }

    default public void log(String message, Object p0, Object p1) {
    }

    default public void log(String message, Object p0, Object p1, Object p2) {
    }

    default public void log(String message, Object p0, Object p1, Object p2, Object p3) {
    }

    default public void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    }

    default public void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    }

    default public void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    }

    default public void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    }

    default public void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    }

    default public void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    }

    default public void log() {
    }
}

