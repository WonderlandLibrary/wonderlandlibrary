package com.sun.javafx.font.freetype;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.transform.BaseTransform;

class FTFontStrike extends PrismFontStrike {
   FT_Matrix matrix;

   protected FTFontStrike(FTFontFile var1, float var2, BaseTransform var3, int var4, FontStrikeDesc var5) {
      super(var1, var2, var3, var4, var5);
      float var6 = PrismFontFactory.getFontSizeLimit();
      if (var3.isTranslateOrIdentity()) {
         this.drawShapes = var2 > var6;
      } else {
         BaseTransform var7 = this.getTransform();
         this.matrix = new FT_Matrix();
         this.matrix.xx = (long)((int)(var7.getMxx() * 65536.0));
         this.matrix.yx = (long)((int)(-var7.getMyx() * 65536.0));
         this.matrix.xy = (long)((int)(-var7.getMxy() * 65536.0));
         this.matrix.yy = (long)((int)(var7.getMyy() * 65536.0));
         if (Math.abs(var7.getMxx() * (double)var2) > (double)var6 || Math.abs(var7.getMyx() * (double)var2) > (double)var6 || Math.abs(var7.getMxy() * (double)var2) > (double)var6 || Math.abs(var7.getMyy() * (double)var2) > (double)var6) {
            this.drawShapes = true;
         }
      }

   }

   protected DisposerRecord createDisposer(FontStrikeDesc var1) {
      return null;
   }

   protected Glyph createGlyph(int var1) {
      return new FTGlyph(this, var1, this.drawShapes);
   }

   protected Path2D createGlyphOutline(int var1) {
      FTFontFile var2 = (FTFontFile)this.getFontResource();
      return var2.createGlyphOutline(var1, this.getSize());
   }

   void initGlyph(FTGlyph var1) {
      FTFontFile var2 = (FTFontFile)this.getFontResource();
      var2.initGlyph(var1, this);
   }
}
