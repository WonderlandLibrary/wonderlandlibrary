package org.apache.commons.lang3.text;

import java.util.Formattable;
import java.util.Formatter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;

public class FormattableUtils {
   private static final String SIMPLEST_FORMAT = "%s";

   public static String toString(Formattable var0) {
      return String.format("%s", var0);
   }

   public static Formatter append(CharSequence var0, Formatter var1, int var2, int var3, int var4) {
      return append(var0, var1, var2, var3, var4, ' ', (CharSequence)null);
   }

   public static Formatter append(CharSequence var0, Formatter var1, int var2, int var3, int var4, char var5) {
      return append(var0, var1, var2, var3, var4, var5, (CharSequence)null);
   }

   public static Formatter append(CharSequence var0, Formatter var1, int var2, int var3, int var4, CharSequence var5) {
      return append(var0, var1, var2, var3, var4, ' ', var5);
   }

   public static Formatter append(CharSequence var0, Formatter var1, int var2, int var3, int var4, char var5, CharSequence var6) {
      Validate.isTrue(var6 == null || var4 < 0 || var6.length() <= var4, "Specified ellipsis '%1$s' exceeds precision of %2$s", var6, var4);
      StringBuilder var7 = new StringBuilder(var0);
      if (var4 >= 0 && var4 < var0.length()) {
         CharSequence var8 = (CharSequence)ObjectUtils.defaultIfNull(var6, "");
         var7.replace(var4 - var8.length(), var0.length(), var8.toString());
      }

      boolean var10 = (var2 & 1) == 1;

      for(int var9 = var7.length(); var9 < var3; ++var9) {
         var7.insert(var10 ? var9 : 0, var5);
      }

      var1.format(var7.toString());
      return var1;
   }
}
