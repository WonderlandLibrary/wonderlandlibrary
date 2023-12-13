// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.bridge;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.Filter;

public class FilterWrapper extends Filter
{
    private final org.apache.logging.log4j.core.Filter filter;
    
    public static Filter adapt(final org.apache.logging.log4j.core.Filter filter) {
        if (filter instanceof Filter) {
            return (Filter)filter;
        }
        if (filter instanceof FilterAdapter) {
            return ((FilterAdapter)filter).getFilter();
        }
        if (filter != null) {
            return new FilterWrapper(filter);
        }
        return null;
    }
    
    public FilterWrapper(final org.apache.logging.log4j.core.Filter filter) {
        this.filter = filter;
    }
    
    public org.apache.logging.log4j.core.Filter getFilter() {
        return this.filter;
    }
    
    @Override
    public int decide(final LoggingEvent event) {
        return 0;
    }
}
