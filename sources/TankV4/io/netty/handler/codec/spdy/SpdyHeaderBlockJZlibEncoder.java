package io.netty.handler.codec.spdy;

import com.jcraft.jzlib.Deflater;
import com.jcraft.jzlib.JZlib;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.compression.CompressionException;

class SpdyHeaderBlockJZlibEncoder extends SpdyHeaderBlockRawEncoder {
   private final Deflater z = new Deflater();
   private boolean finished;

   SpdyHeaderBlockJZlibEncoder(SpdyVersion var1, int var2, int var3, int var4) {
      super(var1);
      if (var2 >= 0 && var2 <= 9) {
         if (var3 >= 9 && var3 <= 15) {
            if (var4 >= 1 && var4 <= 9) {
               int var5 = this.z.deflateInit(var2, var3, var4, JZlib.W_ZLIB);
               if (var5 != 0) {
                  throw new CompressionException("failed to initialize an SPDY header block deflater: " + var5);
               } else {
                  var5 = this.z.deflateSetDictionary(SpdyCodecUtil.SPDY_DICT, SpdyCodecUtil.SPDY_DICT.length);
                  if (var5 != 0) {
                     throw new CompressionException("failed to set the SPDY dictionary: " + var5);
                  }
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

   private void setInput(ByteBuf var1) {
      byte[] var2 = new byte[var1.readableBytes()];
      var1.readBytes(var2);
      this.z.next_in = var2;
      this.z.next_in_index = 0;
      this.z.avail_in = var2.length;
   }

   private void encode(ByteBuf var1) {
      byte[] var2 = new byte[(int)Math.ceil((double)this.z.next_in.length * 1.001D) + 12];
      this.z.next_out = var2;
      this.z.next_out_index = 0;
      this.z.avail_out = var2.length;
      int var3 = this.z.deflate(2);
      if (var3 != 0) {
         throw new CompressionException("compression failure: " + var3);
      } else {
         if (this.z.next_out_index != 0) {
            var1.writeBytes((byte[])var2, 0, this.z.next_out_index);
         }

         this.z.next_in = null;
         this.z.next_out = null;
      }
   }

   public ByteBuf encode(SpdyHeadersFrame var1) throws Exception {
      if (var1 == null) {
         throw new IllegalArgumentException("frame");
      } else if (this.finished) {
         return Unpooled.EMPTY_BUFFER;
      } else {
         ByteBuf var2 = super.encode(var1);
         if (var2.readableBytes() == 0) {
            return Unpooled.EMPTY_BUFFER;
         } else {
            ByteBuf var3 = var2.alloc().buffer();
            this.setInput(var2);
            this.encode(var3);
            return var3;
         }
      }
   }

   public void end() {
      if (!this.finished) {
         this.finished = true;
         this.z.deflateEnd();
         this.z.next_in = null;
         this.z.next_out = null;
      }
   }
}
