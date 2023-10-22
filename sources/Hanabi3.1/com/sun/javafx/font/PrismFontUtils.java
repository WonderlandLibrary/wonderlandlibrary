package com.sun.javafx.font;

import com.sun.javafx.geom.transform.BaseTransform;

public class PrismFontUtils {
   private PrismFontUtils() {
   }

   static Metrics getFontMetrics(PGFont var0) {
      FontStrike var1 = var0.getStrike(BaseTransform.IDENTITY_TRANSFORM, 0);
      return var1.getMetrics();
   }

   static double computeStringWidth(PGFont var0, String var1) {
      if (var1 != null && !var1.equals("")) {
         FontStrike var2 = var0.getStrike(BaseTransform.IDENTITY_TRANSFORM, 0);
         double var3 = 0.0;

         for(int var5 = 0; var5 < var1.length(); ++var5) {
            var3 += (double)var2.getCharAdvance(var1.charAt(var5));
         }

         return var3;
      } else {
         return 0.0;
      }
   }
}
