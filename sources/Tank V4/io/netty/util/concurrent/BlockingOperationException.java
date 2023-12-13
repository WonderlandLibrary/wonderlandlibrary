package io.netty.util.concurrent;

public class BlockingOperationException extends IllegalStateException {
   private static final long serialVersionUID = 2462223247762460301L;

   public BlockingOperationException() {
   }

   public BlockingOperationException(String var1) {
      super(var1);
   }

   public BlockingOperationException(Throwable var1) {
      super(var1);
   }

   public BlockingOperationException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
