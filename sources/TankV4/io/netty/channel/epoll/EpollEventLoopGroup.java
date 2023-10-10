package io.netty.channel.epoll;

import io.netty.channel.MultithreadEventLoopGroup;
import io.netty.util.concurrent.EventExecutor;
import java.util.Iterator;
import java.util.concurrent.ThreadFactory;

public final class EpollEventLoopGroup extends MultithreadEventLoopGroup {
   public EpollEventLoopGroup() {
      this(0);
   }

   public EpollEventLoopGroup(int var1) {
      this(var1, (ThreadFactory)null);
   }

   public EpollEventLoopGroup(int var1, ThreadFactory var2) {
      this(var1, var2, 128);
   }

   public EpollEventLoopGroup(int var1, ThreadFactory var2, int var3) {
      super(var1, var2, var3);
   }

   public void setIoRatio(int var1) {
      Iterator var2 = this.children().iterator();

      while(var2.hasNext()) {
         EventExecutor var3 = (EventExecutor)var2.next();
         ((EpollEventLoop)var3).setIoRatio(var1);
      }

   }

   protected EventExecutor newChild(ThreadFactory var1, Object... var2) throws Exception {
      return new EpollEventLoop(this, var1, (Integer)var2[0]);
   }
}
