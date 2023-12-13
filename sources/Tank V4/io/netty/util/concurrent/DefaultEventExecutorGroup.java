package io.netty.util.concurrent;

import java.util.concurrent.ThreadFactory;

public class DefaultEventExecutorGroup extends MultithreadEventExecutorGroup {
   public DefaultEventExecutorGroup(int var1) {
      this(var1, (ThreadFactory)null);
   }

   public DefaultEventExecutorGroup(int var1, ThreadFactory var2) {
      super(var1, var2);
   }

   protected EventExecutor newChild(ThreadFactory var1, Object... var2) throws Exception {
      return new DefaultEventExecutor(this, var1);
   }
}
