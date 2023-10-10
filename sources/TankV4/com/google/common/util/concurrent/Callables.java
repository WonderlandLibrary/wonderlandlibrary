package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;

public final class Callables {
   private Callables() {
   }

   public static Callable returning(@Nullable Object var0) {
      return new Callable(var0) {
         final Object val$value;

         {
            this.val$value = var1;
         }

         public Object call() {
            return this.val$value;
         }
      };
   }

   static Callable threadRenaming(Callable var0, Supplier var1) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var0);
      return new Callable(var1, var0) {
         final Supplier val$nameSupplier;
         final Callable val$callable;

         {
            this.val$nameSupplier = var1;
            this.val$callable = var2;
         }

         public Object call() throws Exception {
            Thread var1 = Thread.currentThread();
            String var2 = var1.getName();
            boolean var3 = Callables.access$000((String)this.val$nameSupplier.get(), var1);
            Object var4 = this.val$callable.call();
            if (var3) {
               Callables.access$000(var2, var1);
            }

            return var4;
         }
      };
   }

   static Runnable threadRenaming(Runnable var0, Supplier var1) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var0);
      return new Runnable(var1, var0) {
         final Supplier val$nameSupplier;
         final Runnable val$task;

         {
            this.val$nameSupplier = var1;
            this.val$task = var2;
         }

         public void run() {
            Thread var1 = Thread.currentThread();
            String var2 = var1.getName();
            boolean var3 = Callables.access$000((String)this.val$nameSupplier.get(), var1);
            this.val$task.run();
            if (var3) {
               Callables.access$000(var2, var1);
            }

         }
      };
   }

   private static boolean trySetName(String var0, Thread var1) {
      try {
         var1.setName(var0);
         return true;
      } catch (SecurityException var3) {
         return false;
      }
   }

   static boolean access$000(String var0, Thread var1) {
      return trySetName(var0, var1);
   }
}
