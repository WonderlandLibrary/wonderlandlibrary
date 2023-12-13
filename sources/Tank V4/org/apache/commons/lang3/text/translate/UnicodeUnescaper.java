package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;

public class UnicodeUnescaper extends CharSequenceTranslator {
   public int translate(CharSequence var1, int var2, Writer var3) throws IOException {
      if (var1.charAt(var2) == '\\' && var2 + 1 < var1.length() && var1.charAt(var2 + 1) == 'u') {
         int var4;
         for(var4 = 2; var2 + var4 < var1.length() && var1.charAt(var2 + var4) == 'u'; ++var4) {
         }

         if (var2 + var4 < var1.length() && var1.charAt(var2 + var4) == '+') {
            ++var4;
         }

         if (var2 + var4 + 4 <= var1.length()) {
            CharSequence var5 = var1.subSequence(var2 + var4, var2 + var4 + 4);

            try {
               int var6 = Integer.parseInt(var5.toString(), 16);
               var3.write((char)var6);
            } catch (NumberFormatException var7) {
               throw new IllegalArgumentException("Unable to parse unicode value: " + var5, var7);
            }

            return var4 + 4;
         } else {
            throw new IllegalArgumentException("Less than 4 hex digits in unicode value: '" + var1.subSequence(var2, var1.length()) + "' due to end of CharSequence");
         }
      } else {
         return 0;
      }
   }
}
