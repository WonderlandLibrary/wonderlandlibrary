package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;

@Beta
public final class RemovalListeners {
   private RemovalListeners() {
   }

   public static RemovalListener asynchronous(RemovalListener var0, Executor var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new RemovalListener(var1, var0) {
         final Executor val$executor;
         final RemovalListener val$listener;

         {
            this.val$executor = var1;
            this.val$listener = var2;
         }

         public void onRemoval(RemovalNotification var1) {
            this.val$executor.execute(new Runnable(this, var1) {
               final RemovalNotification val$notification;
               final <undefinedtype> this$0;

               {
                  this.this$0 = var1;
                  this.val$notification = var2;
               }

               public void run() {
                  this.this$0.val$listener.onRemoval(this.val$notification);
               }
            });
         }
      };
   }
}
