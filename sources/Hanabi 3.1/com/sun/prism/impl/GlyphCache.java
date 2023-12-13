package com.sun.prism.impl;

import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.packrect.RectanglePacker;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.paint.Color;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.WeakHashMap;

public class GlyphCache {
   private static final int WIDTH;
   private static final int HEIGHT;
   private static ByteBuffer emptyMask;
   private final BaseContext context;
   private final FontStrike strike;
   private static final int SEGSHIFT = 5;
   private static final int SEGSIZE = 32;
   private static final int SEGMASK = 31;
   HashMap glyphDataMap = new HashMap();
   private static final int SUBPIXEL_SHIFT = 27;
   private RectanglePacker packer;
   private boolean isLCDCache;
   static WeakHashMap greyPackerMap;
   static WeakHashMap lcdPackerMap;

   public GlyphCache(BaseContext var1, FontStrike var2) {
      this.context = var1;
      this.strike = var2;
      this.isLCDCache = var2.getAAMode() == 1;
      WeakHashMap var3 = this.isLCDCache ? lcdPackerMap : greyPackerMap;
      this.packer = (RectanglePacker)var3.get(var1);
      if (this.packer == null) {
         ResourceFactory var4 = var1.getResourceFactory();
         Texture var5 = var4.createMaskTexture(WIDTH, HEIGHT, Texture.WrapMode.CLAMP_NOT_NEEDED);
         var5.contentsUseful();
         var5.makePermanent();
         if (!this.isLCDCache) {
            var4.setGlyphTexture(var5);
         }

         var5.setLinearFiltering(false);
         this.packer = new RectanglePacker(var5, WIDTH, HEIGHT);
         var3.put(var1, this.packer);
      }

   }

   public void render(BaseContext var1, GlyphList var2, float var3, float var4, int var5, int var6, Color var7, Color var8, BaseTransform var9, BaseBounds var10) {
      int var11;
      int var12;
      if (this.isLCDCache) {
         var11 = var1.getLCDBuffer().getPhysicalWidth();
         var12 = var1.getLCDBuffer().getPhysicalHeight();
      } else {
         var11 = 1;
         var12 = 1;
      }

      Texture var13 = this.getBackingStore();
      VertexBuffer var14 = var1.getVertexBuffer();
      int var15 = var2.getGlyphCount();
      Color var16 = null;
      Point2D var17 = new Point2D();

      for(int var18 = 0; var18 < var15; ++var18) {
         int var19 = var2.getGlyphCode(var18);
         if ((var19 & 16777215) != 65535) {
            var17.setLocation(var3 + var2.getPosX(var18), var4 + var2.getPosY(var18));
            var9.transform(var17, var17);
            int var20 = this.strike.getQuantizedPosition(var17);
            GlyphData var21 = this.getCachedGlyph(var19, var20);
            if (var21 != null) {
               if (var10 != null) {
                  if (var3 + var2.getPosX(var18) > var10.getMaxX()) {
                     break;
                  }

                  if (var3 + var2.getPosX(var18 + 1) < var10.getMinX()) {
                     continue;
                  }
               }

               if (var7 != null && var8 != null) {
                  int var22 = var2.getCharOffset(var18);
                  if (var5 <= var22 && var22 < var6) {
                     if (var7 != var16) {
                        var14.setPerVertexColor(var7, 1.0F);
                        var16 = var7;
                     }
                  } else if (var8 != var16) {
                     var14.setPerVertexColor(var8, 1.0F);
                     var16 = var8;
                  }
               }

               this.addDataToQuad(var21, var14, var13, var17.x, var17.y, (float)var11, (float)var12);
            }
         }
      }

   }

   private void addDataToQuad(GlyphData var1, VertexBuffer var2, Texture var3, float var4, float var5, float var6, float var7) {
      var5 = (float)Math.round(var5);
      Rectangle var8 = var1.getRect();
      if (var8 != null) {
         int var9 = var1.getBlankBoundary();
         float var10 = (float)(var8.width - var9 * 2);
         float var11 = (float)(var8.height - var9 * 2);
         float var12 = (float)var1.getOriginX() + var4;
         float var13 = (float)var1.getOriginY() + var5;
         float var15 = var13 + var11;
         float var16 = (float)var3.getPhysicalWidth();
         float var17 = (float)var3.getPhysicalHeight();
         float var18 = (float)(var8.x + var9) / var16;
         float var19 = (float)(var8.y + var9) / var17;
         float var20 = var18 + var10 / var16;
         float var21 = var19 + var11 / var17;
         float var14;
         if (this.isLCDCache) {
            var12 = (float)Math.round(var12 * 3.0F) / 3.0F;
            var14 = var12 + var10 / 3.0F;
            float var22 = var12 / var6;
            float var23 = var14 / var6;
            float var24 = var13 / var7;
            float var25 = var15 / var7;
            var2.addQuad(var12, var13, var14, var15, var18, var19, var20, var21, var22, var24, var23, var25);
         } else {
            var12 = (float)Math.round(var12);
            var14 = var12 + var10;
            if (this.context.isSuperShaderEnabled()) {
               var2.addSuperQuad(var12, var13, var14, var15, var18, var19, var20, var21, true);
            } else {
               var2.addQuad(var12, var13, var14, var15, var18, var19, var20, var21);
            }
         }

      }
   }

   public Texture getBackingStore() {
      return this.packer.getBackingStore();
   }

   public void clear() {
      this.glyphDataMap.clear();
   }

   private void clearAll() {
      this.context.flushVertexBuffer();
      this.context.clearGlyphCaches();
      this.packer.clear();
   }

   private GlyphData getCachedGlyph(int var1, int var2) {
      int var3 = var1 >>> 5;
      int var4 = var1 & 31;
      var3 |= var2 << 27;
      GlyphData[] var5 = (GlyphData[])this.glyphDataMap.get(var3);
      if (var5 != null) {
         if (var5[var4] != null) {
            return var5[var4];
         }
      } else {
         var5 = new GlyphData[32];
         this.glyphDataMap.put(var3, var5);
      }

      GlyphData var6 = null;
      Glyph var7 = this.strike.getGlyph(var1);
      if (var7 != null) {
         byte[] var8 = var7.getPixelData(var2);
         if (var8 != null && var8.length != 0) {
            MaskData var9 = MaskData.create(var8, var7.getOriginX(), var7.getOriginY(), var7.getWidth(), var7.getHeight());
            byte var10 = 1;
            int var11 = var9.getWidth() + 2 * var10;
            int var12 = var9.getHeight() + 2 * var10;
            int var13 = var9.getOriginX();
            int var14 = var9.getOriginY();
            Rectangle var15 = new Rectangle(0, 0, var11, var12);
            var6 = new GlyphData(var13, var14, var10, var7.getPixelXAdvance(), var7.getPixelYAdvance(), var15);
            if (!this.packer.add(var15)) {
               if (PulseLogger.PULSE_LOGGING_ENABLED) {
                  PulseLogger.incrementCounter("Font Glyph Cache Cleared");
               }

               this.clearAll();
               if (!this.packer.add(var15)) {
                  if (PrismSettings.verbose) {
                     System.out.println(var15 + " won't fit in GlyphCache");
                  }

                  return null;
               }
            }

            boolean var16 = true;
            Texture var17 = this.getBackingStore();
            int var18 = var15.width;
            int var19 = var15.height;
            int var20 = var17.getPixelFormat().getBytesPerPixelUnit();
            int var21 = var18 * var20;
            int var22 = var21 * var19;
            if (emptyMask == null || var22 > emptyMask.capacity()) {
               emptyMask = BufferUtil.newByteBuffer(var22);
            }

            try {
               var17.update(emptyMask, var17.getPixelFormat(), var15.x, var15.y, 0, 0, var18, var19, var21, var16);
            } catch (Exception var24) {
               if (PrismSettings.verbose) {
                  var24.printStackTrace();
               }

               return null;
            }

            var9.uploadToTexture(var17, var10 + var15.x, var10 + var15.y, var16);
         } else {
            var6 = new GlyphData(0, 0, 0, var7.getPixelXAdvance(), var7.getPixelYAdvance(), (Rectangle)null);
         }

         var5[var4] = var6;
      }

      return var6;
   }

   static {
      WIDTH = PrismSettings.glyphCacheWidth;
      HEIGHT = PrismSettings.glyphCacheHeight;
      greyPackerMap = new WeakHashMap();
      lcdPackerMap = new WeakHashMap();
   }

   static class GlyphData {
      private final int originX;
      private final int originY;
      private final int blankBoundary;
      private final float xAdvance;
      private final float yAdvance;
      private final Rectangle rect;

      GlyphData(int var1, int var2, int var3, float var4, float var5, Rectangle var6) {
         this.originX = var1;
         this.originY = var2;
         this.blankBoundary = var3;
         this.xAdvance = var4;
         this.yAdvance = var5;
         this.rect = var6;
      }

      int getOriginX() {
         return this.originX;
      }

      int getOriginY() {
         return this.originY;
      }

      int getBlankBoundary() {
         return this.blankBoundary;
      }

      float getXAdvance() {
         return this.xAdvance;
      }

      float getYAdvance() {
         return this.yAdvance;
      }

      Rectangle getRect() {
         return this.rect;
      }
   }
}
