package com.sun.javafx.runtime;

import java.io.InputStream;
import java.security.AccessController;
import java.util.Hashtable;

public class SystemProperties {
   private static final String[] sysprop_table = new String[]{"application.codebase", "jfx_specific", "debug", "javafx.debug"};
   private static final String[] jfxprop_table = new String[]{"application.codebase", ""};
   private static final Hashtable sysprop_list = new Hashtable();
   private static final Hashtable jfxprop_list = new Hashtable();
   private static final String versionResourceName = "/com/sun/javafx/runtime/resources/version.properties";
   private static boolean isDebug;
   private static String codebase_value;
   public static final String codebase = "javafx.application.codebase";

   private static void setVersions() {
      InputStream var1 = SystemProperties.class.getResourceAsStream("/com/sun/javafx/runtime/resources/version.properties");

      try {
         int var0 = var1.available();
         byte[] var2 = new byte[var0];
         var1.read(var2);
         String var4 = new String(var2, "utf-8");
         setFXProperty("javafx.version", getValue(var4, "release="));
         setFXProperty("javafx.runtime.version", getValue(var4, "full="));
      } catch (Exception var5) {
      }

   }

   private static String getValue(String var0, String var1) {
      int var3;
      if ((var3 = var0.indexOf(var1)) != -1) {
         String var2 = var0.substring(var3);
         return (var3 = var2.indexOf(10)) != -1 ? var2.substring(var1.length(), var3).trim() : var2.substring(var1.length(), var2.length()).trim();
      } else {
         return "unknown";
      }
   }

   public static void addProperties(String[] var0, boolean var1) {
      if (var0 != null) {
         Hashtable var2;
         if (var1) {
            var2 = jfxprop_list;
         } else {
            var2 = sysprop_list;
         }

         for(int var3 = 0; var3 < var0.length; var3 += 2) {
            var2.put(var0[var3], var0[var3 + 1]);
         }

      }
   }

   public static String getProperty(String var0) {
      Hashtable var1 = sysprop_list;
      if (var0 == null) {
         return null;
      } else if (var0.startsWith("javafx.")) {
         var0 = var0.substring("javafx.".length());
         String var3 = (String)var1.get(var0);
         if (var3 != null && !var3.equals("")) {
            if (var3.equals("jfx_specific")) {
               var1 = jfxprop_list;
               return (String)var1.get(var0);
            } else {
               return System.getProperty(var3);
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static void clearProperty(String var0) {
      if (var0 != null) {
         Hashtable var1 = sysprop_list;
         if (var0.startsWith("javafx.".toString())) {
            var0 = var0.substring("javafx.".length());
            String var3 = (String)var1.get(var0);
            if (var3 != null) {
               var1.remove(var0);
               if (var3.equals("jfx_specific")) {
                  var1 = jfxprop_list;
                  var1.remove(var0);
               }

            }
         }
      }
   }

   public static void setFXProperty(String var0, String var1) {
      Hashtable var2 = sysprop_list;
      if (var0.startsWith("javafx.")) {
         var0 = var0.substring("javafx.".length());
         String var4 = (String)var2.get(var0);
         if (var4 == null) {
            var2.put(var0, "jfx_specific");
            var2 = jfxprop_list;
            var2.put(var0, var1);
         } else if (var4.equals("jfx_specific")) {
            var2 = jfxprop_list;
            var2.put(var0, var1);
            if ("javafx.application.codebase".equals("javafx." + var0)) {
               codebase_value = var1;
            }
         }
      }

   }

   public static boolean isDebug() {
      return isDebug;
   }

   public static String getCodebase() {
      return codebase_value;
   }

   public static void setCodebase(String var0) {
      if (var0 == null) {
         var0 = "";
      }

      codebase_value = var0;
      setFXProperty("javafx.application.codebase", var0);
   }

   static {
      AccessController.doPrivileged(() -> {
         addProperties(sysprop_table, false);
         addProperties(jfxprop_table, true);
         setVersions();
         isDebug = "true".equalsIgnoreCase(getProperty("javafx.debug"));
         return null;
      });
   }
}
