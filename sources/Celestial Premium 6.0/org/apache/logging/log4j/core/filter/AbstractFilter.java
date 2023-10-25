/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractFilter
implements Filter,
LifeCycle {
    protected static final Logger LOGGER = StatusLogger.getLogger();
    protected final Filter.Result onMatch;
    protected final Filter.Result onMismatch;
    private boolean started;

    protected AbstractFilter() {
        this(null, null);
    }

    protected AbstractFilter(Filter.Result onMatch, Filter.Result onMismatch) {
        this.onMatch = onMatch == null ? Filter.Result.NEUTRAL : onMatch;
        this.onMismatch = onMismatch == null ? Filter.Result.DENY : onMismatch;
    }

    @Override
    public void start() {
        this.started = true;
    }

    @Override
    public boolean isStarted() {
        return this.started;
    }

    @Override
    public void stop() {
        this.started = false;
    }

    @Override
    public final Filter.Result getOnMismatch() {
        return this.onMismatch;
    }

    @Override
    public final Filter.Result getOnMatch() {
        return this.onMatch;
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Filter.Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, String msg, Object ... params) {
        return Filter.Result.NEUTRAL;
    }

    @Override
    public Filter.Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        return Filter.Result.NEUTRAL;
    }

    @Override
    public Filter.Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return Filter.Result.NEUTRAL;
    }

    @Override
    public Filter.Result filter(LogEvent event) {
        return Filter.Result.NEUTRAL;
    }
}

