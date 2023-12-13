package io.netty.handler.traffic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import java.util.concurrent.TimeUnit;

public abstract class AbstractTrafficShapingHandler extends ChannelDuplexHandler {
   public static final long DEFAULT_CHECK_INTERVAL = 1000L;
   private static final long MINIMAL_WAIT = 10L;
   protected TrafficCounter trafficCounter;
   private long writeLimit;
   private long readLimit;
   protected long checkInterval;
   private static final AttributeKey READ_SUSPENDED = AttributeKey.valueOf(AbstractTrafficShapingHandler.class.getName() + ".READ_SUSPENDED");
   private static final AttributeKey REOPEN_TASK = AttributeKey.valueOf(AbstractTrafficShapingHandler.class.getName() + ".REOPEN_TASK");

   void setTrafficCounter(TrafficCounter var1) {
      this.trafficCounter = var1;
   }

   protected AbstractTrafficShapingHandler(long var1, long var3, long var5) {
      this.checkInterval = 1000L;
      this.writeLimit = var1;
      this.readLimit = var3;
      this.checkInterval = var5;
   }

   protected AbstractTrafficShapingHandler(long var1, long var3) {
      this(var1, var3, 1000L);
   }

   protected AbstractTrafficShapingHandler() {
      this(0L, 0L, 1000L);
   }

   protected AbstractTrafficShapingHandler(long var1) {
      this(0L, 0L, var1);
   }

   public void configure(long var1, long var3, long var5) {
      this.configure(var1, var3);
      this.configure(var5);
   }

   public void configure(long var1, long var3) {
      this.writeLimit = var1;
      this.readLimit = var3;
      if (this.trafficCounter != null) {
         this.trafficCounter.resetAccounting(System.currentTimeMillis() + 1L);
      }

   }

   public void configure(long var1) {
      this.checkInterval = var1;
      if (this.trafficCounter != null) {
         this.trafficCounter.configure(this.checkInterval);
      }

   }

   protected void doAccounting(TrafficCounter var1) {
   }

   private static long getTimeToWait(long var0, long var2, long var4, long var6) {
      long var8 = var6 - var4;
      return var8 <= 0L ? 0L : (var2 * 1000L / var0 - var8) / 10L * 10L;
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      long var3 = this.calculateSize(var2);
      long var5 = System.currentTimeMillis();
      if (this.trafficCounter != null) {
         this.trafficCounter.bytesRecvFlowControl(var3);
         if (this.readLimit == 0L) {
            var1.fireChannelRead(var2);
            return;
         }

         long var7 = getTimeToWait(this.readLimit, this.trafficCounter.currentReadBytes(), this.trafficCounter.lastTime(), var5);
         if (var7 >= 10L) {
            if (var1 == null) {
               Runnable var11 = new Runnable(this, var1, var2) {
                  final ChannelHandlerContext val$ctx;
                  final Object val$msg;
                  final AbstractTrafficShapingHandler this$0;

                  {
                     this.this$0 = var1;
                     this.val$ctx = var2;
                     this.val$msg = var3;
                  }

                  public void run() {
                     this.val$ctx.fireChannelRead(this.val$msg);
                  }
               };
               var1.executor().schedule(var11, var7, TimeUnit.MILLISECONDS);
               return;
            }

            var1.attr(READ_SUSPENDED).set(true);
            Attribute var9 = var1.attr(REOPEN_TASK);
            Object var10 = (Runnable)var9.get();
            if (var10 == null) {
               var10 = new AbstractTrafficShapingHandler.ReopenReadTimerTask(var1);
               var9.set(var10);
            }

            var1.executor().schedule((Runnable)var10, var7, TimeUnit.MILLISECONDS);
         }
      }

      var1.fireChannelRead(var2);
   }

   public void read(ChannelHandlerContext var1) {
      if (var1 != null) {
         var1.read();
      }

   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      long var4 = System.currentTimeMillis();
      long var6 = this.calculateSize(var2);
      if (var6 > -1L && this.trafficCounter != null) {
         this.trafficCounter.bytesWriteFlowControl(var6);
         if (this.writeLimit == 0L) {
            var1.write(var2, var3);
            return;
         }

         long var8 = getTimeToWait(this.writeLimit, this.trafficCounter.currentWrittenBytes(), this.trafficCounter.lastTime(), var4);
         if (var8 >= 10L) {
            var1.executor().schedule(new Runnable(this, var1, var2, var3) {
               final ChannelHandlerContext val$ctx;
               final Object val$msg;
               final ChannelPromise val$promise;
               final AbstractTrafficShapingHandler this$0;

               {
                  this.this$0 = var1;
                  this.val$ctx = var2;
                  this.val$msg = var3;
                  this.val$promise = var4;
               }

               public void run() {
                  this.val$ctx.write(this.val$msg, this.val$promise);
               }
            }, var8, TimeUnit.MILLISECONDS);
            return;
         }
      }

      var1.write(var2, var3);
   }

   public TrafficCounter trafficCounter() {
      return this.trafficCounter;
   }

   public String toString() {
      return "TrafficShaping with Write Limit: " + this.writeLimit + " Read Limit: " + this.readLimit + " and Counter: " + (this.trafficCounter != null ? this.trafficCounter.toString() : "none");
   }

   protected long calculateSize(Object var1) {
      if (var1 instanceof ByteBuf) {
         return (long)((ByteBuf)var1).readableBytes();
      } else {
         return var1 instanceof ByteBufHolder ? (long)((ByteBufHolder)var1).content().readableBytes() : -1L;
      }
   }

   static AttributeKey access$000() {
      return READ_SUSPENDED;
   }

   private static final class ReopenReadTimerTask implements Runnable {
      final ChannelHandlerContext ctx;

      ReopenReadTimerTask(ChannelHandlerContext var1) {
         this.ctx = var1;
      }

      public void run() {
         this.ctx.attr(AbstractTrafficShapingHandler.access$000()).set(false);
         this.ctx.read();
      }
   }
}
