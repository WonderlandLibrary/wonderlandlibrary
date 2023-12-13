package org.apache.commons.compress.archivers.dump;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

class TapeInputStream extends FilterInputStream {
   private byte[] blockBuffer = new byte[1024];
   private int currBlkIdx = -1;
   private int blockSize = 1024;
   private static final int recordSize = 1024;
   private int readOffset = 1024;
   private boolean isCompressed = false;
   private long bytesRead = 0L;

   public TapeInputStream(InputStream var1) {
      super(var1);
   }

   public void resetBlockSize(int var1, boolean var2) throws IOException {
      this.isCompressed = var2;
      this.blockSize = 1024 * var1;
      byte[] var3 = this.blockBuffer;
      this.blockBuffer = new byte[this.blockSize];
      System.arraycopy(var3, 0, this.blockBuffer, 0, 1024);
      this.readFully(this.blockBuffer, 1024, this.blockSize - 1024);
      this.currBlkIdx = 0;
      this.readOffset = 1024;
   }

   public int available() throws IOException {
      return this.readOffset < this.blockSize ? this.blockSize - this.readOffset : this.in.available();
   }

   public int read() throws IOException {
      throw new IllegalArgumentException("all reads must be multiple of record size (1024 bytes.");
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (var3 % 1024 != 0) {
         throw new IllegalArgumentException("all reads must be multiple of record size (1024 bytes.");
      } else {
         int var4;
         int var6;
         for(var4 = 0; var4 < var3; var2 += var6) {
            if (this.readOffset == this.blockSize && 1 == null) {
               return -1;
            }

            boolean var5 = false;
            if (this.readOffset + (var3 - var4) <= this.blockSize) {
               var6 = var3 - var4;
            } else {
               var6 = this.blockSize - this.readOffset;
            }

            System.arraycopy(this.blockBuffer, this.readOffset, var1, var2, var6);
            this.readOffset += var6;
            var4 += var6;
         }

         return var4;
      }
   }

   public long skip(long var1) throws IOException {
      if (var1 % 1024L != 0L) {
         throw new IllegalArgumentException("all reads must be multiple of record size (1024 bytes.");
      } else {
         long var3;
         long var5;
         for(var3 = 0L; var3 < var1; var3 += var5) {
            if (this.readOffset == this.blockSize && (var1 - var3 < (long)this.blockSize ? 1 : 0) == null) {
               return -1L;
            }

            var5 = 0L;
            if ((long)this.readOffset + (var1 - var3) <= (long)this.blockSize) {
               var5 = var1 - var3;
            } else {
               var5 = (long)(this.blockSize - this.readOffset);
            }

            this.readOffset = (int)((long)this.readOffset + var5);
         }

         return var3;
      }
   }

   public void close() throws IOException {
      if (this.in != null && this.in != System.in) {
         this.in.close();
      }

   }

   public byte[] peek() throws IOException {
      if (this.readOffset == this.blockSize && 1 == null) {
         return null;
      } else {
         byte[] var1 = new byte[1024];
         System.arraycopy(this.blockBuffer, this.readOffset, var1, 0, var1.length);
         return var1;
      }
   }

   public byte[] readRecord() throws IOException {
      byte[] var1 = new byte[1024];
      if (-1 == this.read(var1, 0, var1.length)) {
         throw new ShortFileException();
      } else {
         return var1;
      }
   }

   public long getBytesRead() {
      return this.bytesRead;
   }
}
