package io.netty.util.concurrent;

import java.util.ArrayDeque;

final class DefaultPromise$LateListeners extends ArrayDeque implements Runnable {
   private static final long serialVersionUID = -687137418080392244L;
   final DefaultPromise this$0;

   DefaultPromise$LateListeners(DefaultPromise var1) {
      super(2);
      this.this$0 = var1;
   }

   public void run() {
      if (DefaultPromise.access$100(this.this$0) == null) {
         while(true) {
            GenericFutureListener var1 = (GenericFutureListener)this.poll();
            if (var1 == null) {
               break;
            }

            DefaultPromise.notifyListener0(this.this$0, var1);
         }
      } else {
         DefaultPromise.access$400(this.this$0.executor(), this);
      }

   }
}
