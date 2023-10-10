package org.newdawn.slick.opengl;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class PNGImageData implements LoadableImageData {
   private static final byte[] SIGNATURE = new byte[]{-119, 80, 78, 71, 13, 10, 26, 10};
   private static final int IHDR = 1229472850;
   private static final int PLTE = 1347179589;
   private static final int tRNS = 1951551059;
   private static final int IDAT = 1229209940;
   private static final int IEND = 1229278788;
   private static final byte COLOR_GREYSCALE = 0;
   private static final byte COLOR_TRUECOLOR = 2;
   private static final byte COLOR_INDEXED = 3;
   private static final byte COLOR_GREYALPHA = 4;
   private static final byte COLOR_TRUEALPHA = 6;
   private InputStream input;
   private final CRC32 crc = new CRC32();
   private final byte[] buffer = new byte[4096];
   private int chunkLength;
   private int chunkType;
   private int chunkRemaining;
   private int width;
   private int height;
   private int colorType;
   private int bytesPerPixel;
   private byte[] palette;
   private byte[] paletteA;
   private byte[] transPixel;
   private int bitDepth;
   private int texWidth;
   private int texHeight;
   private ByteBuffer scratch;

   private void init(InputStream var1) throws IOException {
      this.input = var1;
      int var2 = var1.read(this.buffer, 0, SIGNATURE.length);
      if (var2 == SIGNATURE.length && this >= this.buffer) {
         this.openChunk(1229472850);
         this.readIHDR();
         this.closeChunk();

         while(true) {
            this.openChunk();
            switch(this.chunkType) {
            case 1229209940:
               return;
            case 1347179589:
               this.readPLTE();
               break;
            case 1951551059:
               this.readtRNS();
            }

            this.closeChunk();
         }
      } else {
         throw new IOException("Not a valid PNG file");
      }
   }

   public int getHeight() {
      return this.height;
   }

   public int getWidth() {
      return this.width;
   }

   private void decode(ByteBuffer param1, int param2, boolean param3) throws IOException {
      // $FF: Couldn't be decompiled
   }

   private void copyExpand(ByteBuffer param1, byte[] param2) {
      // $FF: Couldn't be decompiled
   }

   private void copy(ByteBuffer var1, byte[] var2) {
      var1.put(var2, 1, var2.length - 1);
   }

   private void unfilter(byte[] var1, byte[] var2) throws IOException {
      switch(var1[0]) {
      case 0:
         break;
      case 1:
         this.unfilterSub(var1);
         break;
      case 2:
         this.unfilterUp(var1, var2);
         break;
      case 3:
         this.unfilterAverage(var1, var2);
         break;
      case 4:
         this.unfilterPaeth(var1, var2);
         break;
      default:
         throw new IOException("invalide filter type in scanline: " + var1[0]);
      }

   }

   private void unfilterSub(byte[] var1) {
      int var2 = this.bytesPerPixel;
      int var3 = this.width * var2;

      for(int var4 = var2 + 1; var4 <= var3; ++var4) {
         var1[var4] += var1[var4 - var2];
      }

   }

   private void unfilterUp(byte[] var1, byte[] var2) {
      int var3 = this.bytesPerPixel;
      int var4 = this.width * var3;

      for(int var5 = 1; var5 <= var4; ++var5) {
         var1[var5] += var2[var5];
      }

   }

   private void unfilterAverage(byte[] var1, byte[] var2) {
      int var3 = this.bytesPerPixel;
      int var4 = this.width * var3;

      int var5;
      for(var5 = 1; var5 <= var3; ++var5) {
         var1[var5] += (byte)((var2[var5] & 255) >>> 1);
      }

      while(var5 <= var4) {
         var1[var5] += (byte)((var2[var5] & 255) + (var1[var5 - var3] & 255) >>> 1);
         ++var5;
      }

   }

   private void unfilterPaeth(byte[] var1, byte[] var2) {
      int var3 = this.bytesPerPixel;
      int var4 = this.width * var3;

      int var5;
      for(var5 = 1; var5 <= var3; ++var5) {
         var1[var5] += var2[var5];
      }

      while(var5 <= var4) {
         int var6 = var1[var5 - var3] & 255;
         int var7 = var2[var5] & 255;
         int var8 = var2[var5 - var3] & 255;
         int var9 = var6 + var7 - var8;
         int var10 = var9 - var6;
         if (var10 < 0) {
            var10 = -var10;
         }

         int var11 = var9 - var7;
         if (var11 < 0) {
            var11 = -var11;
         }

         int var12 = var9 - var8;
         if (var12 < 0) {
            var12 = -var12;
         }

         if (var10 <= var11 && var10 <= var12) {
            var8 = var6;
         } else if (var11 <= var12) {
            var8 = var7;
         }

         var1[var5] += (byte)var8;
         ++var5;
      }

   }

   private void readIHDR() throws IOException {
      this.checkChunkLength(13);
      this.readChunk(this.buffer, 0, 13);
      this.width = this.readInt(this.buffer, 0);
      this.height = this.readInt(this.buffer, 4);
      if (this.buffer[8] != 8) {
         throw new IOException("Unsupported bit depth");
      } else {
         this.colorType = this.buffer[9] & 255;
         switch(this.colorType) {
         case 0:
            this.bytesPerPixel = 1;
            break;
         case 1:
         case 4:
         case 5:
         default:
            throw new IOException("unsupported color format");
         case 2:
            this.bytesPerPixel = 3;
            break;
         case 3:
            this.bytesPerPixel = 1;
            break;
         case 6:
            this.bytesPerPixel = 4;
         }

         if (this.buffer[10] != 0) {
            throw new IOException("unsupported compression method");
         } else if (this.buffer[11] != 0) {
            throw new IOException("unsupported filtering method");
         } else if (this.buffer[12] != 0) {
            throw new IOException("unsupported interlace method");
         }
      }
   }

   private void readPLTE() throws IOException {
      int var1 = this.chunkLength / 3;
      if (var1 >= 1 && var1 <= 256 && this.chunkLength % 3 == 0) {
         this.palette = new byte[var1 * 3];
         this.readChunk(this.palette, 0, this.palette.length);
      } else {
         throw new IOException("PLTE chunk has wrong length");
      }
   }

   private void readtRNS() throws IOException {
      switch(this.colorType) {
      case 0:
         this.checkChunkLength(2);
         this.transPixel = new byte[2];
         this.readChunk(this.transPixel, 0, 2);
      case 1:
      default:
         break;
      case 2:
         this.checkChunkLength(6);
         this.transPixel = new byte[6];
         this.readChunk(this.transPixel, 0, 6);
         break;
      case 3:
         if (this.palette == null) {
            throw new IOException("tRNS chunk without PLTE chunk");
         }

         this.paletteA = new byte[this.palette.length / 3];

         for(int var1 = 0; var1 < this.paletteA.length; ++var1) {
            this.paletteA[var1] = -1;
         }

         this.readChunk(this.paletteA, 0, this.paletteA.length);
      }

   }

   private void closeChunk() throws IOException {
      if (this.chunkRemaining > 0) {
         this.input.skip((long)(this.chunkRemaining + 4));
      } else {
         this.readFully(this.buffer, 0, 4);
         int var1 = this.readInt(this.buffer, 0);
         int var2 = (int)this.crc.getValue();
         if (var2 != var1) {
            throw new IOException("Invalid CRC");
         }
      }

      this.chunkRemaining = 0;
      this.chunkLength = 0;
      this.chunkType = 0;
   }

   private void openChunk() throws IOException {
      this.readFully(this.buffer, 0, 8);
      this.chunkLength = this.readInt(this.buffer, 0);
      this.chunkType = this.readInt(this.buffer, 4);
      this.chunkRemaining = this.chunkLength;
      this.crc.reset();
      this.crc.update(this.buffer, 4, 4);
   }

   private void openChunk(int var1) throws IOException {
      this.openChunk();
      if (this.chunkType != var1) {
         throw new IOException("Expected chunk: " + Integer.toHexString(var1));
      }
   }

   private void checkChunkLength(int var1) throws IOException {
      if (this.chunkLength != var1) {
         throw new IOException("Chunk has wrong size");
      }
   }

   private int readChunk(byte[] var1, int var2, int var3) throws IOException {
      if (var3 > this.chunkRemaining) {
         var3 = this.chunkRemaining;
      }

      this.readFully(var1, var2, var3);
      this.crc.update(var1, var2, var3);
      this.chunkRemaining -= var3;
      return var3;
   }

   private void refillInflater(Inflater var1) throws IOException {
      while(this.chunkRemaining == 0) {
         this.closeChunk();
         this.openChunk(1229209940);
      }

      int var2 = this.readChunk(this.buffer, 0, this.buffer.length);
      var1.setInput(this.buffer, 0, var2);
   }

   private void readChunkUnzip(Inflater var1, byte[] var2, int var3, int var4) throws IOException {
      try {
         do {
            int var5 = var1.inflate(var2, var3, var4);
            if (var5 <= 0) {
               if (var1.finished()) {
                  throw new EOFException();
               }

               if (!var1.needsInput()) {
                  throw new IOException("Can't inflate " + var4 + " bytes");
               }

               this.refillInflater(var1);
            } else {
               var3 += var5;
               var4 -= var5;
            }
         } while(var4 > 0);

      } catch (DataFormatException var7) {
         IOException var6 = new IOException("inflate error");
         var6.initCause(var7);
         throw var6;
      }
   }

   private void readFully(byte[] var1, int var2, int var3) throws IOException {
      do {
         int var4 = this.input.read(var1, var2, var3);
         if (var4 < 0) {
            throw new EOFException();
         }

         var2 += var4;
         var3 -= var4;
      } while(var3 > 0);

   }

   private int readInt(byte[] var1, int var2) {
      return var1[var2] << 24 | (var1[var2 + 1] & 255) << 16 | (var1[var2 + 2] & 255) << 8 | var1[var2 + 3] & 255;
   }

   public int getDepth() {
      return this.bitDepth;
   }

   public ByteBuffer getImageBufferData() {
      return this.scratch;
   }

   public int getTexHeight() {
      return this.texHeight;
   }

   public int getTexWidth() {
      return this.texWidth;
   }

   public ByteBuffer loadImage(InputStream var1) throws IOException {
      return this.loadImage(var1, false, (int[])null);
   }

   public ByteBuffer loadImage(InputStream var1, boolean var2, int[] var3) throws IOException {
      return this.loadImage(var1, var2, false, var3);
   }

   public ByteBuffer loadImage(InputStream param1, boolean param2, boolean param3, int[] param4) throws IOException {
      // $FF: Couldn't be decompiled
   }

   private int toInt(byte var1) {
      return var1 < 0 ? 256 + var1 : var1;
   }

   private int get2Fold(int var1) {
      int var2;
      for(var2 = 2; var2 < var1; var2 *= 2) {
      }

      return var2;
   }

   public void configureEdging(boolean var1) {
   }
}
