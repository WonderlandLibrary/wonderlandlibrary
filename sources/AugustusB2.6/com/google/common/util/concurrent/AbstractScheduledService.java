// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.ScheduledFuture;
import com.google.errorprone.annotations.concurrent.GuardedBy;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.Objects;
import com.google.common.base.Supplier;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.CheckForNull;
import java.util.concurrent.Future;
import com.google.common.base.Preconditions;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.time.Duration;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public abstract class AbstractScheduledService implements Service
{
    private static final Logger logger;
    private final AbstractService delegate;
    
    protected AbstractScheduledService() {
        this.delegate = new ServiceDelegate();
    }
    
    protected abstract void runOneIteration() throws Exception;
    
    protected void startUp() throws Exception {
    }
    
    protected void shutDown() throws Exception {
    }
    
    protected abstract Scheduler scheduler();
    
    protected ScheduledExecutorService executor() {
        class ThreadFactoryImpl implements ThreadFactory
        {
            @Override
            public Thread newThread(final Runnable runnable) {
                return MoreExecutors.newThread(AbstractScheduledService.this.serviceName(), runnable);
            }
        }
        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl());
        this.addListener(new Listener(this) {
            @Override
            public void terminated(final State from) {
                executor.shutdown();
            }
            
            @Override
            public void failed(final State from, final Throwable failure) {
                executor.shutdown();
            }
        }, MoreExecutors.directExecutor());
        return executor;
    }
    
    protected String serviceName() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public String toString() {
        final String serviceName = this.serviceName();
        final String value = String.valueOf(this.state());
        return new StringBuilder(3 + String.valueOf(serviceName).length() + String.valueOf(value).length()).append(serviceName).append(" [").append(value).append("]").toString();
    }
    
    @Override
    public final boolean isRunning() {
        return this.delegate.isRunning();
    }
    
    @Override
    public final State state() {
        return this.delegate.state();
    }
    
    @Override
    public final void addListener(final Listener listener, final Executor executor) {
        this.delegate.addListener(listener, executor);
    }
    
    @Override
    public final Throwable failureCause() {
        return this.delegate.failureCause();
    }
    
    @CanIgnoreReturnValue
    @Override
    public final Service startAsync() {
        this.delegate.startAsync();
        return this;
    }
    
    @CanIgnoreReturnValue
    @Override
    public final Service stopAsync() {
        this.delegate.stopAsync();
        return this;
    }
    
    @Override
    public final void awaitRunning() {
        this.delegate.awaitRunning();
    }
    
    @Override
    public final void awaitRunning(final Duration timeout) throws TimeoutException {
        super.awaitRunning(timeout);
    }
    
    @Override
    public final void awaitRunning(final long timeout, final TimeUnit unit) throws TimeoutException {
        this.delegate.awaitRunning(timeout, unit);
    }
    
    @Override
    public final void awaitTerminated() {
        this.delegate.awaitTerminated();
    }
    
    @Override
    public final void awaitTerminated(final Duration timeout) throws TimeoutException {
        super.awaitTerminated(timeout);
    }
    
    @Override
    public final void awaitTerminated(final long timeout, final TimeUnit unit) throws TimeoutException {
        this.delegate.awaitTerminated(timeout, unit);
    }
    
    static {
        logger = Logger.getLogger(AbstractScheduledService.class.getName());
    }
    
    public abstract static class Scheduler
    {
        public static Scheduler newFixedDelaySchedule(final Duration initialDelay, final Duration delay) {
            return newFixedDelaySchedule(Internal.toNanosSaturated(initialDelay), Internal.toNanosSaturated(delay), TimeUnit.NANOSECONDS);
        }
        
        public static Scheduler newFixedDelaySchedule(final long initialDelay, final long delay, final TimeUnit unit) {
            Preconditions.checkNotNull(unit);
            Preconditions.checkArgument(delay > 0L, "delay must be > 0, found %s", delay);
            return new Scheduler() {
                public Cancellable schedule(final AbstractService service, final ScheduledExecutorService executor, final Runnable task) {
                    return new FutureAsCancellable(executor.scheduleWithFixedDelay(task, initialDelay, delay, unit));
                }
            };
        }
        
        public static Scheduler newFixedRateSchedule(final Duration initialDelay, final Duration period) {
            return newFixedRateSchedule(Internal.toNanosSaturated(initialDelay), Internal.toNanosSaturated(period), TimeUnit.NANOSECONDS);
        }
        
        public static Scheduler newFixedRateSchedule(final long initialDelay, final long period, final TimeUnit unit) {
            Preconditions.checkNotNull(unit);
            Preconditions.checkArgument(period > 0L, "period must be > 0, found %s", period);
            return new Scheduler() {
                public Cancellable schedule(final AbstractService service, final ScheduledExecutorService executor, final Runnable task) {
                    return new FutureAsCancellable(executor.scheduleAtFixedRate(task, initialDelay, period, unit));
                }
            };
        }
        
        abstract Cancellable schedule(final AbstractService p0, final ScheduledExecutorService p1, final Runnable p2);
        
        private Scheduler() {
        }
    }
    
    private final class ServiceDelegate extends AbstractService
    {
        @CheckForNull
        private volatile Cancellable runningTask;
        @CheckForNull
        private volatile ScheduledExecutorService executorService;
        private final ReentrantLock lock;
        private final Runnable task;
        
        private ServiceDelegate() {
            this.lock = new ReentrantLock();
            this.task = new Task();
        }
        
        @Override
        protected final void doStart() {
            (this.executorService = MoreExecutors.renamingDecorator(AbstractScheduledService.this.executor(), new Supplier<String>() {
                @Override
                public String get() {
                    final String serviceName = AbstractScheduledService.this.serviceName();
                    final String value = String.valueOf(ServiceDelegate.this.state());
                    return new StringBuilder(1 + String.valueOf(serviceName).length() + String.valueOf(value).length()).append(serviceName).append(" ").append(value).toString();
                }
            })).execute(new Runnable() {
                @Override
                public void run() {
                    ServiceDelegate.this.lock.lock();
                    try {
                        AbstractScheduledService.this.startUp();
                        ServiceDelegate.this.runningTask = AbstractScheduledService.this.scheduler().schedule(AbstractScheduledService.this.delegate, ServiceDelegate.this.executorService, ServiceDelegate.this.task);
                        ServiceDelegate.this.notifyStarted();
                    }
                    catch (Throwable t) {
                        ServiceDelegate.this.notifyFailed(t);
                        if (ServiceDelegate.this.runningTask != null) {
                            ServiceDelegate.this.runningTask.cancel(false);
                        }
                    }
                    finally {
                        ServiceDelegate.this.lock.unlock();
                    }
                }
            });
        }
        
        @Override
        protected final void doStop() {
            Objects.requireNonNull(this.runningTask);
            Objects.requireNonNull(this.executorService);
            this.runningTask.cancel(false);
            this.executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        ServiceDelegate.this.lock.lock();
                        try {
                            if (ServiceDelegate.this.state() != State.STOPPING) {
                                return;
                            }
                            AbstractScheduledService.this.shutDown();
                        }
                        finally {
                            ServiceDelegate.this.lock.unlock();
                        }
                        ServiceDelegate.this.notifyStopped();
                    }
                    catch (Throwable t) {
                        ServiceDelegate.this.notifyFailed(t);
                    }
                }
            });
        }
        
        @Override
        public String toString() {
            return AbstractScheduledService.this.toString();
        }
        
        class Task implements Runnable
        {
            @Override
            public void run() {
                ServiceDelegate.this.lock.lock();
                try {
                    if (Objects.requireNonNull(ServiceDelegate.this.runningTask).isCancelled()) {
                        return;
                    }
                    AbstractScheduledService.this.runOneIteration();
                }
                catch (Throwable t) {
                    try {
                        AbstractScheduledService.this.shutDown();
                    }
                    catch (Exception ignored) {
                        AbstractScheduledService.logger.log(Level.WARNING, "Error while attempting to shut down the service after failure.", ignored);
                    }
                    ServiceDelegate.this.notifyFailed(t);
                    Objects.requireNonNull(ServiceDelegate.this.runningTask).cancel(false);
                }
                finally {
                    ServiceDelegate.this.lock.unlock();
                }
            }
        }
    }
    
    private static final class FutureAsCancellable implements Cancellable
    {
        private final Future<?> delegate;
        
        FutureAsCancellable(final Future<?> delegate) {
            this.delegate = delegate;
        }
        
        @Override
        public void cancel(final boolean mayInterruptIfRunning) {
            this.delegate.cancel(mayInterruptIfRunning);
        }
        
        @Override
        public boolean isCancelled() {
            return this.delegate.isCancelled();
        }
    }
    
    public abstract static class CustomScheduler extends Scheduler
    {
        @Override
        final Cancellable schedule(final AbstractService service, final ScheduledExecutorService executor, final Runnable runnable) {
            return new ReschedulableCallable(service, executor, runnable).reschedule();
        }
        
        protected abstract Schedule getNextSchedule() throws Exception;
        
        private final class ReschedulableCallable implements Callable<Void>
        {
            private final Runnable wrappedRunnable;
            private final ScheduledExecutorService executor;
            private final AbstractService service;
            private final ReentrantLock lock;
            @CheckForNull
            @GuardedBy("lock")
            private SupplantableFuture cancellationDelegate;
            
            ReschedulableCallable(final AbstractService service, final ScheduledExecutorService executor, final Runnable runnable) {
                this.lock = new ReentrantLock();
                this.wrappedRunnable = runnable;
                this.executor = executor;
                this.service = service;
            }
            
            @CheckForNull
            @Override
            public Void call() throws Exception {
                this.wrappedRunnable.run();
                this.reschedule();
                return null;
            }
            
            @CanIgnoreReturnValue
            public Cancellable reschedule() {
                Schedule schedule;
                try {
                    schedule = CustomScheduler.this.getNextSchedule();
                }
                catch (Throwable t) {
                    this.service.notifyFailed(t);
                    return new FutureAsCancellable(Futures.immediateCancelledFuture());
                }
                Throwable scheduleFailure = null;
                this.lock.lock();
                Cancellable toReturn;
                try {
                    toReturn = this.initializeOrUpdateCancellationDelegate(schedule);
                }
                catch (Throwable e) {
                    scheduleFailure = e;
                    toReturn = new FutureAsCancellable(Futures.immediateCancelledFuture());
                }
                finally {
                    this.lock.unlock();
                }
                if (scheduleFailure != null) {
                    this.service.notifyFailed(scheduleFailure);
                }
                return toReturn;
            }
            
            @GuardedBy("lock")
            private Cancellable initializeOrUpdateCancellationDelegate(final Schedule schedule) {
                if (this.cancellationDelegate == null) {
                    return this.cancellationDelegate = new SupplantableFuture(this.lock, this.submitToExecutor(schedule));
                }
                if (!this.cancellationDelegate.currentFuture.isCancelled()) {
                    this.cancellationDelegate.currentFuture = this.submitToExecutor(schedule);
                }
                return this.cancellationDelegate;
            }
            
            private ScheduledFuture<Void> submitToExecutor(final Schedule schedule) {
                return this.executor.schedule((Callable<Void>)this, schedule.delay, schedule.unit);
            }
        }
        
        private static final class SupplantableFuture implements Cancellable
        {
            private final ReentrantLock lock;
            @GuardedBy("lock")
            private Future<Void> currentFuture;
            
            SupplantableFuture(final ReentrantLock lock, final Future<Void> currentFuture) {
                this.lock = lock;
                this.currentFuture = currentFuture;
            }
            
            @Override
            public void cancel(final boolean mayInterruptIfRunning) {
                this.lock.lock();
                try {
                    this.currentFuture.cancel(mayInterruptIfRunning);
                }
                finally {
                    this.lock.unlock();
                }
            }
            
            @Override
            public boolean isCancelled() {
                this.lock.lock();
                try {
                    return this.currentFuture.isCancelled();
                }
                finally {
                    this.lock.unlock();
                }
            }
        }
        
        protected static final class Schedule
        {
            private final long delay;
            private final TimeUnit unit;
            
            public Schedule(final long delay, final TimeUnit unit) {
                this.delay = delay;
                this.unit = Preconditions.checkNotNull(unit);
            }
        }
    }
    
    interface Cancellable
    {
        void cancel(final boolean p0);
        
        boolean isCancelled();
    }
}
