package io.netty.handler.codec;

public class DecoderException extends CodecException {
   private static final long serialVersionUID = 6926716840699621852L;

   public DecoderException() {
   }

   public DecoderException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public DecoderException(String var1) {
      super(var1);
   }

   public DecoderException(Throwable var1) {
      super(var1);
   }
}
