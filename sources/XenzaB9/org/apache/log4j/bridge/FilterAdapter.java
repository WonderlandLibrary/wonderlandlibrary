// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.bridge;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.CompositeFilter;
import org.apache.log4j.spi.Filter;
import org.apache.logging.log4j.core.filter.AbstractFilter;

public class FilterAdapter extends AbstractFilter
{
    private final org.apache.log4j.spi.Filter filter;
    
    public static Filter adapt(final org.apache.log4j.spi.Filter filter) {
        if (filter instanceof Filter) {
            return (Filter)filter;
        }
        if (filter instanceof FilterWrapper && filter.getNext() == null) {
            return ((FilterWrapper)filter).getFilter();
        }
        if (filter != null) {
            return new FilterAdapter(filter);
        }
        return null;
    }
    
    public static org.apache.log4j.spi.Filter addFilter(final org.apache.log4j.spi.Filter first, final org.apache.log4j.spi.Filter second) {
        if (first == null) {
            return second;
        }
        if (second == null) {
            return first;
        }
        CompositeFilter composite;
        if (first instanceof FilterWrapper && ((FilterWrapper)first).getFilter() instanceof CompositeFilter) {
            composite = (CompositeFilter)((FilterWrapper)first).getFilter();
        }
        else {
            composite = CompositeFilter.createFilters(new Filter[] { adapt(first) });
        }
        return FilterWrapper.adapt(composite.addFilter(adapt(second)));
    }
    
    private FilterAdapter(final org.apache.log4j.spi.Filter filter) {
        this.filter = filter;
    }
    
    @Override
    public Filter.Result filter(final LogEvent event) {
        final LoggingEvent loggingEvent = new LogEventAdapter(event);
        org.apache.log4j.spi.Filter next = this.filter;
        while (next != null) {
            switch (next.decide(loggingEvent)) {
                case 1: {
                    return Filter.Result.ACCEPT;
                }
                case -1: {
                    return Filter.Result.DENY;
                }
                default: {
                    next = next.getNext();
                    continue;
                }
            }
        }
        return Filter.Result.NEUTRAL;
    }
    
    public org.apache.log4j.spi.Filter getFilter() {
        return this.filter;
    }
    
    @Override
    public void start() {
        this.filter.activateOptions();
    }
}
