package org.apache.logging.log4j.core.helpers;

import java.nio.charset.Charset;
import org.apache.logging.log4j.status.StatusLogger;

public final class Charsets {
   public static final Charset UTF_8 = Charset.forName("UTF-8");

   public static Charset getSupportedCharset(String var0) {
      return getSupportedCharset(var0, Charset.defaultCharset());
   }

   public static Charset getSupportedCharset(String var0, Charset var1) {
      Charset var2 = null;
      if (var0 != null && Charset.isSupported(var0)) {
         var2 = Charset.forName(var0);
      }

      if (var2 == null) {
         var2 = var1;
         if (var0 != null) {
            StatusLogger.getLogger().error("Charset " + var0 + " is not supported for layout, using " + var1.displayName());
         }
      }

      return var2;
   }

   private Charsets() {
   }
}
