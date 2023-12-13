package io.netty.handler.codec;

public class CorruptedFrameException extends DecoderException {
   private static final long serialVersionUID = 3918052232492988408L;

   public CorruptedFrameException() {
   }

   public CorruptedFrameException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public CorruptedFrameException(String var1) {
      super(var1);
   }

   public CorruptedFrameException(Throwable var1) {
      super(var1);
   }
}
