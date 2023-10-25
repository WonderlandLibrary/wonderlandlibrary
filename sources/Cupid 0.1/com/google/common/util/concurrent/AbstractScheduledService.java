package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
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
public abstract class AbstractScheduledService implements Service {
  private static final Logger logger = Logger.getLogger(AbstractScheduledService.class.getName());
  
  public static abstract class Scheduler {
    public static Scheduler newFixedDelaySchedule(final long initialDelay, final long delay, final TimeUnit unit) {
      return new Scheduler() {
          public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable task) {
            return executor.scheduleWithFixedDelay(task, initialDelay, delay, unit);
          }
        };
    }
    
    public static Scheduler newFixedRateSchedule(final long initialDelay, final long period, final TimeUnit unit) {
      return new Scheduler() {
          public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable task) {
            return executor.scheduleAtFixedRate(task, initialDelay, period, unit);
          }
        };
    }
    
    private Scheduler() {}
    
    abstract Future<?> schedule(AbstractService param1AbstractService, ScheduledExecutorService param1ScheduledExecutorService, Runnable param1Runnable);
  }
  
  private final AbstractService delegate = new AbstractService() {
      private volatile Future<?> runningTask;
      
      private volatile ScheduledExecutorService executorService;
      
      private final ReentrantLock lock = new ReentrantLock();
      
      private final Runnable task = new Runnable() {
          public void run() {
            AbstractScheduledService.null.this.lock.lock();
            try {
              AbstractScheduledService.this.runOneIteration();
            } catch (Throwable t) {
              try {
                AbstractScheduledService.this.shutDown();
              } catch (Exception ignored) {
                AbstractScheduledService.logger.log(Level.WARNING, "Error while attempting to shut down the service after failure.", ignored);
              } 
              AbstractScheduledService.null.this.notifyFailed(t);
              throw Throwables.propagate(t);
            } finally {
              AbstractScheduledService.null.this.lock.unlock();
            } 
          }
        };
      
      protected final void doStart() {
        this.executorService = MoreExecutors.renamingDecorator(AbstractScheduledService.this.executor(), new Supplier<String>() {
              public String get() {
                return AbstractScheduledService.this.serviceName() + " " + AbstractScheduledService.null.this.state();
              }
            });
        this.executorService.execute(new Runnable() {
              public void run() {
                AbstractScheduledService.null.this.lock.lock();
                try {
                  AbstractScheduledService.this.startUp();
                  AbstractScheduledService.null.this.runningTask = AbstractScheduledService.this.scheduler().schedule(AbstractScheduledService.this.delegate, AbstractScheduledService.null.this.executorService, AbstractScheduledService.null.this.task);
                  AbstractScheduledService.null.this.notifyStarted();
                } catch (Throwable t) {
                  AbstractScheduledService.null.this.notifyFailed(t);
                  throw Throwables.propagate(t);
                } finally {
                  AbstractScheduledService.null.this.lock.unlock();
                } 
              }
            });
      }
      
      protected final void doStop() {
        this.runningTask.cancel(false);
        this.executorService.execute(new Runnable() {
              public void run() {
                try {
                  AbstractScheduledService.null.this.lock.lock();
                  try {
                    if (AbstractScheduledService.null.this.state() != Service.State.STOPPING)
                      return; 
                    AbstractScheduledService.this.shutDown();
                  } finally {
                    AbstractScheduledService.null.this.lock.unlock();
                  } 
                  AbstractScheduledService.null.this.notifyStopped();
                } catch (Throwable t) {
                  AbstractScheduledService.null.this.notifyFailed(t);
                  throw Throwables.propagate(t);
                } 
              }
            });
      }
    };
  
  protected void startUp() throws Exception {}
  
  protected void shutDown() throws Exception {}
  
  protected ScheduledExecutorService executor() {
    final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
          public Thread newThread(Runnable runnable) {
            return MoreExecutors.newThread(AbstractScheduledService.this.serviceName(), runnable);
          }
        });
    addListener(new Service.Listener() {
          public void terminated(Service.State from) {
            executor.shutdown();
          }
          
          public void failed(Service.State from, Throwable failure) {
            executor.shutdown();
          }
        },  MoreExecutors.sameThreadExecutor());
    return executor;
  }
  
  protected String serviceName() {
    return getClass().getSimpleName();
  }
  
  public String toString() {
    return serviceName() + " [" + state() + "]";
  }
  
  public final boolean isRunning() {
    return this.delegate.isRunning();
  }
  
  public final Service.State state() {
    return this.delegate.state();
  }
  
  public final void addListener(Service.Listener listener, Executor executor) {
    this.delegate.addListener(listener, executor);
  }
  
  public final Throwable failureCause() {
    return this.delegate.failureCause();
  }
  
  public final Service startAsync() {
    this.delegate.startAsync();
    return this;
  }
  
  public final Service stopAsync() {
    this.delegate.stopAsync();
    return this;
  }
  
  public final void awaitRunning() {
    this.delegate.awaitRunning();
  }
  
  public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
    this.delegate.awaitRunning(timeout, unit);
  }
  
  public final void awaitTerminated() {
    this.delegate.awaitTerminated();
  }
  
  public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
    this.delegate.awaitTerminated(timeout, unit);
  }
  
  protected abstract void runOneIteration() throws Exception;
  
  protected abstract Scheduler scheduler();
  
  @Beta
  public static abstract class CustomScheduler extends Scheduler {
    private class ReschedulableCallable extends ForwardingFuture<Void> implements Callable<Void> {
      private final Runnable wrappedRunnable;
      
      private final ScheduledExecutorService executor;
      
      private final AbstractService service;
      
      private final ReentrantLock lock = new ReentrantLock();
      
      @GuardedBy("lock")
      private Future<Void> currentFuture;
      
      ReschedulableCallable(AbstractService service, ScheduledExecutorService executor, Runnable runnable) {
        this.wrappedRunnable = runnable;
        this.executor = executor;
        this.service = service;
      }
      
      public Void call() throws Exception {
        this.wrappedRunnable.run();
        reschedule();
        return null;
      }
      
      public void reschedule() {
        this.lock.lock();
        try {
          if (this.currentFuture == null || !this.currentFuture.isCancelled()) {
            AbstractScheduledService.CustomScheduler.Schedule schedule = AbstractScheduledService.CustomScheduler.this.getNextSchedule();
            this.currentFuture = this.executor.schedule(this, schedule.delay, schedule.unit);
          } 
        } catch (Throwable e) {
          this.service.notifyFailed(e);
        } finally {
          this.lock.unlock();
        } 
      }
      
      public boolean cancel(boolean mayInterruptIfRunning) {
        this.lock.lock();
        try {
          return this.currentFuture.cancel(mayInterruptIfRunning);
        } finally {
          this.lock.unlock();
        } 
      }
      
      protected Future<Void> delegate() {
        throw new UnsupportedOperationException("Only cancel is supported by this future");
      }
    }
    
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
        this.unit = (TimeUnit)Preconditions.checkNotNull(unit);
      }
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\commo\\util\concurrent\AbstractScheduledService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */