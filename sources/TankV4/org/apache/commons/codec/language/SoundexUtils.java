package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

final class SoundexUtils {
   static String clean(String var0) {
      if (var0 != null && var0.length() != 0) {
         int var1 = var0.length();
         char[] var2 = new char[var1];
         int var3 = 0;

         for(int var4 = 0; var4 < var1; ++var4) {
            if (Character.isLetter(var0.charAt(var4))) {
               var2[var3++] = var0.charAt(var4);
            }
         }

         if (var3 == var1) {
            return var0.toUpperCase(Locale.ENGLISH);
         } else {
            return (new String(var2, 0, var3)).toUpperCase(Locale.ENGLISH);
         }
      } else {
         return var0;
      }
   }

   static int difference(StringEncoder var0, String var1, String var2) throws EncoderException {
      return differenceEncoded(var0.encode(var1), var0.encode(var2));
   }

   static int differenceEncoded(String var0, String var1) {
      if (var0 != null && var1 != null) {
         int var2 = Math.min(var0.length(), var1.length());
         int var3 = 0;

         for(int var4 = 0; var4 < var2; ++var4) {
            if (var0.charAt(var4) == var1.charAt(var4)) {
               ++var3;
            }
         }

         return var3;
      } else {
         return 0;
      }
   }
}
