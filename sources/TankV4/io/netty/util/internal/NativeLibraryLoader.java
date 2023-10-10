package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.File;
import java.util.Locale;

public final class NativeLibraryLoader {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NativeLibraryLoader.class);
   private static final String NATIVE_RESOURCE_HOME = "META-INF/native/";
   private static final String OSNAME;
   private static final File WORKDIR;

   private static File tmpdir() {
      File var0;
      try {
         var0 = toDirectory(SystemPropertyUtil.get("io.netty.tmpdir"));
         if (var0 != null) {
            logger.debug("-Dio.netty.tmpdir: " + var0);
            return var0;
         }

         var0 = toDirectory(SystemPropertyUtil.get("java.io.tmpdir"));
         if (var0 != null) {
            logger.debug("-Dio.netty.tmpdir: " + var0 + " (java.io.tmpdir)");
            return var0;
         }

         if (isWindows()) {
            var0 = toDirectory(System.getenv("TEMP"));
            if (var0 != null) {
               logger.debug("-Dio.netty.tmpdir: " + var0 + " (%TEMP%)");
               return var0;
            }

            String var1 = System.getenv("USERPROFILE");
            if (var1 != null) {
               var0 = toDirectory(var1 + "\\AppData\\Local\\Temp");
               if (var0 != null) {
                  logger.debug("-Dio.netty.tmpdir: " + var0 + " (%USERPROFILE%\\AppData\\Local\\Temp)");
                  return var0;
               }

               var0 = toDirectory(var1 + "\\Local Settings\\Temp");
               if (var0 != null) {
                  logger.debug("-Dio.netty.tmpdir: " + var0 + " (%USERPROFILE%\\Local Settings\\Temp)");
                  return var0;
               }
            }
         } else {
            var0 = toDirectory(System.getenv("TMPDIR"));
            if (var0 != null) {
               logger.debug("-Dio.netty.tmpdir: " + var0 + " ($TMPDIR)");
               return var0;
            }
         }
      } catch (Exception var2) {
      }

      if (isWindows()) {
         var0 = new File("C:\\Windows\\Temp");
      } else {
         var0 = new File("/tmp");
      }

      logger.warn("Failed to get the temporary directory; falling back to: " + var0);
      return var0;
   }

   private static File toDirectory(String var0) {
      if (var0 == null) {
         return null;
      } else {
         File var1 = new File(var0);
         var1.mkdirs();
         if (!var1.isDirectory()) {
            return null;
         } else {
            try {
               return var1.getAbsoluteFile();
            } catch (Exception var3) {
               return var1;
            }
         }
      }
   }

   private static boolean isWindows() {
      return OSNAME.startsWith("windows");
   }

   public static void load(String param0, ClassLoader param1) {
      // $FF: Couldn't be decompiled
   }

   private NativeLibraryLoader() {
   }

   static {
      OSNAME = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
      String var0 = SystemPropertyUtil.get("io.netty.native.workdir");
      if (var0 != null) {
         File var1 = new File(var0);
         var1.mkdirs();

         try {
            var1 = var1.getAbsoluteFile();
         } catch (Exception var3) {
         }

         WORKDIR = var1;
         logger.debug("-Dio.netty.netty.workdir: " + WORKDIR);
      } else {
         WORKDIR = tmpdir();
         logger.debug("-Dio.netty.netty.workdir: " + WORKDIR + " (io.netty.tmpdir)");
      }

   }
}
