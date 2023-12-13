// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import javax.annotation.concurrent.GuardedBy;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import me.gong.mcleaks.util.google.common.base.Supplier;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Future;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
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
        this.addListener(new Listener() {
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
        return this.serviceName() + " [" + this.state() + "]";
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
    public final void awaitRunning(final long timeout, final TimeUnit unit) throws TimeoutException {
        this.delegate.awaitRunning(timeout, unit);
    }
    
    @Override
    public final void awaitTerminated() {
        this.delegate.awaitTerminated();
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
        public static Scheduler newFixedDelaySchedule(final long initialDelay, final long delay, final TimeUnit unit) {
            Preconditions.checkNotNull(unit);
            Preconditions.checkArgument(delay > 0L, "delay must be > 0, found %s", delay);
            return new Scheduler() {
                public Future<?> schedule(final AbstractService service, final ScheduledExecutorService executor, final Runnable task) {
                    return executor.scheduleWithFixedDelay(task, initialDelay, delay, unit);
                }
            };
        }
        
        public static Scheduler newFixedRateSchedule(final long initialDelay, final long period, final TimeUnit unit) {
            Preconditions.checkNotNull(unit);
            Preconditions.checkArgument(period > 0L, "period must be > 0, found %s", period);
            return new Scheduler() {
                public Future<?> schedule(final AbstractService service, final ScheduledExecutorService executor, final Runnable task) {
                    return executor.scheduleAtFixedRate(task, initialDelay, period, unit);
                }
            };
        }
        
        abstract Future<?> schedule(final AbstractService p0, final ScheduledExecutorService p1, final Runnable p2);
        
        private Scheduler() {
        }
    }
    
    private final class ServiceDelegate extends AbstractService
    {
        private volatile Future<?> runningTask;
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
                    return AbstractScheduledService.this.serviceName() + " " + ServiceDelegate.this.state();
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
                    if (ServiceDelegate.this.runningTask.isCancelled()) {
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
                    ServiceDelegate.this.runningTask.cancel(false);
                }
                finally {
                    ServiceDelegate.this.lock.unlock();
                }
            }
        }
    }
    
    @Beta
    public abstract static class CustomScheduler extends Scheduler
    {
        @Override
        final Future<?> schedule(final AbstractService service, final ScheduledExecutorService executor, final Runnable runnable) {
            final ReschedulableCallable task = new ReschedulableCallable(service, executor, runnable);
            task.reschedule();
            return task;
        }
        
        protected abstract Schedule getNextSchedule() throws Exception;
        
        private class ReschedulableCallable extends ForwardingFuture<Void> implements Callable<Void>
        {
            private final Runnable wrappedRunnable;
            private final ScheduledExecutorService executor;
            private final AbstractService service;
            private final ReentrantLock lock;
            @GuardedBy("lock")
            private Future<Void> currentFuture;
            
            ReschedulableCallable(final AbstractService service, final ScheduledExecutorService executor, final Runnable runnable) {
                this.lock = new ReentrantLock();
                this.wrappedRunnable = runnable;
                this.executor = executor;
                this.service = service;
            }
            
            @Override
            public Void call() throws Exception {
                this.wrappedRunnable.run();
                this.reschedule();
                return null;
            }
            
            public void reschedule() {
                Schedule schedule;
                try {
                    schedule = CustomScheduler.this.getNextSchedule();
                }
                catch (Throwable t) {
                    this.service.notifyFailed(t);
                    return;
                }
                Throwable scheduleFailure = null;
                this.lock.lock();
                try {
                    if (this.currentFuture == null || !this.currentFuture.isCancelled()) {
                        this.currentFuture = (Future<Void>)this.executor.schedule((Callable<Object>)this, schedule.delay, schedule.unit);
                    }
                }
                catch (Throwable e) {
                    scheduleFailure = e;
                }
                finally {
                    this.lock.unlock();
                }
                if (scheduleFailure != null) {
                    this.service.notifyFailed(scheduleFailure);
                }
            }
            
            @Override
            public boolean cancel(final boolean mayInterruptIfRunning) {
                this.lock.lock();
                try {
                    return this.currentFuture.cancel(mayInterruptIfRunning);
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
            
            @Override
            protected Future<Void> delegate() {
                throw new UnsupportedOperationException("Only cancel and isCancelled is supported by this future");
            }
        }
        
        @Beta
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
}
