package io.netty.channel.nio;

import io.netty.channel.MultithreadEventLoopGroup;
import io.netty.util.concurrent.EventExecutor;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.concurrent.ThreadFactory;

public class NioEventLoopGroup extends MultithreadEventLoopGroup {
   public NioEventLoopGroup() {
      this(0);
   }

   public NioEventLoopGroup(int var1) {
      this(var1, (ThreadFactory)null);
   }

   public NioEventLoopGroup(int var1, ThreadFactory var2) {
      this(var1, var2, SelectorProvider.provider());
   }

   public NioEventLoopGroup(int var1, ThreadFactory var2, SelectorProvider var3) {
      super(var1, var2, var3);
   }

   public void setIoRatio(int var1) {
      Iterator var2 = this.children().iterator();

      while(var2.hasNext()) {
         EventExecutor var3 = (EventExecutor)var2.next();
         ((NioEventLoop)var3).setIoRatio(var1);
      }

   }

   public void rebuildSelectors() {
      Iterator var1 = this.children().iterator();

      while(var1.hasNext()) {
         EventExecutor var2 = (EventExecutor)var1.next();
         ((NioEventLoop)var2).rebuildSelector();
      }

   }

   protected EventExecutor newChild(ThreadFactory var1, Object... var2) throws Exception {
      return new NioEventLoop(this, var1, (SelectorProvider)var2[0]);
   }
}
