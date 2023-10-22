package com.sun.prism.impl;

import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.ResourceFactory;
import com.sun.prism.ResourceFactoryListener;
import com.sun.prism.Texture;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public abstract class BaseResourceFactory implements ResourceFactory {
   private final Map clampTexCache;
   private final Map repeatTexCache;
   private final Map mipmapTexCache;
   private final WeakHashMap listenerMap;
   private Texture regionTexture;
   private Texture glyphTexture;
   private boolean superShaderAllowed;

   public BaseResourceFactory() {
      this(new WeakHashMap(), new WeakHashMap(), new WeakHashMap());
   }

   public BaseResourceFactory(Map var1, Map var2, Map var3) {
      this.listenerMap = new WeakHashMap();
      this.clampTexCache = var1;
      this.repeatTexCache = var2;
      this.mipmapTexCache = var3;
   }

   public void addFactoryListener(ResourceFactoryListener var1) {
      this.listenerMap.put(var1, Boolean.TRUE);
   }

   public void removeFactoryListener(ResourceFactoryListener var1) {
      this.listenerMap.remove(var1);
   }

   public boolean isDeviceReady() {
      return true;
   }

   protected void clearTextureCache() {
      this.clearTextureCache(this.clampTexCache);
      this.clearTextureCache(this.repeatTexCache);
      this.clearTextureCache(this.mipmapTexCache);
   }

   protected void clearTextureCache(Map var1) {
      Collection var2 = var1.values();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Texture var4 = (Texture)var3.next();
         var4.dispose();
      }

      var1.clear();
   }

   protected ResourceFactoryListener[] getFactoryListeners() {
      return (ResourceFactoryListener[])this.listenerMap.keySet().toArray(new ResourceFactoryListener[0]);
   }

   protected void notifyReset() {
      this.clampTexCache.clear();
      this.repeatTexCache.clear();
      this.mipmapTexCache.clear();
      ResourceFactoryListener[] var1 = this.getFactoryListeners();
      ResourceFactoryListener[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ResourceFactoryListener var5 = var2[var4];
         if (null != var5) {
            var5.factoryReset();
         }
      }

   }

   protected void notifyReleased() {
      this.clampTexCache.clear();
      this.repeatTexCache.clear();
      this.mipmapTexCache.clear();
      ResourceFactoryListener[] var1 = this.getFactoryListeners();
      ResourceFactoryListener[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ResourceFactoryListener var5 = var2[var4];
         if (null != var5) {
            var5.factoryReleased();
         }
      }

   }

   static long sizeWithMipMap(int var0, int var1, PixelFormat var2) {
      long var3 = 0L;

      int var5;
      for(var5 = var2.getBytesPerPixelUnit(); var0 > 1 && var1 > 1; var1 = var1 + 1 >> 1) {
         var3 += (long)var0 * (long)var1;
         var0 = var0 + 1 >> 1;
      }

      ++var3;
      return var3 * (long)var5;
   }

   public Texture getCachedTexture(Image var1, Texture.WrapMode var2) {
      return this.getCachedTexture(var1, var2, false);
   }

   public Texture getCachedTexture(Image var1, Texture.WrapMode var2, boolean var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("Image must be non-null");
      } else {
         Map var4;
         if (var2 == Texture.WrapMode.CLAMP_TO_EDGE) {
            if (var3) {
               throw new IllegalArgumentException("Mipmap not supported with CLAMP mode: useMipmap = " + var3 + ", wrapMode = " + var2);
            }

            var4 = this.clampTexCache;
         } else {
            if (var2 != Texture.WrapMode.REPEAT) {
               throw new IllegalArgumentException("no caching for " + var2);
            }

            var4 = var3 ? this.mipmapTexCache : this.repeatTexCache;
         }

         Texture var5 = (Texture)var4.get(var1);
         if (var5 != null) {
            var5.lock();
            if (var5.isSurfaceLost()) {
               var4.remove(var1);
               var5 = null;
            }
         }

         int var6 = var1.getSerial();
         if (!var3 && var5 == null) {
            Texture var7 = (Texture)(var2 == Texture.WrapMode.REPEAT ? this.clampTexCache : this.repeatTexCache).get(var1);
            if (var7 != null) {
               var7.lock();
               if (!var7.isSurfaceLost()) {
                  var5 = var7.getSharedTexture(var2);
                  if (var5 != null) {
                     var5.contentsUseful();
                     var4.put(var1, var5);
                  }
               }

               var7.unlock();
            }
         }

         if (var5 == null) {
            int var12 = var1.getWidth();
            int var8 = var1.getHeight();
            TextureResourcePool var9 = this.getTextureResourcePool();
            long var10 = var3 ? sizeWithMipMap(var12, var8, var1.getPixelFormat()) : var9.estimateTextureSize(var12, var8, var1.getPixelFormat());
            if (!var9.prepareForAllocation(var10)) {
               return null;
            }

            var5 = this.createTexture(var1, Texture.Usage.DEFAULT, var2, var3);
            if (var5 != null) {
               var5.setLastImageSerial(var6);
               var4.put(var1, var5);
            }
         } else if (var5.getLastImageSerial() != var6) {
            var5.update(var1, 0, 0, var1.getWidth(), var1.getHeight(), false);
            var5.setLastImageSerial(var6);
         }

         return var5;
      }
   }

   public Texture createTexture(Image var1, Texture.Usage var2, Texture.WrapMode var3) {
      return this.createTexture(var1, var2, var3, false);
   }

   public Texture createTexture(Image var1, Texture.Usage var2, Texture.WrapMode var3, boolean var4) {
      PixelFormat var5 = var1.getPixelFormat();
      int var6 = var1.getWidth();
      int var7 = var1.getHeight();
      Texture var8 = this.createTexture(var5, var2, var3, var6, var7, var4);
      if (var8 != null) {
         var8.update(var1, 0, 0, var6, var7, true);
         var8.contentsUseful();
      }

      return var8;
   }

   public Texture createMaskTexture(int var1, int var2, Texture.WrapMode var3) {
      return this.createTexture(PixelFormat.BYTE_ALPHA, Texture.Usage.DEFAULT, var3, var1, var2);
   }

   public Texture createFloatTexture(int var1, int var2) {
      return this.createTexture(PixelFormat.FLOAT_XYZW, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_ZERO, var1, var2);
   }

   public void setRegionTexture(Texture var1) {
      this.regionTexture = var1;
      this.superShaderAllowed = PrismSettings.superShader && this.regionTexture != null && this.glyphTexture != null;
   }

   public Texture getRegionTexture() {
      return this.regionTexture;
   }

   public void setGlyphTexture(Texture var1) {
      this.glyphTexture = var1;
      this.superShaderAllowed = PrismSettings.superShader && this.regionTexture != null && this.glyphTexture != null;
   }

   public Texture getGlyphTexture() {
      return this.glyphTexture;
   }

   public boolean isSuperShaderAllowed() {
      return this.superShaderAllowed;
   }

   protected boolean canClampToZero() {
      return true;
   }

   protected boolean canClampToEdge() {
      return true;
   }

   protected boolean canRepeat() {
      return true;
   }

   public boolean isWrapModeSupported(Texture.WrapMode var1) {
      switch (var1) {
         case CLAMP_NOT_NEEDED:
            return true;
         case CLAMP_TO_EDGE:
            return this.canClampToEdge();
         case REPEAT:
            return this.canRepeat();
         case CLAMP_TO_ZERO:
            return this.canClampToZero();
         case CLAMP_TO_EDGE_SIMULATED:
         case CLAMP_TO_ZERO_SIMULATED:
         case REPEAT_SIMULATED:
            throw new InternalError("Cannot test support for simulated wrap modes");
         default:
            throw new InternalError("Unrecognized wrap mode: " + var1);
      }
   }
}
