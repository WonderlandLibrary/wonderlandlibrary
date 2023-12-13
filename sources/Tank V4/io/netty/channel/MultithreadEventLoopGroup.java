package io.netty.channel;

import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.MultithreadEventExecutorGroup;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.ThreadFactory;

public abstract class MultithreadEventLoopGroup extends MultithreadEventExecutorGroup implements EventLoopGroup {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(MultithreadEventLoopGroup.class);
   private static final int DEFAULT_EVENT_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", Runtime.getRuntime().availableProcessors() * 2));

   protected MultithreadEventLoopGroup(int var1, ThreadFactory var2, Object... var3) {
      super(var1 == 0 ? DEFAULT_EVENT_LOOP_THREADS : var1, var2, var3);
   }

   protected ThreadFactory newDefaultThreadFactory() {
      return new DefaultThreadFactory(this.getClass(), 10);
   }

   public EventLoop next() {
      return (EventLoop)super.next();
   }

   public ChannelFuture register(Channel var1) {
      return this.next().register(var1);
   }

   public ChannelFuture register(Channel var1, ChannelPromise var2) {
      return this.next().register(var1, var2);
   }

   public EventExecutor next() {
      return this.next();
   }

   static {
      if (logger.isDebugEnabled()) {
         logger.debug("-Dio.netty.eventLoopThreads: {}", (Object)DEFAULT_EVENT_LOOP_THREADS);
      }

   }
}
