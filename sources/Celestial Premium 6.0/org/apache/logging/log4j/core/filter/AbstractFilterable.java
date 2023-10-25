/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.filter;

import java.util.Iterator;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.CompositeFilter;
import org.apache.logging.log4j.core.filter.Filterable;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractFilterable
implements Filterable {
    protected static final Logger LOGGER = StatusLogger.getLogger();
    private volatile Filter filter;

    protected AbstractFilterable(Filter filter) {
        this.filter = filter;
    }

    protected AbstractFilterable() {
    }

    @Override
    public Filter getFilter() {
        return this.filter;
    }

    @Override
    public synchronized void addFilter(Filter filter) {
        if (this.filter == null) {
            this.filter = filter;
        } else if (filter instanceof CompositeFilter) {
            this.filter = ((CompositeFilter)this.filter).addFilter(filter);
        } else {
            Filter[] filters = new Filter[]{this.filter, filter};
            this.filter = CompositeFilter.createFilters(filters);
        }
    }

    @Override
    public synchronized void removeFilter(Filter filter) {
        if (this.filter == filter) {
            this.filter = null;
        } else if (filter instanceof CompositeFilter) {
            CompositeFilter composite = (CompositeFilter)filter;
            if ((composite = composite.removeFilter(filter)).size() > 1) {
                this.filter = composite;
            } else if (composite.size() == 1) {
                Iterator<Filter> iter = composite.iterator();
                this.filter = iter.next();
            } else {
                this.filter = null;
            }
        }
    }

    @Override
    public boolean hasFilter() {
        return this.filter != null;
    }

    public void startFilter() {
        if (this.filter != null && this.filter instanceof LifeCycle) {
            ((LifeCycle)((Object)this.filter)).start();
        }
    }

    public void stopFilter() {
        if (this.filter != null && this.filter instanceof LifeCycle) {
            ((LifeCycle)((Object)this.filter)).stop();
        }
    }

    @Override
    public boolean isFiltered(LogEvent event) {
        return this.filter != null && this.filter.filter(event) == Filter.Result.DENY;
    }
}

