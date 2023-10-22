package com.sun.prism.d3d;

import com.sun.prism.PixelFormat;
import com.sun.prism.impl.PrismTrace;

public class D3DTextureData extends D3DResource.D3DRecord {
   private final long size;
   private final boolean isRTT;
   private final int samples;

   static long estimateSize(int var0, int var1, PixelFormat var2) {
      return (long)var0 * (long)var1 * (long)var2.getBytesPerPixelUnit();
   }

   static long estimateRTSize(int var0, int var1, boolean var2) {
      return (long)var0 * (long)var1 * 4L;
   }

   D3DTextureData(D3DContext var1, long var2, boolean var4, int var5, int var6, PixelFormat var7, int var8) {
      super(var1, var2);
      this.size = var4 ? estimateRTSize(var5, var6, false) : estimateSize(var5, var6, var7);
      this.isRTT = var4;
      this.samples = var8;
      if (var4) {
         PrismTrace.rttCreated(var2, var5, var6, this.size);
      } else {
         PrismTrace.textureCreated(var2, var5, var6, this.size);
      }

   }

   int getSamples() {
      return this.samples;
   }

   long getSize() {
      return this.size;
   }

   protected void markDisposed() {
      long var1 = this.getResource();
      if (var1 != 0L) {
         if (this.isRTT) {
            PrismTrace.rttDisposed(var1);
         } else {
            PrismTrace.textureDisposed(var1);
         }
      }

      super.markDisposed();
   }

   public void dispose() {
      long var1 = this.getResource();
      if (var1 != 0L) {
         if (this.isRTT) {
            PrismTrace.rttDisposed(var1);
         } else {
            PrismTrace.textureDisposed(var1);
         }
      }

      super.dispose();
   }
}
