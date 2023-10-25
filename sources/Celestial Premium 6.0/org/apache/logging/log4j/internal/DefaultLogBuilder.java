/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.internal;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogBuilder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.LambdaUtil;
import org.apache.logging.log4j.util.StackLocatorUtil;
import org.apache.logging.log4j.util.Supplier;

public class DefaultLogBuilder
implements LogBuilder {
    private static Message EMPTY_MESSAGE = new SimpleMessage("");
    private static final String FQCN = DefaultLogBuilder.class.getName();
    private static final Logger LOGGER = StatusLogger.getLogger();
    private final Logger logger;
    private Level level;
    private Marker marker;
    private Throwable throwable;
    private StackTraceElement location;
    private volatile boolean inUse;
    private long threadId;

    public DefaultLogBuilder(Logger logger, Level level) {
        this.logger = logger;
        this.level = level;
        this.threadId = Thread.currentThread().getId();
        this.inUse = true;
    }

    public DefaultLogBuilder(Logger logger) {
        this.logger = logger;
        this.inUse = false;
        this.threadId = Thread.currentThread().getId();
    }

    public LogBuilder reset(Level level) {
        this.inUse = true;
        this.level = level;
        this.marker = null;
        this.throwable = null;
        this.location = null;
        return this;
    }

    @Override
    public LogBuilder withMarker(Marker marker) {
        this.marker = marker;
        return this;
    }

    @Override
    public LogBuilder withThrowable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    @Override
    public LogBuilder withLocation() {
        this.location = StackLocatorUtil.getStackTraceElement(2);
        return this;
    }

    @Override
    public LogBuilder withLocation(StackTraceElement location) {
        this.location = location;
        return this;
    }

    public boolean isInUse() {
        return this.inUse;
    }

    @Override
    public void log(Message message) {
        if (this.isValid()) {
            this.logMessage(message);
        }
    }

    @Override
    public void log(CharSequence message) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message));
        }
    }

    @Override
    public void log(String message) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message));
        }
    }

    @Override
    public void log(String message, Object ... params) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, params));
        }
    }

    @Override
    public void log(String message, Supplier<?> ... params) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, LambdaUtil.getAll(params)));
        }
    }

    @Override
    public void log(Supplier<Message> messageSupplier) {
        if (this.isValid()) {
            this.logMessage(messageSupplier.get());
        }
    }

    @Override
    public void log(Object message) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message));
        }
    }

    @Override
    public void log(String message, Object p0) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, p0));
        }
    }

    @Override
    public void log(String message, Object p0, Object p1) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, p0, p1));
        }
    }

    @Override
    public void log(String message, Object p0, Object p1, Object p2) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, p0, p1, p2));
        }
    }

    @Override
    public void log(String message, Object p0, Object p1, Object p2, Object p3) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, p0, p1, p2, p3));
        }
    }

    @Override
    public void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, p0, p1, p2, p3, p4));
        }
    }

    @Override
    public void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, p0, p1, p2, p3, p4, p5));
        }
    }

    @Override
    public void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, p0, p1, p2, p3, p4, p5, p6));
        }
    }

    @Override
    public void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7));
        }
    }

    @Override
    public void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8));
        }
    }

    @Override
    public void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9));
        }
    }

    @Override
    public void log() {
        if (this.isValid()) {
            this.logMessage(EMPTY_MESSAGE);
        }
    }

    private void logMessage(Message message) {
        try {
            this.logger.logMessage(this.level, this.marker, FQCN, this.location, message, this.throwable);
        }
        finally {
            this.inUse = false;
        }
    }

    private boolean isValid() {
        if (!this.inUse) {
            LOGGER.warn("Attempt to reuse LogBuilder was ignored. {}", StackLocatorUtil.getCallerClass(2));
            return false;
        }
        if (this.threadId != Thread.currentThread().getId()) {
            LOGGER.warn("LogBuilder can only be used on the owning thread. {}", StackLocatorUtil.getCallerClass(2));
            return false;
        }
        return true;
    }
}

