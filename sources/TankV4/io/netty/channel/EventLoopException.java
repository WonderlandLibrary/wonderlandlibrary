package io.netty.channel;

public class EventLoopException extends ChannelException {
   private static final long serialVersionUID = -8969100344583703616L;

   public EventLoopException() {
   }

   public EventLoopException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public EventLoopException(String var1) {
      super(var1);
   }

   public EventLoopException(Throwable var1) {
      super(var1);
   }
}
