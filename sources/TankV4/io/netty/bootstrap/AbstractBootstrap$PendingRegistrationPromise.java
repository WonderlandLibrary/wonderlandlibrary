package io.netty.bootstrap;

import io.netty.channel.Channel;
import io.netty.channel.DefaultChannelPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;

final class AbstractBootstrap$PendingRegistrationPromise extends DefaultChannelPromise {
   private AbstractBootstrap$PendingRegistrationPromise(Channel var1) {
      super(var1);
   }

   protected EventExecutor executor() {
      return (EventExecutor)(this.channel().isRegistered() ? super.executor() : GlobalEventExecutor.INSTANCE);
   }

   AbstractBootstrap$PendingRegistrationPromise(Channel var1, Object var2) {
      this(var1);
   }
}
