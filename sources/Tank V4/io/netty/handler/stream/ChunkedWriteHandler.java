package io.netty.handler.stream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayDeque;
import java.util.Queue;

public class ChunkedWriteHandler extends ChannelDuplexHandler {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ChunkedWriteHandler.class);
   private final Queue queue = new ArrayDeque();
   private volatile ChannelHandlerContext ctx;
   private ChunkedWriteHandler.PendingWrite currentWrite;

   public ChunkedWriteHandler() {
   }

   /** @deprecated */
   @Deprecated
   public ChunkedWriteHandler(int var1) {
      if (var1 <= 0) {
         throw new IllegalArgumentException("maxPendingWrites: " + var1 + " (expected: > 0)");
      }
   }

   public void handlerAdded(ChannelHandlerContext var1) throws Exception {
      this.ctx = var1;
   }

   public void resumeTransfer() {
      ChannelHandlerContext var1 = this.ctx;
      if (var1 != null) {
         if (var1.executor().inEventLoop()) {
            try {
               this.doFlush(var1);
            } catch (Exception var3) {
               if (logger.isWarnEnabled()) {
                  logger.warn("Unexpected exception while sending chunks.", (Throwable)var3);
               }
            }
         } else {
            var1.executor().execute(new Runnable(this, var1) {
               final ChannelHandlerContext val$ctx;
               final ChunkedWriteHandler this$0;

               {
                  this.this$0 = var1;
                  this.val$ctx = var2;
               }

               public void run() {
                  try {
                     ChunkedWriteHandler.access$000(this.this$0, this.val$ctx);
                  } catch (Exception var2) {
                     if (ChunkedWriteHandler.access$100().isWarnEnabled()) {
                        ChunkedWriteHandler.access$100().warn("Unexpected exception while sending chunks.", (Throwable)var2);
                     }
                  }

               }
            });
         }

      }
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      this.queue.add(new ChunkedWriteHandler.PendingWrite(var2, var3));
   }

   public void flush(ChannelHandlerContext var1) throws Exception {
      Channel var2 = var1.channel();
      if (var2.isWritable() || !var2.isActive()) {
         this.doFlush(var1);
      }

   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      this.doFlush(var1);
      super.channelInactive(var1);
   }

   public void channelWritabilityChanged(ChannelHandlerContext var1) throws Exception {
      if (var1.channel().isWritable()) {
         this.doFlush(var1);
      }

      var1.fireChannelWritabilityChanged();
   }

   private void discard(Throwable var1) {
      while(true) {
         ChunkedWriteHandler.PendingWrite var2 = this.currentWrite;
         if (this.currentWrite == null) {
            var2 = (ChunkedWriteHandler.PendingWrite)this.queue.poll();
         } else {
            this.currentWrite = null;
         }

         if (var2 == null) {
            return;
         }

         Object var3 = var2.msg;
         if (var3 instanceof ChunkedInput) {
            ChunkedInput var4 = (ChunkedInput)var3;

            try {
               if (!var4.isEndOfInput()) {
                  if (var1 == null) {
                     var1 = new ClosedChannelException();
                  }

                  var2.fail((Throwable)var1);
               } else {
                  var2.success();
               }

               closeInput(var4);
            } catch (Exception var6) {
               var2.fail(var6);
               logger.warn(ChunkedInput.class.getSimpleName() + ".isEndOfInput() failed", (Throwable)var6);
               closeInput(var4);
            }
         } else {
            if (var1 == null) {
               var1 = new ClosedChannelException();
            }

            var2.fail((Throwable)var1);
         }
      }
   }

   private void doFlush(ChannelHandlerContext var1) throws Exception {
      Channel var2 = var1.channel();
      if (!var2.isActive()) {
         this.discard((Throwable)null);
      } else {
         while(var2.isWritable()) {
            if (this.currentWrite == null) {
               this.currentWrite = (ChunkedWriteHandler.PendingWrite)this.queue.poll();
            }

            if (this.currentWrite == null) {
               break;
            }

            boolean var3 = true;
            ChunkedWriteHandler.PendingWrite var4 = this.currentWrite;
            Object var5 = var4.msg;
            if (var5 instanceof ChunkedInput) {
               ChunkedInput var6 = (ChunkedInput)var5;
               Object var9 = null;

               boolean var7;
               boolean var8;
               try {
                  var9 = var6.readChunk(var1);
                  var7 = var6.isEndOfInput();
                  if (var9 == null) {
                     var8 = !var7;
                  } else {
                     var8 = false;
                  }
               } catch (Throwable var12) {
                  this.currentWrite = null;
                  if (var9 != null) {
                     ReferenceCountUtil.release(var9);
                  }

                  var4.fail(var12);
                  closeInput(var6);
                  break;
               }

               if (var8) {
                  break;
               }

               if (var9 == null) {
                  var9 = Unpooled.EMPTY_BUFFER;
               }

               int var10 = amount(var9);
               ChannelFuture var11 = var1.write(var9);
               if (var7) {
                  this.currentWrite = null;
                  var11.addListener(new ChannelFutureListener(this, var4, var10, var6) {
                     final ChunkedWriteHandler.PendingWrite val$currentWrite;
                     final int val$amount;
                     final ChunkedInput val$chunks;
                     final ChunkedWriteHandler this$0;

                     {
                        this.this$0 = var1;
                        this.val$currentWrite = var2;
                        this.val$amount = var3;
                        this.val$chunks = var4;
                     }

                     public void operationComplete(ChannelFuture var1) throws Exception {
                        this.val$currentWrite.progress(this.val$amount);
                        this.val$currentWrite.success();
                        ChunkedWriteHandler.closeInput(this.val$chunks);
                     }

                     public void operationComplete(Future var1) throws Exception {
                        this.operationComplete((ChannelFuture)var1);
                     }
                  });
               } else if (var2.isWritable()) {
                  var11.addListener(new ChannelFutureListener(this, var5, var4, var10) {
                     final Object val$pendingMessage;
                     final ChunkedWriteHandler.PendingWrite val$currentWrite;
                     final int val$amount;
                     final ChunkedWriteHandler this$0;

                     {
                        this.this$0 = var1;
                        this.val$pendingMessage = var2;
                        this.val$currentWrite = var3;
                        this.val$amount = var4;
                     }

                     public void operationComplete(ChannelFuture var1) throws Exception {
                        if (!var1.isSuccess()) {
                           ChunkedWriteHandler.closeInput((ChunkedInput)this.val$pendingMessage);
                           this.val$currentWrite.fail(var1.cause());
                        } else {
                           this.val$currentWrite.progress(this.val$amount);
                        }

                     }

                     public void operationComplete(Future var1) throws Exception {
                        this.operationComplete((ChannelFuture)var1);
                     }
                  });
               } else {
                  var11.addListener(new ChannelFutureListener(this, var5, var4, var10, var2) {
                     final Object val$pendingMessage;
                     final ChunkedWriteHandler.PendingWrite val$currentWrite;
                     final int val$amount;
                     final Channel val$channel;
                     final ChunkedWriteHandler this$0;

                     {
                        this.this$0 = var1;
                        this.val$pendingMessage = var2;
                        this.val$currentWrite = var3;
                        this.val$amount = var4;
                        this.val$channel = var5;
                     }

                     public void operationComplete(ChannelFuture var1) throws Exception {
                        if (!var1.isSuccess()) {
                           ChunkedWriteHandler.closeInput((ChunkedInput)this.val$pendingMessage);
                           this.val$currentWrite.fail(var1.cause());
                        } else {
                           this.val$currentWrite.progress(this.val$amount);
                           if (this.val$channel.isWritable()) {
                              this.this$0.resumeTransfer();
                           }
                        }

                     }

                     public void operationComplete(Future var1) throws Exception {
                        this.operationComplete((ChannelFuture)var1);
                     }
                  });
               }
            } else {
               var1.write(var5, var4.promise);
               this.currentWrite = null;
            }

            if (var3) {
               var1.flush();
            }

            if (!var2.isActive()) {
               this.discard(new ClosedChannelException());
               return;
            }
         }

      }
   }

   static void closeInput(ChunkedInput var0) {
      try {
         var0.close();
      } catch (Throwable var2) {
         if (logger.isWarnEnabled()) {
            logger.warn("Failed to close a chunked input.", var2);
         }
      }

   }

   private static int amount(Object var0) {
      if (var0 instanceof ByteBuf) {
         return ((ByteBuf)var0).readableBytes();
      } else {
         return var0 instanceof ByteBufHolder ? ((ByteBufHolder)var0).content().readableBytes() : 1;
      }
   }

   static void access$000(ChunkedWriteHandler var0, ChannelHandlerContext var1) throws Exception {
      var0.doFlush(var1);
   }

   static InternalLogger access$100() {
      return logger;
   }

   private static final class PendingWrite {
      final Object msg;
      final ChannelPromise promise;
      private long progress;

      PendingWrite(Object var1, ChannelPromise var2) {
         this.msg = var1;
         this.promise = var2;
      }

      void fail(Throwable var1) {
         ReferenceCountUtil.release(this.msg);
         if (this.promise != null) {
            this.promise.tryFailure(var1);
         }

      }

      void success() {
         if (this.promise instanceof ChannelProgressivePromise) {
            ((ChannelProgressivePromise)this.promise).tryProgress(this.progress, this.progress);
         }

         this.promise.setSuccess();
      }

      void progress(int var1) {
         this.progress += (long)var1;
         if (this.promise instanceof ChannelProgressivePromise) {
            ((ChannelProgressivePromise)this.promise).tryProgress(this.progress, -1L);
         }

      }
   }
}
