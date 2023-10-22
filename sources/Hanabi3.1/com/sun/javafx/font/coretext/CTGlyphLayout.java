package com.sun.javafx.font.coretext;

import com.sun.javafx.font.CompositeFontResource;
import com.sun.javafx.font.CompositeStrike;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.text.GlyphLayout;
import com.sun.javafx.text.TextRun;

class CTGlyphLayout extends GlyphLayout {
   private long createCTLine(long var1, char[] var3, boolean var4, int var5, int var6) {
      long var7 = OS.kCFAllocatorDefault();
      long var9 = OS.CFStringCreateWithCharacters(var7, var3, (long)var5, (long)var6);
      long var11 = 0L;
      if (var9 != 0L) {
         long var13 = OS.CFDictionaryCreateMutable(var7, 4L, OS.kCFTypeDictionaryKeyCallBacks(), OS.kCFTypeDictionaryValueCallBacks());
         if (var13 != 0L) {
            OS.CFDictionaryAddValue(var13, OS.kCTFontAttributeName(), var1);
            long var15;
            if (var4) {
               var15 = OS.CTParagraphStyleCreate(1);
               if (var15 != 0L) {
                  OS.CFDictionaryAddValue(var13, OS.kCTParagraphStyleAttributeName(), var15);
                  OS.CFRelease(var15);
               }
            }

            var15 = OS.CFAttributedStringCreate(var7, var9, var13);
            if (var15 != 0L) {
               var11 = OS.CTLineCreateWithAttributedString(var15);
               OS.CFRelease(var15);
            }

            OS.CFRelease(var13);
         }

         OS.CFRelease(var9);
      }

      return var11;
   }

   private int getFontSlot(long var1, CompositeFontResource var3, String var4, int var5) {
      long var6 = OS.CTRunGetAttributes(var1);
      if (var6 == 0L) {
         return -1;
      } else {
         long var8 = OS.CFDictionaryGetValue(var6, OS.kCTFontAttributeName());
         if (var8 == 0L) {
            return -1;
         } else {
            String var10 = OS.CTFontCopyAttributeDisplayName(var8);
            if (var10 == null) {
               return -1;
            } else {
               if (!var10.equalsIgnoreCase(var4)) {
                  if (var3 == null) {
                     return -1;
                  }

                  var5 = var3.getSlotForFont(var10);
                  if (PrismFontFactory.debugFonts) {
                     System.err.println("\tFallback font= " + var10 + " slot=" + var5);
                  }
               }

               return var5;
            }
         }
      }
   }

   public void layout(TextRun var1, PGFont var2, FontStrike var3, char[] var4) {
      int var5 = 0;
      CompositeFontResource var6 = null;
      if (var3 instanceof CompositeStrike) {
         var6 = (CompositeFontResource)var3.getFontResource();
         var5 = this.getInitialSlot(var6);
         var3 = ((CompositeStrike)var3).getStrikeSlot(var5);
      }

      float var7 = var3.getSize();
      String var8 = var3.getFontResource().getFullName();
      long var9 = ((CTFontStrike)var3).getFontRef();
      if (var9 != 0L) {
         boolean var11 = (var1.getLevel() & 1) != 0;
         long var12 = this.createCTLine(var9, var4, var11, var1.getStart(), var1.getLength());
         if (var12 != 0L) {
            long var14 = OS.CTLineGetGlyphRuns(var12);
            if (var14 != 0L) {
               int var16 = (int)OS.CTLineGetGlyphCount(var12);
               int[] var17 = new int[var16];
               float[] var18 = new float[var16 * 2 + 2];
               int[] var19 = new int[var16];
               long var20 = OS.CFArrayGetCount(var14);
               int var22 = 0;
               int var23 = 0;
               int var24 = 0;

               for(int var25 = 0; (long)var25 < var20; ++var25) {
                  long var26 = OS.CFArrayGetValueAtIndex(var14, (long)var25);
                  if (var26 != 0L) {
                     int var28 = this.getFontSlot(var26, var6, var8, var5);
                     if (var28 != -1) {
                        var22 += OS.CTRunGetGlyphs(var26, var28 << 24, var22, var17);
                     } else {
                        var22 += OS.CTRunGetGlyphs(var26, 0, var22, var17);
                     }

                     if (var7 > 0.0F) {
                        var23 += OS.CTRunGetPositions(var26, var23, var18);
                     }

                     var24 += OS.CTRunGetStringIndices(var26, var24, var19);
                  }
               }

               if (var7 > 0.0F) {
                  var18[var23] = (float)OS.CTLineGetTypographicBounds(var12);
               }

               var1.shape(var16, var17, var18, var19);
            }

            OS.CFRelease(var12);
         }
      }
   }
}
