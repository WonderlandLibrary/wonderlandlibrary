package io.netty.handler.codec;

public class PrematureChannelClosureException extends CodecException {
   private static final long serialVersionUID = 4907642202594703094L;

   public PrematureChannelClosureException() {
   }

   public PrematureChannelClosureException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public PrematureChannelClosureException(String var1) {
      super(var1);
   }

   public PrematureChannelClosureException(Throwable var1) {
      super(var1);
   }
}
