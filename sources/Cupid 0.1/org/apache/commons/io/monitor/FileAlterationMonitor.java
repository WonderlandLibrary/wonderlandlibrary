package org.apache.commons.io.monitor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadFactory;

public final class FileAlterationMonitor implements Runnable {
  private final long interval;
  
  private final List<FileAlterationObserver> observers = new CopyOnWriteArrayList<FileAlterationObserver>();
  
  private Thread thread = null;
  
  private ThreadFactory threadFactory;
  
  private volatile boolean running = false;
  
  public FileAlterationMonitor() {
    this(10000L);
  }
  
  public FileAlterationMonitor(long interval) {
    this.interval = interval;
  }
  
  public FileAlterationMonitor(long interval, FileAlterationObserver... observers) {
    this(interval);
    if (observers != null)
      for (FileAlterationObserver observer : observers)
        addObserver(observer);  
  }
  
  public long getInterval() {
    return this.interval;
  }
  
  public synchronized void setThreadFactory(ThreadFactory threadFactory) {
    this.threadFactory = threadFactory;
  }
  
  public void addObserver(FileAlterationObserver observer) {
    if (observer != null)
      this.observers.add(observer); 
  }
  
  public void removeObserver(FileAlterationObserver observer) {
    if (observer != null)
      while (this.observers.remove(observer)); 
  }
  
  public Iterable<FileAlterationObserver> getObservers() {
    return this.observers;
  }
  
  public synchronized void start() throws Exception {
    if (this.running)
      throw new IllegalStateException("Monitor is already running"); 
    for (FileAlterationObserver observer : this.observers)
      observer.initialize(); 
    this.running = true;
    if (this.threadFactory != null) {
      this.thread = this.threadFactory.newThread(this);
    } else {
      this.thread = new Thread(this);
    } 
    this.thread.start();
  }
  
  public synchronized void stop() throws Exception {
    stop(this.interval);
  }
  
  public synchronized void stop(long stopInterval) throws Exception {
    if (!this.running)
      throw new IllegalStateException("Monitor is not running"); 
    this.running = false;
    try {
      this.thread.join(stopInterval);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } 
    for (FileAlterationObserver observer : this.observers)
      observer.destroy(); 
  }
  
  public void run() {
    while (this.running) {
      for (FileAlterationObserver observer : this.observers)
        observer.checkAndNotify(); 
      if (!this.running)
        break; 
      try {
        Thread.sleep(this.interval);
      } catch (InterruptedException ignored) {}
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\monitor\FileAlterationMonitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */