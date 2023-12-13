package com.ibm.icu.impl;

import com.ibm.icu.util.VersionInfo;

public final class ICUDebug {
   private static String params;
   private static boolean debug;
   private static boolean help;
   public static final String javaVersionString;
   public static final boolean isJDK14OrHigher;
   public static final VersionInfo javaVersion;

   public static VersionInfo getInstanceLenient(String var0) {
      int[] var1 = new int[4];
      boolean var2 = false;
      int var3 = 0;
      int var4 = 0;

      while(var3 < var0.length()) {
         char var5 = var0.charAt(var3++);
         if (var5 >= '0' && var5 <= '9') {
            if (var2) {
               var1[var4] = var1[var4] * 10 + (var5 - 48);
               if (var1[var4] > 255) {
                  var1[var4] = 0;
                  break;
               }
            } else {
               var2 = true;
               var1[var4] = var5 - 48;
            }
         } else if (var2) {
            if (var4 == 3) {
               break;
            }

            var2 = false;
            ++var4;
         }
      }

      return VersionInfo.getInstance(var1[0], var1[1], var1[2], var1[3]);
   }

   public static boolean enabled() {
      return debug;
   }

   public static boolean enabled(String var0) {
      if (debug) {
         boolean var1 = params.indexOf(var0) != -1;
         if (help) {
            System.out.println("\nICUDebug.enabled(" + var0 + ") = " + var1);
         }

         return var1;
      } else {
         return false;
      }
   }

   public static String value(String var0) {
      String var1 = "false";
      if (debug) {
         int var2 = params.indexOf(var0);
         if (var2 != -1) {
            var2 += var0.length();
            if (params.length() > var2 && params.charAt(var2) == '=') {
               ++var2;
               int var3 = params.indexOf(",", var2);
               var1 = params.substring(var2, var3 == -1 ? params.length() : var3);
            } else {
               var1 = "true";
            }
         }

         if (help) {
            System.out.println("\nICUDebug.value(" + var0 + ") = " + var1);
         }
      }

      return var1;
   }

   static {
      try {
         params = System.getProperty("ICUDebug");
      } catch (SecurityException var1) {
      }

      debug = params != null;
      help = debug && (params.equals("") || params.indexOf("help") != -1);
      if (debug) {
         System.out.println("\nICUDebug=" + params);
      }

      javaVersionString = System.getProperty("java.version", "0");
      javaVersion = getInstanceLenient(javaVersionString);
      VersionInfo var0 = VersionInfo.getInstance("1.4.0");
      isJDK14OrHigher = javaVersion.compareTo(var0) >= 0;
   }
}
