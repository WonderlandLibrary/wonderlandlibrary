package io.netty.util.concurrent;

final class DefaultPromise$LateListenerNotifier implements Runnable {
   private GenericFutureListener l;
   final DefaultPromise this$0;

   DefaultPromise$LateListenerNotifier(DefaultPromise var1, GenericFutureListener var2) {
      this.this$0 = var1;
      this.l = var2;
   }

   public void run() {
      DefaultPromise$LateListeners var1 = DefaultPromise.access$500(this.this$0);
      if (this.l != null) {
         if (var1 == null) {
            DefaultPromise.access$502(this.this$0, var1 = new DefaultPromise$LateListeners(this.this$0));
         }

         var1.add(this.l);
         this.l = null;
      }

      var1.run();
   }
}
