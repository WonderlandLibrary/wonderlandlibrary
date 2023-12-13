package io.netty.channel.local;

import io.netty.channel.MultithreadEventLoopGroup;
import io.netty.util.concurrent.EventExecutor;
import java.util.concurrent.ThreadFactory;

public class LocalEventLoopGroup extends MultithreadEventLoopGroup {
   public LocalEventLoopGroup() {
      this(0);
   }

   public LocalEventLoopGroup(int var1) {
      this(var1, (ThreadFactory)null);
   }

   public LocalEventLoopGroup(int var1, ThreadFactory var2) {
      super(var1, var2);
   }

   protected EventExecutor newChild(ThreadFactory var1, Object... var2) throws Exception {
      return new LocalEventLoop(this, var1);
   }
}
