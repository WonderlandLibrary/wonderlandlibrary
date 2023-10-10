// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.internal;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.util.LambdaUtil;
import org.apache.logging.log4j.util.Supplier;
import org.apache.logging.log4j.util.StackLocatorUtil;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.LogBuilder;

public class DefaultLogBuilder implements LogBuilder
{
    private static Message EMPTY_MESSAGE;
    private static final String FQCN;
    private static final Logger LOGGER;
    private final Logger logger;
    private Level level;
    private Marker marker;
    private Throwable throwable;
    private StackTraceElement location;
    private volatile boolean inUse;
    private long threadId;
    
    public DefaultLogBuilder(final Logger logger, final Level level) {
        this.logger = logger;
        this.level = level;
        this.threadId = Thread.currentThread().getId();
        this.inUse = true;
    }
    
    public DefaultLogBuilder(final Logger logger) {
        this.logger = logger;
        this.inUse = false;
        this.threadId = Thread.currentThread().getId();
    }
    
    public LogBuilder reset(final Level level) {
        this.inUse = true;
        this.level = level;
        this.marker = null;
        this.throwable = null;
        this.location = null;
        return this;
    }
    
    @Override
    public LogBuilder withMarker(final Marker marker) {
        this.marker = marker;
        return this;
    }
    
    @Override
    public LogBuilder withThrowable(final Throwable throwable) {
        this.throwable = throwable;
        return this;
    }
    
    @Override
    public LogBuilder withLocation() {
        this.location = StackLocatorUtil.getStackTraceElement(2);
        return this;
    }
    
    @Override
    public LogBuilder withLocation(final StackTraceElement location) {
        this.location = location;
        return this;
    }
    
    public boolean isInUse() {
        return this.inUse;
    }
    
    @Override
    public void log(final Message message) {
        if (this.isValid()) {
            this.logMessage(message);
        }
    }
    
    @Override
    public void log(final CharSequence message) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message));
        }
    }
    
    @Override
    public void log(final String message) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message));
        }
    }
    
    @Override
    public void log(final String message, final Object... params) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, params));
        }
    }
    
    @Override
    public void log(final String message, final Supplier<?>... params) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, LambdaUtil.getAll(params)));
        }
    }
    
    @Override
    public void log(final Supplier<Message> messageSupplier) {
        if (this.isValid()) {
            this.logMessage(messageSupplier.get());
        }
    }
    
    @Override
    public void log(final Object message) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message));
        }
    }
    
    @Override
    public void log(final String message, final Object p0) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, p0));
        }
    }
    
    @Override
    public void log(final String message, final Object p0, final Object p1) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, p0, p1));
        }
    }
    
    @Override
    public void log(final String message, final Object p0, final Object p1, final Object p2) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, p0, p1, p2));
        }
    }
    
    @Override
    public void log(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, p0, p1, p2, p3));
        }
    }
    
    @Override
    public void log(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, p0, p1, p2, p3, p4));
        }
    }
    
    @Override
    public void log(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, p0, p1, p2, p3, p4, p5));
        }
    }
    
    @Override
    public void log(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, p0, p1, p2, p3, p4, p5, p6));
        }
    }
    
    @Override
    public void log(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7));
        }
    }
    
    @Override
    public void log(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8));
        }
    }
    
    @Override
    public void log(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        if (this.isValid()) {
            this.logMessage(this.logger.getMessageFactory().newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9));
        }
    }
    
    @Override
    public void log() {
        if (this.isValid()) {
            this.logMessage(DefaultLogBuilder.EMPTY_MESSAGE);
        }
    }
    
    private void logMessage(final Message message) {
        try {
            this.logger.logMessage(this.level, this.marker, DefaultLogBuilder.FQCN, this.location, message, this.throwable);
        }
        finally {
            this.inUse = false;
        }
    }
    
    private boolean isValid() {
        if (!this.inUse) {
            DefaultLogBuilder.LOGGER.warn("Attempt to reuse LogBuilder was ignored. {}", StackLocatorUtil.getCallerClass(2));
            return false;
        }
        if (this.threadId != Thread.currentThread().getId()) {
            DefaultLogBuilder.LOGGER.warn("LogBuilder can only be used on the owning thread. {}", StackLocatorUtil.getCallerClass(2));
            return false;
        }
        return true;
    }
    
    static {
        DefaultLogBuilder.EMPTY_MESSAGE = new SimpleMessage("");
        FQCN = DefaultLogBuilder.class.getName();
        LOGGER = StatusLogger.getLogger();
    }
}
