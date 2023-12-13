package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.EnumSet;

public class NumericEntityUnescaper extends CharSequenceTranslator {
   private final EnumSet options;

   public NumericEntityUnescaper(NumericEntityUnescaper.OPTION... var1) {
      if (var1.length > 0) {
         this.options = EnumSet.copyOf(Arrays.asList(var1));
      } else {
         this.options = EnumSet.copyOf(Arrays.asList(NumericEntityUnescaper.OPTION.semiColonRequired));
      }

   }

   public int translate(CharSequence var1, int var2, Writer var3) throws IOException {
      int var4 = var1.length();
      if (var1.charAt(var2) == '&' && var2 < var4 - 2 && var1.charAt(var2 + 1) == '#') {
         int var5 = var2 + 2;
         boolean var6 = false;
         char var7 = var1.charAt(var5);
         if (var7 == 'x' || var7 == 'X') {
            ++var5;
            var6 = true;
            if (var5 == var4) {
               return 0;
            }
         }

         int var8;
         for(var8 = var5; var8 < var4 && (var1.charAt(var8) >= '0' && var1.charAt(var8) <= '9' || var1.charAt(var8) >= 'a' && var1.charAt(var8) <= 'f' || var1.charAt(var8) >= 'A' && var1.charAt(var8) <= 'F'); ++var8) {
         }

         boolean var9 = var8 != var4 && var1.charAt(var8) == ';';
         if (!var9) {
            if (NumericEntityUnescaper.OPTION.semiColonRequired == null) {
               return 0;
            }

            if (NumericEntityUnescaper.OPTION.errorIfNoSemiColon == null) {
               throw new IllegalArgumentException("Semi-colon required at end of numeric entity");
            }
         }

         int var10;
         try {
            if (var6) {
               var10 = Integer.parseInt(var1.subSequence(var5, var8).toString(), 16);
            } else {
               var10 = Integer.parseInt(var1.subSequence(var5, var8).toString(), 10);
            }
         } catch (NumberFormatException var12) {
            return 0;
         }

         if (var10 > 65535) {
            char[] var11 = Character.toChars(var10);
            var3.write(var11[0]);
            var3.write(var11[1]);
         } else {
            var3.write(var10);
         }

         return 2 + var8 - var5 + (var6 ? 1 : 0) + (var9 ? 1 : 0);
      } else {
         return 0;
      }
   }

   public static enum OPTION {
      semiColonRequired,
      semiColonOptional,
      errorIfNoSemiColon;

      private static final NumericEntityUnescaper.OPTION[] $VALUES = new NumericEntityUnescaper.OPTION[]{semiColonRequired, semiColonOptional, errorIfNoSemiColon};
   }
}
