// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.util.StackLocatorUtil;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.MessageFactory;

public class ExtendedLoggerWrapper extends AbstractLogger
{
    private static final long serialVersionUID = 1L;
    protected final ExtendedLogger logger;
    
    public ExtendedLoggerWrapper(final ExtendedLogger logger, final String name, final MessageFactory messageFactory) {
        super(name, messageFactory);
        this.logger = logger;
    }
    
    @Override
    public Level getLevel() {
        return this.logger.getLevel();
    }
    
    @Override
    public boolean isEnabled(final Level level, final Marker marker, final Message message, final Throwable t) {
        return this.logger.isEnabled(level, marker, message, t);
    }
    
    @Override
    public boolean isEnabled(final Level level, final Marker marker, final CharSequence message, final Throwable t) {
        return this.logger.isEnabled(level, marker, message, t);
    }
    
    @Override
    public boolean isEnabled(final Level level, final Marker marker, final Object message, final Throwable t) {
        return this.logger.isEnabled(level, marker, message, t);
    }
    
    @Override
    public boolean isEnabled(final Level level, final Marker marker, final String message) {
        return this.logger.isEnabled(level, marker, message);
    }
    
    @Override
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object... params) {
        return this.logger.isEnabled(level, marker, message, params);
    }
    
    @Override
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0) {
        return this.logger.isEnabled(level, marker, message, p0);
    }
    
    @Override
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1) {
        return this.logger.isEnabled(level, marker, message, p0, p1);
    }
    
    @Override
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
        return this.logger.isEnabled(level, marker, message, p0, p1, p2);
    }
    
    @Override
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3);
    }
    
    @Override
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4);
    }
    
    @Override
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5);
    }
    
    @Override
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6);
    }
    
    @Override
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }
    
    @Override
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }
    
    @Override
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
    
    @Override
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Throwable t) {
        return this.logger.isEnabled(level, marker, message, t);
    }
    
    @Override
    public void logMessage(final String fqcn, final Level level, final Marker marker, final Message message, final Throwable t) {
        if (this.logger instanceof LocationAwareLogger && this.requiresLocation()) {
            ((LocationAwareLogger)this.logger).logMessage(level, marker, fqcn, StackLocatorUtil.calcLocation(fqcn), message, t);
        }
        this.logger.logMessage(fqcn, level, marker, message, t);
    }
}
