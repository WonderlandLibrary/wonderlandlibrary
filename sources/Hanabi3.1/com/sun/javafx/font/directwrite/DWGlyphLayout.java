package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.CompositeFontResource;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.text.GlyphLayout;
import com.sun.javafx.text.PrismTextLayout;
import com.sun.javafx.text.TextRun;
import java.util.Arrays;

public class DWGlyphLayout extends GlyphLayout {
   private static final String LOCALE = "en-us";

   protected TextRun addTextRun(PrismTextLayout var1, char[] var2, int var3, int var4, PGFont var5, TextSpan var6, byte var7) {
      IDWriteFactory var8 = DWFactory.getDWriteFactory();
      IDWriteTextAnalyzer var9 = var8.CreateTextAnalyzer();
      if (var9 == null) {
         return new TextRun(var3, var4, var7, false, 0, var6, 0, false);
      } else {
         int var10 = (var7 & 1) != 0 ? 1 : 0;
         JFXTextAnalysisSink var11 = OS.NewJFXTextAnalysisSink(var2, var3, var4, "en-us", var10);
         if (var11 == null) {
            return new TextRun(var3, var4, var7, false, 0, var6, 0, false);
         } else {
            var11.AddRef();
            TextRun var12 = null;
            int var13 = var9.AnalyzeScript(var11, 0, var4, var11);
            if (var13 == 0) {
               while(var11.Next()) {
                  int var14 = var11.GetStart();
                  int var15 = var11.GetLength();
                  DWRITE_SCRIPT_ANALYSIS var16 = var11.GetAnalysis();
                  var12 = new TextRun(var3 + var14, var15, var7, true, var16.script, var6, var16.shapes, false);
                  var1.addTextRun(var12);
               }
            }

            var9.Release();
            var11.Release();
            return var12;
         }
      }
   }

   public void layout(TextRun var1, PGFont var2, FontStrike var3, char[] var4) {
      int var5 = 0;
      FontResource var6 = var2.getFontResource();
      boolean var7 = var6 instanceof CompositeFontResource;
      if (var7) {
         var5 = this.getInitialSlot(var6);
         var6 = ((CompositeFontResource)var6).getSlotResource(var5);
      }

      IDWriteFontFace var8 = ((DWFontFile)var6).getFontFace();
      if (var8 != null) {
         IDWriteFactory var9 = DWFactory.getDWriteFactory();
         IDWriteTextAnalyzer var10 = var9.CreateTextAnalyzer();
         if (var10 != null) {
            Object var11 = null;
            Object var12 = null;
            byte var13 = 0;
            int var14 = var1.getLength();
            short[] var15 = new short[var14];
            short[] var16 = new short[var14];
            int var17 = var14 * 3 / 2 + 16;
            short[] var18 = new short[var17];
            short[] var19 = new short[var17];
            int[] var20 = new int[1];
            boolean var21 = !var1.isLeftToRight();
            DWRITE_SCRIPT_ANALYSIS var22 = new DWRITE_SCRIPT_ANALYSIS();
            var22.script = (short)var1.getScript();
            var22.shapes = var1.getSlot();
            int var23 = var1.getStart();
            int var24 = var10.GetGlyphs(var4, var23, var14, var8, false, var21, var22, (String)null, 0L, (long[])var11, (int[])var12, var13, var17, var15, var16, var18, var19, var20);
            if (var24 == -2147024774) {
               var17 *= 2;
               var18 = new short[var17];
               var19 = new short[var17];
               var24 = var10.GetGlyphs(var4, var23, var14, var8, false, var21, var22, (String)null, 0L, (long[])var11, (int[])var12, var13, var17, var15, var16, var18, var19, var20);
            }

            if (var24 != 0) {
               var10.Release();
            } else {
               int var25 = var20[0];
               int var26 = var21 ? -1 : 1;
               int[] var29 = new int[var25];
               int var30 = var5 << 24;
               boolean var31 = false;
               int var27 = 0;

               for(int var28 = var21 ? var25 - 1 : 0; var27 < var25; var28 += var26) {
                  if (var18[var27] == 0) {
                     var31 = true;
                     if (var7) {
                        break;
                     }
                  }

                  var29[var27] = var18[var28] | var30;
                  ++var27;
               }

               if (var31 && var7) {
                  var10.Release();
                  this.renderShape(var4, var1, var2, var5);
               } else {
                  float var32 = var2.getSize();
                  float[] var33 = new float[var25];
                  float[] var34 = new float[var25 * 2];
                  var10.GetGlyphPlacements(var4, var15, var16, var23, var14, var18, var19, var25, var8, var32, false, var21, var22, (String)null, (long[])var11, (int[])var12, var13, var33, var34);
                  var10.Release();
                  float[] var35 = this.getPositions(var33, var34, var25, var21);
                  int[] var36 = this.getIndices(var15, var25, var21);
                  var1.shape(var25, var29, var35, var36);
               }
            }
         }
      }
   }

   private float[] getPositions(float[] var1, float[] var2, int var3, boolean var4) {
      float[] var5 = new float[var3 * 2 + 2];
      int var6 = 0;
      int var7 = var4 ? var3 - 1 : 0;
      int var8 = var4 ? -1 : 1;

      float var9;
      for(var9 = 0.0F; var6 < var5.length - 2; var7 += var8) {
         int var10 = var7 << 1;
         var5[var6++] = (var4 ? -var2[var10] : var2[var10]) + var9;
         var5[var6++] = -var2[var10 + 1];
         var9 += var1[var7];
      }

      var5[var6++] = var9;
      var5[var6++] = 0.0F;
      return var5;
   }

   private int[] getIndices(short[] var1, int var2, boolean var3) {
      int[] var4 = new int[var2];
      Arrays.fill(var4, -1);

      int var5;
      int var6;
      for(var5 = 0; var5 < var1.length; ++var5) {
         var6 = var1[var5];
         if (0 <= var6 && var6 < var2 && var4[var6] == -1) {
            var4[var6] = var5;
         }
      }

      if (var4.length > 0) {
         if (var4[0] == -1) {
            var4[0] = 0;
         }

         for(var5 = 1; var5 < var4.length; ++var5) {
            if (var4[var5] == -1) {
               var4[var5] = var4[var5 - 1];
            }
         }
      }

      if (var3) {
         for(var5 = 0; var5 < var4.length / 2; ++var5) {
            var6 = var4[var5];
            var4[var5] = var4[var4.length - var5 - 1];
            var4[var4.length - var5 - 1] = var6;
         }
      }

      return var4;
   }

   private String getName(IDWriteLocalizedStrings var1) {
      if (var1 == null) {
         return null;
      } else {
         int var2 = var1.FindLocaleName("en-us");
         String var3 = null;
         if (var2 >= 0) {
            int var4 = var1.GetStringLength(var2);
            var3 = var1.GetString(var2, var4);
         }

         var1.Release();
         return var3;
      }
   }

   private FontResource checkFontResource(FontResource var1, String var2, String var3) {
      if (var1 == null) {
         return null;
      } else if (var2 != null && var2.equals(var1.getPSName())) {
         return var1;
      } else {
         if (var3 != null) {
            if (var3.equals(var1.getFullName())) {
               return var1;
            }

            String var4 = var1.getFamilyName() + " " + var1.getStyleName();
            if (var3.equals(var4)) {
               return var1;
            }
         }

         return null;
      }
   }

   private int getFontSlot(IDWriteFontFace var1, CompositeFontResource var2, String var3, int var4) {
      if (var1 == null) {
         return -1;
      } else {
         IDWriteFontCollection var5 = DWFactory.getFontCollection();
         PrismFontFactory var6 = PrismFontFactory.getFontFactory();
         IDWriteFont var7 = var5.GetFontFromFontFace(var1);
         if (var7 == null) {
            return -1;
         } else {
            IDWriteFontFamily var8 = var7.GetFontFamily();
            String var9 = this.getName(var8.GetFamilyNames());
            var8.Release();
            boolean var10 = var7.GetStyle() != 0;
            boolean var11 = var7.GetWeight() > 400;
            int var12 = var7.GetSimulations();
            byte var13 = 17;
            String var14 = this.getName(var7.GetInformationalStrings(var13));
            var13 = 11;
            String var15 = this.getName(var7.GetInformationalStrings(var13));
            var13 = 12;
            String var16 = this.getName(var7.GetInformationalStrings(var13));
            String var17 = var15 + " " + var16;
            if (PrismFontFactory.debugFonts) {
               String var18 = this.getName(var7.GetFaceNames());
               System.err.println("Mapping IDWriteFont=\"" + var9 + " " + var18 + "\" Postscript name=\"" + var14 + "\" Win32 name=\"" + var17 + "\"");
            }

            var7.Release();
            FontResource var20 = var6.getFontResource(var9, var11, var10, false);
            var20 = this.checkFontResource(var20, var14, var17);
            if (var20 == null) {
               var10 &= (var12 & 2) == 0;
               var11 &= (var12 & 1) == 0;
               var20 = var6.getFontResource(var9, var11, var10, false);
               var20 = this.checkFontResource(var20, var14, var17);
            }

            if (var20 == null) {
               var20 = var6.getFontResource(var17, (String)null, false);
               var20 = this.checkFontResource(var20, var14, var17);
            }

            if (var20 == null) {
               if (PrismFontFactory.debugFonts) {
                  System.err.println("\t**** Failed to map IDWriteFont to Prism ****");
               }

               return -1;
            } else {
               String var19 = var20.getFullName();
               if (!var3.equalsIgnoreCase(var19)) {
                  var4 = var2.getSlotForFont(var19);
               }

               if (PrismFontFactory.debugFonts) {
                  System.err.println("\tFallback full name=\"" + var19 + "\" Postscript name=\"" + var20.getPSName() + "\" Style name=\"" + var20.getStyleName() + "\" slot=" + var4);
               }

               return var4;
            }
         }
      }
   }

   private void renderShape(char[] var1, TextRun var2, PGFont var3, int var4) {
      CompositeFontResource var5 = (CompositeFontResource)var3.getFontResource();
      FontResource var6 = var5.getSlotResource(var4);
      String var7 = var6.getFamilyName();
      String var8 = var6.getFullName();
      int var9 = var6.isBold() ? 700 : 400;
      byte var10 = 5;
      int var11 = var6.isItalic() ? 2 : 0;
      float var12 = var3.getSize();
      float var13 = var12 > 0.0F ? var12 : 1.0F;
      IDWriteFactory var14 = DWFactory.getDWriteFactory();
      IDWriteFontCollection var15 = DWFactory.getFontCollection();
      IDWriteTextFormat var16 = var14.CreateTextFormat(var7, var15, var9, var11, var10, var13, "en-us");
      if (var16 != null) {
         int var17 = var2.getStart();
         int var18 = var2.getLength();
         IDWriteTextLayout var19 = var14.CreateTextLayout(var1, var17, var18, var16, 100000.0F, 100000.0F);
         if (var19 != null) {
            JFXTextRenderer var20 = OS.NewJFXTextRenderer();
            if (var20 != null) {
               var20.AddRef();
               var19.Draw(0L, var20, 0.0F, 0.0F);
               int var21 = var20.GetTotalGlyphCount();
               int[] var22 = new int[var21];
               float[] var23 = new float[var21];
               float[] var24 = new float[var21 * 2];
               short[] var25 = new short[var18];
               int var26 = 0;

               int var29;
               for(int var27 = 0; var20.Next(); var27 += var20.GetLength()) {
                  IDWriteFontFace var28 = var20.GetFontFace();
                  var29 = this.getFontSlot(var28, var5, var8, var4);
                  if (var29 >= 0) {
                     var20.GetGlyphIndices(var22, var26, var29 << 24);
                     var20.GetGlyphOffsets(var24, var26 * 2);
                  }

                  if (var12 > 0.0F) {
                     var20.GetGlyphAdvances(var23, var26);
                  }

                  var20.GetClusterMap(var25, var27, var26);
                  var26 += var20.GetGlyphCount();
               }

               var20.Release();
               boolean var31 = !var2.isLeftToRight();
               if (var31) {
                  for(var29 = 0; var29 < var21 / 2; ++var29) {
                     int var30 = var22[var29];
                     var22[var29] = var22[var21 - var29 - 1];
                     var22[var21 - var29 - 1] = var30;
                  }
               }

               float[] var32 = this.getPositions(var23, var24, var21, var31);
               int[] var33 = this.getIndices(var25, var21, var31);
               var2.shape(var21, var22, var32, var33);
            }

            var19.Release();
         }

         var16.Release();
      }
   }
}
