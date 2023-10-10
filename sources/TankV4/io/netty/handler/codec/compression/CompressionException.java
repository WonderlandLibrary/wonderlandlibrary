package io.netty.handler.codec.compression;

import io.netty.handler.codec.EncoderException;

public class CompressionException extends EncoderException {
   private static final long serialVersionUID = 5603413481274811897L;

   public CompressionException() {
   }

   public CompressionException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public CompressionException(String var1) {
      super(var1);
   }

   public CompressionException(Throwable var1) {
      super(var1);
   }
}
