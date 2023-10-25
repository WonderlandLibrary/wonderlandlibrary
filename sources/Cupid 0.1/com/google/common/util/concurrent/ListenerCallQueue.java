package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

final class ListenerCallQueue<L> implements Runnable {
  private static final Logger logger = Logger.getLogger(ListenerCallQueue.class.getName());
  
  private final L listener;
  
  private final Executor executor;
  
  static abstract class Callback<L> {
    private final String methodCall;
    
    Callback(String methodCall) {
      this.methodCall = methodCall;
    }
    
    void enqueueOn(Iterable<ListenerCallQueue<L>> queues) {
      for (ListenerCallQueue<L> queue : queues)
        queue.add(this); 
    }
    
    abstract void call(L param1L);
  }
  
  @GuardedBy("this")
  private final Queue<Callback<L>> waitQueue = Queues.newArrayDeque();
  
  @GuardedBy("this")
  private boolean isThreadScheduled;
  
  ListenerCallQueue(L listener, Executor executor) {
    this.listener = (L)Preconditions.checkNotNull(listener);
    this.executor = (Executor)Preconditions.checkNotNull(executor);
  }
  
  synchronized void add(Callback<L> callback) {
    this.waitQueue.add(callback);
  }
  
  void execute() {
    boolean scheduleTaskRunner = false;
    synchronized (this) {
      if (!this.isThreadScheduled) {
        this.isThreadScheduled = true;
        scheduleTaskRunner = true;
      } 
    } 
    if (scheduleTaskRunner)
      try {
        this.executor.execute(this);
      } catch (RuntimeException e) {
        synchronized (this) {
          this.isThreadScheduled = false;
        } 
        logger.log(Level.SEVERE, "Exception while running callbacks for " + this.listener + " on " + this.executor, e);
        throw e;
      }  
  }
  
  public void run() {
    boolean stillRunning = true;
    try {
      while (true) {
        Callback<L> nextToRun;
        synchronized (this) {
          Preconditions.checkState(this.isThreadScheduled);
          nextToRun = this.waitQueue.poll();
          if (nextToRun == null) {
            this.isThreadScheduled = false;
            stillRunning = false;
            break;
          } 
        } 
        try {
          nextToRun.call(this.listener);
        } catch (RuntimeException e) {
          logger.log(Level.SEVERE, "Exception while executing callback: " + this.listener + "." + nextToRun.methodCall, e);
        } 
      } 
    } finally {
      if (stillRunning)
        synchronized (this) {
          this.isThreadScheduled = false;
        }  
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\commo\\util\concurrent\ListenerCallQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */