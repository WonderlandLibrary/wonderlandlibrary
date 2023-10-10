package com.google.common.util.concurrent;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

public final class ExecutionList {
   @VisibleForTesting
   static final Logger log = Logger.getLogger(ExecutionList.class.getName());
   @GuardedBy("this")
   private ExecutionList.RunnableExecutorPair runnables;
   @GuardedBy("this")
   private boolean executed;

   public void add(Runnable var1, Executor var2) {
      Preconditions.checkNotNull(var1, "Runnable was null.");
      Preconditions.checkNotNull(var2, "Executor was null.");
      synchronized(this){}
      if (!this.executed) {
         this.runnables = new ExecutionList.RunnableExecutorPair(var1, var2, this.runnables);
      } else {
         executeListener(var1, var2);
      }
   }

   public void execute() {
      synchronized(this){}
      if (!this.executed) {
         this.executed = true;
         ExecutionList.RunnableExecutorPair var1 = this.runnables;
         this.runnables = null;

         ExecutionList.RunnableExecutorPair var2;
         ExecutionList.RunnableExecutorPair var3;
         for(var2 = null; var1 != null; var2 = var3) {
            var3 = var1;
            var1 = var1.next;
            var3.next = var2;
         }

         while(var2 != null) {
            executeListener(var2.runnable, var2.executor);
            var2 = var2.next;
         }

      }
   }

   private static void executeListener(Runnable var0, Executor var1) {
      try {
         var1.execute(var0);
      } catch (RuntimeException var3) {
         log.log(Level.SEVERE, "RuntimeException while executing runnable " + var0 + " with executor " + var1, var3);
      }

   }

   private static final class RunnableExecutorPair {
      final Runnable runnable;
      final Executor executor;
      @Nullable
      ExecutionList.RunnableExecutorPair next;

      RunnableExecutorPair(Runnable var1, Executor var2, ExecutionList.RunnableExecutorPair var3) {
         this.runnable = var1;
         this.executor = var2;
         this.next = var3;
      }
   }
}
