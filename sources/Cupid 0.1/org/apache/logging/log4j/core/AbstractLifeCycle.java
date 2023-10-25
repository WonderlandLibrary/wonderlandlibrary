package org.apache.logging.log4j.core;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public class AbstractLifeCycle implements LifeCycle2 {
  public static final int DEFAULT_STOP_TIMEOUT = 0;
  
  public static final TimeUnit DEFAULT_STOP_TIMEUNIT = TimeUnit.MILLISECONDS;
  
  protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  protected static Logger getStatusLogger() {
    return LOGGER;
  }
  
  private volatile LifeCycle.State state = LifeCycle.State.INITIALIZED;
  
  protected boolean equalsImpl(Object obj) {
    if (this == obj)
      return true; 
    if (obj == null)
      return false; 
    if (getClass() != obj.getClass())
      return false; 
    LifeCycle other = (LifeCycle)obj;
    if (this.state != other.getState())
      return false; 
    return true;
  }
  
  public LifeCycle.State getState() {
    return this.state;
  }
  
  protected int hashCodeImpl() {
    int prime = 31;
    int result = 1;
    result = 31 * result + ((this.state == null) ? 0 : this.state.hashCode());
    return result;
  }
  
  public boolean isInitialized() {
    return (this.state == LifeCycle.State.INITIALIZED);
  }
  
  public boolean isStarted() {
    return (this.state == LifeCycle.State.STARTED);
  }
  
  public boolean isStarting() {
    return (this.state == LifeCycle.State.STARTING);
  }
  
  public boolean isStopped() {
    return (this.state == LifeCycle.State.STOPPED);
  }
  
  public boolean isStopping() {
    return (this.state == LifeCycle.State.STOPPING);
  }
  
  protected void setStarted() {
    setState(LifeCycle.State.STARTED);
  }
  
  protected void setStarting() {
    setState(LifeCycle.State.STARTING);
  }
  
  protected void setState(LifeCycle.State newState) {
    this.state = newState;
  }
  
  protected void setStopped() {
    setState(LifeCycle.State.STOPPED);
  }
  
  protected void setStopping() {
    setState(LifeCycle.State.STOPPING);
  }
  
  public void initialize() {
    this.state = LifeCycle.State.INITIALIZED;
  }
  
  public void start() {
    setStarted();
  }
  
  public void stop() {
    stop(0L, DEFAULT_STOP_TIMEUNIT);
  }
  
  protected boolean stop(Future<?> future) {
    boolean stopped = true;
    if (future != null) {
      if (future.isCancelled() || future.isDone())
        return true; 
      stopped = future.cancel(true);
    } 
    return stopped;
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    this.state = LifeCycle.State.STOPPED;
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\AbstractLifeCycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */