package com.sun.javafx.iio.png;

import com.sun.javafx.iio.common.ImageTools;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class PNGIDATChunkInputStream extends InputStream {
   static final int IDAT_TYPE = 1229209940;
   private DataInputStream source;
   private int numBytesAvailable = 0;
   private boolean foundAllIDATChunks = false;
   private int nextChunkLength = 0;
   private int nextChunkType = 0;

   PNGIDATChunkInputStream(DataInputStream var1, int var2) throws IOException {
      if (var2 < 0) {
         throw new IOException("Invalid chunk length");
      } else {
         this.source = var1;
         this.numBytesAvailable = var2;
      }
   }

   private void nextChunk() throws IOException {
      if (!this.foundAllIDATChunks) {
         ImageTools.skipFully(this.source, 4L);
         int var1 = this.source.readInt();
         if (var1 < 0) {
            throw new IOException("Invalid chunk length");
         }

         int var2 = this.source.readInt();
         if (var2 == 1229209940) {
            this.numBytesAvailable += var1;
         } else {
            this.foundAllIDATChunks = true;
            this.nextChunkLength = var1;
            this.nextChunkType = var2;
         }
      }

   }

   boolean isFoundAllIDATChunks() {
      return this.foundAllIDATChunks;
   }

   int getNextChunkLength() {
      return this.nextChunkLength;
   }

   int getNextChunkType() {
      return this.nextChunkType;
   }

   public int read() throws IOException {
      if (this.numBytesAvailable == 0) {
         this.nextChunk();
      }

      if (this.numBytesAvailable == 0) {
         return -1;
      } else {
         --this.numBytesAvailable;
         return this.source.read();
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (this.numBytesAvailable == 0) {
         this.nextChunk();
         if (this.numBytesAvailable == 0) {
            return -1;
         }
      }

      int var4 = 0;

      while(this.numBytesAvailable > 0 && var3 > 0) {
         int var5 = var3 < this.numBytesAvailable ? var3 : this.numBytesAvailable;
         int var6 = this.source.read(var1, var2, var5);
         if (var6 == -1) {
            throw new EOFException();
         }

         this.numBytesAvailable -= var6;
         var2 += var6;
         var3 -= var6;
         var4 += var6;
         if (this.numBytesAvailable == 0 && var3 > 0) {
            this.nextChunk();
         }
      }

      return var4;
   }
}
