package com.sun.javafx.iio.gif;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.common.ImageLoaderImpl;
import com.sun.javafx.iio.common.ImageTools;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class GIFImageLoader2 extends ImageLoaderImpl {
   static final byte[] FILE_SIG87 = new byte[]{71, 73, 70, 56, 55, 97};
   static final byte[] FILE_SIG89 = new byte[]{71, 73, 70, 56, 57, 97};
   static final byte[] NETSCAPE_SIG = new byte[]{78, 69, 84, 83, 67, 65, 80, 69, 50, 46, 48};
   static final int DEFAULT_FPS = 25;
   InputStream stream = null;
   int screenW;
   int screenH;
   int bgColor;
   byte[][] globalPalette;
   byte[] image;
   int loopCount = 1;

   public GIFImageLoader2(InputStream var1) throws IOException {
      super(GIFDescriptor.getInstance());
      this.stream = var1;
      this.readGlobalHeader();
   }

   private void readGlobalHeader() throws IOException {
      byte[] var1 = this.readBytes(new byte[6]);
      if (!Arrays.equals(FILE_SIG87, var1) && !Arrays.equals(FILE_SIG89, var1)) {
         throw new IOException("Bad GIF signature!");
      } else {
         this.screenW = this.readShort();
         this.screenH = this.readShort();
         int var2 = this.readByte();
         this.bgColor = this.readByte();
         int var3 = this.readByte();
         if ((var2 & 128) != 0) {
            this.globalPalette = this.readPalete(2 << (var2 & 7), -1);
         }

         this.image = new byte[this.screenW * this.screenH * 4];
      }
   }

   private byte[][] readPalete(int var1, int var2) throws IOException {
      byte[][] var3 = new byte[4][var1];
      byte[] var4 = this.readBytes(new byte[var1 * 3]);
      int var5 = 0;

      for(int var6 = 0; var5 != var1; ++var5) {
         for(int var7 = 0; var7 != 3; ++var7) {
            var3[var7][var5] = var4[var6++];
         }

         var3[3][var5] = (byte)(var5 == var2 ? 0 : -1);
      }

      return var3;
   }

   private void consumeAnExtension() throws IOException {
      for(int var1 = this.readByte(); var1 != 0; var1 = this.readByte()) {
         this.skipBytes(var1);
      }

   }

   private void readAppExtension() throws IOException {
      int var1 = this.readByte();
      byte[] var2 = this.readBytes(new byte[var1]);
      if (Arrays.equals(NETSCAPE_SIG, var2)) {
         for(int var3 = this.readByte(); var3 != 0; var3 = this.readByte()) {
            byte[] var4 = this.readBytes(new byte[var3]);
            byte var5 = var4[0];
            if (var3 == 3 && var5 == 1) {
               this.loopCount = var4[1] & 255 | (var4[2] & 255) << 8;
            }
         }
      } else {
         this.consumeAnExtension();
      }

   }

   private int readControlCode() throws IOException {
      int var1 = this.readByte();
      int var2 = this.readByte();
      int var3 = this.readShort();
      int var4 = this.readByte();
      if (var1 == 4 && this.readByte() == 0) {
         return ((var2 & 31) << 24) + (var4 << 16) + var3;
      } else {
         throw new IOException("Bad GIF GraphicControlExtension");
      }
   }

   private int waitForImageFrame() throws IOException {
      int var1 = 0;

      while(true) {
         int var2 = this.stream.read();
         switch (var2) {
            case -1:
            case 59:
               return -1;
            case 33:
               switch (this.readByte()) {
                  case 249:
                     var1 = this.readControlCode();
                     continue;
                  case 255:
                     this.readAppExtension();
                     continue;
                  default:
                     this.consumeAnExtension();
                     continue;
               }
            case 44:
               return var1;
            default:
               throw new IOException("Unexpected GIF control characher 0x" + String.format("%02X", var2));
         }
      }
   }

   private void decodeImage(byte[] var1, int var2, int var3, int[] var4) throws IOException {
      LZWDecoder var5 = new LZWDecoder();
      byte[] var6 = var5.getString();
      int var7 = 0;
      int var8 = 0;
      int var9 = var2;

      while(true) {
         int var10 = var5.readString();
         if (var10 == -1) {
            var5.waitForTerminator();
            return;
         }

         int var11 = 0;

         while(var11 != var10) {
            int var12 = var9 < var10 - var11 ? var9 : var10 - var11;
            System.arraycopy(var6, var11, var1, var8, var12);
            var8 += var12;
            var11 += var12;
            if ((var9 -= var12) == 0) {
               ++var7;
               if (var7 == var3) {
                  var5.waitForTerminator();
                  return;
               }

               int var13 = var4 == null ? var7 : var4[var7];
               var8 = var13 * var2;
               var9 = var2;
            }
         }
      }
   }

   private int[] computeInterlaceReIndex(int var1) {
      int[] var2 = new int[var1];
      int var3 = 0;

      int var4;
      for(var4 = 0; var4 < var1; var4 += 8) {
         var2[var3++] = var4;
      }

      for(var4 = 4; var4 < var1; var4 += 8) {
         var2[var3++] = var4;
      }

      for(var4 = 2; var4 < var1; var4 += 4) {
         var2[var3++] = var4;
      }

      for(var4 = 1; var4 < var1; var4 += 2) {
         var2[var3++] = var4;
      }

      return var2;
   }

   public ImageFrame load(int var1, int var2, int var3, boolean var4, boolean var5) throws IOException {
      int var6 = this.waitForImageFrame();
      if (var6 < 0) {
         return null;
      } else {
         int var7 = this.readShort();
         int var8 = this.readShort();
         int var9 = this.readShort();
         int var10 = this.readShort();
         if (var7 + var9 <= this.screenW && var8 + var10 <= this.screenH) {
            int var11 = this.readByte();
            boolean var12 = (var6 >>> 24 & 1) == 1;
            int var13 = var12 ? var6 >>> 16 & 255 : -1;
            boolean var14 = (var11 & 128) != 0;
            boolean var15 = (var11 & 64) != 0;
            byte[][] var16 = var14 ? this.readPalete(2 << (var11 & 7), var13) : this.globalPalette;
            int[] var17 = ImageTools.computeDimensions(this.screenW, this.screenH, var2, var3, var4);
            var2 = var17[0];
            var3 = var17[1];
            ImageMetadata var18 = this.updateMetadata(var2, var3, var6 & '\uffff');
            int var19 = var6 >>> 26 & 7;
            byte[] var20 = new byte[var9 * var10];
            this.decodeImage(var20, var9, var10, var15 ? this.computeInterlaceReIndex(var10) : null);
            ByteBuffer var21 = this.decodePalette(var20, var16, var13, var7, var8, var9, var10, var19);
            if (this.screenW != var2 || this.screenH != var3) {
               var21 = ImageTools.scaleImage(var21, this.screenW, this.screenH, 4, var2, var3, var5);
            }

            return new ImageFrame(ImageStorage.ImageType.RGBA, var21, var2, var3, var2 * 4, (byte[][])null, var18);
         } else {
            throw new IOException("Wrong GIF image frame size");
         }
      }
   }

   private int readByte() throws IOException {
      int var1 = this.stream.read();
      if (var1 < 0) {
         throw new EOFException();
      } else {
         return var1;
      }
   }

   private int readShort() throws IOException {
      int var1 = this.readByte();
      int var2 = this.readByte();
      return var1 + (var2 << 8);
   }

   private byte[] readBytes(byte[] var1) throws IOException {
      return this.readBytes(var1, 0, var1.length);
   }

   private byte[] readBytes(byte[] var1, int var2, int var3) throws IOException {
      while(var3 > 0) {
         int var4 = this.stream.read(var1, var2, var3);
         if (var4 < 0) {
            throw new EOFException();
         }

         var2 += var4;
         var3 -= var4;
      }

      return var1;
   }

   private void skipBytes(int var1) throws IOException {
      ImageTools.skipFully(this.stream, (long)var1);
   }

   public void dispose() {
   }

   private void restoreToBackground(byte[] var1, int var2, int var3, int var4, int var5) {
      for(int var6 = 0; var6 != var5; ++var6) {
         int var7 = ((var3 + var6) * this.screenW + var2) * 4;

         for(int var8 = 0; var8 != var4; ++var8) {
            var1[var7 + 3] = 0;
            var7 += 4;
         }
      }

   }

   private ByteBuffer decodePalette(byte[] var1, byte[][] var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      byte[] var9 = var8 == 3 ? (byte[])this.image.clone() : this.image;

      for(int var10 = 0; var10 != var7; ++var10) {
         int var11 = ((var5 + var10) * this.screenW + var4) * 4;
         int var12 = var10 * var6;
         int var13;
         int var14;
         if (var3 < 0) {
            for(var13 = 0; var13 != var6; ++var13) {
               var14 = 255 & var1[var12 + var13];
               var9[var11 + 0] = var2[0][var14];
               var9[var11 + 1] = var2[1][var14];
               var9[var11 + 2] = var2[2][var14];
               var9[var11 + 3] = var2[3][var14];
               var11 += 4;
            }
         } else {
            for(var13 = 0; var13 != var6; ++var13) {
               var14 = 255 & var1[var12 + var13];
               if (var14 != var3) {
                  var9[var11 + 0] = var2[0][var14];
                  var9[var11 + 1] = var2[1][var14];
                  var9[var11 + 2] = var2[2][var14];
                  var9[var11 + 3] = var2[3][var14];
               }

               var11 += 4;
            }
         }
      }

      if (var8 != 3) {
         var9 = (byte[])var9.clone();
      }

      if (var8 == 2) {
         this.restoreToBackground(this.image, var4, var5, var6, var7);
      }

      return ByteBuffer.wrap(var9);
   }

   private ImageMetadata updateMetadata(int var1, int var2, int var3) {
      ImageMetadata var4 = new ImageMetadata((Float)null, true, (Integer)null, (Integer)null, (Integer)null, var3 != 0 ? var3 * 10 : 40, this.loopCount, var1, var2, (Integer)null, (Integer)null, (Integer)null);
      this.updateImageMetadata(var4);
      return var4;
   }

   class LZWDecoder {
      private final int initCodeSize = GIFImageLoader2.this.readByte();
      private final int clearCode;
      private final int eofCode;
      private int codeSize;
      private int codeMask;
      private int tableIndex;
      private int oldCode;
      private int blockLength = 0;
      private int blockPos = 0;
      private byte[] block = new byte[255];
      private int inData = 0;
      private int inBits = 0;
      private int[] prefix = new int[4096];
      private byte[] suffix = new byte[4096];
      private byte[] initial = new byte[4096];
      private int[] length = new int[4096];
      private byte[] string = new byte[4096];

      public LZWDecoder() throws IOException {
         this.clearCode = 1 << this.initCodeSize;
         this.eofCode = this.clearCode + 1;
         this.initTable();
      }

      public final int readString() throws IOException {
         int var1 = this.getCode();
         if (var1 == this.eofCode) {
            return -1;
         } else {
            int var2;
            int var3;
            int var4;
            if (var1 == this.clearCode) {
               this.initTable();
               var1 = this.getCode();
               if (var1 == this.eofCode) {
                  return -1;
               }
            } else {
               var3 = this.tableIndex;
               if (var1 < var3) {
                  var2 = var1;
               } else {
                  var2 = this.oldCode;
                  if (var1 != var3) {
                     throw new IOException("Bad GIF LZW: Out-of-sequence code!");
                  }
               }

               var4 = this.oldCode;
               this.prefix[var3] = var4;
               this.suffix[var3] = this.initial[var2];
               this.initial[var3] = this.initial[var4];
               this.length[var3] = this.length[var4] + 1;
               ++this.tableIndex;
               if (this.tableIndex == 1 << this.codeSize && this.tableIndex < 4096) {
                  ++this.codeSize;
                  this.codeMask = (1 << this.codeSize) - 1;
               }
            }

            var2 = var1;
            var3 = this.length[var1];

            for(var4 = var3 - 1; var4 >= 0; --var4) {
               this.string[var4] = this.suffix[var2];
               var2 = this.prefix[var2];
            }

            this.oldCode = var1;
            return var3;
         }
      }

      public final byte[] getString() {
         return this.string;
      }

      public final void waitForTerminator() throws IOException {
         GIFImageLoader2.this.consumeAnExtension();
      }

      private void initTable() {
         int var1 = 1 << this.initCodeSize;

         int var2;
         for(var2 = 0; var2 < var1; ++var2) {
            this.prefix[var2] = -1;
            this.suffix[var2] = (byte)var2;
            this.initial[var2] = (byte)var2;
            this.length[var2] = 1;
         }

         for(var2 = var1; var2 < 4096; ++var2) {
            this.prefix[var2] = -1;
            this.length[var2] = 1;
         }

         this.codeSize = this.initCodeSize + 1;
         this.codeMask = (1 << this.codeSize) - 1;
         this.tableIndex = var1 + 2;
         this.oldCode = 0;
      }

      private int getCode() throws IOException {
         while(this.inBits < this.codeSize) {
            this.inData |= this.nextByte() << this.inBits;
            this.inBits += 8;
         }

         int var1 = this.inData & this.codeMask;
         this.inBits -= this.codeSize;
         this.inData >>>= this.codeSize;
         return var1;
      }

      private int nextByte() throws IOException {
         if (this.blockPos == this.blockLength) {
            this.readData();
         }

         return this.block[this.blockPos++] & 255;
      }

      private void readData() throws IOException {
         this.blockPos = 0;
         this.blockLength = GIFImageLoader2.this.readByte();
         if (this.blockLength > 0) {
            GIFImageLoader2.this.readBytes(this.block, 0, this.blockLength);
         } else {
            throw new EOFException();
         }
      }
   }
}
