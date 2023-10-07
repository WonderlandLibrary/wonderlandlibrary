// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.bridge;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.log4j.spi.Filter;
import org.apache.logging.log4j.Logger;
import org.apache.log4j.Appender;

public class AppenderWrapper implements Appender
{
    private static final Logger LOGGER;
    private final org.apache.logging.log4j.core.Appender appender;
    
    public static Appender adapt(final org.apache.logging.log4j.core.Appender appender) {
        if (appender instanceof Appender) {
            return (Appender)appender;
        }
        if (appender instanceof AppenderAdapter.Adapter) {
            final AppenderAdapter.Adapter adapter = (AppenderAdapter.Adapter)appender;
            if (!adapter.hasFilter()) {
                return adapter.getAppender();
            }
        }
        if (appender != null) {
            return new AppenderWrapper(appender);
        }
        return null;
    }
    
    public AppenderWrapper(final org.apache.logging.log4j.core.Appender appender) {
        this.appender = appender;
    }
    
    public org.apache.logging.log4j.core.Appender getAppender() {
        return this.appender;
    }
    
    @Override
    public void addFilter(final Filter newFilter) {
        if (this.appender instanceof AbstractFilterable) {
            ((AbstractFilterable)this.appender).addFilter(FilterAdapter.adapt(newFilter));
        }
        else {
            AppenderWrapper.LOGGER.warn("Unable to add filter to appender {}, it does not support filters", this.appender.getName());
        }
    }
    
    @Override
    public Filter getFilter() {
        return null;
    }
    
    @Override
    public void clearFilters() {
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public void doAppend(final LoggingEvent event) {
        if (event instanceof LogEventAdapter) {
            this.appender.append(((LogEventAdapter)event).getEvent());
        }
    }
    
    @Override
    public String getName() {
        return this.appender.getName();
    }
    
    @Override
    public void setErrorHandler(final ErrorHandler errorHandler) {
        this.appender.setHandler(new ErrorHandlerAdapter(errorHandler));
    }
    
    @Override
    public ErrorHandler getErrorHandler() {
        return ((ErrorHandlerAdapter)this.appender.getHandler()).getHandler();
    }
    
    @Override
    public void setLayout(final Layout layout) {
    }
    
    @Override
    public Layout getLayout() {
        return new LayoutWrapper(this.appender.getLayout());
    }
    
    @Override
    public void setName(final String name) {
    }
    
    @Override
    public boolean requiresLayout() {
        return false;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
