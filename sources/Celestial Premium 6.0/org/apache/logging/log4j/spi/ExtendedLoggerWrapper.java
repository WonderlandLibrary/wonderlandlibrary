/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LocationAwareLogger;
import org.apache.logging.log4j.util.StackLocatorUtil;

public class ExtendedLoggerWrapper
extends AbstractLogger {
    private static final long serialVersionUID = 1L;
    protected final ExtendedLogger logger;

    public ExtendedLoggerWrapper(ExtendedLogger logger, String name, MessageFactory messageFactory) {
        super(name, messageFactory);
        this.logger = logger;
    }

    public Level getLevel() {
        return this.logger.getLevel();
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, Message message, Throwable t) {
        return this.logger.isEnabled(level, marker, message, t);
    }

    public boolean isEnabled(Level level, Marker marker, CharSequence message, Throwable t) {
        return this.logger.isEnabled(level, marker, message, t);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, Object message, Throwable t) {
        return this.logger.isEnabled(level, marker, message, t);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message) {
        return this.logger.isEnabled(level, marker, message);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object ... params) {
        return this.logger.isEnabled(level, marker, message, params);
    }

    public boolean isEnabled(Level level, Marker marker, String message, Object p0) {
        return this.logger.isEnabled(level, marker, message, p0);
    }

    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1) {
        return this.logger.isEnabled(level, marker, message, p0, p1);
    }

    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
        return this.logger.isEnabled(level, marker, message, p0, p1, p2);
    }

    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
        return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3);
    }

    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4);
    }

    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5);
    }

    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6);
    }

    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Throwable t) {
        return this.logger.isEnabled(level, marker, message, t);
    }

    public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable t) {
        if (this.logger instanceof LocationAwareLogger && this.requiresLocation()) {
            ((LocationAwareLogger)((Object)this.logger)).logMessage(level, marker, fqcn, StackLocatorUtil.calcLocation(fqcn), message, t);
        }
        this.logger.logMessage(fqcn, level, marker, message, t);
    }
}

