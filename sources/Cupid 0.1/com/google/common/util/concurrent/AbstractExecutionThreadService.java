package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Beta
public abstract class AbstractExecutionThreadService implements Service {
  private static final Logger logger = Logger.getLogger(AbstractExecutionThreadService.class.getName());
  
  private final Service delegate = new AbstractService() {
      protected final void doStart() {
        Executor executor = MoreExecutors.renamingDecorator(AbstractExecutionThreadService.this.executor(), new Supplier<String>() {
              public String get() {
                return AbstractExecutionThreadService.this.serviceName();
              }
            });
        executor.execute(new Runnable() {
              public void run() {
                try {
                  AbstractExecutionThreadService.this.startUp();
                  AbstractExecutionThreadService.null.this.notifyStarted();
                  if (AbstractExecutionThreadService.null.this.isRunning())
                    try {
                      AbstractExecutionThreadService.this.run();
                    } catch (Throwable t) {
                      try {
                        AbstractExecutionThreadService.this.shutDown();
                      } catch (Exception ignored) {
                        AbstractExecutionThreadService.logger.log(Level.WARNING, "Error while attempting to shut down the service after failure.", ignored);
                      } 
                      throw t;
                    }  
                  AbstractExecutionThreadService.this.shutDown();
                  AbstractExecutionThreadService.null.this.notifyStopped();
                } catch (Throwable t) {
                  AbstractExecutionThreadService.null.this.notifyFailed(t);
                  throw Throwables.propagate(t);
                } 
              }
            });
      }
      
      protected void doStop() {
        AbstractExecutionThreadService.this.triggerShutdown();
      }
    };
  
  protected void startUp() throws Exception {}
  
  protected void shutDown() throws Exception {}
  
  protected void triggerShutdown() {}
  
  protected Executor executor() {
    return new Executor() {
        public void execute(Runnable command) {
          MoreExecutors.newThread(AbstractExecutionThreadService.this.serviceName(), command).start();
        }
      };
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
  
  protected String serviceName() {
    return getClass().getSimpleName();
  }
  
  protected abstract void run() throws Exception;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\commo\\util\concurrent\AbstractExecutionThreadService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */