package org.apache.logging.log4j.util;

import java.util.Locale;

public final class EnglishEnums {
   private EnglishEnums() {
   }

   public static Enum valueOf(Class var0, String var1) {
      return valueOf(var0, var1, (Enum)null);
   }

   public static Enum valueOf(Class var0, String var1, Enum var2) {
      return var1 == null ? var2 : Enum.valueOf(var0, var1.toUpperCase(Locale.ENGLISH));
   }
}
