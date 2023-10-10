package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.Iterator;
import java.util.Set;

public class SpdyHeaderBlockRawEncoder extends SpdyHeaderBlockEncoder {
   private final int version;

   public SpdyHeaderBlockRawEncoder(SpdyVersion var1) {
      if (var1 == null) {
         throw new NullPointerException("version");
      } else {
         this.version = var1.getVersion();
      }
   }

   private void setLengthField(ByteBuf var1, int var2, int var3) {
      if (this.version < 3) {
         var1.setShort(var2, var3);
      } else {
         var1.setInt(var2, var3);
      }

   }

   private void writeLengthField(ByteBuf var1, int var2) {
      if (this.version < 3) {
         var1.writeShort(var2);
      } else {
         var1.writeInt(var2);
      }

   }

   public ByteBuf encode(SpdyHeadersFrame var1) throws Exception {
      Set var2 = var1.headers().names();
      int var3 = var2.size();
      if (var3 == 0) {
         return Unpooled.EMPTY_BUFFER;
      } else if (var3 > 65535) {
         throw new IllegalArgumentException("header block contains too many headers");
      } else {
         ByteBuf var4 = Unpooled.buffer();
         this.writeLengthField(var4, var3);
         Iterator var5 = var2.iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            byte[] var7 = var6.getBytes("UTF-8");
            this.writeLengthField(var4, var7.length);
            var4.writeBytes(var7);
            int var8 = var4.writerIndex();
            int var9 = 0;
            this.writeLengthField(var4, var9);
            Iterator var10 = var1.headers().getAll(var6).iterator();

            while(var10.hasNext()) {
               String var11 = (String)var10.next();
               byte[] var12 = var11.getBytes("UTF-8");
               if (var12.length > 0) {
                  var4.writeBytes(var12);
                  var4.writeByte(0);
                  var9 += var12.length + 1;
               }
            }

            if (var9 == 0) {
               if (this.version < 3) {
                  throw new IllegalArgumentException("header value cannot be empty: " + var6);
               }
            } else {
               --var9;
            }

            if (var9 > 65535) {
               throw new IllegalArgumentException("header exceeds allowable length: " + var6);
            }

            if (var9 > 0) {
               this.setLengthField(var4, var8, var9);
               var4.writerIndex(var4.writerIndex() - 1);
            }
         }

         return var4;
      }
   }

   void end() {
   }
}
