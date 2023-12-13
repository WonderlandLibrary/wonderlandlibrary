package com.sun.javafx.font.freetype;

import com.sun.javafx.font.Disposer;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.transform.BaseTransform;

class FTFontFile extends PrismFontFile {
   private long library;
   private long face;
   private FTDisposer disposer;

   FTFontFile(String var1, String var2, int var3, boolean var4, boolean var5, boolean var6, boolean var7) throws Exception {
      super(var1, var2, var3, var4, var5, var6, var7);
      this.init();
   }

   private synchronized void init() throws Exception {
      long[] var1 = new long[1];
      int var2 = OSFreetype.FT_Init_FreeType(var1);
      if (var2 != 0) {
         throw new Exception("FT_Init_FreeType Failed error " + var2);
      } else {
         this.library = var1[0];
         if (FTFactory.LCD_SUPPORT) {
            OSFreetype.FT_Library_SetLcdFilter(this.library, 1);
         }

         String var3 = this.getFileName();
         int var4 = this.getFontIndex();
         byte[] var5 = (var3 + "\u0000").getBytes();
         var2 = OSFreetype.FT_New_Face(this.library, var5, (long)var4, var1);
         if (var2 != 0) {
            throw new Exception("FT_New_Face Failed error " + var2 + " Font File " + var3 + " Font Index " + var4);
         } else {
            this.face = var1[0];
            if (!this.isRegistered()) {
               this.disposer = new FTDisposer(this.library, this.face);
               Disposer.addRecord(this, this.disposer);
            }

         }
      }
   }

   protected PrismFontStrike createStrike(float var1, BaseTransform var2, int var3, FontStrikeDesc var4) {
      return new FTFontStrike(this, var1, var2, var3, var4);
   }

   protected synchronized int[] createGlyphBoundingBox(int var1) {
      byte var2 = 1;
      OSFreetype.FT_Load_Glyph(this.face, var1, var2);
      int[] var3 = new int[4];
      FT_GlyphSlotRec var4 = OSFreetype.getGlyphSlot(this.face);
      if (var4 != null && var4.metrics != null) {
         FT_Glyph_Metrics var5 = var4.metrics;
         var3[0] = (int)var5.horiBearingX;
         var3[1] = (int)(var5.horiBearingY - var5.height);
         var3[2] = (int)(var5.horiBearingX + var5.width);
         var3[3] = (int)var5.horiBearingY;
      }

      return var3;
   }

   synchronized Path2D createGlyphOutline(int var1, float var2) {
      int var3 = (int)(var2 * 64.0F);
      OSFreetype.FT_Set_Char_Size(this.face, 0L, (long)var3, 72, 72);
      short var4 = 2058;
      OSFreetype.FT_Load_Glyph(this.face, var1, var4);
      return OSFreetype.FT_Outline_Decompose(this.face);
   }

   synchronized void initGlyph(FTGlyph var1, FTFontStrike var2) {
      float var3 = var2.getSize();
      if (var3 == 0.0F) {
         var1.buffer = new byte[0];
         var1.bitmap = new FT_Bitmap();
      } else {
         int var4 = (int)(var3 * 64.0F);
         OSFreetype.FT_Set_Char_Size(this.face, 0L, (long)var4, 72, 72);
         boolean var5 = var2.getAAMode() == 1 && FTFactory.LCD_SUPPORT;
         int var6 = 14;
         FT_Matrix var7 = var2.matrix;
         if (var7 != null) {
            OSFreetype.FT_Set_Transform(this.face, var7, 0L, 0L);
         } else {
            var6 |= 2048;
         }

         if (var5) {
            var6 |= 196608;
         } else {
            var6 |= 0;
         }

         int var8 = var1.getGlyphCode();
         int var9 = OSFreetype.FT_Load_Glyph(this.face, var8, var6);
         if (var9 != 0) {
            if (PrismFontFactory.debugFonts) {
               System.err.println("FT_Load_Glyph failed " + var9 + " glyph code " + var8 + " load falgs " + var6);
            }

         } else {
            FT_GlyphSlotRec var10 = OSFreetype.getGlyphSlot(this.face);
            if (var10 != null) {
               FT_Bitmap var11 = var10.bitmap;
               if (var11 != null) {
                  byte var12 = var11.pixel_mode;
                  int var13 = var11.width;
                  int var14 = var11.rows;
                  int var15 = var11.pitch;
                  if (var12 != 2 && var12 != 5) {
                     if (PrismFontFactory.debugFonts) {
                        System.err.println("Unexpected pixel mode: " + var12 + " glyph code " + var8 + " load falgs " + var6);
                     }

                  } else {
                     byte[] var16;
                     if (var13 != 0 && var14 != 0) {
                        var16 = OSFreetype.getBitmapData(this.face);
                        if (var16 != null && var15 != var13) {
                           byte[] var17 = new byte[var13 * var14];
                           int var18 = 0;
                           int var19 = 0;

                           for(int var20 = 0; var20 < var14; ++var20) {
                              for(int var21 = 0; var21 < var13; ++var21) {
                                 var17[var19 + var21] = var16[var18 + var21];
                              }

                              var19 += var13;
                              var18 += var15;
                           }

                           var16 = var17;
                        }
                     } else {
                        var16 = new byte[0];
                     }

                     var1.buffer = var16;
                     var1.bitmap = var11;
                     var1.bitmap_left = var10.bitmap_left;
                     var1.bitmap_top = var10.bitmap_top;
                     var1.advanceX = (float)var10.advance_x / 64.0F;
                     var1.advanceY = (float)var10.advance_y / 64.0F;
                     var1.userAdvance = (float)var10.linearHoriAdvance / 65536.0F;
                     var1.lcd = var5;
                  }
               }
            }
         }
      }
   }
}
