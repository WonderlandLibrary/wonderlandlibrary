package io.netty.util.concurrent;

public final class SucceededFuture extends CompleteFuture {
   private final Object result;

   public SucceededFuture(EventExecutor var1, Object var2) {
      super(var1);
      this.result = var2;
   }

   public Throwable cause() {
      return null;
   }

   public boolean isSuccess() {
      return true;
   }

   public Object getNow() {
      return this.result;
   }
}
