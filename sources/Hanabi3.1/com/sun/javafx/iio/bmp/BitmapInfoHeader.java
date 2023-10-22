package com.sun.javafx.iio.bmp;

import java.io.IOException;

final class BitmapInfoHeader {
   static final int BIH_SIZE = 40;
   static final int BIH4_SIZE = 108;
   static final int BIH5_SIZE = 124;
   static final int BI_RGB = 0;
   static final int BI_RLE8 = 1;
   static final int BI_RLE4 = 2;
   static final int BI_BITFIELDS = 3;
   static final int BI_JPEG = 4;
   static final int BI_PNG = 5;
   final int biSize;
   final int biWidth;
   final int biHeight;
   final short biPlanes;
   final short biBitCount;
   final int biCompression;
   final int biSizeImage;
   final int biXPelsPerMeter;
   final int biYPelsPerMeter;
   final int biClrUsed;
   final int biClrImportant;

   BitmapInfoHeader(LEInputStream var1) throws IOException {
      this.biSize = var1.readInt();
      this.biWidth = var1.readInt();
      this.biHeight = var1.readInt();
      this.biPlanes = var1.readShort();
      this.biBitCount = var1.readShort();
      this.biCompression = var1.readInt();
      this.biSizeImage = var1.readInt();
      this.biXPelsPerMeter = var1.readInt();
      this.biYPelsPerMeter = var1.readInt();
      this.biClrUsed = var1.readInt();
      this.biClrImportant = var1.readInt();
      if (this.biSize > 40) {
         if (this.biSize != 108 && this.biSize != 124) {
            throw new IOException("BitmapInfoHeader is corrupt");
         }

         var1.skipBytes(this.biSize - 40);
      }

      this.validate();
   }

   void validate() throws IOException {
      if (this.biBitCount >= 1 && this.biCompression != 4 && this.biCompression != 5) {
         switch (this.biCompression) {
            case 0:
               break;
            case 1:
               if (this.biBitCount != 8) {
                  throw new IOException("Invalid BMP image: Only 8 bpp images can be RLE8 compressed");
               }
               break;
            case 2:
               if (this.biBitCount != 4) {
                  throw new IOException("Invalid BMP image: Only 4 bpp images can be RLE4 compressed");
               }
               break;
            case 3:
               if (this.biBitCount != 16 && this.biBitCount != 32) {
                  throw new IOException("Invalid BMP image: Only 16 or 32 bpp images can use BITFIELDS compression");
               }
               break;
            default:
               throw new IOException("Unknown BMP compression type");
         }

      } else {
         throw new IOException("Unsupported BMP image: Embedded JPEG or PNG images are not supported");
      }
   }
}
