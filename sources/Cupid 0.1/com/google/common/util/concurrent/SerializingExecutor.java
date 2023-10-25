package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

final class SerializingExecutor implements Executor {
  private static final Logger log = Logger.getLogger(SerializingExecutor.class.getName());
  
  private final Executor executor;
  
  @GuardedBy("internalLock")
  private final Queue<Runnable> waitQueue = new ArrayDeque<Runnable>();
  
  @GuardedBy("internalLock")
  private boolean isThreadScheduled = false;
  
  private final TaskRunner taskRunner = new TaskRunner();
  
  private final Object internalLock;
  
  public SerializingExecutor(Executor executor) {
    this.internalLock = new Object() {
        public String toString() {
          return "SerializingExecutor lock: " + super.toString();
        }
      };
    Preconditions.checkNotNull(executor, "'executor' must not be null.");
    this.executor = executor;
  }
  
  public void execute(Runnable r) {
    Preconditions.checkNotNull(r, "'r' must not be null.");
    boolean scheduleTaskRunner = false;
    synchronized (this.internalLock) {
      this.waitQueue.add(r);
      if (!this.isThreadScheduled) {
        this.isThreadScheduled = true;
        scheduleTaskRunner = true;
      } 
    } 
    if (scheduleTaskRunner) {
      boolean threw = true;
      try {
        this.executor.execute(this.taskRunner);
        threw = false;
      } finally {
        if (threw)
          synchronized (this.internalLock) {
            this.isThreadScheduled = false;
          }  
      } 
    } 
  }
  
  private class TaskRunner implements Runnable {
    private TaskRunner() {}
    
    public void run() {
      boolean stillRunning = true;
      try {
        while (true) {
          Runnable nextToRun;
          Preconditions.checkState(SerializingExecutor.this.isThreadScheduled);
          synchronized (SerializingExecutor.this.internalLock) {
            nextToRun = SerializingExecutor.this.waitQueue.poll();
            if (nextToRun == null) {
              SerializingExecutor.this.isThreadScheduled = false;
              stillRunning = false;
              break;
            } 
          } 
          try {
            nextToRun.run();
          } catch (RuntimeException e) {
            SerializingExecutor.log.log(Level.SEVERE, "Exception while executing runnable " + nextToRun, e);
          } 
        } 
      } finally {
        if (stillRunning)
          synchronized (SerializingExecutor.this.internalLock) {
            SerializingExecutor.this.isThreadScheduled = false;
          }  
      } 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\commo\\util\concurrent\SerializingExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */