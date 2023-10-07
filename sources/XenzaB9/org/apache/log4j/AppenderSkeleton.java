// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.OptionHandler;

public abstract class AppenderSkeleton implements Appender, OptionHandler
{
    protected Layout layout;
    protected String name;
    protected Priority threshold;
    protected ErrorHandler errorHandler;
    protected Filter headFilter;
    protected Filter tailFilter;
    protected boolean closed;
    
    public AppenderSkeleton() {
        this.errorHandler = new NoOpErrorHandler();
        this.closed = false;
    }
    
    protected AppenderSkeleton(final boolean isActive) {
        this.errorHandler = new NoOpErrorHandler();
        this.closed = false;
    }
    
    @Override
    public void activateOptions() {
    }
    
    @Override
    public void addFilter(final Filter newFilter) {
        if (this.headFilter == null) {
            this.tailFilter = newFilter;
            this.headFilter = newFilter;
        }
        else {
            this.tailFilter.setNext(newFilter);
            this.tailFilter = newFilter;
        }
    }
    
    protected abstract void append(final LoggingEvent event);
    
    @Override
    public void clearFilters() {
        final Filter filter = null;
        this.tailFilter = filter;
        this.headFilter = filter;
    }
    
    public void finalize() {
    }
    
    @Override
    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }
    
    @Override
    public Filter getFilter() {
        return this.headFilter;
    }
    
    public final Filter getFirstFilter() {
        return this.headFilter;
    }
    
    @Override
    public Layout getLayout() {
        return this.layout;
    }
    
    @Override
    public final String getName() {
        return this.name;
    }
    
    public Priority getThreshold() {
        return this.threshold;
    }
    
    public boolean isAsSevereAsThreshold(final Priority priority) {
        return this.threshold == null || priority.isGreaterOrEqual(this.threshold);
    }
    
    @Override
    public synchronized void doAppend(final LoggingEvent event) {
        this.append(event);
    }
    
    @Override
    public synchronized void setErrorHandler(final ErrorHandler eh) {
        if (eh != null) {
            this.errorHandler = eh;
        }
    }
    
    @Override
    public void setLayout(final Layout layout) {
        this.layout = layout;
    }
    
    @Override
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setThreshold(final Priority threshold) {
        this.threshold = threshold;
    }
    
    public static class NoOpErrorHandler implements ErrorHandler
    {
        @Override
        public void setLogger(final Logger logger) {
        }
        
        @Override
        public void error(final String message, final Exception e, final int errorCode) {
        }
        
        @Override
        public void error(final String message) {
        }
        
        @Override
        public void error(final String message, final Exception e, final int errorCode, final LoggingEvent event) {
        }
        
        @Override
        public void setAppender(final Appender appender) {
        }
        
        @Override
        public void setBackupAppender(final Appender appender) {
        }
    }
}
