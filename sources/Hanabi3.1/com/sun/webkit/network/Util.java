package com.sun.webkit.network;

import java.net.MalformedURLException;

public final class Util {
   private Util() {
      throw new AssertionError();
   }

   public static String adjustUrlForWebKit(String var0) throws MalformedURLException {
      if (URLs.newURL(var0).getProtocol().equals("file")) {
         int var1 = "file:".length();
         if (var1 < var0.length() && var0.charAt(var1) != '/') {
            var0 = var0.substring(0, var1) + "///" + var0.substring(var1);
         }
      }

      return var0;
   }

   static String formatHeaders(String var0) {
      return var0.trim().replaceAll("(?m)^", "    ");
   }
}
