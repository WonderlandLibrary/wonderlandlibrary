package org.apache.commons.lang3.concurrent;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimedSemaphore {
  public static final int NO_LIMIT = 0;
  
  private static final int THREAD_POOL_SIZE = 1;
  
  private final ScheduledExecutorService executorService;
  
  private final long period;
  
  private final TimeUnit unit;
  
  private final boolean ownExecutor;
  
  private ScheduledFuture<?> task;
  
  private long totalAcquireCount;
  
  private long periodCount;
  
  private int limit;
  
  private int acquireCount;
  
  private int lastCallsPerPeriod;
  
  private boolean shutdown;
  
  public TimedSemaphore(long timePeriod, TimeUnit timeUnit, int limit) {
    this(null, timePeriod, timeUnit, limit);
  }
  
  public TimedSemaphore(ScheduledExecutorService service, long timePeriod, TimeUnit timeUnit, int limit) {
    if (timePeriod <= 0L)
      throw new IllegalArgumentException("Time period must be greater 0!"); 
    this.period = timePeriod;
    this.unit = timeUnit;
    if (service != null) {
      this.executorService = service;
      this.ownExecutor = false;
    } else {
      ScheduledThreadPoolExecutor s = new ScheduledThreadPoolExecutor(1);
      s.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
      s.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
      this.executorService = s;
      this.ownExecutor = true;
    } 
    setLimit(limit);
  }
  
  public final synchronized int getLimit() {
    return this.limit;
  }
  
  public final synchronized void setLimit(int limit) {
    this.limit = limit;
  }
  
  public synchronized void shutdown() {
    if (!this.shutdown) {
      if (this.ownExecutor)
        getExecutorService().shutdownNow(); 
      if (this.task != null)
        this.task.cancel(false); 
      this.shutdown = true;
    } 
  }
  
  public synchronized boolean isShutdown() {
    return this.shutdown;
  }
  
  public synchronized void acquire() throws InterruptedException {
    if (isShutdown())
      throw new IllegalStateException("TimedSemaphore is shut down!"); 
    if (this.task == null)
      this.task = startTimer(); 
    boolean canPass = false;
    do {
      canPass = (getLimit() <= 0 || this.acquireCount < getLimit());
      if (!canPass) {
        wait();
      } else {
        this.acquireCount++;
      } 
    } while (!canPass);
  }
  
  public synchronized int getLastAcquiresPerPeriod() {
    return this.lastCallsPerPeriod;
  }
  
  public synchronized int getAcquireCount() {
    return this.acquireCount;
  }
  
  public synchronized int getAvailablePermits() {
    return getLimit() - getAcquireCount();
  }
  
  public synchronized double getAverageCallsPerPeriod() {
    return (this.periodCount == 0L) ? 0.0D : (this.totalAcquireCount / this.periodCount);
  }
  
  public long getPeriod() {
    return this.period;
  }
  
  public TimeUnit getUnit() {
    return this.unit;
  }
  
  protected ScheduledExecutorService getExecutorService() {
    return this.executorService;
  }
  
  protected ScheduledFuture<?> startTimer() {
    return getExecutorService().scheduleAtFixedRate(new Runnable() {
          public void run() {
            TimedSemaphore.this.endOfPeriod();
          }
        },  getPeriod(), getPeriod(), getUnit());
  }
  
  synchronized void endOfPeriod() {
    this.lastCallsPerPeriod = this.acquireCount;
    this.totalAcquireCount += this.acquireCount;
    this.periodCount++;
    this.acquireCount = 0;
    notifyAll();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\concurrent\TimedSemaphore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */