package io.netty.channel;

public class ChannelPipelineException extends ChannelException {
   private static final long serialVersionUID = 3379174210419885980L;

   public ChannelPipelineException() {
   }

   public ChannelPipelineException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public ChannelPipelineException(String var1) {
      super(var1);
   }

   public ChannelPipelineException(Throwable var1) {
      super(var1);
   }
}
