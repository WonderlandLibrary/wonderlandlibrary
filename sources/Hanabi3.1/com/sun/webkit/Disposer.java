package com.sun.webkit;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public final class Disposer implements Runnable {
   private static final ReferenceQueue queue = new ReferenceQueue();
   private static final Disposer disposerInstance = new Disposer();
   private static final Set records = new HashSet();

   public static void addRecord(Object var0, DisposerRecord var1) {
      disposerInstance.add(var0, var1);
   }

   private synchronized void add(Object var1, DisposerRecord var2) {
      records.add(new WeakDisposerRecord(var1, var2));
   }

   public static void addRecord(WeakDisposerRecord var0) {
      disposerInstance.add(var0);
   }

   private synchronized void add(WeakDisposerRecord var1) {
      records.add(var1);
   }

   public void run() {
      while(true) {
         try {
            WeakDisposerRecord var1 = (WeakDisposerRecord)queue.remove();
            var1.clear();
            Disposer.DisposerRunnable.theInstance.enqueue(var1);
         } catch (Exception var2) {
            System.out.println("Exception while removing reference: " + var2);
            var2.printStackTrace();
         }
      }
   }

   static {
      AccessController.doPrivileged(() -> {
         ThreadGroup var0 = Thread.currentThread().getThreadGroup();

         for(ThreadGroup var1 = var0; var1 != null; var1 = var1.getParent()) {
            var0 = var1;
         }

         Thread var2 = new Thread(var0, disposerInstance, "Disposer");
         var2.setDaemon(true);
         var2.setPriority(10);
         var2.start();
         return null;
      });
   }

   public static class WeakDisposerRecord extends WeakReference implements DisposerRecord {
      private final DisposerRecord record;

      protected WeakDisposerRecord(Object var1) {
         super(var1, Disposer.queue);
         this.record = null;
      }

      private WeakDisposerRecord(Object var1, DisposerRecord var2) {
         super(var1, Disposer.queue);
         this.record = var2;
      }

      public void dispose() {
         this.record.dispose();
      }

      // $FF: synthetic method
      WeakDisposerRecord(Object var1, DisposerRecord var2, Object var3) {
         this(var1, var2);
      }
   }

   private static final class DisposerRunnable implements Runnable {
      private static final DisposerRunnable theInstance = new DisposerRunnable();
      private boolean isRunning = false;
      private final Object disposerLock = new Object();
      private final LinkedBlockingQueue disposerQueue = new LinkedBlockingQueue();

      private static DisposerRunnable getInstance() {
         return theInstance;
      }

      private void enqueueAll(Collection var1) {
         synchronized(this.disposerLock) {
            this.disposerQueue.addAll(var1);
            if (!this.isRunning) {
               Invoker.getInvoker().invokeOnEventThread(this);
               this.isRunning = true;
            }

         }
      }

      private void enqueue(WeakDisposerRecord var1) {
         this.enqueueAll(Arrays.asList(var1));
      }

      public void run() {
         while(true) {
            WeakDisposerRecord var1;
            synchronized(this.disposerLock) {
               var1 = (WeakDisposerRecord)this.disposerQueue.poll();
               if (var1 == null) {
                  this.isRunning = false;
                  return;
               }
            }

            if (Disposer.records.contains(var1)) {
               Disposer.records.remove(var1);
               var1.dispose();
            }
         }
      }
   }
}
