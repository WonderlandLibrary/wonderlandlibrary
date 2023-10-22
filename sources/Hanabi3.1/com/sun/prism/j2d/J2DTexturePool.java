package com.sun.prism.j2d;

import com.sun.prism.PixelFormat;
import com.sun.prism.impl.BaseResourcePool;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.ResourcePool;
import com.sun.prism.impl.TextureResourcePool;
import java.awt.image.BufferedImage;

class J2DTexturePool extends BaseResourcePool implements TextureResourcePool {
   static final J2DTexturePool instance = new J2DTexturePool();

   private static long maxVram() {
      long var0 = Runtime.getRuntime().maxMemory();
      long var2 = PrismSettings.maxVram;
      return Math.min(var0 / 4L, var2);
   }

   private static long targetVram() {
      long var0 = maxVram();
      return Math.min(var0 / 2L, PrismSettings.targetVram);
   }

   private J2DTexturePool() {
      super((ResourcePool)null, targetVram(), maxVram());
   }

   public long used() {
      Runtime var1 = Runtime.getRuntime();
      long var2 = var1.totalMemory() - var1.freeMemory();
      long var4 = var1.maxMemory() - var2;
      long var6 = this.max() - this.managed();
      return this.max() - Math.min(var4, var6);
   }

   static long size(int var0, int var1, int var2) {
      long var3 = (long)var0 * (long)var1;
      switch (var2) {
         case 3:
            return var3 * 4L;
         case 5:
            return var3 * 3L;
         case 10:
            return var3;
         default:
            throw new InternalError("Unrecognized BufferedImage");
      }
   }

   public long size(BufferedImage var1) {
      return size(var1.getWidth(), var1.getHeight(), var1.getType());
   }

   public long estimateTextureSize(int var1, int var2, PixelFormat var3) {
      byte var4;
      switch (var3) {
         case BYTE_RGB:
            var4 = 5;
            break;
         case BYTE_GRAY:
            var4 = 10;
            break;
         case INT_ARGB_PRE:
         case BYTE_BGRA_PRE:
            var4 = 3;
            break;
         default:
            throw new InternalError("Unrecognized PixelFormat (" + var3 + ")!");
      }

      return size(var1, var2, var4);
   }

   public long estimateRTTextureSize(int var1, int var2, boolean var3) {
      return size(var1, var2, 3);
   }

   public String toString() {
      return "J2D Texture Pool";
   }
}
