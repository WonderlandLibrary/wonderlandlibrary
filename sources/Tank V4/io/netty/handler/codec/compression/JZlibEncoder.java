package io.netty.handler.codec.compression;

import com.jcraft.jzlib.Deflater;
import com.jcraft.jzlib.JZlib;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelPromiseNotifier;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.EmptyArrays;
import java.util.concurrent.TimeUnit;

public class JZlibEncoder extends ZlibEncoder {
   private final int wrapperOverhead;
   private final Deflater z;
   private volatile boolean finished;
   private volatile ChannelHandlerContext ctx;

   public JZlibEncoder() {
      this(6);
   }

   public JZlibEncoder(int var1) {
      this(ZlibWrapper.ZLIB, var1);
   }

   public JZlibEncoder(ZlibWrapper var1) {
      this(var1, 6);
   }

   public JZlibEncoder(ZlibWrapper var1, int var2) {
      this(var1, var2, 15, 8);
   }

   public JZlibEncoder(ZlibWrapper var1, int var2, int var3, int var4) {
      this.z = new Deflater();
      if (var2 >= 0 && var2 <= 9) {
         if (var3 >= 9 && var3 <= 15) {
            if (var4 >= 1 && var4 <= 9) {
               if (var1 == null) {
                  throw new NullPointerException("wrapper");
               } else if (var1 == ZlibWrapper.ZLIB_OR_NONE) {
                  throw new IllegalArgumentException("wrapper '" + ZlibWrapper.ZLIB_OR_NONE + "' is not " + "allowed for compression.");
               } else {
                  int var5 = this.z.init(var2, var3, var4, ZlibUtil.convertWrapperType(var1));
                  if (var5 != 0) {
                     ZlibUtil.fail(this.z, "initialization failure", var5);
                  }

                  this.wrapperOverhead = ZlibUtil.wrapperOverhead(var1);
               }
            } else {
               throw new IllegalArgumentException("memLevel: " + var4 + " (expected: 1-9)");
            }
         } else {
            throw new IllegalArgumentException("windowBits: " + var3 + " (expected: 9-15)");
         }
      } else {
         throw new IllegalArgumentException("compressionLevel: " + var2 + " (expected: 0-9)");
      }
   }

   public JZlibEncoder(byte[] var1) {
      this(6, var1);
   }

   public JZlibEncoder(int var1, byte[] var2) {
      this(var1, 15, 8, var2);
   }

   public JZlibEncoder(int var1, int var2, int var3, byte[] var4) {
      this.z = new Deflater();
      if (var1 >= 0 && var1 <= 9) {
         if (var2 >= 9 && var2 <= 15) {
            if (var3 >= 1 && var3 <= 9) {
               if (var4 == null) {
                  throw new NullPointerException("dictionary");
               } else {
                  int var5 = this.z.deflateInit(var1, var2, var3, JZlib.W_ZLIB);
                  if (var5 != 0) {
                     ZlibUtil.fail(this.z, "initialization failure", var5);
                  } else {
                     var5 = this.z.deflateSetDictionary(var4, var4.length);
                     if (var5 != 0) {
                        ZlibUtil.fail(this.z, "failed to set the dictionary", var5);
                     }
                  }

                  this.wrapperOverhead = ZlibUtil.wrapperOverhead(ZlibWrapper.ZLIB);
               }
            } else {
               throw new IllegalArgumentException("memLevel: " + var3 + " (expected: 1-9)");
            }
         } else {
            throw new IllegalArgumentException("windowBits: " + var2 + " (expected: 9-15)");
         }
      } else {
         throw new IllegalArgumentException("compressionLevel: " + var1 + " (expected: 0-9)");
      }
   }

   public ChannelFuture close() {
      return this.close(this.ctx().channel().newPromise());
   }

   public ChannelFuture close(ChannelPromise var1) {
      ChannelHandlerContext var2 = this.ctx();
      EventExecutor var3 = var2.executor();
      if (var3.inEventLoop()) {
         return this.finishEncode(var2, var1);
      } else {
         ChannelPromise var4 = var2.newPromise();
         var3.execute(new Runnable(this, var4, var1) {
            final ChannelPromise val$p;
            final ChannelPromise val$promise;
            final JZlibEncoder this$0;

            {
               this.this$0 = var1;
               this.val$p = var2;
               this.val$promise = var3;
            }

            public void run() {
               ChannelFuture var1 = JZlibEncoder.access$100(this.this$0, JZlibEncoder.access$000(this.this$0), this.val$p);
               var1.addListener(new ChannelPromiseNotifier(new ChannelPromise[]{this.val$promise}));
            }
         });
         return var4;
      }
   }

   private ChannelHandlerContext ctx() {
      ChannelHandlerContext var1 = this.ctx;
      if (var1 == null) {
         throw new IllegalStateException("not added to a pipeline");
      } else {
         return var1;
      }
   }

   public boolean isClosed() {
      return this.finished;
   }

   protected void encode(ChannelHandlerContext var1, ByteBuf var2, ByteBuf var3) throws Exception {
      if (!this.finished) {
         int var4 = var2.readableBytes();
         boolean var5 = var2.hasArray();
         this.z.avail_in = var4;
         if (var5) {
            this.z.next_in = var2.array();
            this.z.next_in_index = var2.arrayOffset() + var2.readerIndex();
         } else {
            byte[] var6 = new byte[var4];
            var2.getBytes(var2.readerIndex(), var6);
            this.z.next_in = var6;
            this.z.next_in_index = 0;
         }

         int var12 = this.z.next_in_index;
         int var7 = (int)Math.ceil((double)var4 * 1.001D) + 12 + this.wrapperOverhead;
         var3.ensureWritable(var7);
         this.z.avail_out = var7;
         this.z.next_out = var3.array();
         this.z.next_out_index = var3.arrayOffset() + var3.writerIndex();
         int var8 = this.z.next_out_index;
         int var9 = this.z.deflate(2);
         var2.skipBytes(this.z.next_in_index - var12);
         if (var9 != 0) {
            ZlibUtil.fail(this.z, "compression failure", var9);
         }

         int var10 = this.z.next_out_index - var8;
         if (var10 > 0) {
            var3.writerIndex(var3.writerIndex() + var10);
         }

         this.z.next_in = null;
         this.z.next_out = null;
      }
   }

   public void close(ChannelHandlerContext var1, ChannelPromise var2) {
      ChannelFuture var3 = this.finishEncode(var1, var1.newPromise());
      var3.addListener(new ChannelFutureListener(this, var1, var2) {
         final ChannelHandlerContext val$ctx;
         final ChannelPromise val$promise;
         final JZlibEncoder this$0;

         {
            this.this$0 = var1;
            this.val$ctx = var2;
            this.val$promise = var3;
         }

         public void operationComplete(ChannelFuture var1) throws Exception {
            this.val$ctx.close(this.val$promise);
         }

         public void operationComplete(Future var1) throws Exception {
            this.operationComplete((ChannelFuture)var1);
         }
      });
      if (!var3.isDone()) {
         var1.executor().schedule(new Runnable(this, var1, var2) {
            final ChannelHandlerContext val$ctx;
            final ChannelPromise val$promise;
            final JZlibEncoder this$0;

            {
               this.this$0 = var1;
               this.val$ctx = var2;
               this.val$promise = var3;
            }

            public void run() {
               this.val$ctx.close(this.val$promise);
            }
         }, 10L, TimeUnit.SECONDS);
      }

   }

   private ChannelFuture finishEncode(ChannelHandlerContext var1, ChannelPromise var2) {
      if (this.finished) {
         var2.setSuccess();
         return var2;
      } else {
         this.finished = true;
         this.z.next_in = EmptyArrays.EMPTY_BYTES;
         this.z.next_in_index = 0;
         this.z.avail_in = 0;
         byte[] var4 = new byte[32];
         this.z.next_out = var4;
         this.z.next_out_index = 0;
         this.z.avail_out = var4.length;
         int var5 = this.z.deflate(4);
         if (var5 != 0 && var5 != 1) {
            var2.setFailure(ZlibUtil.deflaterException(this.z, "compression failure", var5));
            this.z.deflateEnd();
            this.z.next_in = null;
            this.z.next_out = null;
            return var2;
         } else {
            ByteBuf var3;
            if (this.z.next_out_index != 0) {
               var3 = Unpooled.wrappedBuffer(var4, 0, this.z.next_out_index);
            } else {
               var3 = Unpooled.EMPTY_BUFFER;
            }

            this.z.deflateEnd();
            this.z.next_in = null;
            this.z.next_out = null;
            return var1.writeAndFlush(var3, var2);
         }
      }
   }

   public void handlerAdded(ChannelHandlerContext var1) throws Exception {
      this.ctx = var1;
   }

   protected void encode(ChannelHandlerContext var1, Object var2, ByteBuf var3) throws Exception {
      this.encode(var1, (ByteBuf)var2, var3);
   }

   static ChannelHandlerContext access$000(JZlibEncoder var0) {
      return var0.ctx();
   }

   static ChannelFuture access$100(JZlibEncoder var0, ChannelHandlerContext var1, ChannelPromise var2) {
      return var0.finishEncode(var1, var2);
   }
}
