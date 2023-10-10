package io.netty.channel;

public class ChannelException extends RuntimeException {
   private static final long serialVersionUID = 2908618315971075004L;

   public ChannelException() {
   }

   public ChannelException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public ChannelException(String var1) {
      super(var1);
   }

   public ChannelException(Throwable var1) {
      super(var1);
   }
}
