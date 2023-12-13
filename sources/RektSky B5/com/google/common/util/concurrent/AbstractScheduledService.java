/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.concurrent.GuardedBy
 */
package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.ForwardingFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

@Beta
public abstract class AbstractScheduledService
implements Service {
    private static final Logger logger = Logger.getLogger(AbstractScheduledService.class.getName());
    private final AbstractService delegate = new AbstractService(){
        private volatile Future<?> runningTask;
        private volatile ScheduledExecutorService executorService;
        private final ReentrantLock lock = new ReentrantLock();
        private final Runnable task = new Runnable(){

            @Override
            public void run() {
                lock.lock();
                try {
                    AbstractScheduledService.this.runOneIteration();
                }
                catch (Throwable t2) {
                    try {
                        AbstractScheduledService.this.shutDown();
                    }
                    catch (Exception ignored) {
                        logger.log(Level.WARNING, "Error while attempting to shut down the service after failure.", ignored);
                    }
                    this.notifyFailed(t2);
                    throw Throwables.propagate(t2);
                }
                finally {
                    lock.unlock();
                }
            }
        };

        @Override
        protected final void doStart() {
            this.executorService = MoreExecutors.renamingDecorator(AbstractScheduledService.this.executor(), new Supplier<String>(){

                @Override
                public String get() {
                    return AbstractScheduledService.this.serviceName() + " " + (Object)((Object)this.state());
                }
            });
            this.executorService.execute(new Runnable(){

                @Override
                public void run() {
                    lock.lock();
                    try {
                        AbstractScheduledService.this.startUp();
                        runningTask = AbstractScheduledService.this.scheduler().schedule(AbstractScheduledService.this.delegate, executorService, task);
                        this.notifyStarted();
                    }
                    catch (Throwable t2) {
                        this.notifyFailed(t2);
                        throw Throwables.propagate(t2);
                    }
                    finally {
                        lock.unlock();
                    }
                }
            });
        }

        @Override
        protected final void doStop() {
            this.runningTask.cancel(false);
            this.executorService.execute(new Runnable(){

                /*
                 * WARNING - Removed try catching itself - possible behaviour change.
                 */
                @Override
                public void run() {
                    try {
                        lock.lock();
                        try {
                            if (this.state() != Service.State.STOPPING) {
                                return;
                            }
                            AbstractScheduledService.this.shutDown();
                        }
                        finally {
                            lock.unlock();
                        }
                        this.notifyStopped();
                    }
                    catch (Throwable t2) {
                        this.notifyFailed(t2);
                        throw Throwables.propagate(t2);
                    }
                }
            });
        }
    };

    protected AbstractScheduledService() {
    }

    protected abstract void runOneIteration() throws Exception;

    protected void startUp() throws Exception {
    }

    protected void shutDown() throws Exception {
    }

    protected abstract Scheduler scheduler();

    protected ScheduledExecutorService executor() {
        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory(){

            @Override
            public Thread newThread(Runnable runnable) {
                return MoreExecutors.newThread(AbstractScheduledService.this.serviceName(), runnable);
            }
        });
        this.addListener(new Service.Listener(){

            @Override
            public void terminated(Service.State from) {
                executor.shutdown();
            }

            @Override
            public void failed(Service.State from, Throwable failure) {
                executor.shutdown();
            }
        }, MoreExecutors.sameThreadExecutor());
        return executor;
    }

    protected String serviceName() {
        return this.getClass().getSimpleName();
    }

    public String toString() {
        return this.serviceName() + " [" + (Object)((Object)this.state()) + "]";
    }

    @Override
    public final boolean isRunning() {
        return this.delegate.isRunning();
    }

    @Override
    public final Service.State state() {
        return this.delegate.state();
    }

    @Override
    public final void addListener(Service.Listener listener, Executor executor) {
        this.delegate.addListener(listener, executor);
    }

    @Override
    public final Throwable failureCause() {
        return this.delegate.failureCause();
    }

    @Override
    public final Service startAsync() {
        this.delegate.startAsync();
        return this;
    }

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
    public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
        this.delegate.awaitRunning(timeout, unit);
    }

    @Override
    public final void awaitTerminated() {
        this.delegate.awaitTerminated();
    }

    @Override
    public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
        this.delegate.awaitTerminated(timeout, unit);
    }

    @Beta
    public static abstract class CustomScheduler
    extends Scheduler {
        @Override
        final Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable runnable) {
            ReschedulableCallable task = new ReschedulableCallable(service, executor, runnable);
            task.reschedule();
            return task;
        }

        protected abstract Schedule getNextSchedule() throws Exception;

        @Beta
        protected static final class Schedule {
            private final long delay;
            private final TimeUnit unit;

            public Schedule(long delay, TimeUnit unit) {
                this.delay = delay;
                this.unit = Preconditions.checkNotNull(unit);
            }
        }

        private class ReschedulableCallable
        extends ForwardingFuture<Void>
        implements Callable<Void> {
            private final Runnable wrappedRunnable;
            private final ScheduledExecutorService executor;
            private final AbstractService service;
            private final ReentrantLock lock = new ReentrantLock();
            @GuardedBy(value="lock")
            private Future<Void> currentFuture;

            ReschedulableCallable(AbstractService service, ScheduledExecutorService executor, Runnable runnable) {
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

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            public void reschedule() {
                this.lock.lock();
                try {
                    if (this.currentFuture == null || !this.currentFuture.isCancelled()) {
                        Schedule schedule = CustomScheduler.this.getNextSchedule();
                        this.currentFuture = this.executor.schedule(this, schedule.delay, schedule.unit);
                    }
                }
                catch (Throwable e2) {
                    this.service.notifyFailed(e2);
                }
                finally {
                    this.lock.unlock();
                }
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                this.lock.lock();
                try {
                    boolean bl = this.currentFuture.cancel(mayInterruptIfRunning);
                    return bl;
                }
                finally {
                    this.lock.unlock();
                }
            }

            @Override
            protected Future<Void> delegate() {
                throw new UnsupportedOperationException("Only cancel is supported by this future");
            }
        }
    }

    public static abstract class Scheduler {
        public static Scheduler newFixedDelaySchedule(final long initialDelay, final long delay, final TimeUnit unit) {
            return new Scheduler(){

                @Override
                public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable task) {
                    return executor.scheduleWithFixedDelay(task, initialDelay, delay, unit);
                }
            };
        }

        public static Scheduler newFixedRateSchedule(final long initialDelay, final long period, final TimeUnit unit) {
            return new Scheduler(){

                @Override
                public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable task) {
                    return executor.scheduleAtFixedRate(task, initialDelay, period, unit);
                }
            };
        }

        abstract Future<?> schedule(AbstractService var1, ScheduledExecutorService var2, Runnable var3);

        private Scheduler() {
        }
    }
}

