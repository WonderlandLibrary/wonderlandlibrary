package io.netty.util.concurrent;

import java.util.concurrent.ThreadFactory;

public class DefaultEventExecutorGroup extends MultithreadEventExecutorGroup {
  public DefaultEventExecutorGroup(int nThreads) {
    this(nThreads, null);
  }
  
  public DefaultEventExecutorGroup(int nThreads, ThreadFactory threadFactory) {
    super(nThreads, threadFactory, new Object[0]);
  }
  
  protected EventExecutor newChild(ThreadFactory threadFactory, Object... args) throws Exception {
    return new DefaultEventExecutor(this, threadFactory);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\DefaultEventExecutorGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */