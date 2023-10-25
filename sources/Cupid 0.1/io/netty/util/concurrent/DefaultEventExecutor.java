package io.netty.util.concurrent;

import java.util.concurrent.ThreadFactory;

final class DefaultEventExecutor extends SingleThreadEventExecutor {
  DefaultEventExecutor(DefaultEventExecutorGroup parent, ThreadFactory threadFactory) {
    super(parent, threadFactory, true);
  }
  
  protected void run() {
    do {
      Runnable task = takeTask();
      if (task == null)
        continue; 
      task.run();
      updateLastExecutionTime();
    } while (!confirmShutdown());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\DefaultEventExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */