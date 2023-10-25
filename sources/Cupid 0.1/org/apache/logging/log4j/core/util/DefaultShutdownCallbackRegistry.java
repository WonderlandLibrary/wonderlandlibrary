package org.apache.logging.log4j.core.util;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.AbstractLifeCycle;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LifeCycle2;
import org.apache.logging.log4j.status.StatusLogger;

public class DefaultShutdownCallbackRegistry implements ShutdownCallbackRegistry, LifeCycle2, Runnable {
  protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private final AtomicReference<LifeCycle.State> state = new AtomicReference<>(LifeCycle.State.INITIALIZED);
  
  private final ThreadFactory threadFactory;
  
  private final Collection<Reference<Cancellable>> hooks = new CopyOnWriteArrayList<>();
  
  private Reference<Thread> shutdownHookRef;
  
  public DefaultShutdownCallbackRegistry() {
    this(Executors.defaultThreadFactory());
  }
  
  protected DefaultShutdownCallbackRegistry(ThreadFactory threadFactory) {
    this.threadFactory = threadFactory;
  }
  
  public void run() {
    if (this.state.compareAndSet(LifeCycle.State.STARTED, LifeCycle.State.STOPPING)) {
      for (Reference<Cancellable> hookRef : this.hooks) {
        Cancellable hook = hookRef.get();
        if (hook != null)
          try {
            hook.run();
          } catch (Throwable t1) {
            try {
              LOGGER.error(SHUTDOWN_HOOK_MARKER, "Caught exception executing shutdown hook {}", hook, t1);
            } catch (Throwable t2) {
              System.err.println("Caught exception " + t2.getClass() + " logging exception " + t1.getClass());
              t1.printStackTrace();
            } 
          }  
      } 
      this.state.set(LifeCycle.State.STOPPED);
    } 
  }
  
  private static class RegisteredCancellable implements Cancellable {
    private Runnable callback;
    
    private Collection<Reference<Cancellable>> registered;
    
    RegisteredCancellable(Runnable callback, Collection<Reference<Cancellable>> registered) {
      this.callback = callback;
      this.registered = registered;
    }
    
    public void cancel() {
      this.callback = null;
      Collection<Reference<Cancellable>> references = this.registered;
      if (references != null) {
        this.registered = null;
        references.removeIf(ref -> {
              Cancellable value = ref.get();
              return (value == null || value == this);
            });
      } 
    }
    
    public void run() {
      Runnable runnableHook = this.callback;
      if (runnableHook != null) {
        runnableHook.run();
        this.callback = null;
      } 
    }
    
    public String toString() {
      return String.valueOf(this.callback);
    }
  }
  
  public Cancellable addShutdownCallback(Runnable callback) {
    if (isStarted()) {
      Cancellable receipt = new RegisteredCancellable(callback, this.hooks);
      this.hooks.add(new SoftReference<>(receipt));
      return receipt;
    } 
    throw new IllegalStateException("Cannot add new shutdown hook as this is not started. Current state: " + ((LifeCycle.State)this.state
        .get()).name());
  }
  
  public void initialize() {}
  
  public void start() {
    if (this.state.compareAndSet(LifeCycle.State.INITIALIZED, LifeCycle.State.STARTING))
      try {
        addShutdownHook(this.threadFactory.newThread(this));
        this.state.set(LifeCycle.State.STARTED);
      } catch (IllegalStateException ex) {
        this.state.set(LifeCycle.State.STOPPED);
        throw ex;
      } catch (Exception e) {
        LOGGER.catching(e);
        this.state.set(LifeCycle.State.STOPPED);
      }  
  }
  
  private void addShutdownHook(Thread thread) {
    this.shutdownHookRef = new WeakReference<>(thread);
    Runtime.getRuntime().addShutdownHook(thread);
  }
  
  public void stop() {
    stop(0L, AbstractLifeCycle.DEFAULT_STOP_TIMEUNIT);
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    if (this.state.compareAndSet(LifeCycle.State.STARTED, LifeCycle.State.STOPPING))
      try {
        removeShutdownHook();
      } finally {
        this.state.set(LifeCycle.State.STOPPED);
      }  
    return true;
  }
  
  private void removeShutdownHook() {
    Thread shutdownThread = this.shutdownHookRef.get();
    if (shutdownThread != null) {
      Runtime.getRuntime().removeShutdownHook(shutdownThread);
      this.shutdownHookRef.enqueue();
    } 
  }
  
  public LifeCycle.State getState() {
    return this.state.get();
  }
  
  public boolean isStarted() {
    return (this.state.get() == LifeCycle.State.STARTED);
  }
  
  public boolean isStopped() {
    return (this.state.get() == LifeCycle.State.STOPPED);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\DefaultShutdownCallbackRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */