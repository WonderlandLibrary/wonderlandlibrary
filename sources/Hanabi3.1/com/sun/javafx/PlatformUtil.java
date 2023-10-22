package com.sun.javafx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.util.Iterator;
import java.util.Properties;

public class PlatformUtil {
   private static final String os = System.getProperty("os.name");
   private static final String version = System.getProperty("os.version");
   private static final boolean embedded;
   private static final String embeddedType;
   private static final boolean useEGL;
   private static final boolean doEGLCompositing;
   private static String javafxPlatform = (String)AccessController.doPrivileged(() -> {
      return System.getProperty("javafx.platform");
   });
   private static final boolean ANDROID;
   private static final boolean WINDOWS;
   private static final boolean WINDOWS_32_BIT;
   private static final boolean WINDOWS_VISTA_OR_LATER;
   private static final boolean WINDOWS_7_OR_LATER;
   private static final boolean MAC;
   private static final boolean LINUX;
   private static final boolean SOLARIS;
   private static final boolean IOS;

   private static boolean versionNumberGreaterThanOrEqualTo(float var0) {
      try {
         return Float.parseFloat(version) >= var0;
      } catch (Exception var2) {
         return false;
      }
   }

   public static boolean isWindows32Bit() {
      return WINDOWS_32_BIT;
   }

   public static boolean isWindows() {
      return WINDOWS;
   }

   public static boolean isWinVistaOrLater() {
      return WINDOWS_VISTA_OR_LATER;
   }

   public static boolean isWin7OrLater() {
      return WINDOWS_7_OR_LATER;
   }

   public static boolean isMac() {
      return MAC;
   }

   public static boolean isLinux() {
      return LINUX;
   }

   public static boolean useEGL() {
      return useEGL;
   }

   public static boolean useEGLWindowComposition() {
      return doEGLCompositing;
   }

   public static boolean useGLES2() {
      String var0 = "false";
      var0 = (String)AccessController.doPrivileged(() -> {
         return System.getProperty("use.gles2");
      });
      return "true".equals(var0);
   }

   public static boolean isSolaris() {
      return SOLARIS;
   }

   public static boolean isUnix() {
      return LINUX || SOLARIS;
   }

   public static boolean isEmbedded() {
      return embedded;
   }

   public static String getEmbeddedType() {
      return embeddedType;
   }

   public static boolean isIOS() {
      return IOS;
   }

   private static void loadPropertiesFromFile(File var0) {
      Properties var1 = new Properties();

      try {
         FileInputStream var2 = new FileInputStream(var0);
         var1.load(var2);
         var2.close();
      } catch (IOException var10) {
         var10.printStackTrace();
      }

      if (javafxPlatform == null) {
         javafxPlatform = var1.getProperty("javafx.platform");
      }

      String var11 = javafxPlatform + ".";
      int var3 = var11.length();
      boolean var4 = false;
      Iterator var5 = var1.keySet().iterator();

      while(var5.hasNext()) {
         Object var6 = var5.next();
         String var7 = (String)var6;
         if (var7.startsWith(var11)) {
            var4 = true;
            String var8 = var7.substring(var3);
            if (System.getProperty(var8) == null) {
               String var9 = var1.getProperty(var7);
               System.setProperty(var8, var9);
            }
         }
      }

      if (!var4) {
         System.err.println("Warning: No settings found for javafx.platform='" + javafxPlatform + "'");
      }

   }

   private static File getRTDir() {
      try {
         String var0 = "PlatformUtil.class";
         Class var1 = PlatformUtil.class;
         URL var2 = var1.getResource(var0);
         if (var2 == null) {
            return null;
         } else {
            String var3 = var2.toString();
            if (var3.startsWith("jar:file:") && var3.indexOf(33) != -1) {
               String var4 = var3.substring(4, var3.lastIndexOf(33));
               int var5 = Math.max(var4.lastIndexOf(47), var4.lastIndexOf(92));
               return (new File((new URL(var4.substring(0, var5 + 1))).getPath())).getParentFile();
            } else {
               return null;
            }
         }
      } catch (MalformedURLException var6) {
         return null;
      }
   }

   private static void loadProperties() {
      String var0 = System.getProperty("java.vm.name");
      String var1 = System.getProperty("os.arch");
      if (javafxPlatform != null || var1 != null && var1.equals("arm") || var0 != null && var0.indexOf("Embedded") > 0) {
         AccessController.doPrivileged(() -> {
            File var0 = getRTDir();
            String var1 = "javafx.platform.properties";
            File var2 = new File(var0, "javafx.platform.properties");
            if (var2.exists()) {
               loadPropertiesFromFile(var2);
               return null;
            } else {
               String var3 = System.getProperty("java.home");
               File var4 = new File(var3, "lib" + File.separator + "javafx.platform.properties");
               if (var4.exists()) {
                  loadPropertiesFromFile(var4);
                  return null;
               } else {
                  String var5 = System.getProperty("javafx.runtime.path");
                  File var6 = new File(var5, File.separator + "javafx.platform.properties");
                  if (var6.exists()) {
                     loadPropertiesFromFile(var6);
                     return null;
                  } else {
                     return null;
                  }
               }
            }
         });
      }
   }

   public static boolean isAndroid() {
      return ANDROID;
   }

   static {
      loadProperties();
      embedded = (Boolean)AccessController.doPrivileged(() -> {
         return Boolean.getBoolean("com.sun.javafx.isEmbedded");
      });
      embeddedType = (String)AccessController.doPrivileged(() -> {
         return System.getProperty("embedded");
      });
      useEGL = (Boolean)AccessController.doPrivileged(() -> {
         return Boolean.getBoolean("use.egl");
      });
      if (useEGL) {
         doEGLCompositing = (Boolean)AccessController.doPrivileged(() -> {
            return Boolean.getBoolean("doNativeComposite");
         });
      } else {
         doEGLCompositing = false;
      }

      ANDROID = "android".equals(javafxPlatform) || "Dalvik".equals(System.getProperty("java.vm.name"));
      WINDOWS = os.startsWith("Windows");
      WINDOWS_32_BIT = os.startsWith("Windows") && !System.getProperty("os.arch").contains("64");
      WINDOWS_VISTA_OR_LATER = WINDOWS && versionNumberGreaterThanOrEqualTo(6.0F);
      WINDOWS_7_OR_LATER = WINDOWS && versionNumberGreaterThanOrEqualTo(6.1F);
      MAC = os.startsWith("Mac");
      LINUX = os.startsWith("Linux") && !ANDROID;
      SOLARIS = os.startsWith("SunOS");
      IOS = os.startsWith("iOS");
   }
}
