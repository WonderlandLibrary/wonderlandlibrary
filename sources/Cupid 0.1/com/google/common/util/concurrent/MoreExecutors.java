package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
  @Beta
  public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
    return (new Application()).getExitingExecutorService(executor, terminationTimeout, timeUnit);
  }
  
  @Beta
  public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
    return (new Application()).getExitingScheduledExecutorService(executor, terminationTimeout, timeUnit);
  }
  
  @Beta
  public static void addDelayedShutdownHook(ExecutorService service, long terminationTimeout, TimeUnit timeUnit) {
    (new Application()).addDelayedShutdownHook(service, terminationTimeout, timeUnit);
  }
  
  @Beta
  public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
    return (new Application()).getExitingExecutorService(executor);
  }
  
  @Beta
  public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
    return (new Application()).getExitingScheduledExecutorService(executor);
  }
  
  @VisibleForTesting
  static class Application {
    final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
      MoreExecutors.useDaemonThreadFactory(executor);
      ExecutorService service = Executors.unconfigurableExecutorService(executor);
      addDelayedShutdownHook(service, terminationTimeout, timeUnit);
      return service;
    }
    
    final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
      MoreExecutors.useDaemonThreadFactory(executor);
      ScheduledExecutorService service = Executors.unconfigurableScheduledExecutorService(executor);
      addDelayedShutdownHook(service, terminationTimeout, timeUnit);
      return service;
    }
    
    final void addDelayedShutdownHook(final ExecutorService service, final long terminationTimeout, final TimeUnit timeUnit) {
      Preconditions.checkNotNull(service);
      Preconditions.checkNotNull(timeUnit);
      addShutdownHook(MoreExecutors.newThread("DelayedShutdownHook-for-" + service, new Runnable() {
              public void run() {
                try {
                  service.shutdown();
                  service.awaitTermination(terminationTimeout, timeUnit);
                } catch (InterruptedException ignored) {}
              }
            }));
    }
    
    final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
      return getExitingExecutorService(executor, 120L, TimeUnit.SECONDS);
    }
    
    final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
      return getExitingScheduledExecutorService(executor, 120L, TimeUnit.SECONDS);
    }
    
    @VisibleForTesting
    void addShutdownHook(Thread hook) {
      Runtime.getRuntime().addShutdownHook(hook);
    }
  }
  
  private static void useDaemonThreadFactory(ThreadPoolExecutor executor) {
    executor.setThreadFactory((new ThreadFactoryBuilder()).setDaemon(true).setThreadFactory(executor.getThreadFactory()).build());
  }
  
  public static ListeningExecutorService sameThreadExecutor() {
    return new SameThreadExecutorService();
  }
  
  private static class SameThreadExecutorService extends AbstractListeningExecutorService {
    private final Lock lock = new ReentrantLock();
    
    private final Condition termination = this.lock.newCondition();
    
    private int runningTasks = 0;
    
    private boolean shutdown = false;
    
    public void execute(Runnable command) {
      startTask();
      try {
        command.run();
      } finally {
        endTask();
      } 
    }
    
    public boolean isShutdown() {
      this.lock.lock();
      try {
        return this.shutdown;
      } finally {
        this.lock.unlock();
      } 
    }
    
    public void shutdown() {
      this.lock.lock();
      try {
        this.shutdown = true;
      } finally {
        this.lock.unlock();
      } 
    }
    
    public List<Runnable> shutdownNow() {
      shutdown();
      return Collections.emptyList();
    }
    
    public boolean isTerminated() {
      this.lock.lock();
      try {
        return (this.shutdown && this.runningTasks == 0);
      } finally {
        this.lock.unlock();
      } 
    }
    
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
      long nanos = unit.toNanos(timeout);
      this.lock.lock();
      try {
        while (true) {
          if (isTerminated())
            return true; 
          if (nanos <= 0L)
            return false; 
          nanos = this.termination.awaitNanos(nanos);
        } 
      } finally {
        this.lock.unlock();
      } 
    }
    
    private void startTask() {
      this.lock.lock();
      try {
        if (isShutdown())
          throw new RejectedExecutionException("Executor already shutdown"); 
        this.runningTasks++;
      } finally {
        this.lock.unlock();
      } 
    }
    
    private void endTask() {
      this.lock.lock();
      try {
        this.runningTasks--;
        if (isTerminated())
          this.termination.signalAll(); 
      } finally {
        this.lock.unlock();
      } 
    }
    
    private SameThreadExecutorService() {}
  }
  
  public static ListeningExecutorService listeningDecorator(ExecutorService delegate) {
    return (delegate instanceof ListeningExecutorService) ? (ListeningExecutorService)delegate : ((delegate instanceof ScheduledExecutorService) ? new ScheduledListeningDecorator((ScheduledExecutorService)delegate) : new ListeningDecorator(delegate));
  }
  
  public static ListeningScheduledExecutorService listeningDecorator(ScheduledExecutorService delegate) {
    return (delegate instanceof ListeningScheduledExecutorService) ? (ListeningScheduledExecutorService)delegate : new ScheduledListeningDecorator(delegate);
  }
  
  private static class ListeningDecorator extends AbstractListeningExecutorService {
    private final ExecutorService delegate;
    
    ListeningDecorator(ExecutorService delegate) {
      this.delegate = (ExecutorService)Preconditions.checkNotNull(delegate);
    }
    
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
      return this.delegate.awaitTermination(timeout, unit);
    }
    
    public boolean isShutdown() {
      return this.delegate.isShutdown();
    }
    
    public boolean isTerminated() {
      return this.delegate.isTerminated();
    }
    
    public void shutdown() {
      this.delegate.shutdown();
    }
    
    public List<Runnable> shutdownNow() {
      return this.delegate.shutdownNow();
    }
    
    public void execute(Runnable command) {
      this.delegate.execute(command);
    }
  }
  
  private static class ScheduledListeningDecorator extends ListeningDecorator implements ListeningScheduledExecutorService {
    final ScheduledExecutorService delegate;
    
    ScheduledListeningDecorator(ScheduledExecutorService delegate) {
      super(delegate);
      this.delegate = (ScheduledExecutorService)Preconditions.checkNotNull(delegate);
    }
    
    public ListenableScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
      ListenableFutureTask<Void> task = ListenableFutureTask.create(command, null);
      ScheduledFuture<?> scheduled = this.delegate.schedule(task, delay, unit);
      return new ListenableScheduledTask(task, scheduled);
    }
    
    public <V> ListenableScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
      ListenableFutureTask<V> task = ListenableFutureTask.create(callable);
      ScheduledFuture<?> scheduled = this.delegate.schedule(task, delay, unit);
      return new ListenableScheduledTask<V>(task, scheduled);
    }
    
    public ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
      NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
      ScheduledFuture<?> scheduled = this.delegate.scheduleAtFixedRate(task, initialDelay, period, unit);
      return new ListenableScheduledTask(task, scheduled);
    }
    
    public ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
      NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
      ScheduledFuture<?> scheduled = this.delegate.scheduleWithFixedDelay(task, initialDelay, delay, unit);
      return new ListenableScheduledTask(task, scheduled);
    }
    
    private static final class ListenableScheduledTask<V> extends ForwardingListenableFuture.SimpleForwardingListenableFuture<V> implements ListenableScheduledFuture<V> {
      private final ScheduledFuture<?> scheduledDelegate;
      
      public ListenableScheduledTask(ListenableFuture<V> listenableDelegate, ScheduledFuture<?> scheduledDelegate) {
        super(listenableDelegate);
        this.scheduledDelegate = scheduledDelegate;
      }
      
      public boolean cancel(boolean mayInterruptIfRunning) {
        boolean cancelled = super.cancel(mayInterruptIfRunning);
        if (cancelled)
          this.scheduledDelegate.cancel(mayInterruptIfRunning); 
        return cancelled;
      }
      
      public long getDelay(TimeUnit unit) {
        return this.scheduledDelegate.getDelay(unit);
      }
      
      public int compareTo(Delayed other) {
        return this.scheduledDelegate.compareTo(other);
      }
    }
    
    private static final class NeverSuccessfulListenableFutureTask extends AbstractFuture<Void> implements Runnable {
      private final Runnable delegate;
      
      public NeverSuccessfulListenableFutureTask(Runnable delegate) {
        this.delegate = (Runnable)Preconditions.checkNotNull(delegate);
      }
      
      public void run() {
        try {
          this.delegate.run();
        } catch (Throwable t) {
          setException(t);
          throw Throwables.propagate(t);
        } 
      }
    }
  }
  
  static <T> T invokeAnyImpl(ListeningExecutorService executorService, Collection<? extends Callable<T>> tasks, boolean timed, long nanos) throws InterruptedException, ExecutionException, TimeoutException {
    Preconditions.checkNotNull(executorService);
    int ntasks = tasks.size();
    Preconditions.checkArgument((ntasks > 0));
    List<Future<T>> futures = Lists.newArrayListWithCapacity(ntasks);
    BlockingQueue<Future<T>> futureQueue = Queues.newLinkedBlockingQueue();
  }
  
  private static <T> ListenableFuture<T> submitAndAddQueueListener(ListeningExecutorService executorService, Callable<T> task, final BlockingQueue<Future<T>> queue) {
    final ListenableFuture<T> future = executorService.submit(task);
    future.addListener(new Runnable() {
          public void run() {
            queue.add(future);
          }
        },  sameThreadExecutor());
    return future;
  }
  
  @Beta
  public static ThreadFactory platformThreadFactory() {
    if (!isAppEngine())
      return Executors.defaultThreadFactory(); 
    try {
      return (ThreadFactory)Class.forName("com.google.appengine.api.ThreadManager").getMethod("currentRequestThreadFactory", new Class[0]).invoke(null, new Object[0]);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
    } catch (InvocationTargetException e) {
      throw Throwables.propagate(e.getCause());
    } 
  }
  
  private static boolean isAppEngine() {
    if (System.getProperty("com.google.appengine.runtime.environment") == null)
      return false; 
    try {
      return (Class.forName("com.google.apphosting.api.ApiProxy").getMethod("getCurrentEnvironment", new Class[0]).invoke(null, new Object[0]) != null);
    } catch (ClassNotFoundException e) {
      return false;
    } catch (InvocationTargetException e) {
      return false;
    } catch (IllegalAccessException e) {
      return false;
    } catch (NoSuchMethodException e) {
      return false;
    } 
  }
  
  static Thread newThread(String name, Runnable runnable) {
    Preconditions.checkNotNull(name);
    Preconditions.checkNotNull(runnable);
    Thread result = platformThreadFactory().newThread(runnable);
    try {
      result.setName(name);
    } catch (SecurityException e) {}
    return result;
  }
  
  static Executor renamingDecorator(final Executor executor, final Supplier<String> nameSupplier) {
    Preconditions.checkNotNull(executor);
    Preconditions.checkNotNull(nameSupplier);
    if (isAppEngine())
      return executor; 
    return new Executor() {
        public void execute(Runnable command) {
          executor.execute(Callables.threadRenaming(command, nameSupplier));
        }
      };
  }
  
  static ExecutorService renamingDecorator(ExecutorService service, final Supplier<String> nameSupplier) {
    Preconditions.checkNotNull(service);
    Preconditions.checkNotNull(nameSupplier);
    if (isAppEngine())
      return service; 
    return new WrappingExecutorService(service) {
        protected <T> Callable<T> wrapTask(Callable<T> callable) {
          return Callables.threadRenaming(callable, nameSupplier);
        }
        
        protected Runnable wrapTask(Runnable command) {
          return Callables.threadRenaming(command, nameSupplier);
        }
      };
  }
  
  static ScheduledExecutorService renamingDecorator(ScheduledExecutorService service, final Supplier<String> nameSupplier) {
    Preconditions.checkNotNull(service);
    Preconditions.checkNotNull(nameSupplier);
    if (isAppEngine())
      return service; 
    return new WrappingScheduledExecutorService(service) {
        protected <T> Callable<T> wrapTask(Callable<T> callable) {
          return Callables.threadRenaming(callable, nameSupplier);
        }
        
        protected Runnable wrapTask(Runnable command) {
          return Callables.threadRenaming(command, nameSupplier);
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
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      service.shutdownNow();
    } 
    return service.isTerminated();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\commo\\util\concurrent\MoreExecutors.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */