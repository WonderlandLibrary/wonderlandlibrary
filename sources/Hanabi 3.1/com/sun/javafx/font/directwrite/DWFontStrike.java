package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;

class DWFontStrike extends PrismFontStrike {
   DWRITE_MATRIX matrix;
   static final boolean SUBPIXEL_ON;
   static final boolean SUBPIXEL_Y;
   static final boolean SUBPIXEL_NATIVE;

   DWFontStrike(DWFontFile var1, float var2, BaseTransform var3, int var4, FontStrikeDesc var5) {
      super(var1, var2, var3, var4, var5);
      float var6 = PrismFontFactory.getFontSizeLimit();
      if (var3.isTranslateOrIdentity()) {
         this.drawShapes = var2 > var6;
      } else {
         BaseTransform var7 = this.getTransform();
         this.matrix = new DWRITE_MATRIX();
         this.matrix.m11 = (float)var7.getMxx();
         this.matrix.m12 = (float)var7.getMyx();
         this.matrix.m21 = (float)var7.getMxy();
         this.matrix.m22 = (float)var7.getMyy();
         if (Math.abs(this.matrix.m11 * var2) > var6 || Math.abs(this.matrix.m12 * var2) > var6 || Math.abs(this.matrix.m21 * var2) > var6 || Math.abs(this.matrix.m22 * var2) > var6) {
            this.drawShapes = true;
         }
      }

   }

   protected DisposerRecord createDisposer(FontStrikeDesc var1) {
      return null;
   }

   public int getQuantizedPosition(Point2D var1) {
      if (SUBPIXEL_ON && (this.matrix == null || SUBPIXEL_NATIVE) && (this.getAAMode() == 0 || SUBPIXEL_NATIVE)) {
         float var2 = var1.x;
         var1.x = (float)((int)var1.x);
         var2 -= var1.x;
         int var3 = 0;
         if (var2 >= 0.66F) {
            var3 = 2;
         } else if (var2 >= 0.33F) {
            var3 = 1;
         }

         if (SUBPIXEL_Y) {
            var2 = var1.y;
            var1.y = (float)((int)var1.y);
            var2 -= var1.y;
            if (var2 >= 0.66F) {
               var3 += 6;
            } else if (var2 >= 0.33F) {
               var3 += 3;
            }
         } else {
            var1.y = (float)Math.round(var1.y);
         }

         return var3;
      } else {
         return super.getQuantizedPosition(var1);
      }
   }

   IDWriteFontFace getFontFace() {
      DWFontFile var1 = (DWFontFile)this.getFontResource();
      return var1.getFontFace();
   }

   RectBounds getBBox(int var1) {
      DWFontFile var2 = (DWFontFile)this.getFontResource();
      return var2.getBBox(var1, this.getSize());
   }

   int getUpem() {
      return ((DWFontFile)this.getFontResource()).getUnitsPerEm();
   }

   protected Path2D createGlyphOutline(int var1) {
      DWFontFile var2 = (DWFontFile)this.getFontResource();
      return var2.getGlyphOutline(var1, this.getSize());
   }

   protected Glyph createGlyph(int var1) {
      return new DWGlyph(this, var1, this.drawShapes);
   }

   static {
      int var0 = PrismFontFactory.getFontFactory().getSubPixelMode();
      SUBPIXEL_ON = (var0 & 1) != 0;
      SUBPIXEL_Y = (var0 & 2) != 0;
      SUBPIXEL_NATIVE = (var0 & 4) != 0;
   }
}
