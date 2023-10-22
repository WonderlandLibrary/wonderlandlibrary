package com.sun.javafx.robot;

import java.nio.Buffer;
import java.nio.IntBuffer;

public class FXRobotImage {
   private final IntBuffer pixelBuffer;
   private final int width;
   private final int height;
   private final int scanlineStride;

   public static FXRobotImage create(Buffer var0, int var1, int var2, int var3) {
      return new FXRobotImage(var0, var1, var2, var3);
   }

   private FXRobotImage(Buffer var1, int var2, int var3, int var4) {
      if (var1 == null) {
         throw new IllegalArgumentException("Pixel buffer must be non-null");
      } else if (var2 > 0 && var3 > 0) {
         this.pixelBuffer = (IntBuffer)var1;
         this.width = var2;
         this.height = var3;
         this.scanlineStride = var4;
      } else {
         throw new IllegalArgumentException("Image dimensions must be > 0");
      }
   }

   public Buffer getPixelBuffer() {
      return this.pixelBuffer;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public int getScanlineStride() {
      return this.scanlineStride;
   }

   public int getPixelStride() {
      return 4;
   }

   public int getArgbPre(int var1, int var2) {
      if (var1 >= 0 && var1 < this.width && var2 >= 0 && var2 < this.height) {
         return this.pixelBuffer.get(var1 + var2 * this.scanlineStride / 4);
      } else {
         throw new IllegalArgumentException("x,y must be >0, <width, height");
      }
   }

   public int getArgb(int var1, int var2) {
      if (var1 >= 0 && var1 < this.width && var2 >= 0 && var2 < this.height) {
         int var3 = this.pixelBuffer.get(var1 + var2 * this.scanlineStride / 4);
         if (var3 >> 24 == -1) {
            return var3;
         } else {
            int var4 = var3 >>> 24;
            int var5 = var3 >> 16 & 255;
            int var6 = var3 >> 8 & 255;
            int var7 = var3 & 255;
            int var8 = var4 + (var4 >> 7);
            var5 = var5 * var8 >> 8;
            var6 = var6 * var8 >> 8;
            var7 = var7 * var8 >> 8;
            return var4 << 24 | var5 << 16 | var6 << 8 | var7;
         }
      } else {
         throw new IllegalArgumentException("x,y must be >0, <width, height");
      }
   }

   public String toString() {
      return super.toString() + " [format=INT_ARGB_PRE width=" + this.width + " height=" + this.height + " scanlineStride=" + this.scanlineStride + " pixelStride=" + this.getPixelStride() + " pixelBuffer=" + this.pixelBuffer + "]";
   }
}
