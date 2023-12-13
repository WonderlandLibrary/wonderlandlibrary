package com.sun.prism.d3d;

import com.sun.prism.Image;
import com.sun.prism.PhongMaterial;
import com.sun.prism.Texture;
import com.sun.prism.TextureMap;
import com.sun.prism.impl.BaseGraphicsResource;
import com.sun.prism.impl.Disposer;
import sun.util.logging.PlatformLogger;

class D3DPhongMaterial extends BaseGraphicsResource implements PhongMaterial {
   static int count = 0;
   private final D3DContext context;
   private final long nativeHandle;
   private TextureMap[] maps;

   private D3DPhongMaterial(D3DContext var1, long var2, Disposer.Record var4) {
      super(var4);
      this.maps = new TextureMap[MAX_MAP_TYPE];
      this.context = var1;
      this.nativeHandle = var2;
      ++count;
   }

   static D3DPhongMaterial create(D3DContext var0) {
      long var1 = var0.createD3DPhongMaterial();
      return new D3DPhongMaterial(var0, var1, new D3DPhongMaterialDisposerRecord(var0, var1));
   }

   long getNativeHandle() {
      return this.nativeHandle;
   }

   public void setDiffuseColor(float var1, float var2, float var3, float var4) {
      this.context.setDiffuseColor(this.nativeHandle, var1, var2, var3, var4);
   }

   public void setSpecularColor(boolean var1, float var2, float var3, float var4, float var5) {
      this.context.setSpecularColor(this.nativeHandle, var1, var2, var3, var4, var5);
   }

   public void setTextureMap(TextureMap var1) {
      this.maps[var1.getType().ordinal()] = var1;
   }

   private Texture setupTexture(TextureMap var1, boolean var2) {
      Image var3 = var1.getImage();
      Texture var4 = var3 == null ? null : this.context.getResourceFactory().getCachedTexture(var3, Texture.WrapMode.REPEAT, var2);
      long var5 = var4 != null ? ((D3DTexture)var4).getNativeTextureObject() : 0L;
      this.context.setMap(this.nativeHandle, var1.getType().ordinal(), var5);
      return var4;
   }

   public void lockTextureMaps() {
      for(int var1 = 0; var1 < MAX_MAP_TYPE; ++var1) {
         Texture var2 = this.maps[var1].getTexture();
         if (!this.maps[var1].isDirty() && var2 != null) {
            var2.lock();
            if (!var2.isSurfaceLost()) {
               continue;
            }
         }

         boolean var3 = var1 == PhongMaterial.DIFFUSE || var1 == PhongMaterial.SELF_ILLUM;
         var2 = this.setupTexture(this.maps[var1], var3);
         this.maps[var1].setTexture(var2);
         this.maps[var1].setDirty(false);
         if (this.maps[var1].getImage() != null && var2 == null) {
            String var4 = PhongMaterial.class.getName();
            PlatformLogger.getLogger(var4).warning("Warning: Low on texture resources. Cannot create texture.");
         }
      }

   }

   public void unlockTextureMaps() {
      for(int var1 = 0; var1 < MAX_MAP_TYPE; ++var1) {
         Texture var2 = this.maps[var1].getTexture();
         if (var2 != null) {
            var2.unlock();
         }
      }

   }

   public void dispose() {
      this.disposerRecord.dispose();
      --count;
   }

   public int getCount() {
      return count;
   }

   static class D3DPhongMaterialDisposerRecord implements Disposer.Record {
      private final D3DContext context;
      private long nativeHandle;

      D3DPhongMaterialDisposerRecord(D3DContext var1, long var2) {
         this.context = var1;
         this.nativeHandle = var2;
      }

      void traceDispose() {
      }

      public void dispose() {
         if (this.nativeHandle != 0L) {
            this.traceDispose();
            this.context.releaseD3DPhongMaterial(this.nativeHandle);
            this.nativeHandle = 0L;
         }

      }
   }
}
