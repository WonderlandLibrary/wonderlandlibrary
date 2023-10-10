package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import java.util.concurrent.ThreadFactory;

public abstract class SingleThreadEventLoop extends SingleThreadEventExecutor implements EventLoop {
   protected SingleThreadEventLoop(EventLoopGroup var1, ThreadFactory var2, boolean var3) {
      super(var1, var2, var3);
   }

   public EventLoopGroup parent() {
      return (EventLoopGroup)super.parent();
   }

   public EventLoop next() {
      return (EventLoop)super.next();
   }

   public ChannelFuture register(Channel var1) {
      return this.register(var1, new DefaultChannelPromise(var1, this));
   }

   public ChannelFuture register(Channel var1, ChannelPromise var2) {
      if (var1 == null) {
         throw new NullPointerException("channel");
      } else if (var2 == null) {
         throw new NullPointerException("promise");
      } else {
         var1.unsafe().register(this, var2);
         return var2;
      }
   }

   public EventExecutorGroup parent() {
      return this.parent();
   }

   public EventExecutor next() {
      return this.next();
   }
}
