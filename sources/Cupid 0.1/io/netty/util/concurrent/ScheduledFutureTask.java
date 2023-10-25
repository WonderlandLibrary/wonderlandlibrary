package io.netty.util.concurrent;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

final class ScheduledFutureTask<V> extends PromiseTask<V> implements ScheduledFuture<V> {
  private static final AtomicLong nextTaskId = new AtomicLong();
  
  private static final long START_TIME = System.nanoTime();
  
  static long nanoTime() {
    return System.nanoTime() - START_TIME;
  }
  
  static long deadlineNanos(long delay) {
    return nanoTime() + delay;
  }
  
  private final long id = nextTaskId.getAndIncrement();
  
  private final Queue<ScheduledFutureTask<?>> delayedTaskQueue;
  
  private long deadlineNanos;
  
  private final long periodNanos;
  
  ScheduledFutureTask(EventExecutor executor, Queue<ScheduledFutureTask<?>> delayedTaskQueue, Runnable runnable, V result, long nanoTime) {
    this(executor, delayedTaskQueue, toCallable(runnable, result), nanoTime);
  }
  
  ScheduledFutureTask(EventExecutor executor, Queue<ScheduledFutureTask<?>> delayedTaskQueue, Callable<V> callable, long nanoTime, long period) {
    super(executor, callable);
    if (period == 0L)
      throw new IllegalArgumentException("period: 0 (expected: != 0)"); 
    this.delayedTaskQueue = delayedTaskQueue;
    this.deadlineNanos = nanoTime;
    this.periodNanos = period;
  }
  
  ScheduledFutureTask(EventExecutor executor, Queue<ScheduledFutureTask<?>> delayedTaskQueue, Callable<V> callable, long nanoTime) {
    super(executor, callable);
    this.delayedTaskQueue = delayedTaskQueue;
    this.deadlineNanos = nanoTime;
    this.periodNanos = 0L;
  }
  
  protected EventExecutor executor() {
    return super.executor();
  }
  
  public long deadlineNanos() {
    return this.deadlineNanos;
  }
  
  public long delayNanos() {
    return Math.max(0L, deadlineNanos() - nanoTime());
  }
  
  public long delayNanos(long currentTimeNanos) {
    return Math.max(0L, deadlineNanos() - currentTimeNanos - START_TIME);
  }
  
  public long getDelay(TimeUnit unit) {
    return unit.convert(delayNanos(), TimeUnit.NANOSECONDS);
  }
  
  public int compareTo(Delayed o) {
    if (this == o)
      return 0; 
    ScheduledFutureTask<?> that = (ScheduledFutureTask)o;
    long d = deadlineNanos() - that.deadlineNanos();
    if (d < 0L)
      return -1; 
    if (d > 0L)
      return 1; 
    if (this.id < that.id)
      return -1; 
    if (this.id == that.id)
      throw new Error(); 
    return 1;
  }
  
  public void run() {
    assert executor().inEventLoop();
    try {
      if (this.periodNanos == 0L) {
        if (setUncancellableInternal()) {
          V result = this.task.call();
          setSuccessInternal(result);
        } 
      } else if (!isCancelled()) {
        this.task.call();
        if (!executor().isShutdown()) {
          long p = this.periodNanos;
          if (p > 0L) {
            this.deadlineNanos += p;
          } else {
            this.deadlineNanos = nanoTime() - p;
          } 
          if (!isCancelled())
            this.delayedTaskQueue.add(this); 
        } 
      } 
    } catch (Throwable cause) {
      setFailureInternal(cause);
    } 
  }
  
  protected StringBuilder toStringBuilder() {
    StringBuilder buf = super.toStringBuilder();
    buf.setCharAt(buf.length() - 1, ',');
    buf.append(" id: ");
    buf.append(this.id);
    buf.append(", deadline: ");
    buf.append(this.deadlineNanos);
    buf.append(", period: ");
    buf.append(this.periodNanos);
    buf.append(')');
    return buf;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\ScheduledFutureTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */