// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.concurrent.Future;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.TimeUnit;

public class AbstractLifeCycle implements LifeCycle2
{
    public static final int DEFAULT_STOP_TIMEOUT = 0;
    public static final TimeUnit DEFAULT_STOP_TIMEUNIT;
    protected static final Logger LOGGER;
    private volatile LifeCycle.State state;
    
    public AbstractLifeCycle() {
        this.state = LifeCycle.State.INITIALIZED;
    }
    
    protected static Logger getStatusLogger() {
        return AbstractLifeCycle.LOGGER;
    }
    
    protected boolean equalsImpl(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final LifeCycle other = (LifeCycle)obj;
        return this.state == other.getState();
    }
    
    @Override
    public LifeCycle.State getState() {
        return this.state;
    }
    
    protected int hashCodeImpl() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.state == null) ? 0 : this.state.hashCode());
        return result;
    }
    
    public boolean isInitialized() {
        return this.state == LifeCycle.State.INITIALIZED;
    }
    
    @Override
    public boolean isStarted() {
        return this.state == LifeCycle.State.STARTED;
    }
    
    public boolean isStarting() {
        return this.state == LifeCycle.State.STARTING;
    }
    
    @Override
    public boolean isStopped() {
        return this.state == LifeCycle.State.STOPPED;
    }
    
    public boolean isStopping() {
        return this.state == LifeCycle.State.STOPPING;
    }
    
    protected void setStarted() {
        this.setState(LifeCycle.State.STARTED);
    }
    
    protected void setStarting() {
        this.setState(LifeCycle.State.STARTING);
    }
    
    protected void setState(final LifeCycle.State newState) {
        this.state = newState;
    }
    
    protected void setStopped() {
        this.setState(LifeCycle.State.STOPPED);
    }
    
    protected void setStopping() {
        this.setState(LifeCycle.State.STOPPING);
    }
    
    @Override
    public void initialize() {
        this.state = LifeCycle.State.INITIALIZED;
    }
    
    @Override
    public void start() {
        this.setStarted();
    }
    
    @Override
    public void stop() {
        this.stop(0L, AbstractLifeCycle.DEFAULT_STOP_TIMEUNIT);
    }
    
    protected boolean stop(final Future<?> future) {
        boolean stopped = true;
        if (future != null) {
            if (future.isCancelled() || future.isDone()) {
                return true;
            }
            stopped = future.cancel(true);
        }
        return stopped;
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.state = LifeCycle.State.STOPPED;
        return true;
    }
    
    static {
        DEFAULT_STOP_TIMEUNIT = TimeUnit.MILLISECONDS;
        LOGGER = StatusLogger.getLogger();
    }
}
