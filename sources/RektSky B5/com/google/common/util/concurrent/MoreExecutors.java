/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.AbstractListeningExecutorService;
import com.google.common.util.concurrent.Callables;
import com.google.common.util.concurrent.ForwardingListenableFuture;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ListenableScheduledFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.common.util.concurrent.WrappingExecutorService;
import com.google.common.util.concurrent.WrappingScheduledExecutorService;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class MoreExecutors {
    private MoreExecutors() {
    }

    @Beta
    public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
        return new Application().getExitingExecutorService(executor, terminationTimeout, timeUnit);
    }

    @Beta
    public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
        return new Application().getExitingScheduledExecutorService(executor, terminationTimeout, timeUnit);
    }

    @Beta
    public static void addDelayedShutdownHook(ExecutorService service, long terminationTimeout, TimeUnit timeUnit) {
        new Application().addDelayedShutdownHook(service, terminationTimeout, timeUnit);
    }

    @Beta
    public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
        return new Application().getExitingExecutorService(executor);
    }

    @Beta
    public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
        return new Application().getExitingScheduledExecutorService(executor);
    }

    private static void useDaemonThreadFactory(ThreadPoolExecutor executor) {
        executor.setThreadFactory(new ThreadFactoryBuilder().setDaemon(true).setThreadFactory(executor.getThreadFactory()).build());
    }

    public static ListeningExecutorService sameThreadExecutor() {
        return new SameThreadExecutorService();
    }

    public static ListeningExecutorService listeningDecorator(ExecutorService delegate) {
        return delegate instanceof ListeningExecutorService ? (ListeningExecutorService)delegate : (delegate instanceof ScheduledExecutorService ? new ScheduledListeningDecorator((ScheduledExecutorService)delegate) : new ListeningDecorator(delegate));
    }

    public static ListeningScheduledExecutorService listeningDecorator(ScheduledExecutorService delegate) {
        return delegate instanceof ListeningScheduledExecutorService ? (ListeningScheduledExecutorService)delegate : new ScheduledListeningDecorator(delegate);
    }

    static <T> T invokeAnyImpl(ListeningExecutorService executorService, Collection<? extends Callable<T>> tasks, boolean timed, long nanos) throws InterruptedException, ExecutionException, TimeoutException {
        ExecutionException ee;
        ArrayList<ListenableFuture<T>> futures;
        block15: {
            Preconditions.checkNotNull(executorService);
            int ntasks = tasks.size();
            Preconditions.checkArgument(ntasks > 0);
            futures = Lists.newArrayListWithCapacity(ntasks);
            LinkedBlockingQueue<Future<T>> futureQueue = Queues.newLinkedBlockingQueue();
            ee = null;
            long lastTime = timed ? System.nanoTime() : 0L;
            Iterator<Callable<T>> it = tasks.iterator();
            futures.add(MoreExecutors.submitAndAddQueueListener(executorService, it.next(), futureQueue));
            --ntasks;
            int active = 1;
            while (true) {
                Object now22;
                Future f2;
                if ((f2 = (Future)futureQueue.poll()) == null) {
                    if (ntasks > 0) {
                        --ntasks;
                        futures.add(MoreExecutors.submitAndAddQueueListener(executorService, it.next(), futureQueue));
                        ++active;
                    } else {
                        if (active == 0) break;
                        if (timed) {
                            f2 = (Future)futureQueue.poll(nanos, TimeUnit.NANOSECONDS);
                            if (f2 == null) {
                                throw new TimeoutException();
                            }
                            long now22 = System.nanoTime();
                            nanos -= now22 - lastTime;
                            lastTime = now22;
                        } else {
                            f2 = (Future)futureQueue.take();
                        }
                    }
                }
                if (f2 == null) continue;
                --active;
                try {
                    now22 = f2.get();
                }
                catch (ExecutionException eex) {
                    ee = eex;
                    continue;
                }
                catch (RuntimeException rex) {
                    ee = new ExecutionException(rex);
                    continue;
                }
                return (T)now22;
                break;
            }
            if (ee != null) break block15;
            ee = new ExecutionException(null);
        }
        throw ee;
        finally {
            for (Future future : futures) {
                future.cancel(true);
            }
        }
    }

    private static <T> ListenableFuture<T> submitAndAddQueueListener(ListeningExecutorService executorService, Callable<T> task, final BlockingQueue<Future<T>> queue) {
        final ListenableFuture<T> future = executorService.submit(task);
        future.addListener(new Runnable(){

            @Override
            public void run() {
                queue.add(future);
            }
        }, MoreExecutors.sameThreadExecutor());
        return future;
    }

    @Beta
    public static ThreadFactory platformThreadFactory() {
        if (!MoreExecutors.isAppEngine()) {
            return Executors.defaultThreadFactory();
        }
        try {
            return (ThreadFactory)Class.forName("com.google.appengine.api.ThreadManager").getMethod("currentRequestThreadFactory", new Class[0]).invoke(null, new Object[0]);
        }
        catch (IllegalAccessException e2) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e2);
        }
        catch (ClassNotFoundException e3) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e3);
        }
        catch (NoSuchMethodException e4) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e4);
        }
        catch (InvocationTargetException e5) {
            throw Throwables.propagate(e5.getCause());
        }
    }

    private static boolean isAppEngine() {
        if (System.getProperty("com.google.appengine.runtime.environment") == null) {
            return false;
        }
        try {
            return Class.forName("com.google.apphosting.api.ApiProxy").getMethod("getCurrentEnvironment", new Class[0]).invoke(null, new Object[0]) != null;
        }
        catch (ClassNotFoundException e2) {
            return false;
        }
        catch (InvocationTargetException e3) {
            return false;
        }
        catch (IllegalAccessException e4) {
            return false;
        }
        catch (NoSuchMethodException e5) {
            return false;
        }
    }

    static Thread newThread(String name, Runnable runnable) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(runnable);
        Thread result = MoreExecutors.platformThreadFactory().newThread(runnable);
        try {
            result.setName(name);
        }
        catch (SecurityException securityException) {
            // empty catch block
        }
        return result;
    }

    static Executor renamingDecorator(final Executor executor, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(nameSupplier);
        if (MoreExecutors.isAppEngine()) {
            return executor;
        }
        return new Executor(){

            @Override
            public void execute(Runnable command) {
                executor.execute(Callables.threadRenaming(command, (Supplier<String>)nameSupplier));
            }
        };
    }

    static ExecutorService renamingDecorator(ExecutorService service, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(service);
        Preconditions.checkNotNull(nameSupplier);
        if (MoreExecutors.isAppEngine()) {
            return service;
        }
        return new WrappingExecutorService(service){

            @Override
            protected <T> Callable<T> wrapTask(Callable<T> callable) {
                return Callables.threadRenaming(callable, (Supplier<String>)nameSupplier);
            }

            @Override
            protected Runnable wrapTask(Runnable command) {
                return Callables.threadRenaming(command, (Supplier<String>)nameSupplier);
            }
        };
    }

    static ScheduledExecutorService renamingDecorator(ScheduledExecutorService service, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(service);
        Preconditions.checkNotNull(nameSupplier);
        if (MoreExecutors.isAppEngine()) {
            return service;
        }
        return new WrappingScheduledExecutorService(service){

            @Override
            protected <T> Callable<T> wrapTask(Callable<T> callable) {
                return Callables.threadRenaming(callable, (Supplier<String>)nameSupplier);
            }

            @Override
            protected Runnable wrapTask(Runnable command) {
                return Callables.threadRenaming(command, (Supplier<String>)nameSupplier);
            }
        };
    }

    @Beta
    public static boolean shutdownAndAwaitTermination(ExecutorService service, long timeout, TimeUnit unit) {
        Preconditions.checkNotNull(unit);
        service.shutdown();
        try {
            long halfTimeoutNanos = TimeUnit.NANOSECONDS.convert(timeout, unit) / 2L;
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

    private static class ScheduledListeningDecorator
    extends ListeningDecorator
    implements ListeningScheduledExecutorService {
        final ScheduledExecutorService delegate;

        ScheduledListeningDecorator(ScheduledExecutorService delegate) {
            super(delegate);
            this.delegate = Preconditions.checkNotNull(delegate);
        }

        @Override
        public ListenableScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
            ListenableFutureTask<Object> task = ListenableFutureTask.create(command, null);
            ScheduledFuture<?> scheduled = this.delegate.schedule(task, delay, unit);
            return new ListenableScheduledTask<Object>(task, scheduled);
        }

        @Override
        public <V> ListenableScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
            ListenableFutureTask<V> task = ListenableFutureTask.create(callable);
            ScheduledFuture<?> scheduled = this.delegate.schedule(task, delay, unit);
            return new ListenableScheduledTask<V>(task, scheduled);
        }

        @Override
        public ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
            NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
            ScheduledFuture<?> scheduled = this.delegate.scheduleAtFixedRate(task, initialDelay, period, unit);
            return new ListenableScheduledTask<Void>(task, scheduled);
        }

        @Override
        public ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
            NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
            ScheduledFuture<?> scheduled = this.delegate.scheduleWithFixedDelay(task, initialDelay, delay, unit);
            return new ListenableScheduledTask<Void>(task, scheduled);
        }

        private static final class NeverSuccessfulListenableFutureTask
        extends AbstractFuture<Void>
        implements Runnable {
            private final Runnable delegate;

            public NeverSuccessfulListenableFutureTask(Runnable delegate) {
                this.delegate = Preconditions.checkNotNull(delegate);
            }

            @Override
            public void run() {
                try {
                    this.delegate.run();
                }
                catch (Throwable t2) {
                    this.setException(t2);
                    throw Throwables.propagate(t2);
                }
            }
        }

        private static final class ListenableScheduledTask<V>
        extends ForwardingListenableFuture.SimpleForwardingListenableFuture<V>
        implements ListenableScheduledFuture<V> {
            private final ScheduledFuture<?> scheduledDelegate;

            public ListenableScheduledTask(ListenableFuture<V> listenableDelegate, ScheduledFuture<?> scheduledDelegate) {
                super(listenableDelegate);
                this.scheduledDelegate = scheduledDelegate;
            }

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                boolean cancelled = super.cancel(mayInterruptIfRunning);
                if (cancelled) {
                    this.scheduledDelegate.cancel(mayInterruptIfRunning);
                }
                return cancelled;
            }

            @Override
            public long getDelay(TimeUnit unit) {
                return this.scheduledDelegate.getDelay(unit);
            }

            @Override
            public int compareTo(Delayed other) {
                return this.scheduledDelegate.compareTo(other);
            }
        }
    }

    private static class ListeningDecorator
    extends AbstractListeningExecutorService {
        private final ExecutorService delegate;

        ListeningDecorator(ExecutorService delegate) {
            this.delegate = Preconditions.checkNotNull(delegate);
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return this.delegate.awaitTermination(timeout, unit);
        }

        @Override
        public boolean isShutdown() {
            return this.delegate.isShutdown();
        }

        @Override
        public boolean isTerminated() {
            return this.delegate.isTerminated();
        }

        @Override
        public void shutdown() {
            this.delegate.shutdown();
        }

        @Override
        public List<Runnable> shutdownNow() {
            return this.delegate.shutdownNow();
        }

        @Override
        public void execute(Runnable command) {
            this.delegate.execute(command);
        }
    }

    private static class SameThreadExecutorService
    extends AbstractListeningExecutorService {
        private final Lock lock = new ReentrantLock();
        private final Condition termination = this.lock.newCondition();
        private int runningTasks = 0;
        private boolean shutdown = false;

        private SameThreadExecutorService() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void execute(Runnable command) {
            this.startTask();
            try {
                command.run();
            }
            finally {
                this.endTask();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean isShutdown() {
            this.lock.lock();
            try {
                boolean bl = this.shutdown;
                return bl;
            }
            finally {
                this.lock.unlock();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void shutdown() {
            this.lock.lock();
            try {
                this.shutdown = true;
            }
            finally {
                this.lock.unlock();
            }
        }

        @Override
        public List<Runnable> shutdownNow() {
            this.shutdown();
            return Collections.emptyList();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean isTerminated() {
            this.lock.lock();
            try {
                boolean bl = this.shutdown && this.runningTasks == 0;
                return bl;
            }
            finally {
                this.lock.unlock();
            }
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            long nanos = unit.toNanos(timeout);
            this.lock.lock();
            try {
                while (true) {
                    if (this.isTerminated()) {
                        boolean bl = true;
                        return bl;
                    }
                    if (nanos <= 0L) {
                        boolean bl = false;
                        return bl;
                    }
                    nanos = this.termination.awaitNanos(nanos);
                }
            }
            finally {
                this.lock.unlock();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void startTask() {
            this.lock.lock();
            try {
                if (this.isShutdown()) {
                    throw new RejectedExecutionException("Executor already shutdown");
                }
                ++this.runningTasks;
            }
            finally {
                this.lock.unlock();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void endTask() {
            this.lock.lock();
            try {
                --this.runningTasks;
                if (this.isTerminated()) {
                    this.termination.signalAll();
                }
            }
            finally {
                this.lock.unlock();
            }
        }
    }

    @VisibleForTesting
    static class Application {
        Application() {
        }

        final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
            MoreExecutors.useDaemonThreadFactory(executor);
            ExecutorService service = Executors.unconfigurableExecutorService(executor);
            this.addDelayedShutdownHook(service, terminationTimeout, timeUnit);
            return service;
        }

        final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
            MoreExecutors.useDaemonThreadFactory(executor);
            ScheduledExecutorService service = Executors.unconfigurableScheduledExecutorService(executor);
            this.addDelayedShutdownHook(service, terminationTimeout, timeUnit);
            return service;
        }

        final void addDelayedShutdownHook(final ExecutorService service, final long terminationTimeout, final TimeUnit timeUnit) {
            Preconditions.checkNotNull(service);
            Preconditions.checkNotNull(timeUnit);
            this.addShutdownHook(MoreExecutors.newThread("DelayedShutdownHook-for-" + service, new Runnable(){

                @Override
                public void run() {
                    try {
                        service.shutdown();
                        service.awaitTermination(terminationTimeout, timeUnit);
                    }
                    catch (InterruptedException interruptedException) {
                        // empty catch block
                    }
                }
            }));
        }

        final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
            return this.getExitingExecutorService(executor, 120L, TimeUnit.SECONDS);
        }

        final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
            return this.getExitingScheduledExecutorService(executor, 120L, TimeUnit.SECONDS);
        }

        @VisibleForTesting
        void addShutdownHook(Thread hook) {
            Runtime.getRuntime().addShutdownHook(hook);
        }
    }
}

