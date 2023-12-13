package com.sun.javafx.font.coretext;

import com.sun.javafx.font.Glyph;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;

class CTGlyph implements Glyph {
   private CTFontStrike strike;
   private int glyphCode;
   private CGRect bounds;
   private double xAdvance;
   private double yAdvance;
   private boolean drawShapes;
   private static boolean LCD_CONTEXT = true;
   private static boolean CACHE_CONTEXT = true;
   private static long cachedContextRef;
   private static final int BITMAP_WIDTH = 256;
   private static final int BITMAP_HEIGHT = 256;
   private static final int MAX_SIZE = 320;
   private static final long GRAY_COLORSPACE = OS.CGColorSpaceCreateDeviceGray();
   private static final long RGB_COLORSPACE = OS.CGColorSpaceCreateDeviceRGB();

   CTGlyph(CTFontStrike var1, int var2, boolean var3) {
      this.strike = var1;
      this.glyphCode = var2;
      this.drawShapes = var3;
   }

   public int getGlyphCode() {
      return this.glyphCode;
   }

   public RectBounds getBBox() {
      CGRect var1 = this.strike.getBBox(this.glyphCode);
      return var1 == null ? new RectBounds() : new RectBounds((float)var1.origin.x, (float)var1.origin.y, (float)(var1.origin.x + var1.size.width), (float)(var1.origin.y + var1.size.height));
   }

   private void checkBounds() {
      if (this.bounds == null) {
         this.bounds = new CGRect();
         if (this.strike.getSize() != 0.0F) {
            long var1 = this.strike.getFontRef();
            if (var1 != 0L) {
               byte var3 = 0;
               CGSize var4 = new CGSize();
               OS.CTFontGetAdvancesForGlyphs(var1, var3, (short)this.glyphCode, var4);
               this.xAdvance = var4.width;
               this.yAdvance = -var4.height;
               if (!this.drawShapes) {
                  CTFontFile var5 = (CTFontFile)this.strike.getFontResource();
                  float[] var6 = new float[4];
                  var5.getGlyphBoundingBox((short)this.glyphCode, this.strike.getSize(), var6);
                  this.bounds.origin.x = (double)var6[0];
                  this.bounds.origin.y = (double)var6[1];
                  this.bounds.size.width = (double)(var6[2] - var6[0]);
                  this.bounds.size.height = (double)(var6[3] - var6[1]);
                  if (this.strike.matrix != null) {
                     OS.CGRectApplyAffineTransform(this.bounds, this.strike.matrix);
                  }

                  if (!(this.bounds.size.width < 0.0) && !(this.bounds.size.height < 0.0) && !(this.bounds.size.width > 320.0) && !(this.bounds.size.height > 320.0)) {
                     this.bounds.origin.x = (double)((int)Math.floor(this.bounds.origin.x) - 1);
                     this.bounds.origin.y = (double)((int)Math.floor(this.bounds.origin.y) - 1);
                     this.bounds.size.width = (double)((int)Math.ceil(this.bounds.size.width) + 1 + 1 + 1);
                     this.bounds.size.height = (double)((int)Math.ceil(this.bounds.size.height) + 1 + 1 + 1);
                  } else {
                     this.bounds.origin.x = this.bounds.origin.y = this.bounds.size.width = this.bounds.size.height = 0.0;
                  }

               }
            }
         }
      }
   }

   public Shape getShape() {
      return this.strike.createGlyphOutline(this.glyphCode);
   }

   private long createContext(boolean var1, int var2, int var3) {
      byte var6 = 8;
      long var4;
      int var7;
      int var8;
      if (var1) {
         var4 = RGB_COLORSPACE;
         var7 = var2 * 4;
         var8 = OS.kCGBitmapByteOrder32Host | 2;
      } else {
         var4 = GRAY_COLORSPACE;
         var7 = var2;
         var8 = 0;
      }

      long var9 = OS.CGBitmapContextCreate(0L, (long)var2, (long)var3, (long)var6, (long)var7, var4, var8);
      boolean var11 = this.strike.isSubPixelGlyph();
      OS.CGContextSetAllowsFontSmoothing(var9, var1);
      OS.CGContextSetAllowsAntialiasing(var9, true);
      OS.CGContextSetAllowsFontSubpixelPositioning(var9, var11);
      OS.CGContextSetAllowsFontSubpixelQuantization(var9, var11);
      return var9;
   }

   private long getCachedContext(boolean var1) {
      if (cachedContextRef == 0L) {
         cachedContextRef = this.createContext(var1, 256, 256);
      }

      return cachedContextRef;
   }

   private synchronized byte[] getImage(double var1, double var3, int var5, int var6, int var7) {
      if (var5 != 0 && var6 != 0) {
         long var8 = this.strike.getFontRef();
         boolean var10 = this.isLCDGlyph();
         boolean var11 = LCD_CONTEXT || var10;
         CGAffineTransform var12 = this.strike.matrix;
         boolean var13 = CACHE_CONTEXT & 256 >= var5 & 256 >= var6;
         long var14 = var13 ? this.getCachedContext(var11) : this.createContext(var11, var5, var6);
         if (var14 == 0L) {
            return new byte[0];
         } else {
            OS.CGContextSetRGBFillColor(var14, 1.0, 1.0, 1.0, 1.0);
            CGRect var16 = new CGRect();
            var16.size.width = (double)var5;
            var16.size.height = (double)var6;
            OS.CGContextFillRect(var14, var16);
            double var17 = 0.0;
            double var19 = 0.0;
            if (var12 != null) {
               OS.CGContextTranslateCTM(var14, -var1, -var3);
            } else {
               var17 = var1 - (double)this.strike.getSubPixelPosition(var7);
               var19 = var3;
            }

            OS.CGContextSetRGBFillColor(var14, 0.0, 0.0, 0.0, 1.0);
            OS.CTFontDrawGlyphs(var8, (short)this.glyphCode, -var17, -var19, var14);
            if (var12 != null) {
               OS.CGContextTranslateCTM(var14, var1, var3);
            }

            byte[] var21;
            if (var10) {
               var21 = OS.CGBitmapContextGetData(var14, var5, var6, 24);
            } else {
               var21 = OS.CGBitmapContextGetData(var14, var5, var6, 8);
            }

            if (var21 == null) {
               this.bounds = new CGRect();
               var21 = new byte[0];
            }

            if (!var13) {
               OS.CGContextRelease(var14);
            }

            return var21;
         }
      } else {
         return new byte[0];
      }
   }

   public byte[] getPixelData() {
      return this.getPixelData(0);
   }

   public byte[] getPixelData(int var1) {
      this.checkBounds();
      return this.getImage(this.bounds.origin.x, this.bounds.origin.y, (int)this.bounds.size.width, (int)this.bounds.size.height, var1);
   }

   public float getAdvance() {
      this.checkBounds();
      return (float)this.xAdvance;
   }

   public float getPixelXAdvance() {
      this.checkBounds();
      return (float)this.xAdvance;
   }

   public float getPixelYAdvance() {
      this.checkBounds();
      return (float)this.yAdvance;
   }

   public int getWidth() {
      this.checkBounds();
      int var1 = (int)this.bounds.size.width;
      return this.isLCDGlyph() ? var1 * 3 : var1;
   }

   public int getHeight() {
      this.checkBounds();
      return (int)this.bounds.size.height;
   }

   public int getOriginX() {
      this.checkBounds();
      return (int)this.bounds.origin.x;
   }

   public int getOriginY() {
      this.checkBounds();
      int var1 = (int)this.bounds.size.height;
      int var2 = (int)this.bounds.origin.y;
      return -var1 - var2;
   }

   public boolean isLCDGlyph() {
      return this.strike.getAAMode() == 1;
   }
}
