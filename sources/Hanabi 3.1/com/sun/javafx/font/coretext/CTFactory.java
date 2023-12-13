package com.sun.javafx.font.coretext;

import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.text.GlyphLayout;

public class CTFactory extends PrismFontFactory {
   public static PrismFontFactory getFactory() {
      return new CTFactory();
   }

   private CTFactory() {
   }

   protected PrismFontFile createFontFile(String var1, String var2, int var3, boolean var4, boolean var5, boolean var6, boolean var7) throws Exception {
      return new CTFontFile(var1, var2, var3, var4, var5, var6, var7);
   }

   public GlyphLayout createGlyphLayout() {
      return new CTGlyphLayout();
   }

   protected boolean registerEmbeddedFont(String var1) {
      boolean var2 = CTFontFile.registerFont(var1);
      if (debugFonts) {
         if (var2) {
            System.err.println("[CoreText] Font registration succeeded:" + var1);
         } else {
            System.err.println("[CoreText] Font registration failed:" + var1);
         }
      }

      return var2;
   }
}
