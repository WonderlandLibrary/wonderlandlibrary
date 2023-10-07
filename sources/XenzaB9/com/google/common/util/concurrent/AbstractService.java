// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.util.concurrent;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.concurrent.Executor;
import com.google.common.base.Preconditions;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.Collections;
import java.util.ArrayList;
import javax.annotation.concurrent.GuardedBy;
import java.util.List;
import com.google.common.annotations.Beta;

@Beta
public abstract class AbstractService implements Service
{
    private static final ListenerCallQueue.Callback<Listener> STARTING_CALLBACK;
    private static final ListenerCallQueue.Callback<Listener> RUNNING_CALLBACK;
    private static final ListenerCallQueue.Callback<Listener> STOPPING_FROM_STARTING_CALLBACK;
    private static final ListenerCallQueue.Callback<Listener> STOPPING_FROM_RUNNING_CALLBACK;
    private static final ListenerCallQueue.Callback<Listener> TERMINATED_FROM_NEW_CALLBACK;
    private static final ListenerCallQueue.Callback<Listener> TERMINATED_FROM_RUNNING_CALLBACK;
    private static final ListenerCallQueue.Callback<Listener> TERMINATED_FROM_STOPPING_CALLBACK;
    private final Monitor monitor;
    private final Monitor.Guard isStartable;
    private final Monitor.Guard isStoppable;
    private final Monitor.Guard hasReachedRunning;
    private final Monitor.Guard isStopped;
    @GuardedBy("monitor")
    private final List<ListenerCallQueue<Listener>> listeners;
    @GuardedBy("monitor")
    private volatile StateSnapshot snapshot;
    
    private static ListenerCallQueue.Callback<Listener> terminatedCallback(final State from) {
        final String value = String.valueOf(String.valueOf(from));
        return new ListenerCallQueue.Callback<Listener>(new StringBuilder(21 + value.length()).append("terminated({from = ").append(value).append("})").toString()) {
            @Override
            void call(final Listener listener) {
                listener.terminated(from);
            }
        };
    }
    
    private static ListenerCallQueue.Callback<Listener> stoppingCallback(final State from) {
        final String value = String.valueOf(String.valueOf(from));
        return new ListenerCallQueue.Callback<Listener>(new StringBuilder(19 + value.length()).append("stopping({from = ").append(value).append("})").toString()) {
            @Override
            void call(final Listener listener) {
                listener.stopping(from);
            }
        };
    }
    
    protected AbstractService() {
        this.monitor = new Monitor();
        this.isStartable = new Monitor.Guard(this.monitor) {
            @Override
            public boolean isSatisfied() {
                return AbstractService.this.state() == State.NEW;
            }
        };
        this.isStoppable = new Monitor.Guard(this.monitor) {
            @Override
            public boolean isSatisfied() {
                return AbstractService.this.state().compareTo(State.RUNNING) <= 0;
            }
        };
        this.hasReachedRunning = new Monitor.Guard(this.monitor) {
            @Override
            public boolean isSatisfied() {
                return AbstractService.this.state().compareTo(State.RUNNING) >= 0;
            }
        };
        this.isStopped = new Monitor.Guard(this.monitor) {
            @Override
            public boolean isSatisfied() {
                return AbstractService.this.state().isTerminal();
            }
        };
        this.listeners = Collections.synchronizedList(new ArrayList<ListenerCallQueue<Listener>>());
        this.snapshot = new StateSnapshot(State.NEW);
    }
    
    protected abstract void doStart();
    
    protected abstract void doStop();
    
    @Override
    public final Service startAsync() {
        if (this.monitor.enterIf(this.isStartable)) {
            try {
                this.snapshot = new StateSnapshot(State.STARTING);
                this.starting();
                this.doStart();
            }
            catch (final Throwable startupFailure) {
                this.notifyFailed(startupFailure);
            }
            finally {
                this.monitor.leave();
                this.executeListeners();
            }
            return this;
        }
        final String value = String.valueOf(String.valueOf(this));
        throw new IllegalStateException(new StringBuilder(33 + value.length()).append("Service ").append(value).append(" has already been started").toString());
    }
    
    @Override
    public final Service stopAsync() {
        if (this.monitor.enterIf(this.isStoppable)) {
            try {
                final State previous = this.state();
                switch (previous) {
                    case NEW: {
                        this.snapshot = new StateSnapshot(State.TERMINATED);
                        this.terminated(State.NEW);
                        break;
                    }
                    case STARTING: {
                        this.snapshot = new StateSnapshot(State.STARTING, true, null);
                        this.stopping(State.STARTING);
                        break;
                    }
                    case RUNNING: {
                        this.snapshot = new StateSnapshot(State.STOPPING);
                        this.stopping(State.RUNNING);
                        this.doStop();
                        break;
                    }
                    case STOPPING:
                    case TERMINATED:
                    case FAILED: {
                        final String value = String.valueOf(String.valueOf(previous));
                        throw new AssertionError((Object)new StringBuilder(45 + value.length()).append("isStoppable is incorrectly implemented, saw: ").append(value).toString());
                    }
                    default: {
                        final String value2 = String.valueOf(String.valueOf(previous));
                        throw new AssertionError((Object)new StringBuilder(18 + value2.length()).append("Unexpected state: ").append(value2).toString());
                    }
                }
            }
            catch (final Throwable shutdownFailure) {
                this.notifyFailed(shutdownFailure);
            }
            finally {
                this.monitor.leave();
                this.executeListeners();
            }
        }
        return this;
    }
    
    @Override
    public final void awaitRunning() {
        this.monitor.enterWhenUninterruptibly(this.hasReachedRunning);
        try {
            this.checkCurrentState(State.RUNNING);
        }
        finally {
            this.monitor.leave();
        }
    }
    
    @Override
    public final void awaitRunning(final long timeout, final TimeUnit unit) throws TimeoutException {
        if (this.monitor.enterWhenUninterruptibly(this.hasReachedRunning, timeout, unit)) {
            try {
                this.checkCurrentState(State.RUNNING);
            }
            finally {
                this.monitor.leave();
            }
            return;
        }
        final String value = String.valueOf(String.valueOf(this));
        final String value2 = String.valueOf(String.valueOf(this.state()));
        throw new TimeoutException(new StringBuilder(66 + value.length() + value2.length()).append("Timed out waiting for ").append(value).append(" to reach the RUNNING state. ").append("Current state: ").append(value2).toString());
    }
    
    @Override
    public final void awaitTerminated() {
        this.monitor.enterWhenUninterruptibly(this.isStopped);
        try {
            this.checkCurrentState(State.TERMINATED);
        }
        finally {
            this.monitor.leave();
        }
    }
    
    @Override
    public final void awaitTerminated(final long timeout, final TimeUnit unit) throws TimeoutException {
        if (this.monitor.enterWhenUninterruptibly(this.isStopped, timeout, unit)) {
            try {
                this.checkCurrentState(State.TERMINATED);
            }
            finally {
                this.monitor.leave();
            }
            return;
        }
        final String value = String.valueOf(String.valueOf(this));
        final String value2 = String.valueOf(String.valueOf(this.state()));
        throw new TimeoutException(new StringBuilder(65 + value.length() + value2.length()).append("Timed out waiting for ").append(value).append(" to reach a terminal state. ").append("Current state: ").append(value2).toString());
    }
    
    @GuardedBy("monitor")
    private void checkCurrentState(final State expected) {
        final State actual = this.state();
        if (actual == expected) {
            return;
        }
        if (actual == State.FAILED) {
            final String value = String.valueOf(String.valueOf(expected));
            throw new IllegalStateException(new StringBuilder(55 + value.length()).append("Expected the service to be ").append(value).append(", but the service has FAILED").toString(), this.failureCause());
        }
        final String value2 = String.valueOf(String.valueOf(expected));
        final String value3 = String.valueOf(String.valueOf(actual));
        throw new IllegalStateException(new StringBuilder(37 + value2.length() + value3.length()).append("Expected the service to be ").append(value2).append(", but was ").append(value3).toString());
    }
    
    protected final void notifyStarted() {
        this.monitor.enter();
        try {
            if (this.snapshot.state != State.STARTING) {
                final String value = String.valueOf(String.valueOf(this.snapshot.state));
                final IllegalStateException failure = new IllegalStateException(new StringBuilder(43 + value.length()).append("Cannot notifyStarted() when the service is ").append(value).toString());
                this.notifyFailed(failure);
                throw failure;
            }
            if (this.snapshot.shutdownWhenStartupFinishes) {
                this.snapshot = new StateSnapshot(State.STOPPING);
                this.doStop();
            }
            else {
                this.snapshot = new StateSnapshot(State.RUNNING);
                this.running();
            }
        }
        finally {
            this.monitor.leave();
            this.executeListeners();
        }
    }
    
    protected final void notifyStopped() {
        this.monitor.enter();
        try {
            final State previous = this.snapshot.state;
            if (previous != State.STOPPING && previous != State.RUNNING) {
                final String value = String.valueOf(String.valueOf(previous));
                final IllegalStateException failure = new IllegalStateException(new StringBuilder(43 + value.length()).append("Cannot notifyStopped() when the service is ").append(value).toString());
                this.notifyFailed(failure);
                throw failure;
            }
            this.snapshot = new StateSnapshot(State.TERMINATED);
            this.terminated(previous);
        }
        finally {
            this.monitor.leave();
            this.executeListeners();
        }
    }
    
    protected final void notifyFailed(final Throwable cause) {
        Preconditions.checkNotNull(cause);
        this.monitor.enter();
        try {
            final State previous = this.state();
            switch (previous) {
                case NEW:
                case TERMINATED: {
                    final String value = String.valueOf(String.valueOf(previous));
                    throw new IllegalStateException(new StringBuilder(22 + value.length()).append("Failed while in state:").append(value).toString(), cause);
                }
                case STARTING:
                case RUNNING:
                case STOPPING: {
                    this.snapshot = new StateSnapshot(State.FAILED, false, cause);
                    this.failed(previous, cause);
                    break;
                }
                case FAILED: {
                    break;
                }
                default: {
                    final String value2 = String.valueOf(String.valueOf(previous));
                    throw new AssertionError((Object)new StringBuilder(18 + value2.length()).append("Unexpected state: ").append(value2).toString());
                }
            }
        }
        finally {
            this.monitor.leave();
            this.executeListeners();
        }
    }
    
    @Override
    public final boolean isRunning() {
        return this.state() == State.RUNNING;
    }
    
    @Override
    public final State state() {
        return this.snapshot.externalState();
    }
    
    @Override
    public final Throwable failureCause() {
        return this.snapshot.failureCause();
    }
    
    @Override
    public final void addListener(final Listener listener, final Executor executor) {
        Preconditions.checkNotNull(listener, (Object)"listener");
        Preconditions.checkNotNull(executor, (Object)"executor");
        this.monitor.enter();
        try {
            if (!this.state().isTerminal()) {
                this.listeners.add(new ListenerCallQueue<Listener>(listener, executor));
            }
        }
        finally {
            this.monitor.leave();
        }
    }
    
    @Override
    public String toString() {
        final String value = String.valueOf(String.valueOf(this.getClass().getSimpleName()));
        final String value2 = String.valueOf(String.valueOf(this.state()));
        return new StringBuilder(3 + value.length() + value2.length()).append(value).append(" [").append(value2).append("]").toString();
    }
    
    private void executeListeners() {
        if (!this.monitor.isOccupiedByCurrentThread()) {
            for (int i = 0; i < this.listeners.size(); ++i) {
                this.listeners.get(i).execute();
            }
        }
    }
    
    @GuardedBy("monitor")
    private void starting() {
        AbstractService.STARTING_CALLBACK.enqueueOn(this.listeners);
    }
    
    @GuardedBy("monitor")
    private void running() {
        AbstractService.RUNNING_CALLBACK.enqueueOn(this.listeners);
    }
    
    @GuardedBy("monitor")
    private void stopping(final State from) {
        if (from == State.STARTING) {
            AbstractService.STOPPING_FROM_STARTING_CALLBACK.enqueueOn(this.listeners);
        }
        else {
            if (from != State.RUNNING) {
                throw new AssertionError();
            }
            AbstractService.STOPPING_FROM_RUNNING_CALLBACK.enqueueOn(this.listeners);
        }
    }
    
    @GuardedBy("monitor")
    private void terminated(final State from) {
        switch (from) {
            case NEW: {
                AbstractService.TERMINATED_FROM_NEW_CALLBACK.enqueueOn(this.listeners);
                break;
            }
            case RUNNING: {
                AbstractService.TERMINATED_FROM_RUNNING_CALLBACK.enqueueOn(this.listeners);
                break;
            }
            case STOPPING: {
                AbstractService.TERMINATED_FROM_STOPPING_CALLBACK.enqueueOn(this.listeners);
                break;
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    @GuardedBy("monitor")
    private void failed(final State from, final Throwable cause) {
        final String value = String.valueOf(String.valueOf(from));
        final String value2 = String.valueOf(String.valueOf(cause));
        new ListenerCallQueue.Callback<Listener>(new StringBuilder(27 + value.length() + value2.length()).append("failed({from = ").append(value).append(", cause = ").append(value2).append("})").toString()) {
            @Override
            void call(final Listener listener) {
                listener.failed(from, cause);
            }
        }.enqueueOn(this.listeners);
    }
    
    static {
        STARTING_CALLBACK = new ListenerCallQueue.Callback<Listener>("starting()") {
            @Override
            void call(final Listener listener) {
                listener.starting();
            }
        };
        RUNNING_CALLBACK = new ListenerCallQueue.Callback<Listener>("running()") {
            @Override
            void call(final Listener listener) {
                listener.running();
            }
        };
        STOPPING_FROM_STARTING_CALLBACK = stoppingCallback(State.STARTING);
        STOPPING_FROM_RUNNING_CALLBACK = stoppingCallback(State.RUNNING);
        TERMINATED_FROM_NEW_CALLBACK = terminatedCallback(State.NEW);
        TERMINATED_FROM_RUNNING_CALLBACK = terminatedCallback(State.RUNNING);
        TERMINATED_FROM_STOPPING_CALLBACK = terminatedCallback(State.STOPPING);
    }
    
    @Immutable
    private static final class StateSnapshot
    {
        final State state;
        final boolean shutdownWhenStartupFinishes;
        @Nullable
        final Throwable failure;
        
        StateSnapshot(final State internalState) {
            this(internalState, false, null);
        }
        
        StateSnapshot(final State internalState, final boolean shutdownWhenStartupFinishes, @Nullable final Throwable failure) {
            Preconditions.checkArgument(!shutdownWhenStartupFinishes || internalState == State.STARTING, "shudownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", internalState);
            Preconditions.checkArgument(!(failure != null ^ internalState == State.FAILED), "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", internalState, failure);
            this.state = internalState;
            this.shutdownWhenStartupFinishes = shutdownWhenStartupFinishes;
            this.failure = failure;
        }
        
        State externalState() {
            if (this.shutdownWhenStartupFinishes && this.state == State.STARTING) {
                return State.STOPPING;
            }
            return this.state;
        }
        
        Throwable failureCause() {
            Preconditions.checkState(this.state == State.FAILED, "failureCause() is only valid if the service has failed, service is %s", this.state);
            return this.failure;
        }
    }
}
