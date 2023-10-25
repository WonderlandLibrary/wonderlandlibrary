package org.apache.logging.log4j.core;

public interface LifeCycle {
  State getState();
  
  void initialize();
  
  void start();
  
  void stop();
  
  boolean isStarted();
  
  boolean isStopped();
  
  public enum State {
    INITIALIZING, INITIALIZED, STARTING, STARTED, STOPPING, STOPPED;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\LifeCycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */