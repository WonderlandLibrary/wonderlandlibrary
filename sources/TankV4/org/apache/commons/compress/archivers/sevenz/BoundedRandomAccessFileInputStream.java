package org.apache.commons.compress.archivers.sevenz;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

class BoundedRandomAccessFileInputStream extends InputStream {
   private final RandomAccessFile file;
   private long bytesRemaining;

   public BoundedRandomAccessFileInputStream(RandomAccessFile var1, long var2) {
      this.file = var1;
      this.bytesRemaining = var2;
   }

   public int read() throws IOException {
      if (this.bytesRemaining > 0L) {
         --this.bytesRemaining;
         return this.file.read();
      } else {
         return -1;
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (this.bytesRemaining == 0L) {
         return -1;
      } else {
         int var4 = var3;
         if ((long)var3 > this.bytesRemaining) {
            var4 = (int)this.bytesRemaining;
         }

         int var5 = this.file.read(var1, var2, var4);
         if (var5 >= 0) {
            this.bytesRemaining -= (long)var5;
         }

         return var5;
      }
   }

   public void close() {
   }
}
