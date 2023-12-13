package com.sun.webkit.text;

import java.text.Normalizer;
import java.text.Normalizer.Form;

final class TextNormalizer {
   private static final int FORM_NFC = 0;
   private static final int FORM_NFD = 1;
   private static final int FORM_NFKC = 2;
   private static final int FORM_NFKD = 3;

   private static String normalize(String var0, int var1) {
      Normalizer.Form var2;
      switch (var1) {
         case 0:
            var2 = Form.NFC;
            break;
         case 1:
            var2 = Form.NFD;
            break;
         case 2:
            var2 = Form.NFKC;
            break;
         case 3:
            var2 = Form.NFKD;
            break;
         default:
            throw new IllegalArgumentException("invalid type: " + var1);
      }

      return Normalizer.normalize(var0, var2);
   }
}
