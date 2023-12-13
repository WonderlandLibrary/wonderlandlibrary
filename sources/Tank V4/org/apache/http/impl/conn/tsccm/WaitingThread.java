package org.apache.http.impl.conn.tsccm;

import java.util.Date;
import java.util.concurrent.locks.Condition;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
public class WaitingThread {
   private final Condition cond;
   private final RouteSpecificPool pool;
   private Thread waiter;
   private boolean aborted;

   public WaitingThread(Condition var1, RouteSpecificPool var2) {
      Args.notNull(var1, "Condition");
      this.cond = var1;
      this.pool = var2;
   }

   public final Condition getCondition() {
      return this.cond;
   }

   public final RouteSpecificPool getPool() {
      return this.pool;
   }

   public final Thread getThread() {
      return this.waiter;
   }

   public boolean await(Date var1) throws InterruptedException {
      if (this.waiter != null) {
         throw new IllegalStateException("A thread is already waiting on this object.\ncaller: " + Thread.currentThread() + "\nwaiter: " + this.waiter);
      } else if (this.aborted) {
         throw new InterruptedException("Operation interrupted");
      } else {
         this.waiter = Thread.currentThread();
         boolean var2 = false;
         if (var1 != null) {
            var2 = this.cond.awaitUntil(var1);
         } else {
            this.cond.await();
            var2 = true;
         }

         if (this.aborted) {
            throw new InterruptedException("Operation interrupted");
         } else {
            this.waiter = null;
            return var2;
         }
      }
   }

   public void wakeup() {
      if (this.waiter == null) {
         throw new IllegalStateException("Nobody waiting on this object.");
      } else {
         this.cond.signalAll();
      }
   }

   public void interrupt() {
      this.aborted = true;
      this.cond.signalAll();
   }
}
