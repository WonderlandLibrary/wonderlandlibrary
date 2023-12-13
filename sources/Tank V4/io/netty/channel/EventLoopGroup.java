package io.netty.channel;

import io.netty.util.concurrent.EventExecutorGroup;

public interface EventLoopGroup extends EventExecutorGroup {
   EventLoop next();

   ChannelFuture register(Channel var1);

   ChannelFuture register(Channel var1, ChannelPromise var2);
}
