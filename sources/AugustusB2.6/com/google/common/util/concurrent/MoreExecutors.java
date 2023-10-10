// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.Delayed;
import java.util.concurrent.ScheduledFuture;
import java.util.Collections;
import com.google.errorprone.annotations.concurrent.GuardedBy;
import com.google.common.annotations.VisibleForTesting;
import java.util.concurrent.RejectedExecutionException;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.base.Supplier;
import java.lang.reflect.InvocationTargetException;
import com.google.common.base.Throwables;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.List;
import java.util.concurrent.Future;
import com.google.common.collect.Queues;
import com.google.common.collect.Lists;
import com.google.common.base.Preconditions;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Callable;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.time.Duration;
import java.util.concurrent.ThreadPoolExecutor;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
public final class MoreExecutors
{
    private MoreExecutors() {
    }
    
    @Beta
    @GwtIncompatible
    public static ExecutorService getExitingExecutorService(final ThreadPoolExecutor executor, final Duration terminationTimeout) {
        return getExitingExecutorService(executor, Internal.toNanosSaturated(terminationTimeout), TimeUnit.NANOSECONDS);
    }
    
    @Beta
    @GwtIncompatible
    public static ExecutorService getExitingExecutorService(final ThreadPoolExecutor executor, final long terminationTimeout, final TimeUnit timeUnit) {
        return new Application().getExitingExecutorService(executor, terminationTimeout, timeUnit);
    }
    
    @Beta
    @GwtIncompatible
    public static ExecutorService getExitingExecutorService(final ThreadPoolExecutor executor) {
        return new Application().getExitingExecutorService(executor);
    }
    
    @Beta
    @GwtIncompatible
    public static ScheduledExecutorService getExitingScheduledExecutorService(final ScheduledThreadPoolExecutor executor, final Duration terminationTimeout) {
        return getExitingScheduledExecutorService(executor, Internal.toNanosSaturated(terminationTimeout), TimeUnit.NANOSECONDS);
    }
    
    @Beta
    @GwtIncompatible
    public static ScheduledExecutorService getExitingScheduledExecutorService(final ScheduledThreadPoolExecutor executor, final long terminationTimeout, final TimeUnit timeUnit) {
        return new Application().getExitingScheduledExecutorService(executor, terminationTimeout, timeUnit);
    }
    
    @Beta
    @GwtIncompatible
    public static ScheduledExecutorService getExitingScheduledExecutorService(final ScheduledThreadPoolExecutor executor) {
        return new Application().getExitingScheduledExecutorService(executor);
    }
    
    @Beta
    @GwtIncompatible
    public static void addDelayedShutdownHook(final ExecutorService service, final Duration terminationTimeout) {
        addDelayedShutdownHook(service, Internal.toNanosSaturated(terminationTimeout), TimeUnit.NANOSECONDS);
    }
    
    @Beta
    @GwtIncompatible
    public static void addDelayedShutdownHook(final ExecutorService service, final long terminationTimeout, final TimeUnit timeUnit) {
        new Application().addDelayedShutdownHook(service, terminationTimeout, timeUnit);
    }
    
    @GwtIncompatible
    private static void useDaemonThreadFactory(final ThreadPoolExecutor executor) {
        executor.setThreadFactory(new ThreadFactoryBuilder().setDaemon(true).setThreadFactory(executor.getThreadFactory()).build());
    }
    
    @GwtIncompatible
    public static ListeningExecutorService newDirectExecutorService() {
        return new DirectExecutorService();
    }
    
    public static Executor directExecutor() {
        return DirectExecutor.INSTANCE;
    }
    
    @Beta
    @GwtIncompatible
    public static Executor newSequentialExecutor(final Executor delegate) {
        return new SequentialExecutor(delegate);
    }
    
    @GwtIncompatible
    public static ListeningExecutorService listeningDecorator(final ExecutorService delegate) {
        return (delegate instanceof ListeningExecutorService) ? ((ListeningExecutorService)delegate) : ((delegate instanceof ScheduledExecutorService) ? new ScheduledListeningDecorator((ScheduledExecutorService)delegate) : new ListeningDecorator(delegate));
    }
    
    @GwtIncompatible
    public static ListeningScheduledExecutorService listeningDecorator(final ScheduledExecutorService delegate) {
        return (delegate instanceof ListeningScheduledExecutorService) ? ((ListeningScheduledExecutorService)delegate) : new ScheduledListeningDecorator(delegate);
    }
    
    @ParametricNullness
    @GwtIncompatible
    static <T> T invokeAnyImpl(final ListeningExecutorService executorService, final Collection<? extends Callable<T>> tasks, final boolean timed, final Duration timeout) throws InterruptedException, ExecutionException, TimeoutException {
        return invokeAnyImpl(executorService, tasks, timed, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
    }
    
    @ParametricNullness
    @GwtIncompatible
    static <T> T invokeAnyImpl(final ListeningExecutorService executorService, final Collection<? extends Callable<T>> tasks, final boolean timed, final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        Preconditions.checkNotNull(executorService);
        Preconditions.checkNotNull(unit);
        int ntasks = tasks.size();
        Preconditions.checkArgument(ntasks > 0);
        final List<Future<T>> futures = (List<Future<T>>)Lists.newArrayListWithCapacity(ntasks);
        final BlockingQueue<Future<T>> futureQueue = (BlockingQueue<Future<T>>)Queues.newLinkedBlockingQueue();
        long timeoutNanos = unit.toNanos(timeout);
        try {
            ExecutionException ee = null;
            long lastTime = timed ? System.nanoTime() : 0L;
            final Iterator<? extends Callable<T>> it = tasks.iterator();
            futures.add(submitAndAddQueueListener(executorService, (Callable<T>)it.next(), futureQueue));
            --ntasks;
            int active = 1;
            while (true) {
                Future<T> f = futureQueue.poll();
                if (f == null) {
                    if (ntasks > 0) {
                        --ntasks;
                        futures.add(submitAndAddQueueListener(executorService, (Callable<T>)it.next(), futureQueue));
                        ++active;
                    }
                    else {
                        if (active == 0) {
                            if (ee == null) {
                                ee = new ExecutionException((Throwable)null);
                            }
                            throw ee;
                        }
                        if (timed) {
                            f = futureQueue.poll(timeoutNanos, TimeUnit.NANOSECONDS);
                            if (f == null) {
                                throw new TimeoutException();
                            }
                            final long now = System.nanoTime();
                            timeoutNanos -= now - lastTime;
                            lastTime = now;
                        }
                        else {
                            f = futureQueue.take();
                        }
                    }
                }
                if (f != null) {
                    --active;
                    try {
                        return f.get();
                    }
                    catch (ExecutionException eex) {
                        ee = eex;
                    }
                    catch (RuntimeException rex) {
                        ee = new ExecutionException(rex);
                    }
                }
            }
        }
        finally {
            for (final Future<T> f2 : futures) {
                f2.cancel(true);
            }
        }
    }
    
    @GwtIncompatible
    private static <T> ListenableFuture<T> submitAndAddQueueListener(final ListeningExecutorService executorService, final Callable<T> task, final BlockingQueue<Future<T>> queue) {
        final ListenableFuture<T> future = executorService.submit(task);
        future.addListener(new Runnable() {
            @Override
            public void run() {
                queue.add(future);
            }
        }, directExecutor());
        return future;
    }
    
    @Beta
    @GwtIncompatible
    public static ThreadFactory platformThreadFactory() {
        if (!isAppEngineWithApiClasses()) {
            return Executors.defaultThreadFactory();
        }
        try {
            return (ThreadFactory)Class.forName("com.google.appengine.api.ThreadManager").getMethod("currentRequestThreadFactory", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
        }
        catch (ClassNotFoundException e2) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e2);
        }
        catch (NoSuchMethodException e3) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e3);
        }
        catch (InvocationTargetException e4) {
            throw Throwables.propagate(e4.getCause());
        }
    }
    
    @GwtIncompatible
    private static boolean isAppEngineWithApiClasses() {
        if (System.getProperty("com.google.appengine.runtime.environment") == null) {
            return false;
        }
        try {
            Class.forName("com.google.appengine.api.utils.SystemProperty");
        }
        catch (ClassNotFoundException e) {
            return false;
        }
        try {
            return Class.forName("com.google.apphosting.api.ApiProxy").getMethod("getCurrentEnvironment", (Class<?>[])new Class[0]).invoke(null, new Object[0]) != null;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
        catch (InvocationTargetException e2) {
            return false;
        }
        catch (IllegalAccessException e3) {
            return false;
        }
        catch (NoSuchMethodException e4) {
            return false;
        }
    }
    
    @GwtIncompatible
    static Thread newThread(final String name, final Runnable runnable) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(runnable);
        final Thread result = platformThreadFactory().newThread(runnable);
        try {
            result.setName(name);
        }
        catch (SecurityException ex) {}
        return result;
    }
    
    @GwtIncompatible
    static Executor renamingDecorator(final Executor executor, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(nameSupplier);
        return new Executor() {
            @Override
            public void execute(final Runnable command) {
                executor.execute(Callables.threadRenaming(command, nameSupplier));
            }
        };
    }
    
    @GwtIncompatible
    static ExecutorService renamingDecorator(final ExecutorService service, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(service);
        Preconditions.checkNotNull(nameSupplier);
        return new WrappingExecutorService(service) {
            @Override
            protected <T> Callable<T> wrapTask(final Callable<T> callable) {
                return Callables.threadRenaming(callable, nameSupplier);
            }
            
            @Override
            protected Runnable wrapTask(final Runnable command) {
                return Callables.threadRenaming(command, nameSupplier);
            }
        };
    }
    
    @GwtIncompatible
    static ScheduledExecutorService renamingDecorator(final ScheduledExecutorService service, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(service);
        Preconditions.checkNotNull(nameSupplier);
        return new WrappingScheduledExecutorService(service) {
            @Override
            protected <T> Callable<T> wrapTask(final Callable<T> callable) {
                return Callables.threadRenaming(callable, nameSupplier);
            }
            
            @Override
            protected Runnable wrapTask(final Runnable command) {
                return Callables.threadRenaming(command, nameSupplier);
            }
        };
    }
    
    @Beta
    @CanIgnoreReturnValue
    @GwtIncompatible
    public static boolean shutdownAndAwaitTermination(final ExecutorService service, final Duration timeout) {
        return shutdownAndAwaitTermination(service, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
    }
    
    @Beta
    @CanIgnoreReturnValue
    @GwtIncompatible
    public static boolean shutdownAndAwaitTermination(final ExecutorService service, final long timeout, final TimeUnit unit) {
        final long halfTimeoutNanos = unit.toNanos(timeout) / 2L;
        service.shutdown();
        try {
            if (!service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS)) {
                service.shutdownNow();
                service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS);
            }
        }
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            service.shutdownNow();
        }
        return service.isTerminated();
    }
    
    static Executor rejectionPropagatingExecutor(final Executor delegate, final AbstractFuture<?> future) {
        Preconditions.checkNotNull(delegate);
        Preconditions.checkNotNull(future);
        if (delegate == directExecutor()) {
            return delegate;
        }
        return new Executor() {
            @Override
            public void execute(final Runnable command) {
                try {
                    delegate.execute(command);
                }
                catch (RejectedExecutionException e) {
                    future.setException(e);
                }
            }
        };
    }
    
    @GwtIncompatible
    @VisibleForTesting
    static class Application
    {
        final ExecutorService getExitingExecutorService(final ThreadPoolExecutor executor, final long terminationTimeout, final TimeUnit timeUnit) {
            useDaemonThreadFactory(executor);
            final ExecutorService service = Executors.unconfigurableExecutorService(executor);
            this.addDelayedShutdownHook(executor, terminationTimeout, timeUnit);
            return service;
        }
        
        final ExecutorService getExitingExecutorService(final ThreadPoolExecutor executor) {
            return this.getExitingExecutorService(executor, 120L, TimeUnit.SECONDS);
        }
        
        final ScheduledExecutorService getExitingScheduledExecutorService(final ScheduledThreadPoolExecutor executor, final long terminationTimeout, final TimeUnit timeUnit) {
            useDaemonThreadFactory(executor);
            final ScheduledExecutorService service = Executors.unconfigurableScheduledExecutorService(executor);
            this.addDelayedShutdownHook(executor, terminationTimeout, timeUnit);
            return service;
        }
        
        final ScheduledExecutorService getExitingScheduledExecutorService(final ScheduledThreadPoolExecutor executor) {
            return this.getExitingScheduledExecutorService(executor, 120L, TimeUnit.SECONDS);
        }
        
        final void addDelayedShutdownHook(final ExecutorService service, final long terminationTimeout, final TimeUnit timeUnit) {
            Preconditions.checkNotNull(service);
            Preconditions.checkNotNull(timeUnit);
            final String value = String.valueOf(service);
            this.addShutdownHook(MoreExecutors.newThread(new StringBuilder(24 + String.valueOf(value).length()).append("DelayedShutdownHook-for-").append(value).toString(), new Runnable(this) {
                @Override
                public void run() {
                    try {
                        service.shutdown();
                        service.awaitTermination(terminationTimeout, timeUnit);
                    }
                    catch (InterruptedException ex) {}
                }
            }));
        }
        
        @VisibleForTesting
        void addShutdownHook(final Thread hook) {
            Runtime.getRuntime().addShutdownHook(hook);
        }
    }
    
    @GwtIncompatible
    private static final class DirectExecutorService extends AbstractListeningExecutorService
    {
        private final Object lock;
        @GuardedBy("lock")
        private int runningTasks;
        @GuardedBy("lock")
        private boolean shutdown;
        
        private DirectExecutorService() {
            this.lock = new Object();
            this.runningTasks = 0;
            this.shutdown = false;
        }
        
        @Override
        public void execute(final Runnable command) {
            this.startTask();
            try {
                command.run();
            }
            finally {
                this.endTask();
            }
        }
        
        @Override
        public boolean isShutdown() {
            synchronized (this.lock) {
                return this.shutdown;
            }
        }
        
        @Override
        public void shutdown() {
            synchronized (this.lock) {
                this.shutdown = true;
                if (this.runningTasks == 0) {
                    this.lock.notifyAll();
                }
            }
        }
        
        @Override
        public List<Runnable> shutdownNow() {
            this.shutdown();
            return Collections.emptyList();
        }
        
        @Override
        public boolean isTerminated() {
            synchronized (this.lock) {
                return this.shutdown && this.runningTasks == 0;
            }
        }
        
        @Override
        public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
            long nanos = unit.toNanos(timeout);
            synchronized (this.lock) {
                while (!this.shutdown || this.runningTasks != 0) {
                    if (nanos <= 0L) {
                        return false;
                    }
                    final long now = System.nanoTime();
                    TimeUnit.NANOSECONDS.timedWait(this.lock, nanos);
                    nanos -= System.nanoTime() - now;
                }
                return true;
            }
        }
        
        private void startTask() {
            synchronized (this.lock) {
                if (this.shutdown) {
                    throw new RejectedExecutionException("Executor already shutdown");
                }
                ++this.runningTasks;
            }
        }
        
        private void endTask() {
            synchronized (this.lock) {
                final int runningTasks = this.runningTasks - 1;
                this.runningTasks = runningTasks;
                final int numRunning = runningTasks;
                if (numRunning == 0) {
                    this.lock.notifyAll();
                }
            }
        }
    }
    
    @GwtIncompatible
    private static class ListeningDecorator extends AbstractListeningExecutorService
    {
        private final ExecutorService delegate;
        
        ListeningDecorator(final ExecutorService delegate) {
            this.delegate = Preconditions.checkNotNull(delegate);
        }
        
        @Override
        public final boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
            return this.delegate.awaitTermination(timeout, unit);
        }
        
        @Override
        public final boolean isShutdown() {
            return this.delegate.isShutdown();
        }
        
        @Override
        public final boolean isTerminated() {
            return this.delegate.isTerminated();
        }
        
        @Override
        public final void shutdown() {
            this.delegate.shutdown();
        }
        
        @Override
        public final List<Runnable> shutdownNow() {
            return this.delegate.shutdownNow();
        }
        
        @Override
        public final void execute(final Runnable command) {
            this.delegate.execute(command);
        }
        
        @Override
        public final String toString() {
            final String string = super.toString();
            final String value = String.valueOf(this.delegate);
            return new StringBuilder(2 + String.valueOf(string).length() + String.valueOf(value).length()).append(string).append("[").append(value).append("]").toString();
        }
    }
    
    @GwtIncompatible
    private static final class ScheduledListeningDecorator extends ListeningDecorator implements ListeningScheduledExecutorService
    {
        final ScheduledExecutorService delegate;
        
        ScheduledListeningDecorator(final ScheduledExecutorService delegate) {
            super(delegate);
            this.delegate = Preconditions.checkNotNull(delegate);
        }
        
        @Override
        public ListenableScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
            final TrustedListenableFutureTask<Void> task = TrustedListenableFutureTask.create(command, (Void)null);
            final ScheduledFuture<?> scheduled = this.delegate.schedule(task, delay, unit);
            return new ListenableScheduledTask<Object>(task, scheduled);
        }
        
        @Override
        public <V> ListenableScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
            final TrustedListenableFutureTask<V> task = TrustedListenableFutureTask.create(callable);
            final ScheduledFuture<?> scheduled = this.delegate.schedule(task, delay, unit);
            return new ListenableScheduledTask<V>(task, scheduled);
        }
        
        @Override
        public ListenableScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
            final NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
            final ScheduledFuture<?> scheduled = this.delegate.scheduleAtFixedRate(task, initialDelay, period, unit);
            return new ListenableScheduledTask<Object>(task, scheduled);
        }
        
        @Override
        public ListenableScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
            final NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
            final ScheduledFuture<?> scheduled = this.delegate.scheduleWithFixedDelay(task, initialDelay, delay, unit);
            return new ListenableScheduledTask<Object>(task, scheduled);
        }
        
        private static final class ListenableScheduledTask<V> extends SimpleForwardingListenableFuture<V> implements ListenableScheduledFuture<V>
        {
            private final ScheduledFuture<?> scheduledDelegate;
            
            public ListenableScheduledTask(final ListenableFuture<V> listenableDelegate, final ScheduledFuture<?> scheduledDelegate) {
                super(listenableDelegate);
                this.scheduledDelegate = scheduledDelegate;
            }
            
            @Override
            public boolean cancel(final boolean mayInterruptIfRunning) {
                final boolean cancelled = super.cancel(mayInterruptIfRunning);
                if (cancelled) {
                    this.scheduledDelegate.cancel(mayInterruptIfRunning);
                }
                return cancelled;
            }
            
            @Override
            public long getDelay(final TimeUnit unit) {
                return this.scheduledDelegate.getDelay(unit);
            }
            
            @Override
            public int compareTo(final Delayed other) {
                return this.scheduledDelegate.compareTo(other);
            }
        }
        
        @GwtIncompatible
        private static final class NeverSuccessfulListenableFutureTask extends TrustedFuture<Void> implements Runnable
        {
            private final Runnable delegate;
            
            public NeverSuccessfulListenableFutureTask(final Runnable delegate) {
                this.delegate = Preconditions.checkNotNull(delegate);
            }
            
            @Override
            public void run() {
                try {
                    this.delegate.run();
                }
                catch (Throwable t) {
                    this.setException(t);
                    throw Throwables.propagate(t);
                }
            }
            
            @Override
            protected String pendingToString() {
                final String value = String.valueOf(this.delegate);
                return new StringBuilder(7 + String.valueOf(value).length()).append("task=[").append(value).append("]").toString();
            }
        }
    }
}
