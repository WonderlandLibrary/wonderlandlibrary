package com.sun.javafx.font.freetype;

import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.text.GlyphLayout;
import com.sun.javafx.text.TextRun;

public class FTFactory extends PrismFontFactory {
   static boolean LCD_SUPPORT;

   public static PrismFontFactory getFactory() {
      FTFactory var0 = null;
      long[] var1 = new long[1];
      int var2 = OSFreetype.FT_Init_FreeType(var1);
      long var3 = var1[0];
      int[] var5 = new int[1];
      int[] var6 = new int[1];
      int[] var7 = new int[1];
      if (var2 == 0) {
         var0 = new FTFactory();
         OSFreetype.FT_Library_Version(var3, var5, var6, var7);
         var2 = OSFreetype.FT_Library_SetLcdFilter(var3, 1);
         LCD_SUPPORT = var2 == 0;
         OSFreetype.FT_Done_FreeType(var3);
      }

      if (PrismFontFactory.debugFonts) {
         if (var0 != null) {
            String var8 = var5[0] + "." + var6[0] + "." + var7[0];
            System.err.println("Freetype2 Loaded (version " + var8 + ")");
            String var9 = LCD_SUPPORT ? "Enabled" : "Disabled";
            System.err.println("LCD support " + var9);
         } else {
            System.err.println("Freetype2 Failed (error " + var2 + ")");
         }
      }

      return var0;
   }

   private FTFactory() {
   }

   protected PrismFontFile createFontFile(String var1, String var2, int var3, boolean var4, boolean var5, boolean var6, boolean var7) throws Exception {
      return new FTFontFile(var1, var2, var3, var4, var5, var6, var7);
   }

   public GlyphLayout createGlyphLayout() {
      if (OSFreetype.isPangoEnabled()) {
         return new PangoGlyphLayout();
      } else {
         return (GlyphLayout)(OSFreetype.isHarfbuzzEnabled() ? new HBGlyphLayout() : new StubGlyphLayout());
      }
   }

   public boolean isLCDTextSupported() {
      return LCD_SUPPORT && super.isLCDTextSupported();
   }

   protected boolean registerEmbeddedFont(String var1) {
      long[] var2 = new long[1];
      int var3 = OSFreetype.FT_Init_FreeType(var2);
      if (var3 != 0) {
         return false;
      } else {
         long var4 = var2[0];
         byte[] var6 = (var1 + "\u0000").getBytes();
         var3 = OSFreetype.FT_New_Face(var4, var6, 0L, var2);
         if (var3 != 0) {
            long var7 = var2[0];
            OSFreetype.FT_Done_Face(var7);
         }

         OSFreetype.FT_Done_FreeType(var4);
         if (var3 != 0) {
            return false;
         } else {
            return OSFreetype.isPangoEnabled() ? OSPango.FcConfigAppFontAddFile(0L, var1) : true;
         }
      }
   }

   private static class StubGlyphLayout extends GlyphLayout {
      public StubGlyphLayout() {
      }

      public void layout(TextRun var1, PGFont var2, FontStrike var3, char[] var4) {
      }
   }
}
