// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j;

import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.Filter;

public interface Appender
{
    void addFilter(final Filter newFilter);
    
    Filter getFilter();
    
    void clearFilters();
    
    void close();
    
    void doAppend(final LoggingEvent event);
    
    String getName();
    
    void setErrorHandler(final ErrorHandler errorHandler);
    
    ErrorHandler getErrorHandler();
    
    void setLayout(final Layout layout);
    
    Layout getLayout();
    
    void setName(final String name);
    
    boolean requiresLayout();
}
