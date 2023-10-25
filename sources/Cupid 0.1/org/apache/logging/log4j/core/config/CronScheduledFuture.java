package org.apache.logging.log4j.core.config;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CronScheduledFuture<V> implements ScheduledFuture<V> {
  private volatile FutureData futureData;
  
  public CronScheduledFuture(ScheduledFuture<V> future, Date runDate) {
    this.futureData = new FutureData(future, runDate);
  }
  
  public Date getFireTime() {
    return this.futureData.runDate;
  }
  
  void reset(ScheduledFuture<?> future, Date runDate) {
    this.futureData = new FutureData(future, runDate);
  }
  
  public long getDelay(TimeUnit unit) {
    return this.futureData.scheduledFuture.getDelay(unit);
  }
  
  public int compareTo(Delayed delayed) {
    return this.futureData.scheduledFuture.compareTo(delayed);
  }
  
  public boolean cancel(boolean mayInterruptIfRunning) {
    return this.futureData.scheduledFuture.cancel(mayInterruptIfRunning);
  }
  
  public boolean isCancelled() {
    return this.futureData.scheduledFuture.isCancelled();
  }
  
  public boolean isDone() {
    return this.futureData.scheduledFuture.isDone();
  }
  
  public V get() throws InterruptedException, ExecutionException {
    return (V)this.futureData.scheduledFuture.get();
  }
  
  public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    return (V)this.futureData.scheduledFuture.get(timeout, unit);
  }
  
  private class FutureData {
    private final ScheduledFuture<?> scheduledFuture;
    
    private final Date runDate;
    
    FutureData(ScheduledFuture<?> future, Date runDate) {
      this.scheduledFuture = future;
      this.runDate = runDate;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\CronScheduledFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */