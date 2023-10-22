package com.sun.glass.ui;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public final class InvokeLaterDispatcher extends Thread {
   private final BlockingDeque deque = new LinkedBlockingDeque();
   private final Object LOCK = new StringBuilder("InvokeLaterLock");
   private boolean nestedEventLoopEntered = false;
   private volatile boolean leavingNestedEventLoop = false;
   private final InvokeLaterSubmitter invokeLaterSubmitter;

   public InvokeLaterDispatcher(InvokeLaterSubmitter var1) {
      this.setDaemon(true);
      this.invokeLaterSubmitter = var1;
   }

   public void run() {
      try {
         while(true) {
            Runnable var1 = (Runnable)this.deque.takeFirst();
            if (this.leavingNestedEventLoop) {
               this.deque.addFirst(var1);
               synchronized(this.LOCK) {
                  while(this.leavingNestedEventLoop) {
                     this.LOCK.wait();
                  }
               }
            } else {
               Future var2 = new Future(var1);
               this.invokeLaterSubmitter.submitForLaterInvocation(var2);
               synchronized(this.LOCK) {
                  try {
                     while(!var2.isDone() && !this.nestedEventLoopEntered) {
                        this.LOCK.wait();
                     }
                  } finally {
                     this.nestedEventLoopEntered = false;
                  }
               }
            }
         }
      } catch (InterruptedException var13) {
      }
   }

   public void invokeAndWait(Runnable var1) {
      Future var2 = new Future(var1);
      this.invokeLaterSubmitter.submitForLaterInvocation(var2);
      synchronized(this.LOCK) {
         try {
            while(!var2.isDone()) {
               this.LOCK.wait();
            }
         } catch (InterruptedException var6) {
         }

      }
   }

   public void invokeLater(Runnable var1) {
      this.deque.addLast(var1);
   }

   public void notifyEnteringNestedEventLoop() {
      synchronized(this.LOCK) {
         this.nestedEventLoopEntered = true;
         this.LOCK.notifyAll();
      }
   }

   public void notifyLeavingNestedEventLoop() {
      synchronized(this.LOCK) {
         this.leavingNestedEventLoop = true;
         this.LOCK.notifyAll();
      }
   }

   public void notifyLeftNestedEventLoop() {
      synchronized(this.LOCK) {
         this.leavingNestedEventLoop = false;
         this.LOCK.notifyAll();
      }
   }

   private class Future implements Runnable {
      private boolean done = false;
      private final Runnable runnable;

      public Future(Runnable var2) {
         this.runnable = var2;
      }

      public boolean isDone() {
         return this.done;
      }

      public void run() {
         boolean var9 = false;

         try {
            var9 = true;
            this.runnable.run();
            var9 = false;
         } finally {
            if (var9) {
               synchronized(InvokeLaterDispatcher.this.LOCK) {
                  this.done = true;
                  InvokeLaterDispatcher.this.LOCK.notifyAll();
               }
            }
         }

         synchronized(InvokeLaterDispatcher.this.LOCK) {
            this.done = true;
            InvokeLaterDispatcher.this.LOCK.notifyAll();
         }
      }
   }

   public interface InvokeLaterSubmitter {
      void submitForLaterInvocation(Runnable var1);
   }
}
