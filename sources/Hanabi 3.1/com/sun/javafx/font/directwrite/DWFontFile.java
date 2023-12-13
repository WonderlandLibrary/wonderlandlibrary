package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.Disposer;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;

class DWFontFile extends PrismFontFile {
   private IDWriteFontFace fontFace = this.createFontFace();
   private DWDisposer disposer;

   DWFontFile(String var1, String var2, int var3, boolean var4, boolean var5, boolean var6, boolean var7) throws Exception {
      super(var1, var2, var3, var4, var5, var6, var7);
      if (PrismFontFactory.debugFonts && this.fontFace == null) {
         System.err.println("Failed to create IDWriteFontFace for " + this);
      }

      if (var6) {
         this.disposer = new DWDisposer(this.fontFace);
         Disposer.addRecord(this, this.disposer);
      }

   }

   private IDWriteFontFace createEmbeddedFontFace() {
      IDWriteFactory var1 = DWFactory.getDWriteFactory();
      IDWriteFontFile var2 = var1.CreateFontFileReference(this.getFileName());
      if (var2 == null) {
         return null;
      } else {
         boolean[] var3 = new boolean[1];
         int[] var4 = new int[1];
         int[] var5 = new int[1];
         int[] var6 = new int[1];
         int var7 = var2.Analyze(var3, var4, var5, var6);
         IDWriteFontFace var8 = null;
         if (var7 == 0 && var3[0]) {
            int var9 = this.getFontIndex();
            byte var10 = 0;
            var8 = var1.CreateFontFace(var5[0], var2, var9, var10);
         }

         var2.Release();
         return var8;
      }
   }

   private IDWriteFontFace createFontFace() {
      if (this.isEmbeddedFont()) {
         return this.createEmbeddedFontFace();
      } else {
         IDWriteFontCollection var1 = DWFactory.getFontCollection();
         int var2 = var1.FindFamilyName(this.getFamilyName());
         if (var2 == -1) {
            return this.createEmbeddedFontFace();
         } else {
            IDWriteFontFamily var3 = var1.GetFontFamily(var2);
            if (var3 == null) {
               return null;
            } else {
               int var4 = this.isBold() ? 700 : 400;
               byte var5 = 5;
               int var6 = this.isItalic() ? 2 : 0;
               IDWriteFont var7 = var3.GetFirstMatchingFont(var4, var5, var6);
               var3.Release();
               if (var7 == null) {
                  return null;
               } else {
                  IDWriteFontFace var8 = var7.CreateFontFace();
                  var7.Release();
                  return var8;
               }
            }
         }
      }
   }

   IDWriteFontFace getFontFace() {
      return this.fontFace;
   }

   Path2D getGlyphOutline(int var1, float var2) {
      if (this.fontFace == null) {
         return null;
      } else {
         return var2 == 0.0F ? new Path2D() : this.fontFace.GetGlyphRunOutline(var2, (short)var1, false);
      }
   }

   RectBounds getBBox(int var1, float var2) {
      float[] var3 = new float[4];
      this.getGlyphBoundingBox(var1, var2, var3);
      return new RectBounds(var3[0], var3[1], var3[2], var3[3]);
   }

   protected int[] createGlyphBoundingBox(int var1) {
      if (this.fontFace == null) {
         return null;
      } else {
         DWRITE_GLYPH_METRICS var2 = this.fontFace.GetDesignGlyphMetrics((short)var1, false);
         if (var2 == null) {
            return null;
         } else {
            int[] var3 = new int[]{var2.leftSideBearing, var2.verticalOriginY - var2.advanceHeight + var2.bottomSideBearing, var2.advanceWidth - var2.rightSideBearing, var2.verticalOriginY - var2.topSideBearing};
            return var3;
         }
      }
   }

   protected PrismFontStrike createStrike(float var1, BaseTransform var2, int var3, FontStrikeDesc var4) {
      return new DWFontStrike(this, var1, var2, var3, var4);
   }

   protected synchronized void disposeOnShutdown() {
      if (this.fontFace != null) {
         if (this.disposer != null) {
            this.disposer.dispose();
         } else {
            this.fontFace.Release();
            if (PrismFontFactory.debugFonts) {
               System.err.println("null disposer for " + this.fontFace);
            }
         }

         if (PrismFontFactory.debugFonts) {
            System.err.println("fontFace freed: " + this.fontFace);
         }

         this.fontFace = null;
      }

      super.disposeOnShutdown();
   }
}
