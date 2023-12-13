package com.sun.javafx.iio.bmp;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.common.ImageLoaderImpl;
import com.sun.javafx.iio.common.ImageTools;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

final class BMPImageLoader extends ImageLoaderImpl {
   static final short BM = 19778;
   static final int BFH_SIZE = 14;
   final LEInputStream data;
   int bfSize;
   int bfOffBits;
   byte[] bgra_palette;
   BitmapInfoHeader bih;
   int[] bitMasks;
   int[] bitOffsets;

   BMPImageLoader(InputStream var1) throws IOException {
      super(BMPDescriptor.theInstance);
      this.data = new LEInputStream(var1);
      if (this.data.readShort() != 19778) {
         throw new IOException("Invalid BMP file signature");
      } else {
         this.readHeader();
      }
   }

   private void readHeader() throws IOException {
      this.bfSize = this.data.readInt();
      this.data.skipBytes(4);
      this.bfOffBits = this.data.readInt();
      this.bih = new BitmapInfoHeader(this.data);
      if (this.bfOffBits < this.bih.biSize + 14) {
         throw new IOException("Invalid bitmap bits offset");
      } else {
         if (this.bih.biSize + 14 != this.bfOffBits) {
            int var1 = this.bfOffBits - this.bih.biSize - 14;
            int var2 = var1 / 4;
            this.bgra_palette = new byte[var2 * 4];
            int var3 = this.data.in.read(this.bgra_palette);
            if (var3 < var1) {
               this.data.skipBytes(var1 - var3);
            }
         }

         if (this.bih.biCompression == 3) {
            this.parseBitfields();
         } else if (this.bih.biCompression == 0 && this.bih.biBitCount == 16) {
            this.bitMasks = new int[]{31744, 992, 31};
            this.bitOffsets = new int[]{10, 5, 0};
         }

      }
   }

   private void parseBitfields() throws IOException {
      if (this.bgra_palette.length != 12) {
         throw new IOException("Invalid bit masks");
      } else {
         this.bitMasks = new int[3];
         this.bitOffsets = new int[3];

         for(int var1 = 0; var1 < 3; ++var1) {
            int var2 = getDWord(this.bgra_palette, var1 * 4);
            this.bitMasks[var1] = var2;
            int var3 = 0;
            if (var2 != 0) {
               while((var2 & 1) == 0) {
                  ++var3;
                  var2 >>>= 1;
               }

               if (!isPow2Minus1(var2)) {
                  throw new IOException("Bit mask is not contiguous");
               }
            }

            this.bitOffsets[var1] = var3;
         }

         if (!checkDisjointMasks(this.bitMasks[0], this.bitMasks[1], this.bitMasks[2])) {
            throw new IOException("Bit masks overlap");
         }
      }
   }

   static boolean checkDisjointMasks(int var0, int var1, int var2) {
      return (var0 & var1 | var0 & var2 | var1 & var2) == 0;
   }

   static boolean isPow2Minus1(int var0) {
      return (var0 & var0 + 1) == 0;
   }

   public void dispose() {
   }

   private void readRLE(byte[] var1, int var2, int var3, boolean var4) throws IOException {
      int var5 = this.bih.biSizeImage;
      if (var5 == 0) {
         var5 = this.bfSize - this.bfOffBits;
      }

      byte[] var6 = new byte[var5];
      ImageTools.readFully(this.data.in, var6);
      boolean var7 = this.bih.biHeight > 0;
      int var8 = var7 ? var3 - 1 : 0;
      int var9 = 0;
      int var10 = var8 * var2;

      while(true) {
         while(var9 < var5) {
            int var11 = getByte(var6, var9++);
            int var12 = getByte(var6, var9++);
            int var13;
            int var14;
            int var15;
            if (var11 == 0) {
               int var17;
               switch (var12) {
                  case 0:
                     var8 += var7 ? -1 : 1;
                     var10 = var8 * var2;
                     continue;
                  case 1:
                     return;
                  case 2:
                     var13 = getByte(var6, var9++);
                     var14 = getByte(var6, var9++);
                     var8 += var14;
                     var10 += var14 * var2;
                     var10 += var13 * 3;
                     continue;
                  default:
                     var15 = 0;
                     var17 = 0;
               }

               while(var17 < var12) {
                  int var16;
                  if (var4) {
                     if ((var17 & 1) == 0) {
                        var15 = getByte(var6, var9++);
                        var16 = (var15 & 240) >> 4;
                     } else {
                        var16 = var15 & 15;
                     }
                  } else {
                     var16 = getByte(var6, var9++);
                  }

                  var10 = this.setRGBFromPalette(var1, var10, var16);
                  ++var17;
               }

               if (var4) {
                  if ((var12 & 3) == 1 || (var12 & 3) == 2) {
                     ++var9;
                  }
               } else if ((var12 & 1) == 1) {
                  ++var9;
               }
            } else if (var4) {
               var13 = (var12 & 240) >> 4;
               var14 = var12 & 15;

               for(var15 = 0; var15 < var11; ++var15) {
                  var10 = this.setRGBFromPalette(var1, var10, (var15 & 1) == 0 ? var13 : var14);
               }
            } else {
               for(var13 = 0; var13 < var11; ++var13) {
                  var10 = this.setRGBFromPalette(var1, var10, var12);
               }
            }
         }

         return;
      }
   }

   private int setRGBFromPalette(byte[] var1, int var2, int var3) {
      var3 *= 4;
      var1[var2++] = this.bgra_palette[var3 + 2];
      var1[var2++] = this.bgra_palette[var3 + 1];
      var1[var2++] = this.bgra_palette[var3];
      return var2;
   }

   private void readPackedBits(byte[] var1, int var2, int var3) throws IOException {
      int var4 = 8 / this.bih.biBitCount;
      int var5 = (this.bih.biWidth + var4 - 1) / var4;
      int var6 = var5 + 3 & -4;
      int var7 = (1 << this.bih.biBitCount) - 1;
      byte[] var8 = new byte[var6];

      for(int var9 = 0; var9 != var3; ++var9) {
         ImageTools.readFully(this.data.in, var8);
         int var10 = this.bih.biHeight < 0 ? var9 : var3 - var9 - 1;
         int var11 = var10 * var2;

         for(int var12 = 0; var12 != this.bih.biWidth; ++var12) {
            int var13 = var12 * this.bih.biBitCount;
            byte var14 = var8[var13 / 8];
            int var15 = 8 - (var13 & 7) - this.bih.biBitCount;
            int var16 = var14 >> var15 & var7;
            var11 = this.setRGBFromPalette(var1, var11, var16);
         }
      }

   }

   private static int getDWord(byte[] var0, int var1) {
      return var0[var1] & 255 | (var0[var1 + 1] & 255) << 8 | (var0[var1 + 2] & 255) << 16 | (var0[var1 + 3] & 255) << 24;
   }

   private static int getWord(byte[] var0, int var1) {
      return var0[var1] & 255 | (var0[var1 + 1] & 255) << 8;
   }

   private static int getByte(byte[] var0, int var1) {
      return var0[var1] & 255;
   }

   private static byte convertFrom5To8Bit(int var0, int var1, int var2) {
      int var3 = (var0 & var1) >>> var2;
      return (byte)(var3 << 3 | var3 >> 2);
   }

   private static byte convertFromXTo8Bit(int var0, int var1, int var2) {
      int var3 = (var0 & var1) >>> var2;
      return (byte)((int)((double)var3 * 255.0 / (double)(var1 >>> var2)));
   }

   private void read16Bit(byte[] var1, int var2, int var3, BitConverter var4) throws IOException {
      int var5 = this.bih.biWidth * 2;
      int var6 = var5 + 3 & -4;
      byte[] var7 = new byte[var6];

      for(int var8 = 0; var8 != var3; ++var8) {
         ImageTools.readFully(this.data.in, var7);
         int var9 = this.bih.biHeight < 0 ? var8 : var3 - var8 - 1;
         int var10 = var9 * var2;

         for(int var11 = 0; var11 != this.bih.biWidth; ++var11) {
            int var12 = getWord(var7, var11 * 2);

            for(int var13 = 0; var13 < 3; ++var13) {
               var1[var10++] = var4.convert(var12, this.bitMasks[var13], this.bitOffsets[var13]);
            }
         }
      }

   }

   private void read32BitRGB(byte[] var1, int var2, int var3) throws IOException {
      int var4 = this.bih.biWidth * 4;
      byte[] var5 = new byte[var4];

      for(int var6 = 0; var6 != var3; ++var6) {
         ImageTools.readFully(this.data.in, var5);
         int var7 = this.bih.biHeight < 0 ? var6 : var3 - var6 - 1;
         int var8 = var7 * var2;

         for(int var9 = 0; var9 != this.bih.biWidth; ++var9) {
            int var10 = var9 * 4;
            var1[var8++] = var5[var10 + 2];
            var1[var8++] = var5[var10 + 1];
            var1[var8++] = var5[var10];
         }
      }

   }

   private void read32BitBF(byte[] var1, int var2, int var3) throws IOException {
      int var4 = this.bih.biWidth * 4;
      byte[] var5 = new byte[var4];

      for(int var6 = 0; var6 != var3; ++var6) {
         ImageTools.readFully(this.data.in, var5);
         int var7 = this.bih.biHeight < 0 ? var6 : var3 - var6 - 1;
         int var8 = var7 * var2;

         for(int var9 = 0; var9 != this.bih.biWidth; ++var9) {
            int var10 = var9 * 4;
            int var11 = getDWord(var5, var10);

            for(int var12 = 0; var12 < 3; ++var12) {
               var1[var8++] = convertFromXTo8Bit(var11, this.bitMasks[var12], this.bitOffsets[var12]);
            }
         }
      }

   }

   private void read24Bit(byte[] var1, int var2, int var3) throws IOException {
      int var4 = var2 + 3 & -4;
      int var5 = var4 - var2;

      for(int var6 = 0; var6 != var3; ++var6) {
         int var7 = this.bih.biHeight < 0 ? var6 : var3 - var6 - 1;
         int var8 = var7 * var2;
         ImageTools.readFully(this.data.in, var1, var8, var2);
         this.data.skipBytes(var5);
         BGRtoRGB(var1, var8, var2);
      }

   }

   static void BGRtoRGB(byte[] var0, int var1, int var2) {
      for(int var3 = var2 / 3; var3 != 0; --var3) {
         byte var4 = var0[var1];
         byte var5 = var0[var1 + 2];
         var0[var1 + 2] = var4;
         var0[var1] = var5;
         var1 += 3;
      }

   }

   public ImageFrame load(int var1, int var2, int var3, boolean var4, boolean var5) throws IOException {
      if (0 != var1) {
         return null;
      } else {
         int var6 = Math.abs(this.bih.biHeight);
         int[] var7 = ImageTools.computeDimensions(this.bih.biWidth, var6, var2, var3, var4);
         var2 = var7[0];
         var3 = var7[1];
         ImageMetadata var8 = new ImageMetadata((Float)null, Boolean.TRUE, (Integer)null, (Integer)null, (Integer)null, (Integer)null, (Integer)null, var2, var3, (Integer)null, (Integer)null, (Integer)null);
         this.updateImageMetadata(var8);
         byte var9 = 3;
         int var10 = this.bih.biWidth * var9;
         byte[] var11 = new byte[var10 * var6];
         switch (this.bih.biBitCount) {
            case 1:
               this.readPackedBits(var11, var10, var6);
               break;
            case 4:
               if (this.bih.biCompression == 2) {
                  this.readRLE(var11, var10, var6, true);
               } else {
                  this.readPackedBits(var11, var10, var6);
               }
               break;
            case 8:
               if (this.bih.biCompression == 1) {
                  this.readRLE(var11, var10, var6, false);
               } else {
                  this.readPackedBits(var11, var10, var6);
               }
               break;
            case 16:
               if (this.bih.biCompression == 3) {
                  this.read16Bit(var11, var10, var6, BMPImageLoader::convertFromXTo8Bit);
               } else {
                  this.read16Bit(var11, var10, var6, BMPImageLoader::convertFrom5To8Bit);
               }
               break;
            case 24:
               this.read24Bit(var11, var10, var6);
               break;
            case 32:
               if (this.bih.biCompression == 3) {
                  this.read32BitBF(var11, var10, var6);
               } else {
                  this.read32BitRGB(var11, var10, var6);
               }
               break;
            default:
               throw new IOException("Unknown BMP bit depth");
         }

         ByteBuffer var12 = ByteBuffer.wrap(var11);
         if (this.bih.biWidth != var2 || var6 != var3) {
            var12 = ImageTools.scaleImage(var12, this.bih.biWidth, var6, var9, var2, var3, var5);
         }

         return new ImageFrame(ImageStorage.ImageType.RGB, var12, var2, var3, var2 * var9, (byte[][])null, var8);
      }
   }

   @FunctionalInterface
   private interface BitConverter {
      byte convert(int var1, int var2, int var3);
   }
}
