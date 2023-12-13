package com.sun.webkit;

import com.sun.webkit.perf.PerfLogger;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Invoker {
   private static Invoker instance;
   private static final PerfLogger locksLog = PerfLogger.getLogger("Locks");

   public static synchronized void setInvoker(Invoker var0) {
      instance = var0;
   }

   public static synchronized Invoker getInvoker() {
      return instance;
   }

   protected boolean lock(ReentrantLock var1) {
      if (var1.getHoldCount() == 0) {
         var1.lock();
         locksLog.resumeCount(this.isEventThread() ? "EventThread" : "RenderThread");
         return true;
      } else {
         return false;
      }
   }

   protected boolean unlock(ReentrantLock var1) {
      if (var1.getHoldCount() != 0) {
         locksLog.suspendCount(this.isEventThread() ? "EventThread" : "RenderThread");
         var1.unlock();
         return true;
      } else {
         return false;
      }
   }

   protected abstract boolean isEventThread();

   public void checkEventThread() {
      if (!this.isEventThread()) {
         throw new IllegalStateException("Current thread is not event thread, current thread: " + Thread.currentThread().getName());
      }
   }

   public abstract void invokeOnEventThread(Runnable var1);

   public abstract void postOnEventThread(Runnable var1);
}
