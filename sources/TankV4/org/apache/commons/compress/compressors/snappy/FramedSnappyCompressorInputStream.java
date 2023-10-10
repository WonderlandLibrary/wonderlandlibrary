package org.apache.commons.compress.compressors.snappy;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.utils.BoundedInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class FramedSnappyCompressorInputStream extends CompressorInputStream {
   static final long MASK_OFFSET = 2726488792L;
   private static final int STREAM_IDENTIFIER_TYPE = 255;
   private static final int COMPRESSED_CHUNK_TYPE = 0;
   private static final int UNCOMPRESSED_CHUNK_TYPE = 1;
   private static final int PADDING_CHUNK_TYPE = 254;
   private static final int MIN_UNSKIPPABLE_TYPE = 2;
   private static final int MAX_UNSKIPPABLE_TYPE = 127;
   private static final int MAX_SKIPPABLE_TYPE = 253;
   private static final byte[] SZ_SIGNATURE = new byte[]{-1, 6, 0, 0, 115, 78, 97, 80, 112, 89};
   private final PushbackInputStream in;
   private SnappyCompressorInputStream currentCompressedChunk;
   private final byte[] oneByte = new byte[1];
   private boolean endReached;
   private boolean inUncompressedChunk;
   private int uncompressedBytesRemaining;
   private long expectedChecksum = -1L;
   private final PureJavaCrc32C checksum = new PureJavaCrc32C();

   public FramedSnappyCompressorInputStream(InputStream var1) throws IOException {
      this.in = new PushbackInputStream(var1, 1);
      this.readStreamIdentifier();
   }

   public int read() throws IOException {
      return this.read(this.oneByte, 0, 1) == -1 ? -1 : this.oneByte[0] & 255;
   }

   public void close() throws IOException {
      if (this.currentCompressedChunk != null) {
         this.currentCompressedChunk.close();
         this.currentCompressedChunk = null;
      }

      this.in.close();
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4 = this.readOnce(var1, var2, var3);
      if (var4 == -1) {
         this.readNextBlock();
         if (this.endReached) {
            return -1;
         }

         var4 = this.readOnce(var1, var2, var3);
      }

      return var4;
   }

   public int available() throws IOException {
      if (this.inUncompressedChunk) {
         return Math.min(this.uncompressedBytesRemaining, this.in.available());
      } else {
         return this.currentCompressedChunk != null ? this.currentCompressedChunk.available() : 0;
      }
   }

   private int readOnce(byte[] var1, int var2, int var3) throws IOException {
      int var4 = -1;
      if (this.inUncompressedChunk) {
         int var5 = Math.min(this.uncompressedBytesRemaining, var3);
         if (var5 == 0) {
            return -1;
         }

         var4 = this.in.read(var1, var2, var5);
         if (var4 != -1) {
            this.uncompressedBytesRemaining -= var4;
            this.count(var4);
         }
      } else if (this.currentCompressedChunk != null) {
         long var7 = this.currentCompressedChunk.getBytesRead();
         var4 = this.currentCompressedChunk.read(var1, var2, var3);
         if (var4 == -1) {
            this.currentCompressedChunk.close();
            this.currentCompressedChunk = null;
         } else {
            this.count(this.currentCompressedChunk.getBytesRead() - var7);
         }
      }

      if (var4 > 0) {
         this.checksum.update(var1, var2, var4);
      }

      return var4;
   }

   private void readNextBlock() throws IOException {
      this.verifyLastChecksumAndReset();
      this.inUncompressedChunk = false;
      int var1 = this.readOneByte();
      if (var1 == -1) {
         this.endReached = true;
      } else if (var1 == 255) {
         this.in.unread(var1);
         this.pushedBackBytes(1L);
         this.readStreamIdentifier();
         this.readNextBlock();
      } else if (var1 != 254 && (var1 <= 127 || var1 > 253)) {
         if (var1 >= 2 && var1 <= 127) {
            throw new IOException("unskippable chunk with type " + var1 + " (hex " + Integer.toHexString(var1) + ")" + " detected.");
         }

         if (var1 == 1) {
            this.inUncompressedChunk = true;
            this.uncompressedBytesRemaining = this.readSize() - 4;
            this.expectedChecksum = unmask(this.readCrc());
         } else {
            if (var1 != 0) {
               throw new IOException("unknown chunk type " + var1 + " detected.");
            }

            long var2 = (long)(this.readSize() - 4);
            this.expectedChecksum = unmask(this.readCrc());
            this.currentCompressedChunk = new SnappyCompressorInputStream(new BoundedInputStream(this.in, var2));
            this.count(this.currentCompressedChunk.getBytesRead());
         }
      } else {
         this.skipBlock();
         this.readNextBlock();
      }

   }

   private long readCrc() throws IOException {
      byte[] var1 = new byte[4];
      int var2 = IOUtils.readFully(this.in, var1);
      this.count(var2);
      if (var2 != 4) {
         throw new IOException("premature end of stream");
      } else {
         long var3 = 0L;

         for(int var5 = 0; var5 < 4; ++var5) {
            var3 |= ((long)var1[var5] & 255L) << 8 * var5;
         }

         return var3;
      }
   }

   static long unmask(long var0) {
      var0 -= 2726488792L;
      var0 &= 4294967295L;
      return (var0 >> 17 | var0 << 15) & 4294967295L;
   }

   private int readSize() throws IOException {
      boolean var1 = false;
      int var2 = 0;

      for(int var3 = 0; var3 < 3; ++var3) {
         int var4 = this.readOneByte();
         if (var4 == -1) {
            throw new IOException("premature end of stream");
         }

         var2 |= var4 << var3 * 8;
      }

      return var2;
   }

   private void skipBlock() throws IOException {
      int var1 = this.readSize();
      long var2 = IOUtils.skip(this.in, (long)var1);
      this.count(var2);
      if (var2 != (long)var1) {
         throw new IOException("premature end of stream");
      }
   }

   private void readStreamIdentifier() throws IOException {
      byte[] var1 = new byte[10];
      int var2 = IOUtils.readFully(this.in, var1);
      this.count(var2);
      if (10 != var2 || var1 < 10) {
         throw new IOException("Not a framed Snappy stream");
      }
   }

   private int readOneByte() throws IOException {
      int var1 = this.in.read();
      if (var1 != -1) {
         this.count(1);
         return var1 & 255;
      } else {
         return -1;
      }
   }

   private void verifyLastChecksumAndReset() throws IOException {
      if (this.expectedChecksum >= 0L && this.expectedChecksum != this.checksum.getValue()) {
         throw new IOException("Checksum verification failed");
      } else {
         this.expectedChecksum = -1L;
         this.checksum.reset();
      }
   }
}
