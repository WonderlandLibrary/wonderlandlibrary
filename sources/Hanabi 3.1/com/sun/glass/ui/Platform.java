package com.sun.glass.ui;

import java.security.AccessController;
import java.util.Locale;

final class Platform {
   public static final String MAC = "Mac";
   public static final String WINDOWS = "Win";
   public static final String GTK = "Gtk";
   public static final String IOS = "Ios";
   public static final String UNKNOWN = "unknown";
   private static String type = null;

   public static synchronized String determinePlatform() {
      if (type == null) {
         String var0 = (String)AccessController.doPrivileged(() -> {
            return System.getProperty("glass.platform");
         });
         if (var0 != null) {
            if (var0.equals("macosx")) {
               type = "Mac";
            } else if (var0.equals("windows")) {
               type = "Win";
            } else if (var0.equals("linux")) {
               type = "Gtk";
            } else if (var0.equals("gtk")) {
               type = "Gtk";
            } else if (var0.equals("ios")) {
               type = "Ios";
            } else {
               type = var0;
            }

            return type;
         }

         String var1 = System.getProperty("os.name");
         String var2 = var1.toLowerCase(Locale.ROOT);
         if (!var2.startsWith("mac") && !var2.startsWith("darwin")) {
            if (var2.startsWith("wind")) {
               type = "Win";
            } else if (var2.startsWith("linux")) {
               type = "Gtk";
            }
         } else {
            type = "Mac";
         }
      }

      return type;
   }
}
