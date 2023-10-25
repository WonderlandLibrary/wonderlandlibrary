package io.netty.util.concurrent;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class GlobalEventExecutor extends AbstractEventExecutor {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(GlobalEventExecutor.class);
  
  private static final long SCHEDULE_PURGE_INTERVAL = TimeUnit.SECONDS.toNanos(1L);
  
  public static final GlobalEventExecutor INSTANCE = new GlobalEventExecutor();
  
  final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
  
  final Queue<ScheduledFutureTask<?>> delayedTaskQueue = new PriorityQueue<ScheduledFutureTask<?>>();
  
  final ScheduledFutureTask<Void> purgeTask = new ScheduledFutureTask<Void>(this, this.delayedTaskQueue, Executors.callable(new PurgeTask(), null), ScheduledFutureTask.deadlineNanos(SCHEDULE_PURGE_INTERVAL), -SCHEDULE_PURGE_INTERVAL);
  
  private final ThreadFactory threadFactory = new DefaultThreadFactory(getClass());
  
  private final TaskRunner taskRunner = new TaskRunner();
  
  private final AtomicBoolean started = new AtomicBoolean();
  
  volatile Thread thread;
  
  private final Future<?> terminationFuture = new FailedFuture(this, new UnsupportedOperationException());
  
  private GlobalEventExecutor() {
    this.delayedTaskQueue.add(this.purgeTask);
  }
  
  public EventExecutorGroup parent() {
    return null;
  }
  
  Runnable takeTask() {
    BlockingQueue<Runnable> taskQueue = this.taskQueue;
    while (true) {
      Runnable runnable;
      ScheduledFutureTask<?> delayedTask = this.delayedTaskQueue.peek();
      if (delayedTask == null) {
        Runnable task = null;
        try {
          task = taskQueue.take();
        } catch (InterruptedException e) {}
        return task;
      } 
      long delayNanos = delayedTask.delayNanos();
      if (delayNanos > 0L) {
        try {
          runnable = taskQueue.poll(delayNanos, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
          return null;
        } 
      } else {
        runnable = taskQueue.poll();
      } 
      if (runnable == null) {
        fetchFromDelayedQueue();
        runnable = taskQueue.poll();
      } 
      if (runnable != null)
        return runnable; 
    } 
  }
  
  private void fetchFromDelayedQueue() {
    long nanoTime = 0L;
    while (true) {
      ScheduledFutureTask<?> delayedTask = this.delayedTaskQueue.peek();
      if (delayedTask == null)
        break; 
      if (nanoTime == 0L)
        nanoTime = ScheduledFutureTask.nanoTime(); 
      if (delayedTask.deadlineNanos() <= nanoTime) {
        this.delayedTaskQueue.remove();
        this.taskQueue.add(delayedTask);
        continue;
      } 
      break;
    } 
  }
  
  public int pendingTasks() {
    return this.taskQueue.size();
  }
  
  private void addTask(Runnable task) {
    if (task == null)
      throw new NullPointerException("task"); 
    this.taskQueue.add(task);
  }
  
  public boolean inEventLoop(Thread thread) {
    return (thread == this.thread);
  }
  
  public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
    return terminationFuture();
  }
  
  public Future<?> terminationFuture() {
    return this.terminationFuture;
  }
  
  @Deprecated
  public void shutdown() {
    throw new UnsupportedOperationException();
  }
  
  public boolean isShuttingDown() {
    return false;
  }
  
  public boolean isShutdown() {
    return false;
  }
  
  public boolean isTerminated() {
    return false;
  }
  
  public boolean awaitTermination(long timeout, TimeUnit unit) {
    return false;
  }
  
  public boolean awaitInactivity(long timeout, TimeUnit unit) throws InterruptedException {
    if (unit == null)
      throw new NullPointerException("unit"); 
    Thread thread = this.thread;
    if (thread == null)
      throw new IllegalStateException("thread was not started"); 
    thread.join(unit.toMillis(timeout));
    return !thread.isAlive();
  }
  
  public void execute(Runnable task) {
    if (task == null)
      throw new NullPointerException("task"); 
    addTask(task);
    if (!inEventLoop())
      startThread(); 
  }
  
  public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
    if (command == null)
      throw new NullPointerException("command"); 
    if (unit == null)
      throw new NullPointerException("unit"); 
    if (delay < 0L)
      throw new IllegalArgumentException(String.format("delay: %d (expected: >= 0)", new Object[] { Long.valueOf(delay) })); 
    return schedule(new ScheduledFutureTask(this, this.delayedTaskQueue, command, null, ScheduledFutureTask.deadlineNanos(unit.toNanos(delay))));
  }
  
  public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
    if (callable == null)
      throw new NullPointerException("callable"); 
    if (unit == null)
      throw new NullPointerException("unit"); 
    if (delay < 0L)
      throw new IllegalArgumentException(String.format("delay: %d (expected: >= 0)", new Object[] { Long.valueOf(delay) })); 
    return schedule(new ScheduledFutureTask<V>(this, this.delayedTaskQueue, callable, ScheduledFutureTask.deadlineNanos(unit.toNanos(delay))));
  }
  
  public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
    if (command == null)
      throw new NullPointerException("command"); 
    if (unit == null)
      throw new NullPointerException("unit"); 
    if (initialDelay < 0L)
      throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", new Object[] { Long.valueOf(initialDelay) })); 
    if (period <= 0L)
      throw new IllegalArgumentException(String.format("period: %d (expected: > 0)", new Object[] { Long.valueOf(period) })); 
    return schedule(new ScheduledFutureTask(this, this.delayedTaskQueue, Executors.callable(command, null), ScheduledFutureTask.deadlineNanos(unit.toNanos(initialDelay)), unit.toNanos(period)));
  }
  
  public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
    if (command == null)
      throw new NullPointerException("command"); 
    if (unit == null)
      throw new NullPointerException("unit"); 
    if (initialDelay < 0L)
      throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", new Object[] { Long.valueOf(initialDelay) })); 
    if (delay <= 0L)
      throw new IllegalArgumentException(String.format("delay: %d (expected: > 0)", new Object[] { Long.valueOf(delay) })); 
    return schedule(new ScheduledFutureTask(this, this.delayedTaskQueue, Executors.callable(command, null), ScheduledFutureTask.deadlineNanos(unit.toNanos(initialDelay)), -unit.toNanos(delay)));
  }
  
  private <V> ScheduledFuture<V> schedule(final ScheduledFutureTask<V> task) {
    if (task == null)
      throw new NullPointerException("task"); 
    if (inEventLoop()) {
      this.delayedTaskQueue.add(task);
    } else {
      execute(new Runnable() {
            public void run() {
              GlobalEventExecutor.this.delayedTaskQueue.add(task);
            }
          });
    } 
    return task;
  }
  
  private void startThread() {
    if (this.started.compareAndSet(false, true)) {
      Thread t = this.threadFactory.newThread(this.taskRunner);
      t.start();
      this.thread = t;
    } 
  }
  
  final class TaskRunner implements Runnable {
    public void run() {
      while (true) {
        Runnable task = GlobalEventExecutor.this.takeTask();
        if (task != null) {
          try {
            task.run();
          } catch (Throwable t) {
            GlobalEventExecutor.logger.warn("Unexpected exception from the global event executor: ", t);
          } 
          if (task != GlobalEventExecutor.this.purgeTask)
            continue; 
        } 
        if (GlobalEventExecutor.this.taskQueue.isEmpty() && GlobalEventExecutor.this.delayedTaskQueue.size() == 1) {
          boolean stopped = GlobalEventExecutor.this.started.compareAndSet(true, false);
          assert stopped;
          if (GlobalEventExecutor.this.taskQueue.isEmpty() && GlobalEventExecutor.this.delayedTaskQueue.size() == 1)
            break; 
          if (!GlobalEventExecutor.this.started.compareAndSet(false, true))
            break; 
        } 
      } 
    }
  }
  
  private final class PurgeTask implements Runnable {
    private PurgeTask() {}
    
    public void run() {
      Iterator<ScheduledFutureTask<?>> i = GlobalEventExecutor.this.delayedTaskQueue.iterator();
      while (i.hasNext()) {
        ScheduledFutureTask<?> task = i.next();
        if (task.isCancelled())
          i.remove(); 
      } 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\GlobalEventExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */