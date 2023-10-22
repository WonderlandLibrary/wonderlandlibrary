package com.sun.prism.d3d;

import com.sun.prism.PixelFormat;
import com.sun.prism.impl.BaseResourcePool;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.TextureResourcePool;

class D3DVramPool extends BaseResourcePool implements TextureResourcePool {
   public static final D3DVramPool instance = new D3DVramPool();

   private D3DVramPool() {
      super(PrismSettings.targetVram, PrismSettings.maxVram);
   }

   public long size(D3DTextureData var1) {
      return var1.getSize();
   }

   public long estimateTextureSize(int var1, int var2, PixelFormat var3) {
      return (long)var1 * (long)var2 * (long)var3.getBytesPerPixelUnit();
   }

   public long estimateRTTextureSize(int var1, int var2, boolean var3) {
      return (long)var1 * (long)var2 * 4L;
   }

   public String toString() {
      return "D3D Vram Pool";
   }
}
