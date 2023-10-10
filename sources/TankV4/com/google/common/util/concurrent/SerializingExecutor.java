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
   private final Queue waitQueue = new ArrayDeque();
   @GuardedBy("internalLock")
   private boolean isThreadScheduled = false;
   private final SerializingExecutor.TaskRunner taskRunner = new SerializingExecutor.TaskRunner(this);
   private final Object internalLock = new Object(this) {
      final SerializingExecutor this$0;

      {
         this.this$0 = var1;
      }

      public String toString() {
         return "SerializingExecutor lock: " + super.toString();
      }
   };

   public SerializingExecutor(Executor var1) {
      Preconditions.checkNotNull(var1, "'executor' must not be null.");
      this.executor = var1;
   }

   public void execute(Runnable var1) {
      Preconditions.checkNotNull(var1, "'r' must not be null.");
      boolean var2 = false;
      Object var3;
      synchronized(var3 = this.internalLock){}
      this.waitQueue.add(var1);
      if (!this.isThreadScheduled) {
         this.isThreadScheduled = true;
         var2 = true;
      }

      if (var2) {
         boolean var9 = true;
         this.executor.execute(this.taskRunner);
         var9 = false;
         if (var9) {
            Object var4;
            synchronized(var4 = this.internalLock){}
            this.isThreadScheduled = false;
         }
      }

   }

   static boolean access$100(SerializingExecutor var0) {
      return var0.isThreadScheduled;
   }

   static Object access$200(SerializingExecutor var0) {
      return var0.internalLock;
   }

   static Queue access$300(SerializingExecutor var0) {
      return var0.waitQueue;
   }

   static boolean access$102(SerializingExecutor var0, boolean var1) {
      return var0.isThreadScheduled = var1;
   }

   static Logger access$400() {
      return log;
   }

   private class TaskRunner implements Runnable {
      final SerializingExecutor this$0;

      private TaskRunner(SerializingExecutor var1) {
         this.this$0 = var1;
      }

      public void run() {
         boolean var1 = true;

         while(true) {
            Preconditions.checkState(SerializingExecutor.access$100(this.this$0));
            Object var3;
            synchronized(var3 = SerializingExecutor.access$200(this.this$0)){}
            Runnable var2 = (Runnable)SerializingExecutor.access$300(this.this$0).poll();
            if (var2 == null) {
               SerializingExecutor.access$102(this.this$0, false);
               var1 = false;
               if (var1) {
                  Object var10;
                  synchronized(var10 = SerializingExecutor.access$200(this.this$0)){}
                  SerializingExecutor.access$102(this.this$0, false);
               }

               return;
            }

            try {
               var2.run();
            } catch (RuntimeException var9) {
               SerializingExecutor.access$400().log(Level.SEVERE, "Exception while executing runnable " + var2, var9);
            }
         }
      }

      TaskRunner(SerializingExecutor var1, Object var2) {
         this(var1);
      }
   }
}
