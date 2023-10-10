package org.apache.commons.io.monitor;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadFactory;

public final class FileAlterationMonitor implements Runnable {
   private final long interval;
   private final List observers;
   private Thread thread;
   private ThreadFactory threadFactory;
   private volatile boolean running;

   public FileAlterationMonitor() {
      this(10000L);
   }

   public FileAlterationMonitor(long var1) {
      this.observers = new CopyOnWriteArrayList();
      this.thread = null;
      this.running = false;
      this.interval = var1;
   }

   public FileAlterationMonitor(long var1, FileAlterationObserver... var3) {
      this(var1);
      if (var3 != null) {
         FileAlterationObserver[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            FileAlterationObserver var7 = var4[var6];
            this.addObserver(var7);
         }
      }

   }

   public long getInterval() {
      return this.interval;
   }

   public synchronized void setThreadFactory(ThreadFactory var1) {
      this.threadFactory = var1;
   }

   public void addObserver(FileAlterationObserver var1) {
      if (var1 != null) {
         this.observers.add(var1);
      }

   }

   public void removeObserver(FileAlterationObserver var1) {
      if (var1 != null) {
         while(true) {
            if (this.observers.remove(var1)) {
               continue;
            }
         }
      }

   }

   public Iterable getObservers() {
      return this.observers;
   }

   public synchronized void start() throws Exception {
      if (this.running) {
         throw new IllegalStateException("Monitor is already running");
      } else {
         Iterator var1 = this.observers.iterator();

         while(var1.hasNext()) {
            FileAlterationObserver var2 = (FileAlterationObserver)var1.next();
            var2.initialize();
         }

         this.running = true;
         if (this.threadFactory != null) {
            this.thread = this.threadFactory.newThread(this);
         } else {
            this.thread = new Thread(this);
         }

         this.thread.start();
      }
   }

   public synchronized void stop() throws Exception {
      this.stop(this.interval);
   }

   public synchronized void stop(long var1) throws Exception {
      if (!this.running) {
         throw new IllegalStateException("Monitor is not running");
      } else {
         this.running = false;

         try {
            this.thread.join(var1);
         } catch (InterruptedException var5) {
            Thread.currentThread().interrupt();
         }

         Iterator var3 = this.observers.iterator();

         while(var3.hasNext()) {
            FileAlterationObserver var4 = (FileAlterationObserver)var3.next();
            var4.destroy();
         }

      }
   }

   public void run() {
      while(true) {
         if (this.running) {
            Iterator var1 = this.observers.iterator();

            while(var1.hasNext()) {
               FileAlterationObserver var2 = (FileAlterationObserver)var1.next();
               var2.checkAndNotify();
            }

            if (this.running) {
               try {
                  Thread.sleep(this.interval);
               } catch (InterruptedException var3) {
               }
               continue;
            }
         }

         return;
      }
   }
}
