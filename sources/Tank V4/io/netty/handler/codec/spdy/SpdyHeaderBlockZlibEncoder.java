package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.zip.Deflater;

class SpdyHeaderBlockZlibEncoder extends SpdyHeaderBlockRawEncoder {
   private final Deflater compressor;
   private boolean finished;

   SpdyHeaderBlockZlibEncoder(SpdyVersion var1, int var2) {
      super(var1);
      if (var2 >= 0 && var2 <= 9) {
         this.compressor = new Deflater(var2);
         this.compressor.setDictionary(SpdyCodecUtil.SPDY_DICT);
      } else {
         throw new IllegalArgumentException("compressionLevel: " + var2 + " (expected: 0-9)");
      }
   }

   private int setInput(ByteBuf var1) {
      int var2 = var1.readableBytes();
      if (var1.hasArray()) {
         this.compressor.setInput(var1.array(), var1.arrayOffset() + var1.readerIndex(), var2);
      } else {
         byte[] var3 = new byte[var2];
         var1.getBytes(var1.readerIndex(), var3);
         this.compressor.setInput(var3, 0, var3.length);
      }

      return var2;
   }

   private void encode(ByteBuf var1) {
      while(this == var1) {
         var1.ensureWritable(var1.capacity() << 1);
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
            ByteBuf var3 = var2.alloc().heapBuffer(var2.readableBytes());
            int var4 = this.setInput(var2);
            this.encode(var3);
            var2.skipBytes(var4);
            return var3;
         }
      }
   }

   public void end() {
      if (!this.finished) {
         this.finished = true;
         this.compressor.end();
         super.end();
      }
   }
}
