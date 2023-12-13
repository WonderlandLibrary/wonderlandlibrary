package io.netty.handler.codec;

public class TooLongFrameException extends DecoderException {
   private static final long serialVersionUID = -1995801950698951640L;

   public TooLongFrameException() {
   }

   public TooLongFrameException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public TooLongFrameException(String var1) {
      super(var1);
   }

   public TooLongFrameException(Throwable var1) {
      super(var1);
   }
}
