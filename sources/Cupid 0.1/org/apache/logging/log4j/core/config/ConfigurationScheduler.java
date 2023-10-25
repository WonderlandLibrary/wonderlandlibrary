package org.apache.logging.log4j.core.config;

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.AbstractLifeCycle;
import org.apache.logging.log4j.core.util.CronExpression;
import org.apache.logging.log4j.core.util.Log4jThreadFactory;
import org.apache.logging.log4j.status.StatusLogger;

public class ConfigurationScheduler extends AbstractLifeCycle {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static final String SIMPLE_NAME = "Log4j2 " + ConfigurationScheduler.class.getSimpleName();
  
  private static final int MAX_SCHEDULED_ITEMS = 5;
  
  private volatile ScheduledExecutorService executorService;
  
  private int scheduledItems = 0;
  
  private final String name;
  
  public ConfigurationScheduler() {
    this(SIMPLE_NAME);
  }
  
  public ConfigurationScheduler(String name) {
    this.name = name;
  }
  
  public void start() {
    super.start();
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    setStopping();
    if (isExecutorServiceSet()) {
      LOGGER.debug("{} shutting down threads in {}", this.name, getExecutorService());
      this.executorService.shutdown();
      try {
        this.executorService.awaitTermination(timeout, timeUnit);
      } catch (InterruptedException ie) {
        this.executorService.shutdownNow();
        try {
          this.executorService.awaitTermination(timeout, timeUnit);
        } catch (InterruptedException inner) {
          LOGGER.warn("{} stopped but some scheduled services may not have completed.", this.name);
        } 
        Thread.currentThread().interrupt();
      } 
    } 
    setStopped();
    return true;
  }
  
  public boolean isExecutorServiceSet() {
    return (this.executorService != null);
  }
  
  public void incrementScheduledItems() {
    if (isExecutorServiceSet()) {
      LOGGER.error("{} attempted to increment scheduled items after start", this.name);
    } else {
      this.scheduledItems++;
    } 
  }
  
  public void decrementScheduledItems() {
    if (!isStarted() && this.scheduledItems > 0)
      this.scheduledItems--; 
  }
  
  public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
    return getExecutorService().schedule(callable, delay, unit);
  }
  
  public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
    return getExecutorService().schedule(command, delay, unit);
  }
  
  public CronScheduledFuture<?> scheduleWithCron(CronExpression cronExpression, Runnable command) {
    return scheduleWithCron(cronExpression, new Date(), command);
  }
  
  public CronScheduledFuture<?> scheduleWithCron(CronExpression cronExpression, Date startDate, Runnable command) {
    Date fireDate = cronExpression.getNextValidTimeAfter((startDate == null) ? new Date() : startDate);
    CronRunnable runnable = new CronRunnable(command, cronExpression);
    ScheduledFuture<?> future = schedule(runnable, nextFireInterval(fireDate), TimeUnit.MILLISECONDS);
    CronScheduledFuture<?> cronScheduledFuture = new CronScheduledFuture(future, fireDate);
    runnable.setScheduledFuture(cronScheduledFuture);
    LOGGER.debug("{} scheduled cron expression {} to fire at {}", this.name, cronExpression.getCronExpression(), fireDate);
    return cronScheduledFuture;
  }
  
  public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
    return getExecutorService().scheduleAtFixedRate(command, initialDelay, period, unit);
  }
  
  public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
    return getExecutorService().scheduleWithFixedDelay(command, initialDelay, delay, unit);
  }
  
  public long nextFireInterval(Date fireDate) {
    return fireDate.getTime() - (new Date()).getTime();
  }
  
  private ScheduledExecutorService getExecutorService() {
    if (this.executorService == null)
      synchronized (this) {
        if (this.executorService == null)
          if (this.scheduledItems > 0) {
            LOGGER.debug("{} starting {} threads", this.name, Integer.valueOf(this.scheduledItems));
            this.scheduledItems = Math.min(this.scheduledItems, 5);
            ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(this.scheduledItems, (ThreadFactory)Log4jThreadFactory.createDaemonThreadFactory("Scheduled"));
            executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
            executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
            this.executorService = executor;
          } else {
            LOGGER.debug("{}: No scheduled items", this.name);
          }  
      }  
    return this.executorService;
  }
  
  public class CronRunnable implements Runnable {
    private final CronExpression cronExpression;
    
    private final Runnable runnable;
    
    private CronScheduledFuture<?> scheduledFuture;
    
    public CronRunnable(Runnable runnable, CronExpression cronExpression) {
      this.cronExpression = cronExpression;
      this.runnable = runnable;
    }
    
    public void setScheduledFuture(CronScheduledFuture<?> future) {
      this.scheduledFuture = future;
    }
    
    public void run() {
      try {
        long millis = this.scheduledFuture.getFireTime().getTime() - System.currentTimeMillis();
        if (millis > 0L) {
          ConfigurationScheduler.LOGGER.debug("{} Cron thread woke up {} millis early. Sleeping", ConfigurationScheduler.this.name, Long.valueOf(millis));
          try {
            Thread.sleep(millis);
          } catch (InterruptedException interruptedException) {}
        } 
        this.runnable.run();
      } catch (Throwable ex) {
        ConfigurationScheduler.LOGGER.error("{} caught error running command", ConfigurationScheduler.this.name, ex);
      } finally {
        Date fireDate = this.cronExpression.getNextValidTimeAfter(new Date());
        ScheduledFuture<?> future = ConfigurationScheduler.this.schedule(this, ConfigurationScheduler.this.nextFireInterval(fireDate), TimeUnit.MILLISECONDS);
        ConfigurationScheduler.LOGGER.debug("{} Cron expression {} scheduled to fire again at {}", ConfigurationScheduler.this.name, this.cronExpression.getCronExpression(), fireDate);
        this.scheduledFuture.reset(future, fireDate);
      } 
    }
    
    public String toString() {
      return "CronRunnable{" + this.cronExpression.getCronExpression() + " - " + this.scheduledFuture.getFireTime();
    }
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder("ConfigurationScheduler [name=");
    sb.append(this.name);
    sb.append(", [");
    if (this.executorService != null) {
      Queue<Runnable> queue = ((ScheduledThreadPoolExecutor)this.executorService).getQueue();
      boolean first = true;
      for (Runnable runnable : queue) {
        if (!first)
          sb.append(", "); 
        sb.append(runnable.toString());
        first = false;
      } 
    } 
    sb.append("]");
    return sb.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\ConfigurationScheduler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */