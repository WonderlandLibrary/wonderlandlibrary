package io.netty.channel.local;

import io.netty.channel.MultithreadEventLoopGroup;
import io.netty.util.concurrent.EventExecutor;
import java.util.concurrent.ThreadFactory;

public class LocalEventLoopGroup extends MultithreadEventLoopGroup {
  public LocalEventLoopGroup() {
    this(0);
  }
  
  public LocalEventLoopGroup(int nThreads) {
    this(nThreads, null);
  }
  
  public LocalEventLoopGroup(int nThreads, ThreadFactory threadFactory) {
    super(nThreads, threadFactory, new Object[0]);
  }
  
  protected EventExecutor newChild(ThreadFactory threadFactory, Object... args) throws Exception {
    return (EventExecutor)new LocalEventLoop(this, threadFactory);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\local\LocalEventLoopGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */