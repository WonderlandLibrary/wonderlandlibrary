package com.sun.javafx.font.coretext;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.transform.BaseTransform;

class CTFontStrike extends PrismFontStrike {
   private long fontRef;
   CGAffineTransform matrix;
   static final float SUBPIXEL4_SIZE = 12.0F;
   static final float SUBPIXEL3_SIZE = 18.0F;
   static final float SUBPIXEL2_SIZE = 34.0F;
   private static final boolean SUBPIXEL;

   CTFontStrike(CTFontFile var1, float var2, BaseTransform var3, int var4, FontStrikeDesc var5) {
      super(var1, var2, var3, var4, var5);
      float var6 = PrismFontFactory.getFontSizeLimit();
      if (var3.isTranslateOrIdentity()) {
         this.drawShapes = var2 > var6;
      } else {
         BaseTransform var7 = this.getTransform();
         this.matrix = new CGAffineTransform();
         this.matrix.a = var7.getMxx();
         this.matrix.b = -var7.getMyx();
         this.matrix.c = -var7.getMxy();
         this.matrix.d = var7.getMyy();
         if (Math.abs(this.matrix.a * (double)var2) > (double)var6 || Math.abs(this.matrix.b * (double)var2) > (double)var6 || Math.abs(this.matrix.c * (double)var2) > (double)var6 || Math.abs(this.matrix.d * (double)var2) > (double)var6) {
            this.drawShapes = true;
         }
      }

      long var9;
      if (var1.isEmbeddedFont()) {
         var9 = var1.getCGFontRef();
         if (var9 != 0L) {
            this.fontRef = OS.CTFontCreateWithGraphicsFont(var9, (double)var2, this.matrix, 0L);
         }
      } else {
         var9 = OS.CFStringCreate(var1.getPSName());
         if (var9 != 0L) {
            this.fontRef = OS.CTFontCreateWithName(var9, (double)var2, this.matrix);
            OS.CFRelease(var9);
         }
      }

      if (this.fontRef == 0L && PrismFontFactory.debugFonts) {
         System.err.println("Failed to create CTFont for " + this);
      }

   }

   long getFontRef() {
      return this.fontRef;
   }

   protected DisposerRecord createDisposer(FontStrikeDesc var1) {
      CTFontFile var2 = (CTFontFile)this.getFontResource();
      return new CTStrikeDisposer(var2, var1, this.fontRef);
   }

   protected Glyph createGlyph(int var1) {
      return new CTGlyph(this, var1, this.drawShapes);
   }

   public int getQuantizedPosition(Point2D var1) {
      if (SUBPIXEL && this.matrix == null) {
         float var2;
         if (this.getSize() < 12.0F) {
            var2 = var1.x;
            var1.x = (float)((int)var1.x);
            var2 -= var1.x;
            var1.y = (float)Math.round(var1.y);
            if (var2 >= 0.75F) {
               return 3;
            }

            if (var2 >= 0.5F) {
               return 2;
            }

            if (var2 >= 0.25F) {
               return 1;
            }

            return 0;
         }

         if (this.getAAMode() == 0) {
            if (this.getSize() < 18.0F) {
               var2 = var1.x;
               var1.x = (float)((int)var1.x);
               var2 -= var1.x;
               var1.y = (float)Math.round(var1.y);
               if (var2 >= 0.66F) {
                  return 2;
               }

               if (var2 >= 0.33F) {
                  return 1;
               }

               return 0;
            }

            if (this.getSize() < 34.0F) {
               var2 = var1.x;
               var1.x = (float)((int)var1.x);
               var2 -= var1.x;
               var1.y = (float)Math.round(var1.y);
               if (var2 >= 0.5F) {
                  return 1;
               }
            }

            return 0;
         }
      }

      return super.getQuantizedPosition(var1);
   }

   float getSubPixelPosition(int var1) {
      if (var1 == 0) {
         return 0.0F;
      } else {
         float var2 = this.getSize();
         if (var2 < 12.0F) {
            if (var1 == 3) {
               return 0.75F;
            } else if (var1 == 2) {
               return 0.5F;
            } else {
               return var1 == 1 ? 0.25F : 0.0F;
            }
         } else if (this.getAAMode() == 1) {
            return 0.0F;
         } else if (var2 < 18.0F) {
            if (var1 == 2) {
               return 0.66F;
            } else {
               return var1 == 1 ? 0.33F : 0.0F;
            }
         } else {
            return var2 < 34.0F && var1 == 1 ? 0.5F : 0.0F;
         }
      }
   }

   boolean isSubPixelGlyph() {
      return SUBPIXEL && this.matrix == null;
   }

   protected Path2D createGlyphOutline(int var1) {
      CTFontFile var2 = (CTFontFile)this.getFontResource();
      return var2.getGlyphOutline(var1, this.getSize());
   }

   CGRect getBBox(int var1) {
      CTFontFile var2 = (CTFontFile)this.getFontResource();
      return var2.getBBox(var1, this.getSize());
   }

   static {
      int var0 = PrismFontFactory.getFontFactory().getSubPixelMode();
      SUBPIXEL = (var0 & 1) != 0;
   }
}
