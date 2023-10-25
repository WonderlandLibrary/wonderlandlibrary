package io.netty.util;

import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.internal.MpscLinkedQueueNode;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ThreadDeathWatcher {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(ThreadDeathWatcher.class);
  
  private static final ThreadFactory threadFactory = (ThreadFactory)new DefaultThreadFactory(ThreadDeathWatcher.class, true, 1);
  
  private static final Queue<Entry> pendingEntries = PlatformDependent.newMpscQueue();
  
  private static final Watcher watcher = new Watcher();
  
  private static final AtomicBoolean started = new AtomicBoolean();
  
  private static volatile Thread watcherThread;
  
  public static void watch(Thread thread, Runnable task) {
    if (thread == null)
      throw new NullPointerException("thread"); 
    if (task == null)
      throw new NullPointerException("task"); 
    if (!thread.isAlive())
      throw new IllegalArgumentException("thread must be alive."); 
    schedule(thread, task, true);
  }
  
  public static void unwatch(Thread thread, Runnable task) {
    if (thread == null)
      throw new NullPointerException("thread"); 
    if (task == null)
      throw new NullPointerException("task"); 
    schedule(thread, task, false);
  }
  
  private static void schedule(Thread thread, Runnable task, boolean isWatch) {
    pendingEntries.add(new Entry(thread, task, isWatch));
    if (started.compareAndSet(false, true)) {
      Thread watcherThread = threadFactory.newThread(watcher);
      watcherThread.start();
      ThreadDeathWatcher.watcherThread = watcherThread;
    } 
  }
  
  public static boolean awaitInactivity(long timeout, TimeUnit unit) throws InterruptedException {
    if (unit == null)
      throw new NullPointerException("unit"); 
    Thread watcherThread = ThreadDeathWatcher.watcherThread;
    if (watcherThread != null) {
      watcherThread.join(unit.toMillis(timeout));
      return !watcherThread.isAlive();
    } 
    return true;
  }
  
  private static final class Watcher implements Runnable {
    private final List<ThreadDeathWatcher.Entry> watchees = new ArrayList<ThreadDeathWatcher.Entry>();
    
    public void run() {
      while (true) {
        fetchWatchees();
        notifyWatchees();
        fetchWatchees();
        notifyWatchees();
        try {
          Thread.sleep(1000L);
        } catch (InterruptedException ignore) {}
        if (this.watchees.isEmpty() && ThreadDeathWatcher.pendingEntries.isEmpty()) {
          boolean stopped = ThreadDeathWatcher.started.compareAndSet(true, false);
          assert stopped;
          if (ThreadDeathWatcher.pendingEntries.isEmpty())
            break; 
          if (!ThreadDeathWatcher.started.compareAndSet(false, true))
            break; 
        } 
      } 
    }
    
    private void fetchWatchees() {
      while (true) {
        ThreadDeathWatcher.Entry e = ThreadDeathWatcher.pendingEntries.poll();
        if (e == null)
          break; 
        if (e.isWatch) {
          this.watchees.add(e);
          continue;
        } 
        this.watchees.remove(e);
      } 
    }
    
    private void notifyWatchees() {
      List<ThreadDeathWatcher.Entry> watchees = this.watchees;
      for (int i = 0; i < watchees.size(); ) {
        ThreadDeathWatcher.Entry e = watchees.get(i);
        if (!e.thread.isAlive()) {
          watchees.remove(i);
          try {
            e.task.run();
          } catch (Throwable t) {
            ThreadDeathWatcher.logger.warn("Thread death watcher task raised an exception:", t);
          } 
          continue;
        } 
        i++;
      } 
    }
    
    private Watcher() {}
  }
  
  private static final class Entry extends MpscLinkedQueueNode<Entry> {
    final Thread thread;
    
    final Runnable task;
    
    final boolean isWatch;
    
    Entry(Thread thread, Runnable task, boolean isWatch) {
      this.thread = thread;
      this.task = task;
      this.isWatch = isWatch;
    }
    
    public Entry value() {
      return this;
    }
    
    public int hashCode() {
      return this.thread.hashCode() ^ this.task.hashCode();
    }
    
    public boolean equals(Object obj) {
      if (obj == this)
        return true; 
      if (!(obj instanceof Entry))
        return false; 
      Entry that = (Entry)obj;
      return (this.thread == that.thread && this.task == that.task);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\ThreadDeathWatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */