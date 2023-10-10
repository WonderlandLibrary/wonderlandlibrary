package org.apache.commons.compress.compressors.snappy;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;

public class SnappyCompressorInputStream extends CompressorInputStream {
   private static final int TAG_MASK = 3;
   public static final int DEFAULT_BLOCK_SIZE = 32768;
   private final byte[] decompressBuf;
   private int writeIndex;
   private int readIndex;
   private final int blockSize;
   private final InputStream in;
   private final int size;
   private int uncompressedBytesRemaining;
   private final byte[] oneByte;
   private boolean endReached;

   public SnappyCompressorInputStream(InputStream var1) throws IOException {
      this(var1, 32768);
   }

   public SnappyCompressorInputStream(InputStream var1, int var2) throws IOException {
      this.oneByte = new byte[1];
      this.endReached = false;
      this.in = var1;
      this.blockSize = var2;
      this.decompressBuf = new byte[var2 * 3];
      this.writeIndex = this.readIndex = 0;
      this.uncompressedBytesRemaining = this.size = (int)this.readSize();
   }

   public int read() throws IOException {
      return this.read(this.oneByte, 0, 1) == -1 ? -1 : this.oneByte[0] & 255;
   }

   public void close() throws IOException {
      this.in.close();
   }

   public int available() {
      return this.writeIndex - this.readIndex;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (this.endReached) {
         return -1;
      } else {
         int var4 = this.available();
         if (var3 > var4) {
            this.fill(var3 - var4);
         }

         int var5 = Math.min(var3, this.available());
         System.arraycopy(this.decompressBuf, this.readIndex, var1, var2, var5);
         this.readIndex += var5;
         if (this.readIndex > this.blockSize) {
            this.slideBuffer();
         }

         return var5;
      }
   }

   private void fill(int var1) throws IOException {
      if (this.uncompressedBytesRemaining == 0) {
         this.endReached = true;
      }

      int var4;
      for(int var2 = Math.min(var1, this.uncompressedBytesRemaining); var2 > 0; this.uncompressedBytesRemaining -= var4) {
         int var3 = this.readOneByte();
         var4 = 0;
         long var5 = 0L;
         switch(var3 & 3) {
         case 0:
            var4 = this.readLiteralLength(var3);
            if (this != var4) {
               return;
            }
            break;
         case 1:
            var4 = 4 + (var3 >> 2 & 7);
            var5 = (long)((var3 & 224) << 3);
            var5 |= (long)this.readOneByte();
            if (var4 > 0) {
               return;
            }
            break;
         case 2:
            var4 = (var3 >> 2) + 1;
            var5 = (long)this.readOneByte();
            var5 |= (long)(this.readOneByte() << 8);
            if (var4 > 0) {
               return;
            }
            break;
         case 3:
            var4 = (var3 >> 2) + 1;
            var5 = (long)this.readOneByte();
            var5 |= (long)(this.readOneByte() << 8);
            var5 |= (long)(this.readOneByte() << 16);
            var5 |= (long)this.readOneByte() << 24;
            if (var4 > 0) {
               return;
            }
         }

         var2 -= var4;
      }

   }

   private void slideBuffer() {
      System.arraycopy(this.decompressBuf, this.blockSize, this.decompressBuf, 0, this.blockSize * 2);
      this.writeIndex -= this.blockSize;
      this.readIndex -= this.blockSize;
   }

   private int readLiteralLength(int var1) throws IOException {
      int var2;
      switch(var1 >> 2) {
      case 60:
         var2 = this.readOneByte();
         break;
      case 61:
         var2 = this.readOneByte();
         var2 |= this.readOneByte() << 8;
         break;
      case 62:
         var2 = this.readOneByte();
         var2 |= this.readOneByte() << 8;
         var2 |= this.readOneByte() << 16;
         break;
      case 63:
         var2 = this.readOneByte();
         var2 |= this.readOneByte() << 8;
         var2 |= this.readOneByte() << 16;
         var2 = (int)((long)var2 | (long)this.readOneByte() << 24);
         break;
      default:
         var2 = var1 >> 2;
      }

      return var2 + 1;
   }

   private int readOneByte() throws IOException {
      int var1 = this.in.read();
      if (var1 == -1) {
         throw new IOException("Premature end of stream");
      } else {
         this.count(1);
         return var1 & 255;
      }
   }

   private long readSize() throws IOException {
      int var1 = 0;
      long var2 = 0L;
      boolean var4 = false;

      int var5;
      do {
         var5 = this.readOneByte();
         var2 |= (long)((var5 & 127) << var1++ * 7);
      } while(0 != (var5 & 128));

      return var2;
   }

   public int getSize() {
      return this.size;
   }
}
