package com.sun.prism.sw;

import com.sun.prism.PixelFormat;
import com.sun.prism.impl.BaseResourcePool;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.ResourcePool;
import com.sun.prism.impl.TextureResourcePool;

class SWTexturePool extends BaseResourcePool implements TextureResourcePool {
   static final SWTexturePool instance = new SWTexturePool();

   private static long maxVram() {
      long var0 = Runtime.getRuntime().maxMemory();
      long var2 = PrismSettings.maxVram;
      return Math.min(var0 / 4L, var2);
   }

   private static long targetVram() {
      long var0 = maxVram();
      return Math.min(var0 / 2L, PrismSettings.targetVram);
   }

   private SWTexturePool() {
      super((ResourcePool)null, targetVram(), maxVram());
   }

   public long used() {
      return 0L;
   }

   public long size(SWTexture var1) {
      long var2 = (long)var1.getPhysicalWidth();
      var2 *= (long)var1.getPhysicalHeight();
      if (var1 instanceof SWArgbPreTexture) {
         var2 *= 4L;
      }

      return var2;
   }

   public long estimateTextureSize(int var1, int var2, PixelFormat var3) {
      switch (var3) {
         case BYTE_ALPHA:
            return (long)var1 * (long)var2;
         default:
            return (long)var1 * (long)var2 * 4L;
      }
   }

   public long estimateRTTextureSize(int var1, int var2, boolean var3) {
      return (long)var1 * (long)var2 * 4L;
   }
}
