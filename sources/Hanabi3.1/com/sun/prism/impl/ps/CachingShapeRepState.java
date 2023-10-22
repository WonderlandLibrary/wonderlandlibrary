package com.sun.prism.impl.ps;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.Texture;
import com.sun.prism.impl.Disposer;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.VertexBuffer;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.impl.shape.ShapeUtil;
import com.sun.prism.paint.Paint;
import com.sun.prism.ps.Shader;
import java.util.Arrays;
import java.util.Comparator;

class CachingShapeRepState {
   private static final BaseTransform IDENT;
   private static final MaskCache maskCache;
   private int renderCount;
   private Boolean tryCache;
   private BaseTransform lastXform;
   private final MaskTexData texData = new MaskTexData();
   private float[] bbox;
   private final Object disposerReferent = new Object();
   private final Disposer.Record disposerRecord;

   private static boolean equalsIgnoreTranslation(BaseTransform var0, BaseTransform var1) {
      if (var0 == var1) {
         return true;
      } else {
         return var0.getMxx() == var1.getMxx() && var0.getMxy() == var1.getMxy() && var0.getMyx() == var1.getMyx() && var0.getMyy() == var1.getMyy();
      }
   }

   CachingShapeRepState() {
      this.disposerRecord = new CSRDisposerRecord(this.texData);
      Disposer.addRecord(this.disposerReferent, this.disposerRecord);
   }

   void fillNoCache(Graphics var1, Shape var2) {
      var1.fill(var2);
   }

   void drawNoCache(Graphics var1, Shape var2) {
      var1.draw(var2);
   }

   void invalidate() {
      this.renderCount = 0;
      this.tryCache = null;
      this.lastXform = null;
      this.bbox = null;
   }

   private void invalidateMaskTexData() {
      this.tryCache = null;
      this.lastXform = null;
      maskCache.unref(this.texData);
   }

   void render(Graphics var1, Shape var2, RectBounds var3, BasicStroke var4) {
      BaseTransform var5 = var1.getTransformNoClone();
      if (this.lastXform == null || !equalsIgnoreTranslation(var5, this.lastXform)) {
         this.invalidateMaskTexData();
         if (this.lastXform != null) {
            this.renderCount = 0;
         }
      }

      if (this.texData.cacheEntry != null) {
         this.texData.maskTex.lock();
         if (this.texData.maskTex.isSurfaceLost()) {
            this.texData.maskTex.unlock();
            this.invalidateMaskTexData();
         }
      }

      RectBounds var6 = null;
      boolean var7 = false;
      if (this.tryCache == null) {
         if (var5.isIdentity()) {
            var6 = var3;
         } else {
            var6 = new RectBounds();
            var7 = true;
            var6 = (RectBounds)var5.transform((BaseBounds)var3, (BaseBounds)var6);
         }

         this.tryCache = !var6.isEmpty() && maskCache.hasRoom(var6);
      }

      ++this.renderCount;
      if (this.tryCache != Boolean.FALSE && this.renderCount > 1 && var1 instanceof BaseShaderGraphics && !((BaseShaderGraphics)var1).isComplexPaint()) {
         BaseShaderGraphics var8 = (BaseShaderGraphics)var1;
         BaseShaderContext var9 = var8.getContext();
         if (this.lastXform == null || !this.lastXform.equals(var5)) {
            if (var6 == null) {
               if (var5.isIdentity()) {
                  var6 = var3;
               } else {
                  var6 = new RectBounds();
                  var7 = true;
                  var6 = (RectBounds)var5.transform((BaseBounds)var3, (BaseBounds)var6);
               }
            }

            if (this.texData.cacheEntry != null) {
               this.texData.adjustOrigin(var5);
            } else {
               maskCache.get(var9, this.texData, var2, var4, var5, var6, var7, var1.isAntialiasedShape());
            }

            if (this.lastXform == null) {
               this.lastXform = var5.copy();
            } else {
               this.lastXform.setTransform(var5);
            }
         }

         Paint var10 = var8.getPaint();
         float var11 = 0.0F;
         float var12 = 0.0F;
         float var13 = 0.0F;
         float var14 = 0.0F;
         if (var10.isProportional()) {
            if (this.bbox == null) {
               this.bbox = new float[]{Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
               Shape.accumulate(this.bbox, var2, BaseTransform.IDENTITY_TRANSFORM);
            }

            var11 = this.bbox[0];
            var12 = this.bbox[1];
            var13 = this.bbox[2] - var11;
            var14 = this.bbox[3] - var12;
         }

         int var15 = this.texData.maskW;
         int var16 = this.texData.maskH;
         Texture var17 = this.texData.maskTex;
         float var18 = (float)var17.getPhysicalWidth();
         float var19 = (float)var17.getPhysicalHeight();
         float var20 = this.texData.maskX;
         float var21 = this.texData.maskY;
         float var22 = var20 + (float)var15;
         float var23 = var21 + (float)var16;
         float var24 = (float)var17.getContentX() / var18;
         float var25 = (float)var17.getContentY() / var19;
         float var26 = var24 + (float)var15 / var18;
         float var27 = var25 + (float)var16 / var19;
         if (PrismSettings.primTextureSize != 0) {
            Shader var28 = var9.validatePaintOp(var8, IDENT, BaseShaderContext.MaskType.ALPHA_TEXTURE, this.texData.maskTex, var11, var12, var13, var14);
            VertexBuffer var29 = var9.getVertexBuffer();
            var29.addQuad(var20, var21, var22, var23, var24, var25, var26, var27, var8.getPaintTextureTx(var5, var28, var11, var12, var13, var14));
         } else {
            var9.validatePaintOp(var8, IDENT, this.texData.maskTex, var11, var12, var13, var14);
            VertexBuffer var30 = var9.getVertexBuffer();
            var30.addQuad(var20, var21, var22, var23, var24, var25, var26, var27);
         }

         var17.unlock();
      } else {
         if (var4 == null) {
            this.fillNoCache(var1, var2);
         } else {
            this.drawNoCache(var1, var2);
         }

      }
   }

   void dispose() {
      this.invalidate();
   }

   static {
      IDENT = BaseTransform.IDENTITY_TRANSFORM;
      maskCache = new MaskCache();
   }

   private static class CSRDisposerRecord implements Disposer.Record {
      private MaskTexData texData;

      private CSRDisposerRecord(MaskTexData var1) {
         this.texData = var1;
      }

      public void dispose() {
         if (this.texData != null) {
            CachingShapeRepState.maskCache.unref(this.texData);
            this.texData = null;
         }

      }

      // $FF: synthetic method
      CSRDisposerRecord(MaskTexData var1, Object var2) {
         this(var1);
      }
   }

   private static class MaskCache {
      private static final int MAX_MASK_DIM = 512;
      private static final int MAX_SIZE_IN_PIXELS = 4194304;
      private static Comparator comparator = (var0, var1) -> {
         int var2 = Float.compare(var0.xformBounds.getWidth(), var1.xformBounds.getWidth());
         return var2 != 0 ? var2 : Float.compare(var0.xformBounds.getHeight(), var1.xformBounds.getHeight());
      };
      private CacheEntry[] entries;
      private int entriesSize;
      private int totalPixels;
      private CacheEntry tmpKey;

      private MaskCache() {
         this.entries = new CacheEntry[8];
         this.entriesSize = 0;
         this.tmpKey = new CacheEntry();
         this.tmpKey.xformBounds = new RectBounds();
      }

      private void ensureSize(int var1) {
         if (this.entries.length < var1) {
            CacheEntry[] var2 = new CacheEntry[var1 * 3 / 2];
            System.arraycopy(this.entries, 0, var2, 0, this.entries.length);
            this.entries = var2;
         }

      }

      private void addEntry(CacheEntry var1) {
         this.ensureSize(this.entriesSize + 1);
         int var2 = Arrays.binarySearch(this.entries, 0, this.entriesSize, var1, comparator);
         if (var2 < 0) {
            var2 = ~var2;
         }

         System.arraycopy(this.entries, var2, this.entries, var2 + 1, this.entriesSize - var2);
         this.entries[var2] = var1;
         ++this.entriesSize;
      }

      private void removeEntry(CacheEntry var1) {
         int var2 = Arrays.binarySearch(this.entries, 0, this.entriesSize, var1, comparator);
         if (var2 < 0) {
            throw new IllegalStateException("Trying to remove a cached item that's not in the cache");
         } else {
            if (this.entries[var2] != var1) {
               this.tmpKey.xformBounds.deriveWithNewBounds(0.0F, 0.0F, 0.0F, var1.xformBounds.getWidth(), Math.nextAfter(var1.xformBounds.getHeight(), Double.NEGATIVE_INFINITY), 0.0F);
               var2 = Arrays.binarySearch(this.entries, 0, this.entriesSize, this.tmpKey, comparator);
               if (var2 < 0) {
                  var2 = ~var2;
               }

               this.tmpKey.xformBounds.deriveWithNewBounds(0.0F, 0.0F, 0.0F, var1.xformBounds.getWidth(), Math.nextAfter(var1.xformBounds.getHeight(), Double.POSITIVE_INFINITY), 0.0F);
               int var3 = Arrays.binarySearch(this.entries, 0, this.entriesSize, this.tmpKey, comparator);
               if (var3 < 0) {
                  var3 = ~var3;
               }

               while(this.entries[var2] != var1 && var2 < var3) {
                  ++var2;
               }

               if (var2 >= var3) {
                  throw new IllegalStateException("Trying to remove a cached item that's not in the cache");
               }
            }

            System.arraycopy(this.entries, var2 + 1, this.entries, var2, this.entriesSize - var2 - 1);
            --this.entriesSize;
         }
      }

      boolean hasRoom(RectBounds var1) {
         int var2 = (int)(var1.getWidth() + 0.5F);
         int var3 = (int)(var1.getHeight() + 0.5F);
         int var4 = var2 * var3;
         return var2 <= 512 && var3 <= 512 && this.totalPixels + var4 <= 4194304;
      }

      boolean entryMatches(CacheEntry var1, Shape var2, BasicStroke var3, BaseTransform var4, boolean var5) {
         boolean var10000;
         label29: {
            if (var1.antialiasedShape == var5 && CachingShapeRepState.equalsIgnoreTranslation(var4, var1.xform) && var1.shape.equals(var2)) {
               if (var3 == null) {
                  if (var1.stroke == null) {
                     break label29;
                  }
               } else if (var3.equals(var1.stroke)) {
                  break label29;
               }
            }

            var10000 = false;
            return var10000;
         }

         var10000 = true;
         return var10000;
      }

      void get(BaseShaderContext var1, MaskTexData var2, Shape var3, BasicStroke var4, BaseTransform var5, RectBounds var6, boolean var7, boolean var8) {
         if (var2 == null) {
            throw new InternalError("MaskTexData must be non-null");
         } else if (var2.cacheEntry != null) {
            throw new InternalError("CacheEntry should already be null");
         } else {
            this.tmpKey.xformBounds.deriveWithNewBounds(0.0F, 0.0F, 0.0F, var6.getWidth(), Math.nextAfter(var6.getHeight(), Double.NEGATIVE_INFINITY), 0.0F);
            int var9 = Arrays.binarySearch(this.entries, 0, this.entriesSize, this.tmpKey, comparator);
            if (var9 < 0) {
               var9 = ~var9;
            }

            this.tmpKey.xformBounds.deriveWithNewBounds(0.0F, 0.0F, 0.0F, var6.getWidth(), Math.nextAfter(var6.getHeight(), Double.POSITIVE_INFINITY), 0.0F);
            int var10 = Arrays.binarySearch(this.entries, 0, this.entriesSize, this.tmpKey, comparator);
            if (var10 < 0) {
               var10 = ~var10;
            }

            for(; var9 < var10; ++var9) {
               CacheEntry var11 = this.entries[var9];
               if (this.entryMatches(var11, var3, var4, var5, var8)) {
                  var11.texData.maskTex.lock();
                  if (!var11.texData.maskTex.isSurfaceLost()) {
                     ++var11.refCount;
                     var11.texData.copyInto(var2);
                     var2.cacheEntry = var11;
                     var2.adjustOrigin(var5);
                     return;
                  }

                  var11.texData.maskTex.unlock();
               }
            }

            MaskData var15 = ShapeUtil.rasterizeShape(var3, var4, var6, var5, true, var8);
            int var12 = var15.getWidth();
            int var13 = var15.getHeight();
            var2.maskX = (float)var15.getOriginX();
            var2.maskY = (float)var15.getOriginY();
            var2.maskW = var12;
            var2.maskH = var13;
            var2.maskTex = var1.getResourceFactory().createMaskTexture(var12, var13, Texture.WrapMode.CLAMP_TO_ZERO);
            var15.uploadToTexture(var2.maskTex, 0, 0, false);
            var2.maskTex.contentsUseful();
            CacheEntry var14 = new CacheEntry();
            var14.shape = var3.copy();
            if (var4 != null) {
               var14.stroke = var4.copy();
            }

            var14.xform = var5.copy();
            var14.xformBounds = var7 ? var6 : (RectBounds)var6.copy();
            var14.texData = var2.copy();
            var14.antialiasedShape = var8;
            var14.refCount = 1;
            var2.cacheEntry = var14;
            this.addEntry(var14);
            this.totalPixels += var12 * var13;
         }
      }

      void unref(MaskTexData var1) {
         if (var1 == null) {
            throw new InternalError("MaskTexData must be non-null");
         } else {
            CacheEntry var2 = var1.cacheEntry;
            if (var2 != null) {
               var1.cacheEntry = null;
               var1.maskTex = null;
               --var2.refCount;
               if (var2.refCount <= 0) {
                  this.removeEntry(var2);
                  var2.shape = null;
                  var2.stroke = null;
                  var2.xform = null;
                  var2.xformBounds = null;
                  var2.texData.maskTex.dispose();
                  var2.antialiasedShape = false;
                  var2.texData = null;
                  this.totalPixels -= var1.maskW * var1.maskH;
               }

            }
         }
      }

      // $FF: synthetic method
      MaskCache(Object var1) {
         this();
      }
   }

   private static class CacheEntry {
      Shape shape;
      BasicStroke stroke;
      BaseTransform xform;
      RectBounds xformBounds;
      MaskTexData texData;
      boolean antialiasedShape;
      int refCount;

      private CacheEntry() {
      }

      // $FF: synthetic method
      CacheEntry(Object var1) {
         this();
      }
   }

   private static class MaskTexData {
      private CacheEntry cacheEntry;
      private Texture maskTex;
      private float maskX;
      private float maskY;
      private int maskW;
      private int maskH;

      private MaskTexData() {
      }

      void adjustOrigin(BaseTransform var1) {
         float var2 = (float)(var1.getMxt() - this.cacheEntry.xform.getMxt());
         float var3 = (float)(var1.getMyt() - this.cacheEntry.xform.getMyt());
         this.maskX = this.cacheEntry.texData.maskX + var2;
         this.maskY = this.cacheEntry.texData.maskY + var3;
      }

      MaskTexData copy() {
         MaskTexData var1 = new MaskTexData();
         var1.cacheEntry = this.cacheEntry;
         var1.maskTex = this.maskTex;
         var1.maskX = this.maskX;
         var1.maskY = this.maskY;
         var1.maskW = this.maskW;
         var1.maskH = this.maskH;
         return var1;
      }

      void copyInto(MaskTexData var1) {
         if (var1 == null) {
            throw new InternalError("MaskTexData must be non-null");
         } else {
            var1.cacheEntry = this.cacheEntry;
            var1.maskTex = this.maskTex;
            var1.maskX = this.maskX;
            var1.maskY = this.maskY;
            var1.maskW = this.maskW;
            var1.maskH = this.maskH;
         }
      }

      // $FF: synthetic method
      MaskTexData(Object var1) {
         this();
      }
   }
}
