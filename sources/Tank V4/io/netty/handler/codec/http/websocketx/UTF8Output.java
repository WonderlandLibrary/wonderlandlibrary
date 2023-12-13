package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;

final class UTF8Output {
   private static final int UTF8_ACCEPT = 0;
   private static final int UTF8_REJECT = 12;
   private static final byte[] TYPES = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 11, 6, 6, 6, 5, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8};
   private static final byte[] STATES = new byte[]{0, 12, 24, 36, 60, 96, 84, 12, 12, 12, 48, 72, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 0, 12, 12, 12, 12, 12, 0, 12, 0, 12, 12, 12, 24, 12, 12, 12, 12, 12, 24, 12, 24, 12, 12, 12, 12, 12, 12, 12, 12, 12, 24, 12, 12, 12, 12, 12, 24, 12, 12, 12, 12, 12, 12, 12, 24, 12, 12, 12, 12, 12, 12, 12, 12, 12, 36, 12, 36, 12, 12, 12, 36, 12, 12, 12, 12, 12, 36, 12, 36, 12, 12, 12, 36, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12};
   private int state = 0;
   private int codep;
   private final StringBuilder stringBuilder;

   UTF8Output(ByteBuf var1) {
      this.stringBuilder = new StringBuilder(var1.readableBytes());
      this.write(var1);
   }

   public void write(ByteBuf var1) {
      for(int var2 = var1.readerIndex(); var2 < var1.writerIndex(); ++var2) {
         this.write(var1.getByte(var2));
      }

   }

   public void write(byte[] var1) {
      byte[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         byte var5 = var2[var4];
         this.write(var5);
      }

   }

   public void write(int var1) {
      byte var2 = TYPES[var1 & 255];
      this.codep = this.state != 0 ? var1 & 63 | this.codep << 6 : 255 >> var2 & var1;
      this.state = STATES[this.state + var2];
      if (this.state == 0) {
         this.stringBuilder.append((char)this.codep);
      } else if (this.state == 12) {
         throw new UTF8Exception("bytes are not UTF-8");
      }

   }

   public String toString() {
      if (this.state != 0) {
         throw new UTF8Exception("bytes are not UTF-8");
      } else {
         return this.stringBuilder.toString();
      }
   }
}
