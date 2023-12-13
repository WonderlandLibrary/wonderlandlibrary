package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;

public class DWGlyph implements Glyph {
   private DWFontStrike strike;
   private DWRITE_GLYPH_METRICS metrics;
   private DWRITE_GLYPH_RUN run;
   private float pixelXAdvance;
   private float pixelYAdvance;
   private RECT rect;
   private boolean drawShapes;
   private byte[][] pixelData;
   private RECT[] rects;
   private static final boolean CACHE_TARGET = true;
   private static IWICBitmap cachedBitmap;
   private static ID2D1RenderTarget cachedTarget;
   private static final int BITMAP_WIDTH = 256;
   private static final int BITMAP_HEIGHT = 256;
   private static final int BITMAP_PIXEL_FORMAT = 8;
   private static D2D1_COLOR_F BLACK = new D2D1_COLOR_F(0.0F, 0.0F, 0.0F, 1.0F);
   private static D2D1_COLOR_F WHITE = new D2D1_COLOR_F(1.0F, 1.0F, 1.0F, 1.0F);
   private static D2D1_MATRIX_3X2_F D2D2_MATRIX_IDENTITY = new D2D1_MATRIX_3X2_F(1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F);

   DWGlyph(DWFontStrike var1, int var2, boolean var3) {
      this.strike = var1;
      this.drawShapes = var3;
      int var4 = DWFontStrike.SUBPIXEL_Y ? 9 : 3;
      this.pixelData = new byte[var4][];
      this.rects = new RECT[var4];
      IDWriteFontFace var5 = var1.getFontFace();
      this.run = new DWRITE_GLYPH_RUN();
      this.run.fontFace = var5 != null ? var5.ptr : 0L;
      this.run.fontEmSize = var1.getSize();
      this.run.glyphIndices = (short)var2;
      this.run.glyphAdvances = 0.0F;
      this.run.advanceOffset = 0.0F;
      this.run.ascenderOffset = 0.0F;
      this.run.bidiLevel = 0;
      this.run.isSideways = false;
   }

   void checkMetrics() {
      if (this.metrics == null) {
         IDWriteFontFace var1 = this.strike.getFontFace();
         if (var1 != null) {
            this.metrics = var1.GetDesignGlyphMetrics(this.run.glyphIndices, false);
            if (this.metrics != null) {
               float var2 = (float)this.strike.getUpem();
               this.pixelXAdvance = (float)this.metrics.advanceWidth * this.strike.getSize() / var2;
               this.pixelYAdvance = 0.0F;
               if (this.strike.matrix != null) {
                  Point2D var3 = new Point2D(this.pixelXAdvance, this.pixelYAdvance);
                  this.strike.getTransform().transform(var3, var3);
                  this.pixelXAdvance = var3.x;
                  this.pixelYAdvance = var3.y;
               }
            }

         }
      }
   }

   void checkBounds() {
      if (this.rect == null) {
         byte var1 = 1;
         IDWriteGlyphRunAnalysis var2 = this.createAnalysis(0.0F, 0.0F);
         if (var2 != null) {
            this.rect = var2.GetAlphaTextureBounds(var1);
            if (this.rect == null || this.rect.right - this.rect.left == 0 || this.rect.bottom - this.rect.top == 0) {
               this.rect = var2.GetAlphaTextureBounds(0);
            }

            var2.Release();
         }

         if (this.rect == null) {
            this.rect = new RECT();
         } else {
            --this.rect.left;
            --this.rect.top;
            ++this.rect.right;
            ++this.rect.bottom;
         }

      }
   }

   byte[] getLCDMask(float var1, float var2) {
      IDWriteGlyphRunAnalysis var3 = this.createAnalysis(var1, var2);
      byte[] var4 = null;
      if (var3 != null) {
         byte var5 = 1;
         this.rect = var3.GetAlphaTextureBounds(var5);
         if (this.rect != null && this.rect.right - this.rect.left != 0 && this.rect.bottom - this.rect.top != 0) {
            var4 = var3.CreateAlphaTexture(var5, this.rect);
         } else {
            this.rect = var3.GetAlphaTextureBounds(0);
            if (this.rect != null && this.rect.right - this.rect.left != 0 && this.rect.bottom - this.rect.top != 0) {
               var4 = this.getD2DMask(var1, var2, true);
            }
         }

         var3.Release();
      }

      if (var4 == null) {
         var4 = new byte[0];
         this.rect = new RECT();
      }

      return var4;
   }

   byte[] getD2DMask(float var1, float var2, boolean var3) {
      this.checkBounds();
      if (this.getWidth() != 0 && this.getHeight() != 0 && this.run.fontFace != 0L) {
         float var4 = (float)this.rect.left;
         float var5 = (float)this.rect.top;
         int var6 = this.rect.right - this.rect.left;
         int var7 = this.rect.bottom - this.rect.top;
         boolean var8 = 256 >= var6 && 256 >= var7;
         IWICBitmap var9;
         ID2D1RenderTarget var10;
         if (var8) {
            var9 = this.getCachedBitmap();
            var10 = this.getCachedRenderingTarget();
         } else {
            var9 = this.createBitmap(var6, var7);
            var10 = this.createRenderingTarget(var9);
         }

         if (var9 != null && var10 != null) {
            DWRITE_MATRIX var11 = this.strike.matrix;
            D2D1_MATRIX_3X2_F var12;
            if (var11 != null) {
               var12 = new D2D1_MATRIX_3X2_F(var11.m11, var11.m12, var11.m21, var11.m22, -var4 + var1, -var5 + var2);
               var5 = 0.0F;
               var4 = 0.0F;
            } else {
               var12 = D2D2_MATRIX_IDENTITY;
               var4 -= var1;
               var5 -= var2;
            }

            var10.BeginDraw();
            var10.SetTransform(var12);
            var10.Clear(WHITE);
            D2D1_POINT_2F var13 = new D2D1_POINT_2F(-var4, -var5);
            ID2D1Brush var14 = var10.CreateSolidColorBrush(BLACK);
            if (!var3) {
               var10.SetTextAntialiasMode(2);
            }

            var10.DrawGlyphRun(var13, this.run, var14, 0);
            int var15 = var10.EndDraw();
            var14.Release();
            if (var15 != 0) {
               var9.Release();
               cachedBitmap = null;
               var10.Release();
               cachedTarget = null;
               if (PrismFontFactory.debugFonts) {
                  System.err.println("Rendering failed=" + var15);
               }

               this.rect.left = this.rect.top = this.rect.right = this.rect.bottom = 0;
               return null;
            } else {
               byte[] var16 = null;
               IWICBitmapLock var17 = var9.Lock(0, 0, var6, var7, 1);
               if (var17 != null) {
                  byte[] var18 = var17.GetDataPointer();
                  if (var18 != null) {
                     int var19 = var17.GetStride();
                     int var20 = 0;
                     int var21 = 0;
                     byte var22 = -1;
                     int var23;
                     int var24;
                     int var25;
                     if (var3) {
                        var16 = new byte[var6 * var7 * 3];

                        for(var23 = 0; var23 < var7; ++var23) {
                           var24 = var21;

                           for(var25 = 0; var25 < var6; ++var25) {
                              var16[var20++] = (byte)(var22 - var18[var24++]);
                              var16[var20++] = (byte)(var22 - var18[var24++]);
                              var16[var20++] = (byte)(var22 - var18[var24++]);
                              ++var24;
                           }

                           var21 += var19;
                        }
                     } else {
                        var16 = new byte[var6 * var7];

                        for(var23 = 0; var23 < var7; ++var23) {
                           var24 = var21;

                           for(var25 = 0; var25 < var6; ++var25) {
                              var16[var20++] = (byte)(var22 - var18[var24]);
                              var24 += 4;
                           }

                           var21 += var19;
                        }
                     }
                  }

                  var17.Release();
               }

               if (!var8) {
                  var9.Release();
                  var10.Release();
               }

               return var16;
            }
         } else {
            return new byte[0];
         }
      } else {
         return new byte[0];
      }
   }

   IDWriteGlyphRunAnalysis createAnalysis(float var1, float var2) {
      if (this.run.fontFace == 0L) {
         return null;
      } else {
         IDWriteFactory var3 = DWFactory.getDWriteFactory();
         int var4 = DWFontStrike.SUBPIXEL_Y ? 5 : 4;
         byte var5 = 0;
         DWRITE_MATRIX var6 = this.strike.matrix;
         float var7 = 1.0F;
         return var3.CreateGlyphRunAnalysis(this.run, var7, var6, var4, var5, var1, var2);
      }
   }

   IWICBitmap getCachedBitmap() {
      if (cachedBitmap == null) {
         cachedBitmap = this.createBitmap(256, 256);
      }

      return cachedBitmap;
   }

   ID2D1RenderTarget getCachedRenderingTarget() {
      if (cachedTarget == null) {
         cachedTarget = this.createRenderingTarget(this.getCachedBitmap());
      }

      return cachedTarget;
   }

   IWICBitmap createBitmap(int var1, int var2) {
      IWICImagingFactory var3 = DWFactory.getWICFactory();
      return var3.CreateBitmap(var1, var2, 8, 1);
   }

   ID2D1RenderTarget createRenderingTarget(IWICBitmap var1) {
      D2D1_RENDER_TARGET_PROPERTIES var2 = new D2D1_RENDER_TARGET_PROPERTIES();
      var2.type = 0;
      var2.pixelFormat.format = 0;
      var2.pixelFormat.alphaMode = 0;
      var2.dpiX = 0.0F;
      var2.dpiY = 0.0F;
      var2.usage = 0;
      var2.minLevel = 0;
      ID2D1Factory var3 = DWFactory.getD2DFactory();
      return var3.CreateWicBitmapRenderTarget(var1, var2);
   }

   public int getGlyphCode() {
      return this.run.glyphIndices;
   }

   public RectBounds getBBox() {
      return this.strike.getBBox(this.run.glyphIndices);
   }

   public float getAdvance() {
      this.checkMetrics();
      if (this.metrics == null) {
         return 0.0F;
      } else {
         float var1 = (float)this.strike.getUpem();
         return (float)this.metrics.advanceWidth * this.strike.getSize() / var1;
      }
   }

   public Shape getShape() {
      return this.strike.createGlyphOutline(this.run.glyphIndices);
   }

   public byte[] getPixelData() {
      return this.getPixelData(0);
   }

   public byte[] getPixelData(int var1) {
      byte[] var2 = this.pixelData[var1];
      if (var2 == null) {
         float var3 = 0.0F;
         float var4 = 0.0F;
         int var5 = var1;
         if (var1 >= 6) {
            var5 = var1 - 6;
            var4 = 0.66F;
         } else if (var1 >= 3) {
            var5 = var1 - 3;
            var4 = 0.33F;
         }

         if (var5 == 1) {
            var3 = 0.33F;
         }

         if (var5 == 2) {
            var3 = 0.66F;
         }

         this.pixelData[var1] = var2 = this.isLCDGlyph() ? this.getLCDMask(var3, var4) : this.getD2DMask(var3, var4, false);
         this.rects[var1] = this.rect;
      } else {
         this.rect = this.rects[var1];
      }

      return var2;
   }

   public float getPixelXAdvance() {
      this.checkMetrics();
      return this.pixelXAdvance;
   }

   public float getPixelYAdvance() {
      this.checkMetrics();
      return this.pixelYAdvance;
   }

   public int getWidth() {
      this.checkBounds();
      return (this.rect.right - this.rect.left) * (this.isLCDGlyph() ? 3 : 1);
   }

   public int getHeight() {
      this.checkBounds();
      return this.rect.bottom - this.rect.top;
   }

   public int getOriginX() {
      this.checkBounds();
      return this.rect.left;
   }

   public int getOriginY() {
      this.checkBounds();
      return this.rect.top;
   }

   public boolean isLCDGlyph() {
      return this.strike.getAAMode() == 1;
   }
}
