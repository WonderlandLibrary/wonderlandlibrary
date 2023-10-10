package io.netty.handler.codec.compression;

import com.jcraft.jzlib.Inflater;
import com.jcraft.jzlib.JZlib;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;

public class JZlibDecoder extends ZlibDecoder {
   private final Inflater z;
   private byte[] dictionary;
   private volatile boolean finished;

   public JZlibDecoder() {
      this(ZlibWrapper.ZLIB);
   }

   public JZlibDecoder(ZlibWrapper var1) {
      this.z = new Inflater();
      if (var1 == null) {
         throw new NullPointerException("wrapper");
      } else {
         int var2 = this.z.init(ZlibUtil.convertWrapperType(var1));
         if (var2 != 0) {
            ZlibUtil.fail(this.z, "initialization failure", var2);
         }

      }
   }

   public JZlibDecoder(byte[] var1) {
      this.z = new Inflater();
      if (var1 == null) {
         throw new NullPointerException("dictionary");
      } else {
         this.dictionary = var1;
         int var2 = this.z.inflateInit(JZlib.W_ZLIB);
         if (var2 != 0) {
            ZlibUtil.fail(this.z, "initialization failure", var2);
         }

      }
   }

   public boolean isClosed() {
      return this.finished;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      if (this.finished) {
         var2.skipBytes(var2.readableBytes());
      } else if (var2.isReadable()) {
         int var4 = var2.readableBytes();
         this.z.avail_in = var4;
         if (var2.hasArray()) {
            this.z.next_in = var2.array();
            this.z.next_in_index = var2.arrayOffset() + var2.readerIndex();
         } else {
            byte[] var5 = new byte[var4];
            var2.getBytes(var2.readerIndex(), var5);
            this.z.next_in = var5;
            this.z.next_in_index = 0;
         }

         int var13 = this.z.next_in_index;
         int var6 = var4 << 1;
         ByteBuf var7 = var1.alloc().heapBuffer(var6);

         label50:
         while(true) {
            this.z.avail_out = var6;
            var7.ensureWritable(var6);
            this.z.next_out = var7.array();
            this.z.next_out_index = var7.arrayOffset() + var7.writerIndex();
            int var8 = this.z.next_out_index;
            int var9 = this.z.inflate(2);
            int var10 = this.z.next_out_index - var8;
            if (var10 > 0) {
               var7.writerIndex(var7.writerIndex() + var10);
            }

            switch(var9) {
            case -5:
               if (this.z.avail_in <= 0) {
                  break label50;
               }
               break;
            case -4:
            case -3:
            case -2:
            case -1:
            default:
               ZlibUtil.fail(this.z, "decompression failure", var9);
            case 0:
               break;
            case 1:
               this.finished = true;
               this.z.inflateEnd();
               break label50;
            case 2:
               if (this.dictionary == null) {
                  ZlibUtil.fail(this.z, "decompression failure", var9);
               } else {
                  var9 = this.z.inflateSetDictionary(this.dictionary, this.dictionary.length);
                  if (var9 != 0) {
                     ZlibUtil.fail(this.z, "failed to set the dictionary", var9);
                  }
               }
            }
         }

         var2.skipBytes(this.z.next_in_index - var13);
         if (var7.isReadable()) {
            var3.add(var7);
         } else {
            var7.release();
         }

         this.z.next_in = null;
         this.z.next_out = null;
      }
   }
}
