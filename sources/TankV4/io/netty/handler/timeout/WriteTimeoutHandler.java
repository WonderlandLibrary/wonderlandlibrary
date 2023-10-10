package io.netty.handler.timeout;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class WriteTimeoutHandler extends ChannelOutboundHandlerAdapter {
   private final long timeoutMillis;
   private boolean closed;

   public WriteTimeoutHandler(int var1) {
      this((long)var1, TimeUnit.SECONDS);
   }

   public WriteTimeoutHandler(long var1, TimeUnit var3) {
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

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      this.scheduleTimeout(var1, var3);
      var1.write(var2, var3);
   }

   private void scheduleTimeout(ChannelHandlerContext var1, ChannelPromise var2) {
      if (this.timeoutMillis > 0L) {
         ScheduledFuture var3 = var1.executor().schedule(new Runnable(this, var2, var1) {
            final ChannelPromise val$future;
            final ChannelHandlerContext val$ctx;
            final WriteTimeoutHandler this$0;

            {
               this.this$0 = var1;
               this.val$future = var2;
               this.val$ctx = var3;
            }

            public void run() {
               if (this.val$future.tryFailure(WriteTimeoutException.INSTANCE)) {
                  try {
                     this.this$0.writeTimedOut(this.val$ctx);
                  } catch (Throwable var2) {
                     this.val$ctx.fireExceptionCaught(var2);
                  }
               }

            }
         }, this.timeoutMillis, TimeUnit.MILLISECONDS);
         var2.addListener(new ChannelFutureListener(this, var3) {
            final java.util.concurrent.ScheduledFuture val$sf;
            final WriteTimeoutHandler this$0;

            {
               this.this$0 = var1;
               this.val$sf = var2;
            }

            public void operationComplete(ChannelFuture var1) throws Exception {
               this.val$sf.cancel(false);
            }

            public void operationComplete(Future var1) throws Exception {
               this.operationComplete((ChannelFuture)var1);
            }
         });
      }

   }

   protected void writeTimedOut(ChannelHandlerContext var1) throws Exception {
      if (!this.closed) {
         var1.fireExceptionCaught(WriteTimeoutException.INSTANCE);
         var1.close();
         this.closed = true;
      }

   }
}
