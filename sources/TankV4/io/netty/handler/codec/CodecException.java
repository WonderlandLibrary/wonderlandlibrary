package io.netty.handler.codec;

public class CodecException extends RuntimeException {
   private static final long serialVersionUID = -1464830400709348473L;

   public CodecException() {
   }

   public CodecException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public CodecException(String var1) {
      super(var1);
   }

   public CodecException(Throwable var1) {
      super(var1);
   }
}
