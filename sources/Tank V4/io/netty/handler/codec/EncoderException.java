package io.netty.handler.codec;

public class EncoderException extends CodecException {
   private static final long serialVersionUID = -5086121160476476774L;

   public EncoderException() {
   }

   public EncoderException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public EncoderException(String var1) {
      super(var1);
   }

   public EncoderException(Throwable var1) {
      super(var1);
   }
}
