package com.sun.webkit;

import java.io.InputStream;

public final class SimpleSharedBufferInputStream extends InputStream {
   private final SharedBuffer sharedBuffer;
   private long position;

   public SimpleSharedBufferInputStream(SharedBuffer var1) {
      if (var1 == null) {
         throw new NullPointerException("sharedBuffer is null");
      } else {
         this.sharedBuffer = var1;
      }
   }

   public int read() {
      byte[] var1 = new byte[1];
      int var2 = this.sharedBuffer.getSomeData(this.position, var1, 0, 1);
      if (var2 != 0) {
         ++this.position;
         return var1[0] & 255;
      } else {
         return -1;
      }
   }

   public int read(byte[] var1, int var2, int var3) {
      if (var1 == null) {
         throw new NullPointerException("b is null");
      } else if (var2 < 0) {
         throw new IndexOutOfBoundsException("off is negative");
      } else if (var3 < 0) {
         throw new IndexOutOfBoundsException("len is negative");
      } else if (var3 > var1.length - var2) {
         throw new IndexOutOfBoundsException("len is greater than b.length - off");
      } else if (var3 == 0) {
         return 0;
      } else {
         int var4 = this.sharedBuffer.getSomeData(this.position, var1, var2, var3);
         if (var4 != 0) {
            this.position += (long)var4;
            return var4;
         } else {
            return -1;
         }
      }
   }

   public long skip(long var1) {
      long var3 = this.sharedBuffer.size() - this.position;
      if (var1 < var3) {
         var3 = var1 < 0L ? 0L : var1;
      }

      this.position += var3;
      return var3;
   }

   public int available() {
      return (int)Math.min(this.sharedBuffer.size() - this.position, 2147483647L);
   }
}
