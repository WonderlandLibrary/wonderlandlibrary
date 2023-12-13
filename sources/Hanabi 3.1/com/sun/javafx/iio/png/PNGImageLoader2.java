package com.sun.javafx.iio.png;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.common.ImageLoaderImpl;
import com.sun.javafx.iio.common.ImageTools;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public final class PNGImageLoader2 extends ImageLoaderImpl {
   static final byte[] FILE_SIG = new byte[]{-119, 80, 78, 71, 13, 10, 26, 10};
   static final int IHDR_TYPE = 1229472850;
   static final int PLTE_TYPE = 1347179589;
   static final int IDAT_TYPE = 1229209940;
   static final int IEND_TYPE = 1229278788;
   static final int tRNS_TYPE = 1951551059;
   static final int PNG_COLOR_GRAY = 0;
   static final int PNG_COLOR_RGB = 2;
   static final int PNG_COLOR_PALETTE = 3;
   static final int PNG_COLOR_GRAY_ALPHA = 4;
   static final int PNG_COLOR_RGB_ALPHA = 6;
   static final int[] numBandsPerColorType = new int[]{1, -1, 3, 1, 2, -1, 4};
   static final int PNG_FILTER_NONE = 0;
   static final int PNG_FILTER_SUB = 1;
   static final int PNG_FILTER_UP = 2;
   static final int PNG_FILTER_AVERAGE = 3;
   static final int PNG_FILTER_PAETH = 4;
   private final DataInputStream stream;
   private int width;
   private int height;
   private int bitDepth;
   private int colorType;
   private boolean isInterlaced;
   private boolean tRNS_present = false;
   private boolean tRNS_GRAY_RGB = false;
   private int trnsR;
   private int trnsG;
   private int trnsB;
   private byte[][] palette;
   private static final int[] starting_y = new int[]{0, 0, 4, 0, 2, 0, 1, 0};
   private static final int[] starting_x = new int[]{0, 4, 0, 2, 0, 1, 0, 0};
   private static final int[] increment_y = new int[]{8, 8, 8, 4, 4, 2, 2, 1};
   private static final int[] increment_x = new int[]{8, 8, 4, 4, 2, 2, 1, 1};

   public PNGImageLoader2(InputStream var1) throws IOException {
      super(PNGDescriptor.getInstance());
      this.stream = new DataInputStream(var1);
      byte[] var2 = this.readBytes(new byte[8]);
      if (!Arrays.equals(FILE_SIG, var2)) {
         throw new IOException("Bad PNG signature!");
      } else {
         this.readHeader();
      }
   }

   private void readHeader() throws IOException {
      int[] var1 = this.readChunk();
      if (var1[1] != 1229472850 && var1[0] != 13) {
         throw new IOException("Bad PNG header!");
      } else {
         this.width = this.stream.readInt();
         this.height = this.stream.readInt();
         if (this.width != 0 && this.height != 0) {
            this.bitDepth = this.stream.readByte();
            if (this.bitDepth != 1 && this.bitDepth != 2 && this.bitDepth != 4 && this.bitDepth != 8 && this.bitDepth != 16) {
               throw new IOException("Bad PNG bit depth");
            } else {
               this.colorType = this.stream.readByte();
               if (this.colorType <= 6 && this.colorType != 1 && this.colorType != 5) {
                  if ((this.colorType == 3 || this.colorType == 0 || this.bitDepth >= 8) && (this.colorType != 3 || this.bitDepth != 16)) {
                     byte var2 = this.stream.readByte();
                     if (var2 != 0) {
                        throw new IOException("Bad PNG comression!");
                     } else {
                        byte var3 = this.stream.readByte();
                        if (var3 != 0) {
                           throw new IOException("Bad PNG filter method!");
                        } else {
                           byte var4 = this.stream.readByte();
                           if (var4 != 0 && var4 != 1) {
                              throw new IOException("Unknown interlace method (not 0 or 1)!");
                           } else {
                              int var5 = this.stream.readInt();
                              this.isInterlaced = var4 == 1;
                           }
                        }
                     }
                  } else {
                     throw new IOException("Bad color type/bit depth combination!");
                  }
               } else {
                  throw new IOException("Bad PNG color type");
               }
            }
         } else {
            throw new IOException("Bad PNG image size!");
         }
      }
   }

   private int[] readChunk() throws IOException {
      return new int[]{this.stream.readInt(), this.stream.readInt()};
   }

   private byte[] readBytes(byte[] var1) throws IOException {
      return this.readBytes(var1, 0, var1.length);
   }

   private byte[] readBytes(byte[] var1, int var2, int var3) throws IOException {
      this.stream.readFully(var1, var2, var3);
      return var1;
   }

   private void skip(int var1) throws IOException {
      if (var1 != this.stream.skipBytes(var1)) {
         throw new EOFException();
      }
   }

   private void readPaletteChunk(int var1) throws IOException {
      int var2 = var1 / 3;
      int var3 = 1 << this.bitDepth;
      if (var2 > var3) {
         this.emitWarning("PLTE chunk contains too many entries for bit depth, ignoring extras.");
         var2 = var3;
      }

      this.palette = new byte[3][var3];
      byte[] var4 = this.readBytes(new byte[var1]);
      int var5 = 0;

      for(int var6 = 0; var5 != var2; ++var5) {
         for(int var7 = 0; var7 != 3; ++var7) {
            this.palette[var7][var5] = var4[var6++];
         }
      }

   }

   private void parsePaletteChunk(int var1) throws IOException {
      if (this.palette != null) {
         this.emitWarning("A PNG image may not contain more than one PLTE chunk.\nThe chunk wil be ignored.");
         this.skip(var1);
      } else {
         switch (this.colorType) {
            case 0:
            case 4:
               this.emitWarning("A PNG gray or gray alpha image cannot have a PLTE chunk.\nThe chunk wil be ignored.");
            case 1:
            case 2:
            default:
               this.skip(var1);
               return;
            case 3:
               this.readPaletteChunk(var1);
         }
      }
   }

   private boolean readPaletteTransparency(int var1) throws IOException {
      if (this.palette == null) {
         this.emitWarning("tRNS chunk without prior PLTE chunk, ignoring it.");
         this.skip(var1);
         return false;
      } else {
         byte[][] var2 = new byte[4][];
         System.arraycopy(this.palette, 0, var2, 0, 3);
         int var3 = this.palette[0].length;
         var2[3] = new byte[var3];
         int var4 = var1 < var3 ? var1 : var3;
         this.readBytes(var2[3], 0, var4);

         for(int var5 = var4; var5 < var3; ++var5) {
            var2[3][var5] = -1;
         }

         if (var4 < var1) {
            this.skip(var1 - var4);
         }

         this.palette = var2;
         return true;
      }
   }

   private boolean readGrayTransparency(int var1) throws IOException {
      if (var1 == 2) {
         this.trnsG = this.stream.readShort();
         return true;
      } else {
         return false;
      }
   }

   private boolean readRgbTransparency(int var1) throws IOException {
      if (var1 == 6) {
         this.trnsR = this.stream.readShort();
         this.trnsG = this.stream.readShort();
         this.trnsB = this.stream.readShort();
         return true;
      } else {
         return false;
      }
   }

   private void parseTransparencyChunk(int var1) throws IOException {
      switch (this.colorType) {
         case 0:
            this.tRNS_GRAY_RGB = this.tRNS_present = this.readGrayTransparency(var1);
            break;
         case 1:
         default:
            this.emitWarning("TransparencyChunk may not present when alpha explicitly defined");
            this.skip(var1);
            break;
         case 2:
            this.tRNS_GRAY_RGB = this.tRNS_present = this.readRgbTransparency(var1);
            break;
         case 3:
            this.tRNS_present = this.readPaletteTransparency(var1);
      }

   }

   private int parsePngMeta() throws IOException {
      while(true) {
         int[] var1 = this.readChunk();
         if (var1[0] < 0) {
            throw new IOException("Invalid chunk length");
         }

         switch (var1[1]) {
            case 1229209940:
               return var1[0];
            case 1229278788:
               return 0;
            case 1347179589:
               this.parsePaletteChunk(var1[0]);
               break;
            case 1951551059:
               this.parseTransparencyChunk(var1[0]);
               break;
            default:
               this.skip(var1[0]);
         }

         int var2 = this.stream.readInt();
      }
   }

   public void dispose() {
   }

   private ImageStorage.ImageType getType() {
      switch (this.colorType) {
         case 0:
            return this.tRNS_present ? ImageStorage.ImageType.GRAY_ALPHA : ImageStorage.ImageType.GRAY;
         case 1:
         case 5:
         default:
            throw new RuntimeException();
         case 2:
            return this.tRNS_present ? ImageStorage.ImageType.RGBA : ImageStorage.ImageType.RGB;
         case 3:
            return ImageStorage.ImageType.PALETTE;
         case 4:
            return ImageStorage.ImageType.GRAY_ALPHA;
         case 6:
            return ImageStorage.ImageType.RGBA;
      }
   }

   private void doSubFilter(byte[] var1, int var2) {
      int var3 = var1.length;

      for(int var4 = var2; var4 != var3; ++var4) {
         var1[var4] += var1[var4 - var2];
      }

   }

   private void doUpFilter(byte[] var1, byte[] var2) {
      int var3 = var1.length;

      for(int var4 = 0; var4 != var3; ++var4) {
         var1[var4] += var2[var4];
      }

   }

   private void doAvrgFilter(byte[] var1, byte[] var2, int var3) {
      int var4 = var1.length;

      int var5;
      for(var5 = 0; var5 != var3; ++var5) {
         var1[var5] = (byte)(var1[var5] + (var2[var5] & 255) / 2);
      }

      for(var5 = var3; var5 != var4; ++var5) {
         var1[var5] = (byte)(var1[var5] + ((var1[var5 - var3] & 255) + (var2[var5] & 255)) / 2);
      }

   }

   private static int paethPr(int var0, int var1, int var2) {
      int var3 = Math.abs(var1 - var2);
      int var4 = Math.abs(var0 - var2);
      int var5 = Math.abs(var1 - var2 + var0 - var2);
      return var3 <= var4 && var3 <= var5 ? var0 : (var4 <= var5 ? var1 : var2);
   }

   private void doPaethFilter(byte[] var1, byte[] var2, int var3) {
      int var4 = var1.length;

      int var5;
      for(var5 = 0; var5 != var3; ++var5) {
         var1[var5] += var2[var5];
      }

      for(var5 = var3; var5 != var4; ++var5) {
         var1[var5] = (byte)(var1[var5] + paethPr(var1[var5 - var3] & 255, var2[var5] & 255, var2[var5 - var3] & 255));
      }

   }

   private void doFilter(byte[] var1, byte[] var2, int var3, int var4) {
      switch (var3) {
         case 1:
            this.doSubFilter(var1, var4);
            break;
         case 2:
            this.doUpFilter(var1, var2);
            break;
         case 3:
            this.doAvrgFilter(var1, var2, var4);
            break;
         case 4:
            this.doPaethFilter(var1, var2, var4);
      }

   }

   private void downsample16to8trns_gray(byte[] var1, byte[] var2, int var3, int var4) {
      int var5 = var1.length / 2;
      int var6 = 0;

      for(int var7 = var3; var6 < var5; ++var6) {
         short var8 = (short)((var1[var6 * 2] & 255) * 256 + (var1[var6 * 2 + 1] & 255));
         var2[var7 + 0] = var1[var6 * 2];
         var2[var7 + 1] = (byte)(var8 == this.trnsG ? 0 : -1);
         var7 += var4 * 2;
      }

   }

   private void downsample16to8trns_rgb(byte[] var1, byte[] var2, int var3, int var4) {
      int var5 = var1.length / 2 / 3;
      int var6 = 0;

      for(int var7 = var3; var6 < var5; ++var6) {
         int var8 = var6 * 6;
         short var9 = (short)((var1[var8 + 0] & 255) * 256 + (var1[var8 + 1] & 255));
         short var10 = (short)((var1[var8 + 2] & 255) * 256 + (var1[var8 + 3] & 255));
         short var11 = (short)((var1[var8 + 4] & 255) * 256 + (var1[var8 + 5] & 255));
         var2[var7 + 0] = var1[var8 + 0];
         var2[var7 + 1] = var1[var8 + 2];
         var2[var7 + 2] = var1[var8 + 4];
         var2[var7 + 3] = (byte)(var9 == this.trnsR && var10 == this.trnsG && var11 == this.trnsB ? 0 : -1);
         var7 += var4 * 4;
      }

   }

   private void downsample16to8_plain(byte[] var1, byte[] var2, int var3, int var4, int var5) {
      int var6 = var1.length / 2 / var5 * var5;
      int var7 = var4 * var5;
      int var8 = 0;

      for(int var9 = var3; var8 != var6; var8 += var5) {
         for(int var10 = 0; var10 != var5; ++var10) {
            var2[var9 + var10] = var1[(var8 + var10) * 2];
         }

         var9 += var7;
      }

   }

   private void downsample16to8(byte[] var1, byte[] var2, int var3, int var4, int var5) {
      if (!this.tRNS_GRAY_RGB) {
         this.downsample16to8_plain(var1, var2, var3, var4, var5);
      } else if (this.colorType == 0) {
         this.downsample16to8trns_gray(var1, var2, var3, var4);
      } else if (this.colorType == 2) {
         this.downsample16to8trns_rgb(var1, var2, var3, var4);
      }

   }

   private void copyTrns_gray(byte[] var1, byte[] var2, int var3, int var4) {
      byte var5 = (byte)this.trnsG;
      int var6 = 0;
      int var7 = var3;

      for(int var8 = var1.length; var6 < var8; ++var6) {
         byte var9 = var1[var6];
         var2[var7] = var9;
         var2[var7 + 1] = (byte)(var9 == var5 ? 0 : -1);
         var7 += 2 * var4;
      }

   }

   private void copyTrns_rgb(byte[] var1, byte[] var2, int var3, int var4) {
      byte var5 = (byte)this.trnsR;
      byte var6 = (byte)this.trnsG;
      byte var7 = (byte)this.trnsB;
      int var8 = var1.length / 3;
      int var9 = 0;

      for(int var10 = var3; var9 < var8; ++var9) {
         byte var11 = var1[var9 * 3];
         byte var12 = var1[var9 * 3 + 1];
         byte var13 = var1[var9 * 3 + 2];
         var2[var10 + 0] = var11;
         var2[var10 + 1] = var12;
         var2[var10 + 2] = var13;
         var2[var10 + 3] = (byte)(var11 == var5 && var12 == var6 && var13 == var7 ? 0 : -1);
         var10 += var4 * 4;
      }

   }

   private void copy_plain(byte[] var1, byte[] var2, int var3, int var4, int var5) {
      int var6 = var1.length;
      int var7 = var4 * var5;
      int var8 = 0;

      for(int var9 = var3; var8 != var6; var8 += var5) {
         for(int var10 = 0; var10 != var5; ++var10) {
            var2[var9 + var10] = var1[var8 + var10];
         }

         var9 += var7;
      }

   }

   private void copy(byte[] var1, byte[] var2, int var3, int var4, int var5) {
      if (!this.tRNS_GRAY_RGB) {
         if (var4 == 1) {
            System.arraycopy(var1, 0, var2, var3, var1.length);
         } else {
            this.copy_plain(var1, var2, var3, var4, var5);
         }
      } else if (this.colorType == 0) {
         this.copyTrns_gray(var1, var2, var3, var4);
      } else if (this.colorType == 2) {
         this.copyTrns_rgb(var1, var2, var3, var4);
      }

   }

   private void upsampleTo8Palette(byte[] var1, byte[] var2, int var3, int var4, int var5) {
      int var6 = 8 / this.bitDepth;
      int var7 = (1 << this.bitDepth) - 1;
      int var8 = 0;

      for(int var9 = 0; var8 < var4; var8 += var6) {
         int var10 = var4 - var8 < var6 ? var4 - var8 : var6;
         int var11 = var1[var9] >> (var6 - var10) * this.bitDepth;

         for(int var12 = var10 - 1; var12 >= 0; --var12) {
            var2[var3 + (var8 + var12) * var5] = (byte)(var11 & var7);
            var11 >>= this.bitDepth;
         }

         ++var9;
      }

   }

   private void upsampleTo8Gray(byte[] var1, byte[] var2, int var3, int var4, int var5) {
      int var6 = 8 / this.bitDepth;
      int var7 = (1 << this.bitDepth) - 1;
      int var8 = var7 / 2;
      int var9 = 0;

      for(int var10 = 0; var9 < var4; var9 += var6) {
         int var11 = var4 - var9 < var6 ? var4 - var9 : var6;
         int var12 = var1[var10] >> (var6 - var11) * this.bitDepth;

         for(int var13 = var11 - 1; var13 >= 0; --var13) {
            var2[var3 + (var9 + var13) * var5] = (byte)(((var12 & var7) * 255 + var8) / var7);
            var12 >>= this.bitDepth;
         }

         ++var10;
      }

   }

   private void upsampleTo8GrayTrns(byte[] var1, byte[] var2, int var3, int var4, int var5) {
      int var6 = 8 / this.bitDepth;
      int var7 = (1 << this.bitDepth) - 1;
      int var8 = var7 / 2;
      int var9 = 0;

      for(int var10 = 0; var9 < var4; var9 += var6) {
         int var11 = var4 - var9 < var6 ? var4 - var9 : var6;
         int var12 = var1[var10] >> (var6 - var11) * this.bitDepth;

         for(int var13 = var11 - 1; var13 >= 0; --var13) {
            int var14 = var3 + (var9 + var13) * var5 * 2;
            int var15 = var12 & var7;
            var2[var14] = (byte)((var15 * 255 + var8) / var7);
            var2[var14 + 1] = (byte)(var15 == this.trnsG ? 0 : -1);
            var12 >>= this.bitDepth;
         }

         ++var10;
      }

   }

   private void upsampleTo8(byte[] var1, byte[] var2, int var3, int var4, int var5, int var6) {
      if (this.colorType == 3) {
         this.upsampleTo8Palette(var1, var2, var3, var4, var5);
      } else if (var6 == 1) {
         this.upsampleTo8Gray(var1, var2, var3, var4, var5);
      } else if (this.tRNS_GRAY_RGB && var6 == 2) {
         this.upsampleTo8GrayTrns(var1, var2, var3, var4, var5);
      }

   }

   private static int mipSize(int var0, int var1, int[] var2, int[] var3) {
      return (var0 - var2[var1] + var3[var1] - 1) / var3[var1];
   }

   private static int mipPos(int var0, int var1, int[] var2, int[] var3) {
      return var2[var1] + var0 * var3[var1];
   }

   private void loadMip(byte[] var1, InputStream var2, int var3) throws IOException {
      int var4 = mipSize(this.width, var3, starting_x, increment_x);
      int var5 = mipSize(this.height, var3, starting_y, increment_y);
      int var6 = (var4 * this.bitDepth * numBandsPerColorType[this.colorType] + 7) / 8;
      byte[] var7 = new byte[var6];
      byte[] var8 = new byte[var6];
      int var9 = this.bpp();
      int var10 = numBandsPerColorType[this.colorType] * this.bytesPerColor();

      for(int var11 = 0; var11 != var5; ++var11) {
         int var12 = var2.read();
         if (var12 == -1) {
            throw new EOFException();
         }

         if (var2.read(var7) != var6) {
            throw new EOFException();
         }

         this.doFilter(var7, var8, var12, var10);
         int var13 = (mipPos(var11, var3, starting_y, increment_y) * this.width + starting_x[var3]) * var9;
         int var14 = increment_x[var3];
         if (this.bitDepth == 16) {
            this.downsample16to8(var7, var1, var13, var14, var9);
         } else if (this.bitDepth < 8) {
            this.upsampleTo8(var7, var1, var13, var4, var14, var9);
         } else {
            this.copy(var7, var1, var13, var14, var9);
         }

         byte[] var15 = var7;
         var7 = var8;
         var8 = var15;
      }

   }

   private void load(byte[] var1, InputStream var2) throws IOException {
      if (this.isInterlaced) {
         for(int var3 = 0; var3 != 7; ++var3) {
            if (this.width > starting_x[var3] && this.height > starting_y[var3]) {
               this.loadMip(var1, var2, var3);
            }
         }
      } else {
         this.loadMip(var1, var2, 7);
      }

   }

   private ImageFrame decodePalette(byte[] var1, ImageMetadata var2) {
      int var3 = this.tRNS_present ? 4 : 3;
      byte[] var4 = new byte[this.width * this.height * var3];
      int var5 = this.width * this.height;
      int var6;
      int var7;
      int var8;
      if (this.tRNS_present) {
         var6 = 0;

         for(var7 = 0; var6 != var5; ++var6) {
            var8 = 255 & var1[var6];
            var4[var7 + 0] = this.palette[0][var8];
            var4[var7 + 1] = this.palette[1][var8];
            var4[var7 + 2] = this.palette[2][var8];
            var4[var7 + 3] = this.palette[3][var8];
            var7 += 4;
         }
      } else {
         var6 = 0;

         for(var7 = 0; var6 != var5; ++var6) {
            var8 = 255 & var1[var6];
            var4[var7 + 0] = this.palette[0][var8];
            var4[var7 + 1] = this.palette[1][var8];
            var4[var7 + 2] = this.palette[2][var8];
            var7 += 3;
         }
      }

      ImageStorage.ImageType var9 = this.tRNS_present ? ImageStorage.ImageType.RGBA : ImageStorage.ImageType.RGB;
      return new ImageFrame(var9, ByteBuffer.wrap(var4), this.width, this.height, this.width * var3, (byte[][])null, var2);
   }

   private int bpp() {
      return numBandsPerColorType[this.colorType] + (this.tRNS_GRAY_RGB ? 1 : 0);
   }

   private int bytesPerColor() {
      return this.bitDepth == 16 ? 2 : 1;
   }

   public ImageFrame load(int var1, int var2, int var3, boolean var4, boolean var5) throws IOException {
      if (var1 != 0) {
         return null;
      } else {
         int var6 = this.parsePngMeta();
         if (var6 == 0) {
            this.emitWarning("No image data in PNG");
            return null;
         } else {
            int[] var7 = ImageTools.computeDimensions(this.width, this.height, var2, var3, var4);
            var2 = var7[0];
            var3 = var7[1];
            ImageMetadata var8 = new ImageMetadata((Float)null, true, (Integer)null, (Integer)null, (Integer)null, (Integer)null, (Integer)null, var2, var3, (Integer)null, (Integer)null, (Integer)null);
            this.updateImageMetadata(var8);
            int var9 = this.bpp();
            ByteBuffer var10 = ByteBuffer.allocate(var9 * this.width * this.height);
            PNGIDATChunkInputStream var11 = new PNGIDATChunkInputStream(this.stream, var6);
            Inflater var12 = new Inflater();
            BufferedInputStream var13 = new BufferedInputStream(new InflaterInputStream(var11, var12));

            try {
               this.load(var10.array(), var13);
            } catch (IOException var18) {
               throw var18;
            } finally {
               if (var12 != null) {
                  var12.end();
               }

            }

            ImageFrame var14 = this.colorType == 3 ? this.decodePalette(var10.array(), var8) : new ImageFrame(this.getType(), var10, this.width, this.height, var9 * this.width, this.palette, var8);
            if (this.width != var2 || this.height != var3) {
               var14 = ImageTools.scaleImageFrame(var14, var2, var3, var5);
            }

            return var14;
         }
      }
   }
}
