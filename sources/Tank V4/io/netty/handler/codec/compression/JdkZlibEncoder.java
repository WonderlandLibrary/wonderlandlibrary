package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelPromiseNotifier;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.zip.CRC32;
import java.util.zip.Deflater;

public class JdkZlibEncoder extends ZlibEncoder {
   private final ZlibWrapper wrapper;
   private final byte[] encodeBuf;
   private final Deflater deflater;
   private volatile boolean finished;
   private volatile ChannelHandlerContext ctx;
   private final CRC32 crc;
   private static final byte[] gzipHeader = new byte[]{31, -117, 8, 0, 0, 0, 0, 0, 0, 0};
   private boolean writeHeader;

   public JdkZlibEncoder() {
      this(6);
   }

   public JdkZlibEncoder(int var1) {
      this(ZlibWrapper.ZLIB, var1);
   }

   public JdkZlibEncoder(ZlibWrapper var1) {
      this(var1, 6);
   }

   public JdkZlibEncoder(ZlibWrapper var1, int var2) {
      this.encodeBuf = new byte[8192];
      this.crc = new CRC32();
      this.writeHeader = true;
      if (var2 >= 0 && var2 <= 9) {
         if (var1 == null) {
            throw new NullPointerException("wrapper");
         } else if (var1 == ZlibWrapper.ZLIB_OR_NONE) {
            throw new IllegalArgumentException("wrapper '" + ZlibWrapper.ZLIB_OR_NONE + "' is not " + "allowed for compression.");
         } else {
            this.wrapper = var1;
            this.deflater = new Deflater(var2, var1 != ZlibWrapper.ZLIB);
         }
      } else {
         throw new IllegalArgumentException("compressionLevel: " + var2 + " (expected: 0-9)");
      }
   }

   public JdkZlibEncoder(byte[] var1) {
      this(6, var1);
   }

   public JdkZlibEncoder(int var1, byte[] var2) {
      this.encodeBuf = new byte[8192];
      this.crc = new CRC32();
      this.writeHeader = true;
      if (var1 >= 0 && var1 <= 9) {
         if (var2 == null) {
            throw new NullPointerException("dictionary");
         } else {
            this.wrapper = ZlibWrapper.ZLIB;
            this.deflater = new Deflater(var1);
            this.deflater.setDictionary(var2);
         }
      } else {
         throw new IllegalArgumentException("compressionLevel: " + var1 + " (expected: 0-9)");
      }
   }

   public ChannelFuture close() {
      return this.close(this.ctx().newPromise());
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
            final JdkZlibEncoder this$0;

            {
               this.this$0 = var1;
               this.val$p = var2;
               this.val$promise = var3;
            }

            public void run() {
               ChannelFuture var1 = JdkZlibEncoder.access$100(this.this$0, JdkZlibEncoder.access$000(this.this$0), this.val$p);
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
      if (this.finished) {
         var3.writeBytes(var2);
      } else {
         byte[] var4 = new byte[var2.readableBytes()];
         var2.readBytes(var4);
         int var5 = (int)Math.ceil((double)var4.length * 1.001D) + 12;
         if (this.writeHeader) {
            this.writeHeader = false;
            switch(this.wrapper) {
            case GZIP:
               var3.ensureWritable(var5 + gzipHeader.length);
               var3.writeBytes(gzipHeader);
               break;
            case ZLIB:
               var3.ensureWritable(var5 + 2);
               break;
            default:
               var3.ensureWritable(var5);
            }
         } else {
            var3.ensureWritable(var5);
         }

         if (this.wrapper == ZlibWrapper.GZIP) {
            this.crc.update(var4);
         }

         this.deflater.setInput(var4);

         while(!this.deflater.needsInput()) {
            this.deflate(var3);
         }

      }
   }

   public void close(ChannelHandlerContext var1, ChannelPromise var2) throws Exception {
      ChannelFuture var3 = this.finishEncode(var1, var1.newPromise());
      var3.addListener(new ChannelFutureListener(this, var1, var2) {
         final ChannelHandlerContext val$ctx;
         final ChannelPromise val$promise;
         final JdkZlibEncoder this$0;

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
            final JdkZlibEncoder this$0;

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
         ByteBuf var3 = var1.alloc().buffer();
         if (this.writeHeader && this.wrapper == ZlibWrapper.GZIP) {
            this.writeHeader = false;
            var3.writeBytes(gzipHeader);
         }

         this.deflater.finish();

         while(!this.deflater.finished()) {
            this.deflate(var3);
         }

         if (this.wrapper == ZlibWrapper.GZIP) {
            int var4 = (int)this.crc.getValue();
            int var5 = this.deflater.getTotalIn();
            var3.writeByte(var4);
            var3.writeByte(var4 >>> 8);
            var3.writeByte(var4 >>> 16);
            var3.writeByte(var4 >>> 24);
            var3.writeByte(var5);
            var3.writeByte(var5 >>> 8);
            var3.writeByte(var5 >>> 16);
            var3.writeByte(var5 >>> 24);
         }

         this.deflater.end();
         return var1.writeAndFlush(var3, var2);
      }
   }

   private void deflate(ByteBuf var1) {
      int var2;
      do {
         var2 = this.deflater.deflate(this.encodeBuf, 0, this.encodeBuf.length, 2);
         var1.writeBytes((byte[])this.encodeBuf, 0, var2);
      } while(var2 > 0);

   }

   public void handlerAdded(ChannelHandlerContext var1) throws Exception {
      this.ctx = var1;
   }

   protected void encode(ChannelHandlerContext var1, Object var2, ByteBuf var3) throws Exception {
      this.encode(var1, (ByteBuf)var2, var3);
   }

   static ChannelHandlerContext access$000(JdkZlibEncoder var0) {
      return var0.ctx();
   }

   static ChannelFuture access$100(JdkZlibEncoder var0, ChannelHandlerContext var1, ChannelPromise var2) {
      return var0.finishEncode(var1, var2);
   }
}
