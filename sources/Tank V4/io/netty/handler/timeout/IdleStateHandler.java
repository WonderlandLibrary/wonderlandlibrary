package io.netty.handler.timeout;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class IdleStateHandler extends ChannelDuplexHandler {
   private final long readerIdleTimeMillis;
   private final long writerIdleTimeMillis;
   private final long allIdleTimeMillis;
   volatile ScheduledFuture readerIdleTimeout;
   volatile long lastReadTime;
   private boolean firstReaderIdleEvent;
   volatile ScheduledFuture writerIdleTimeout;
   volatile long lastWriteTime;
   private boolean firstWriterIdleEvent;
   volatile ScheduledFuture allIdleTimeout;
   private boolean firstAllIdleEvent;
   private volatile int state;

   public IdleStateHandler(int var1, int var2, int var3) {
      this((long)var1, (long)var2, (long)var3, TimeUnit.SECONDS);
   }

   public IdleStateHandler(long var1, long var3, long var5, TimeUnit var7) {
      this.firstReaderIdleEvent = true;
      this.firstWriterIdleEvent = true;
      this.firstAllIdleEvent = true;
      if (var7 == null) {
         throw new NullPointerException("unit");
      } else {
         if (var1 <= 0L) {
            this.readerIdleTimeMillis = 0L;
         } else {
            this.readerIdleTimeMillis = Math.max(var7.toMillis(var1), 1L);
         }

         if (var3 <= 0L) {
            this.writerIdleTimeMillis = 0L;
         } else {
            this.writerIdleTimeMillis = Math.max(var7.toMillis(var3), 1L);
         }

         if (var5 <= 0L) {
            this.allIdleTimeMillis = 0L;
         } else {
            this.allIdleTimeMillis = Math.max(var7.toMillis(var5), 1L);
         }

      }
   }

   public long getReaderIdleTimeInMillis() {
      return this.readerIdleTimeMillis;
   }

   public long getWriterIdleTimeInMillis() {
      return this.writerIdleTimeMillis;
   }

   public long getAllIdleTimeInMillis() {
      return this.allIdleTimeMillis;
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
      this.firstReaderIdleEvent = this.firstAllIdleEvent = true;
      var1.fireChannelRead(var2);
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      var3.addListener(new ChannelFutureListener(this) {
         final IdleStateHandler this$0;

         {
            this.this$0 = var1;
         }

         public void operationComplete(ChannelFuture var1) throws Exception {
            this.this$0.lastWriteTime = System.currentTimeMillis();
            IdleStateHandler.access$002(this.this$0, IdleStateHandler.access$102(this.this$0, true));
         }

         public void operationComplete(Future var1) throws Exception {
            this.operationComplete((ChannelFuture)var1);
         }
      });
      var1.write(var2, var3);
   }

   private void initialize(ChannelHandlerContext var1) {
      switch(this.state) {
      case 1:
      case 2:
         return;
      default:
         this.state = 1;
         EventExecutor var2 = var1.executor();
         this.lastReadTime = this.lastWriteTime = System.currentTimeMillis();
         if (this.readerIdleTimeMillis > 0L) {
            this.readerIdleTimeout = var2.schedule(new IdleStateHandler.ReaderIdleTimeoutTask(this, var1), this.readerIdleTimeMillis, TimeUnit.MILLISECONDS);
         }

         if (this.writerIdleTimeMillis > 0L) {
            this.writerIdleTimeout = var2.schedule(new IdleStateHandler.WriterIdleTimeoutTask(this, var1), this.writerIdleTimeMillis, TimeUnit.MILLISECONDS);
         }

         if (this.allIdleTimeMillis > 0L) {
            this.allIdleTimeout = var2.schedule(new IdleStateHandler.AllIdleTimeoutTask(this, var1), this.allIdleTimeMillis, TimeUnit.MILLISECONDS);
         }

      }
   }

   private void destroy() {
      this.state = 2;
      if (this.readerIdleTimeout != null) {
         this.readerIdleTimeout.cancel(false);
         this.readerIdleTimeout = null;
      }

      if (this.writerIdleTimeout != null) {
         this.writerIdleTimeout.cancel(false);
         this.writerIdleTimeout = null;
      }

      if (this.allIdleTimeout != null) {
         this.allIdleTimeout.cancel(false);
         this.allIdleTimeout = null;
      }

   }

   protected void channelIdle(ChannelHandlerContext var1, IdleStateEvent var2) throws Exception {
      var1.fireUserEventTriggered(var2);
   }

   static boolean access$002(IdleStateHandler var0, boolean var1) {
      return var0.firstWriterIdleEvent = var1;
   }

   static boolean access$102(IdleStateHandler var0, boolean var1) {
      return var0.firstAllIdleEvent = var1;
   }

   static long access$200(IdleStateHandler var0) {
      return var0.readerIdleTimeMillis;
   }

   static boolean access$300(IdleStateHandler var0) {
      return var0.firstReaderIdleEvent;
   }

   static boolean access$302(IdleStateHandler var0, boolean var1) {
      return var0.firstReaderIdleEvent = var1;
   }

   static long access$400(IdleStateHandler var0) {
      return var0.writerIdleTimeMillis;
   }

   static boolean access$000(IdleStateHandler var0) {
      return var0.firstWriterIdleEvent;
   }

   static long access$500(IdleStateHandler var0) {
      return var0.allIdleTimeMillis;
   }

   static boolean access$100(IdleStateHandler var0) {
      return var0.firstAllIdleEvent;
   }

   private final class AllIdleTimeoutTask implements Runnable {
      private final ChannelHandlerContext ctx;
      final IdleStateHandler this$0;

      AllIdleTimeoutTask(IdleStateHandler var1, ChannelHandlerContext var2) {
         this.this$0 = var1;
         this.ctx = var2;
      }

      public void run() {
         if (this.ctx.channel().isOpen()) {
            long var1 = System.currentTimeMillis();
            long var3 = Math.max(this.this$0.lastReadTime, this.this$0.lastWriteTime);
            long var5 = IdleStateHandler.access$500(this.this$0) - (var1 - var3);
            if (var5 <= 0L) {
               this.this$0.allIdleTimeout = this.ctx.executor().schedule(this, IdleStateHandler.access$500(this.this$0), TimeUnit.MILLISECONDS);

               try {
                  IdleStateEvent var7;
                  if (IdleStateHandler.access$100(this.this$0)) {
                     IdleStateHandler.access$102(this.this$0, false);
                     var7 = IdleStateEvent.FIRST_ALL_IDLE_STATE_EVENT;
                  } else {
                     var7 = IdleStateEvent.ALL_IDLE_STATE_EVENT;
                  }

                  this.this$0.channelIdle(this.ctx, var7);
               } catch (Throwable var8) {
                  this.ctx.fireExceptionCaught(var8);
               }
            } else {
               this.this$0.allIdleTimeout = this.ctx.executor().schedule(this, var5, TimeUnit.MILLISECONDS);
            }

         }
      }
   }

   private final class WriterIdleTimeoutTask implements Runnable {
      private final ChannelHandlerContext ctx;
      final IdleStateHandler this$0;

      WriterIdleTimeoutTask(IdleStateHandler var1, ChannelHandlerContext var2) {
         this.this$0 = var1;
         this.ctx = var2;
      }

      public void run() {
         if (this.ctx.channel().isOpen()) {
            long var1 = System.currentTimeMillis();
            long var3 = this.this$0.lastWriteTime;
            long var5 = IdleStateHandler.access$400(this.this$0) - (var1 - var3);
            if (var5 <= 0L) {
               this.this$0.writerIdleTimeout = this.ctx.executor().schedule(this, IdleStateHandler.access$400(this.this$0), TimeUnit.MILLISECONDS);

               try {
                  IdleStateEvent var7;
                  if (IdleStateHandler.access$000(this.this$0)) {
                     IdleStateHandler.access$002(this.this$0, false);
                     var7 = IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT;
                  } else {
                     var7 = IdleStateEvent.WRITER_IDLE_STATE_EVENT;
                  }

                  this.this$0.channelIdle(this.ctx, var7);
               } catch (Throwable var8) {
                  this.ctx.fireExceptionCaught(var8);
               }
            } else {
               this.this$0.writerIdleTimeout = this.ctx.executor().schedule(this, var5, TimeUnit.MILLISECONDS);
            }

         }
      }
   }

   private final class ReaderIdleTimeoutTask implements Runnable {
      private final ChannelHandlerContext ctx;
      final IdleStateHandler this$0;

      ReaderIdleTimeoutTask(IdleStateHandler var1, ChannelHandlerContext var2) {
         this.this$0 = var1;
         this.ctx = var2;
      }

      public void run() {
         if (this.ctx.channel().isOpen()) {
            long var1 = System.currentTimeMillis();
            long var3 = this.this$0.lastReadTime;
            long var5 = IdleStateHandler.access$200(this.this$0) - (var1 - var3);
            if (var5 <= 0L) {
               this.this$0.readerIdleTimeout = this.ctx.executor().schedule(this, IdleStateHandler.access$200(this.this$0), TimeUnit.MILLISECONDS);

               try {
                  IdleStateEvent var7;
                  if (IdleStateHandler.access$300(this.this$0)) {
                     IdleStateHandler.access$302(this.this$0, false);
                     var7 = IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT;
                  } else {
                     var7 = IdleStateEvent.READER_IDLE_STATE_EVENT;
                  }

                  this.this$0.channelIdle(this.ctx, var7);
               } catch (Throwable var8) {
                  this.ctx.fireExceptionCaught(var8);
               }
            } else {
               this.this$0.readerIdleTimeout = this.ctx.executor().schedule(this, var5, TimeUnit.MILLISECONDS);
            }

         }
      }
   }
}
