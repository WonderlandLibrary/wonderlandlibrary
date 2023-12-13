package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;

public class SpdyHeaderBlockRawDecoder extends SpdyHeaderBlockDecoder {
   private static final int LENGTH_FIELD_SIZE = 4;
   private final int version;
   private final int maxHeaderSize;
   private int headerSize;
   private int numHeaders = -1;

   public SpdyHeaderBlockRawDecoder(SpdyVersion var1, int var2) {
      if (var1 == null) {
         throw new NullPointerException("spdyVersion");
      } else {
         this.version = var1.getVersion();
         this.maxHeaderSize = var2;
      }
   }

   private static int readLengthField(ByteBuf var0) {
      int var1 = SpdyCodecUtil.getSignedInt(var0, var0.readerIndex());
      var0.skipBytes(4);
      return var1;
   }

   void decode(ByteBuf var1, SpdyHeadersFrame var2) throws Exception {
      if (var1 == null) {
         throw new NullPointerException("encoded");
      } else if (var2 == null) {
         throw new NullPointerException("frame");
      } else {
         if (this.numHeaders == -1) {
            if (var1.readableBytes() < 4) {
               return;
            }

            this.numHeaders = readLengthField(var1);
            if (this.numHeaders < 0) {
               var2.setInvalid();
               return;
            }
         }

         while(this.numHeaders > 0) {
            int var3 = this.headerSize;
            var1.markReaderIndex();
            if (var1.readableBytes() < 4) {
               var1.resetReaderIndex();
               return;
            }

            int var4 = readLengthField(var1);
            if (var4 <= 0) {
               var2.setInvalid();
               return;
            }

            var3 += var4;
            if (var3 > this.maxHeaderSize) {
               var2.setTruncated();
               return;
            }

            if (var1.readableBytes() < var4) {
               var1.resetReaderIndex();
               return;
            }

            byte[] var5 = new byte[var4];
            var1.readBytes(var5);
            String var6 = new String(var5, "UTF-8");
            if (var2.headers().contains(var6)) {
               var2.setInvalid();
               return;
            }

            if (var1.readableBytes() < 4) {
               var1.resetReaderIndex();
               return;
            }

            int var7 = readLengthField(var1);
            if (var7 < 0) {
               var2.setInvalid();
               return;
            }

            if (var7 == 0) {
               var2.headers().add(var6, (Object)"");
               --this.numHeaders;
               this.headerSize = var3;
            } else {
               var3 += var7;
               if (var3 > this.maxHeaderSize) {
                  var2.setTruncated();
                  return;
               }

               if (var1.readableBytes() < var7) {
                  var1.resetReaderIndex();
                  return;
               }

               byte[] var8 = new byte[var7];
               var1.readBytes(var8);
               int var9 = 0;

               for(int var10 = 0; var9 < var7; var10 = var9) {
                  while(var9 < var8.length && var8[var9] != 0) {
                     ++var9;
                  }

                  if (var9 < var8.length && var8[var9 + 1] == 0) {
                     var2.setInvalid();
                     return;
                  }

                  String var11 = new String(var8, var10, var9 - var10, "UTF-8");

                  try {
                     var2.headers().add(var6, (Object)var11);
                  } catch (IllegalArgumentException var13) {
                     var2.setInvalid();
                     return;
                  }

                  ++var9;
               }

               --this.numHeaders;
               this.headerSize = var3;
            }
         }

      }
   }

   void reset() {
      this.headerSize = 0;
      this.numHeaders = -1;
   }

   void end() {
   }
}
