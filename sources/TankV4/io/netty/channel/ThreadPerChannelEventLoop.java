package io.netty.channel;

import io.netty.util.concurrent.Future;

public class ThreadPerChannelEventLoop extends SingleThreadEventLoop {
   private final ThreadPerChannelEventLoopGroup parent;
   private Channel ch;

   public ThreadPerChannelEventLoop(ThreadPerChannelEventLoopGroup var1) {
      super(var1, var1.threadFactory, true);
      this.parent = var1;
   }

   public ChannelFuture register(Channel var1, ChannelPromise var2) {
      return super.register(var1, var2).addListener(new ChannelFutureListener(this) {
         final ThreadPerChannelEventLoop this$0;

         {
            this.this$0 = var1;
         }

         public void operationComplete(ChannelFuture var1) throws Exception {
            if (var1.isSuccess()) {
               ThreadPerChannelEventLoop.access$002(this.this$0, var1.channel());
            } else {
               this.this$0.deregister();
            }

         }

         public void operationComplete(Future var1) throws Exception {
            this.operationComplete((ChannelFuture)var1);
         }
      });
   }

   protected void run() {
      while(true) {
         Runnable var1 = this.takeTask();
         if (var1 != null) {
            var1.run();
            this.updateLastExecutionTime();
         }

         Channel var2 = this.ch;
         if (this.isShuttingDown()) {
            if (var2 != null) {
               var2.unsafe().close(var2.unsafe().voidPromise());
            }

            if (this.confirmShutdown()) {
               return;
            }
         } else if (var2 != null && !var2.isRegistered()) {
            this.runAllTasks();
            this.deregister();
         }
      }
   }

   protected void deregister() {
      this.ch = null;
      this.parent.activeChildren.remove(this);
      this.parent.idleChildren.add(this);
   }

   static Channel access$002(ThreadPerChannelEventLoop var0, Channel var1) {
      return var0.ch = var1;
   }
}
