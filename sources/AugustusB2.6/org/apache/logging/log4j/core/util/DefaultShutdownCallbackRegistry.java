// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.AbstractLifeCycle;
import java.lang.ref.WeakReference;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.lang.ref.Reference;
import java.util.Collection;
import java.util.concurrent.ThreadFactory;
import org.apache.logging.log4j.core.LifeCycle;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LifeCycle2;

public class DefaultShutdownCallbackRegistry implements ShutdownCallbackRegistry, LifeCycle2, Runnable
{
    protected static final Logger LOGGER;
    private final AtomicReference<LifeCycle.State> state;
    private final ThreadFactory threadFactory;
    private final Collection<Reference<Cancellable>> hooks;
    private Reference<Thread> shutdownHookRef;
    
    public DefaultShutdownCallbackRegistry() {
        this(Executors.defaultThreadFactory());
    }
    
    protected DefaultShutdownCallbackRegistry(final ThreadFactory threadFactory) {
        this.state = new AtomicReference<LifeCycle.State>(LifeCycle.State.INITIALIZED);
        this.hooks = new CopyOnWriteArrayList<Reference<Cancellable>>();
        this.threadFactory = threadFactory;
    }
    
    @Override
    public void run() {
        if (this.state.compareAndSet(LifeCycle.State.STARTED, LifeCycle.State.STOPPING)) {
            for (final Reference<Cancellable> hookRef : this.hooks) {
                final Cancellable hook = hookRef.get();
                if (hook != null) {
                    try {
                        hook.run();
                    }
                    catch (Throwable t1) {
                        try {
                            DefaultShutdownCallbackRegistry.LOGGER.error(DefaultShutdownCallbackRegistry.SHUTDOWN_HOOK_MARKER, "Caught exception executing shutdown hook {}", hook, t1);
                        }
                        catch (Throwable t2) {
                            System.err.println("Caught exception " + t2.getClass() + " logging exception " + t1.getClass());
                            t1.printStackTrace();
                        }
                    }
                }
            }
            this.state.set(LifeCycle.State.STOPPED);
        }
    }
    
    @Override
    public Cancellable addShutdownCallback(final Runnable callback) {
        if (this.isStarted()) {
            final Cancellable receipt = new RegisteredCancellable(callback, this.hooks);
            this.hooks.add(new SoftReference<Cancellable>(receipt));
            return receipt;
        }
        throw new IllegalStateException("Cannot add new shutdown hook as this is not started. Current state: " + this.state.get().name());
    }
    
    @Override
    public void initialize() {
    }
    
    @Override
    public void start() {
        if (this.state.compareAndSet(LifeCycle.State.INITIALIZED, LifeCycle.State.STARTING)) {
            try {
                this.addShutdownHook(this.threadFactory.newThread(this));
                this.state.set(LifeCycle.State.STARTED);
            }
            catch (IllegalStateException ex) {
                this.state.set(LifeCycle.State.STOPPED);
                throw ex;
            }
            catch (Exception e) {
                DefaultShutdownCallbackRegistry.LOGGER.catching(e);
                this.state.set(LifeCycle.State.STOPPED);
            }
        }
    }
    
    private void addShutdownHook(final Thread thread) {
        this.shutdownHookRef = new WeakReference<Thread>(thread);
        Runtime.getRuntime().addShutdownHook(thread);
    }
    
    @Override
    public void stop() {
        this.stop(0L, AbstractLifeCycle.DEFAULT_STOP_TIMEUNIT);
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        if (this.state.compareAndSet(LifeCycle.State.STARTED, LifeCycle.State.STOPPING)) {
            try {
                this.removeShutdownHook();
            }
            finally {
                this.state.set(LifeCycle.State.STOPPED);
            }
        }
        return true;
    }
    
    private void removeShutdownHook() {
        final Thread shutdownThread = this.shutdownHookRef.get();
        if (shutdownThread != null) {
            Runtime.getRuntime().removeShutdownHook(shutdownThread);
            this.shutdownHookRef.enqueue();
        }
    }
    
    @Override
    public LifeCycle.State getState() {
        return this.state.get();
    }
    
    @Override
    public boolean isStarted() {
        return this.state.get() == LifeCycle.State.STARTED;
    }
    
    @Override
    public boolean isStopped() {
        return this.state.get() == LifeCycle.State.STOPPED;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
    
    private static class RegisteredCancellable implements Cancellable
    {
        private Runnable callback;
        private Collection<Reference<Cancellable>> registered;
        
        RegisteredCancellable(final Runnable callback, final Collection<Reference<Cancellable>> registered) {
            this.callback = callback;
            this.registered = registered;
        }
        
        @Override
        public void cancel() {
            this.callback = null;
            final Collection<Reference<Cancellable>> references = this.registered;
            if (references != null) {
                this.registered = null;
                final Cancellable value;
                references.removeIf(ref -> {
                    value = ref.get();
                    return value == null || value == this;
                });
            }
        }
        
        @Override
        public void run() {
            final Runnable runnableHook = this.callback;
            if (runnableHook != null) {
                runnableHook.run();
                this.callback = null;
            }
        }
        
        @Override
        public String toString() {
            return String.valueOf(this.callback);
        }
    }
}
