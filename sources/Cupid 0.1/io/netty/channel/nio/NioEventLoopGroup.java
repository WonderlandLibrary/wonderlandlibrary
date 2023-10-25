package io.netty.channel.nio;

import io.netty.channel.MultithreadEventLoopGroup;
import io.netty.util.concurrent.EventExecutor;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.ThreadFactory;

public class NioEventLoopGroup extends MultithreadEventLoopGroup {
  public NioEventLoopGroup() {
    this(0);
  }
  
  public NioEventLoopGroup(int nThreads) {
    this(nThreads, null);
  }
  
  public NioEventLoopGroup(int nThreads, ThreadFactory threadFactory) {
    this(nThreads, threadFactory, SelectorProvider.provider());
  }
  
  public NioEventLoopGroup(int nThreads, ThreadFactory threadFactory, SelectorProvider selectorProvider) {
    super(nThreads, threadFactory, new Object[] { selectorProvider });
  }
  
  public void setIoRatio(int ioRatio) {
    for (EventExecutor e : children())
      ((NioEventLoop)e).setIoRatio(ioRatio); 
  }
  
  public void rebuildSelectors() {
    for (EventExecutor e : children())
      ((NioEventLoop)e).rebuildSelector(); 
  }
  
  protected EventExecutor newChild(ThreadFactory threadFactory, Object... args) throws Exception {
    return (EventExecutor)new NioEventLoop(this, threadFactory, (SelectorProvider)args[0]);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\nio\NioEventLoopGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */