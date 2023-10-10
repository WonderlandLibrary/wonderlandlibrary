package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

final class ListenerCallQueue implements Runnable {
   private static final Logger logger = Logger.getLogger(ListenerCallQueue.class.getName());
   private final Object listener;
   private final Executor executor;
   @GuardedBy("this")
   private final Queue waitQueue = Queues.newArrayDeque();
   @GuardedBy("this")
   private boolean isThreadScheduled;

   ListenerCallQueue(Object var1, Executor var2) {
      this.listener = Preconditions.checkNotNull(var1);
      this.executor = (Executor)Preconditions.checkNotNull(var2);
   }

   synchronized void add(ListenerCallQueue.Callback var1) {
      this.waitQueue.add(var1);
   }

   void execute() {
      boolean var1 = false;
      synchronized(this){}
      if (!this.isThreadScheduled) {
         this.isThreadScheduled = true;
         var1 = true;
      }

      if (var1) {
         try {
            this.executor.execute(this);
         } catch (RuntimeException var5) {
            synchronized(this){}
            this.isThreadScheduled = false;
            logger.log(Level.SEVERE, "Exception while running callbacks for " + this.listener + " on " + this.executor, var5);
            throw var5;
         }
      }

   }

   public void run() {
      boolean var1 = true;

      while(true) {
         synchronized(this){}
         Preconditions.checkState(this.isThreadScheduled);
         ListenerCallQueue.Callback var2 = (ListenerCallQueue.Callback)this.waitQueue.poll();
         if (var2 == null) {
            this.isThreadScheduled = false;
            var1 = false;
            if (var1) {
               synchronized(this){}
               this.isThreadScheduled = false;
            }

            return;
         }

         try {
            var2.call(this.listener);
         } catch (RuntimeException var9) {
            logger.log(Level.SEVERE, "Exception while executing callback: " + this.listener + "." + ListenerCallQueue.Callback.access$000(var2), var9);
         }
      }
   }

   abstract static class Callback {
      private final String methodCall;

      Callback(String var1) {
         this.methodCall = var1;
      }

      abstract void call(Object var1);

      void enqueueOn(Iterable var1) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            ListenerCallQueue var3 = (ListenerCallQueue)var2.next();
            var3.add(this);
         }

      }

      static String access$000(ListenerCallQueue.Callback var0) {
         return var0.methodCall;
      }
   }
}
