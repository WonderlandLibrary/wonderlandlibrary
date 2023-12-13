package io.netty.channel.embedded;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.AbstractEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

final class EmbeddedEventLoop extends AbstractEventExecutor implements EventLoop {
   private final Queue tasks = new ArrayDeque(2);

   public void execute(Runnable var1) {
      if (var1 == null) {
         throw new NullPointerException("command");
      } else {
         this.tasks.add(var1);
      }
   }

   void runTasks() {
      while(true) {
         Runnable var1 = (Runnable)this.tasks.poll();
         if (var1 == null) {
            return;
         }

         var1.run();
      }
   }

   public Future shutdownGracefully(long var1, long var3, TimeUnit var5) {
      throw new UnsupportedOperationException();
   }

   public Future terminationFuture() {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public void shutdown() {
      throw new UnsupportedOperationException();
   }

   public boolean isShuttingDown() {
      return false;
   }

   public boolean isShutdown() {
      return false;
   }

   public boolean isTerminated() {
      return false;
   }

   public boolean awaitTermination(long var1, TimeUnit var3) throws InterruptedException {
      Thread.sleep(var3.toMillis(var1));
      return false;
   }

   public ChannelFuture register(Channel var1) {
      return this.register(var1, new DefaultChannelPromise(var1, this));
   }

   public ChannelFuture register(Channel var1, ChannelPromise var2) {
      var1.unsafe().register(this, var2);
      return var2;
   }

   public boolean inEventLoop() {
      return true;
   }

   public boolean inEventLoop(Thread var1) {
      return true;
   }

   public EventLoop next() {
      return this;
   }

   public EventLoopGroup parent() {
      return this;
   }

   public EventExecutor next() {
      return this.next();
   }

   public EventExecutorGroup parent() {
      return this.parent();
   }
}
