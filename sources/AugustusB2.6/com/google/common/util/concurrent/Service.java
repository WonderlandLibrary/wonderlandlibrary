// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.time.Duration;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Create an AbstractIdleService")
@ElementTypesAreNonnullByDefault
@GwtIncompatible
public interface Service
{
    @CanIgnoreReturnValue
    Service startAsync();
    
    boolean isRunning();
    
    State state();
    
    @CanIgnoreReturnValue
    Service stopAsync();
    
    void awaitRunning();
    
    default void awaitRunning(final Duration timeout) throws TimeoutException {
        this.awaitRunning(Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
    }
    
    void awaitRunning(final long p0, final TimeUnit p1) throws TimeoutException;
    
    void awaitTerminated();
    
    default void awaitTerminated(final Duration timeout) throws TimeoutException {
        this.awaitTerminated(Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
    }
    
    void awaitTerminated(final long p0, final TimeUnit p1) throws TimeoutException;
    
    Throwable failureCause();
    
    void addListener(final Listener p0, final Executor p1);
    
    public enum State
    {
        NEW, 
        STARTING, 
        RUNNING, 
        STOPPING, 
        TERMINATED, 
        FAILED;
        
        private static /* synthetic */ State[] $values() {
            return new State[] { State.NEW, State.STARTING, State.RUNNING, State.STOPPING, State.TERMINATED, State.FAILED };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    public abstract static class Listener
    {
        public void starting() {
        }
        
        public void running() {
        }
        
        public void stopping(final State from) {
        }
        
        public void terminated(final State from) {
        }
        
        public void failed(final State from, final Throwable failure) {
        }
    }
}
