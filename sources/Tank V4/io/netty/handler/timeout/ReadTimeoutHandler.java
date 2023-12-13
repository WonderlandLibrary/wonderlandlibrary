package io.netty.handler.timeout;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ReadTimeoutHandler extends ChannelInboundHandlerAdapter {
   private final long timeoutMillis;
   private volatile ScheduledFuture timeout;
   private volatile long lastReadTime;
   private volatile int state;
   private boolean closed;

   public ReadTimeoutHandler(int var1) {
      this((long)var1, TimeUnit.SECONDS);
   }

   public ReadTimeoutHandler(long var1, TimeUnit var3) {
      if (var3 == null) {
         throw new NullPointerException("unit");
      } else {
         if (var1 <= 0L) {
            this.timeoutMillis = 0L;
         } else {
            this.timeoutMillis = Math.max(var3.toMillis(var1), 1L);
         }

      }
   }

   public void handlerAdded(ChannelHandlerContext var1) throws Exception {
      if (var1.channel().isActive() && var1.channel().isRegistered()) {
         this.initialize(var1);
      }

   }

   public void handlerRemoved(ChannelHandlerContext var1) throws Exception {
      this.destroy();
   }

   public void channelRegistered(ChannelHandlerContext var1) throws Exception {
      if (var1.channel().isActive()) {
         this.initialize(var1);
      }

      super.channelRegistered(var1);
   }

   public void channelActive(ChannelHandlerContext var1) throws Exception {
      this.initialize(var1);
      super.channelActive(var1);
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      this.destroy();
      super.channelInactive(var1);
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      this.lastReadTime = System.currentTimeMillis();
      var1.fireChannelRead(var2);
   }

   private void initialize(ChannelHandlerContext var1) {
      switch(this.state) {
      case 1:
      case 2:
         return;
      default:
         this.state = 1;
         this.lastReadTime = System.currentTimeMillis();
         if (this.timeoutMillis > 0L) {
            this.timeout = var1.executor().schedule(new ReadTimeoutHandler.ReadTimeoutTask(this, var1), this.timeoutMillis, TimeUnit.MILLISECONDS);
         }

      }
   }

   private void destroy() {
      this.state = 2;
      if (this.timeout != null) {
         this.timeout.cancel(false);
         this.timeout = null;
      }

   }

   protected void readTimedOut(ChannelHandlerContext var1) throws Exception {
      if (!this.closed) {
         var1.fireExceptionCaught(ReadTimeoutException.INSTANCE);
         var1.close();
         this.closed = true;
      }

   }

   static long access$000(ReadTimeoutHandler var0) {
      return var0.timeoutMillis;
   }

   static long access$100(ReadTimeoutHandler var0) {
      return var0.lastReadTime;
   }

   static ScheduledFuture access$202(ReadTimeoutHandler var0, ScheduledFuture var1) {
      return var0.timeout = var1;
   }

   private final class ReadTimeoutTask implements Runnable {
      private final ChannelHandlerContext ctx;
      final ReadTimeoutHandler this$0;

      ReadTimeoutTask(ReadTimeoutHandler var1, ChannelHandlerContext var2) {
         this.this$0 = var1;
         this.ctx = var2;
      }

      public void run() {
         if (this.ctx.channel().isOpen()) {
            long var1 = System.currentTimeMillis();
            long var3 = ReadTimeoutHandler.access$000(this.this$0) - (var1 - ReadTimeoutHandler.access$100(this.this$0));
            if (var3 <= 0L) {
               ReadTimeoutHandler.access$202(this.this$0, this.ctx.executor().schedule(this, ReadTimeoutHandler.access$000(this.this$0), TimeUnit.MILLISECONDS));

               try {
                  this.this$0.readTimedOut(this.ctx);
               } catch (Throwable var6) {
                  this.ctx.fireExceptionCaught(var6);
               }
            } else {
               ReadTimeoutHandler.access$202(this.this$0, this.ctx.executor().schedule(this, var3, TimeUnit.MILLISECONDS));
            }

         }
      }
   }
}
