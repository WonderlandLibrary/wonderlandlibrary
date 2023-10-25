/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.message.Message;

@Plugin(name="filters", category="Core", printObject=true)
public final class CompositeFilter
implements Iterable<Filter>,
Filter,
LifeCycle {
    private final List<Filter> filters;
    private final boolean hasFilters;
    private boolean isStarted;

    private CompositeFilter() {
        this.filters = new ArrayList<Filter>();
        this.hasFilters = false;
    }

    private CompositeFilter(List<Filter> filters) {
        if (filters == null) {
            this.filters = Collections.unmodifiableList(new ArrayList());
            this.hasFilters = false;
            return;
        }
        this.filters = Collections.unmodifiableList(filters);
        this.hasFilters = this.filters.size() > 0;
    }

    public CompositeFilter addFilter(Filter filter) {
        ArrayList<Filter> filters = new ArrayList<Filter>(this.filters);
        filters.add(filter);
        return new CompositeFilter(Collections.unmodifiableList(filters));
    }

    public CompositeFilter removeFilter(Filter filter) {
        ArrayList<Filter> filters = new ArrayList<Filter>(this.filters);
        filters.remove(filter);
        return new CompositeFilter(Collections.unmodifiableList(filters));
    }

    @Override
    public Iterator<Filter> iterator() {
        return this.filters.iterator();
    }

    public List<Filter> getFilters() {
        return this.filters;
    }

    public boolean hasFilters() {
        return this.hasFilters;
    }

    public int size() {
        return this.filters.size();
    }

    @Override
    public void start() {
        for (Filter filter : this.filters) {
            if (!(filter instanceof LifeCycle)) continue;
            ((LifeCycle)((Object)filter)).start();
        }
        this.isStarted = true;
    }

    @Override
    public void stop() {
        for (Filter filter : this.filters) {
            if (!(filter instanceof LifeCycle)) continue;
            ((LifeCycle)((Object)filter)).stop();
        }
        this.isStarted = false;
    }

    @Override
    public boolean isStarted() {
        return this.isStarted;
    }

    @Override
    public Filter.Result getOnMismatch() {
        return Filter.Result.NEUTRAL;
    }

    @Override
    public Filter.Result getOnMatch() {
        return Filter.Result.NEUTRAL;
    }

    @Override
    public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object ... params) {
        Filter.Result result = Filter.Result.NEUTRAL;
        for (Filter filter : this.filters) {
            result = filter.filter(logger, level, marker, msg, params);
            if (result != Filter.Result.ACCEPT && result != Filter.Result.DENY) continue;
            return result;
        }
        return result;
    }

    @Override
    public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        Filter.Result result = Filter.Result.NEUTRAL;
        for (Filter filter : this.filters) {
            result = filter.filter(logger, level, marker, msg, t);
            if (result != Filter.Result.ACCEPT && result != Filter.Result.DENY) continue;
            return result;
        }
        return result;
    }

    @Override
    public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        Filter.Result result = Filter.Result.NEUTRAL;
        for (Filter filter : this.filters) {
            result = filter.filter(logger, level, marker, msg, t);
            if (result != Filter.Result.ACCEPT && result != Filter.Result.DENY) continue;
            return result;
        }
        return result;
    }

    @Override
    public Filter.Result filter(LogEvent event) {
        Filter.Result result = Filter.Result.NEUTRAL;
        for (Filter filter : this.filters) {
            result = filter.filter(event);
            if (result != Filter.Result.ACCEPT && result != Filter.Result.DENY) continue;
            return result;
        }
        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Filter filter : this.filters) {
            if (sb.length() == 0) {
                sb.append("{");
            } else {
                sb.append(", ");
            }
            sb.append(filter.toString());
        }
        if (sb.length() > 0) {
            sb.append("}");
        }
        return sb.toString();
    }

    @PluginFactory
    public static CompositeFilter createFilters(@PluginElement(value="Filters") Filter[] filters) {
        ArrayList<Filter> f = filters == null || filters.length == 0 ? new ArrayList() : Arrays.asList(filters);
        return new CompositeFilter(f);
    }
}

