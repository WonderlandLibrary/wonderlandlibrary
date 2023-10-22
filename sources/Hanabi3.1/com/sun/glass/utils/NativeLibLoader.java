package com.sun.glass.utils;

import java.io.File;
import java.net.URI;
import java.security.AccessController;
import java.util.HashSet;

public class NativeLibLoader {
   private static final HashSet loaded = new HashSet();
   private static boolean verbose = false;
   private static File libDir = null;
   private static String libPrefix = "";
   private static String libSuffix = "";

   public static synchronized void loadLibrary(String var0) {
      if (!loaded.contains(var0)) {
         loadLibraryInternal(var0);
         loaded.add(var0);
      }

   }

   private static String[] initializePath(String var0) {
      String var1 = System.getProperty(var0, "");
      String var2 = File.pathSeparator;
      int var3 = var1.length();
      int var4 = var1.indexOf(var2);

      int var6;
      for(var6 = 0; var4 >= 0; var4 = var1.indexOf(var2, var4 + 1)) {
         ++var6;
      }

      String[] var7 = new String[var6 + 1];
      var4 = 0;
      var6 = 0;

      for(int var5 = var1.indexOf(var2); var5 >= 0; var5 = var1.indexOf(var2, var4)) {
         if (var5 - var4 > 0) {
            var7[var6++] = var1.substring(var4, var5);
         } else if (var5 - var4 == 0) {
            var7[var6++] = ".";
         }

         var4 = var5 + 1;
      }

      var7[var6] = var1.substring(var4, var3);
      return var7;
   }

   private static void loadLibraryInternal(String var0) {
      try {
         loadLibraryFullPath(var0);
      } catch (UnsatisfiedLinkError var10) {
         UnsatisfiedLinkError var1 = var10;
         String[] var2 = initializePath("java.library.path");
         int var3 = 0;

         while(true) {
            if (var3 >= var2.length) {
               try {
                  System.loadLibrary(var0);
                  if (verbose) {
                     System.err.println("WARNING: " + var1.toString());
                     System.err.println("    using System.loadLibrary(" + var0 + ") as a fallback");
                  }
                  break;
               } catch (UnsatisfiedLinkError var8) {
                  if ("iOS".equals(System.getProperty("os.name")) && var0.contains("-")) {
                     var0 = var0.replace("-", "_");

                     try {
                        System.loadLibrary(var0);
                        return;
                     } catch (UnsatisfiedLinkError var7) {
                        throw var7;
                     }
                  }

                  throw var10;
               }
            }

            try {
               String var4 = var2[var3];
               if (!var4.endsWith(File.separator)) {
                  var4 = var4 + File.separator;
               }

               String var5 = System.mapLibraryName(var0);
               File var6 = new File(var4 + var5);
               System.load(var6.getAbsolutePath());
               if (verbose) {
                  System.err.println("Loaded " + var6.getAbsolutePath() + " from java.library.path");
               }

               return;
            } catch (UnsatisfiedLinkError var9) {
               ++var3;
            }
         }
      }

   }

   private static void loadLibraryFullPath(String var0) {
      try {
         if (libDir == null) {
            String var1 = "NativeLibLoader.class";
            Class var2 = NativeLibLoader.class;
            String var3 = var2.getResource(var1).toString();
            if (!var3.startsWith("jar:file:") || var3.indexOf(33) == -1) {
               throw new UnsatisfiedLinkError("Invalid URL for class: " + var3);
            }

            String var4 = var3.substring(4, var3.lastIndexOf(33));
            int var5 = Math.max(var4.lastIndexOf(47), var4.lastIndexOf(92));
            String var6 = System.getProperty("os.name");
            String var7 = null;
            if (var6.startsWith("Windows")) {
               var7 = "../../bin";
            } else if (var6.startsWith("Mac")) {
               var7 = "..";
            } else if (var6.startsWith("Linux")) {
               var7 = "../" + System.getProperty("os.arch");
            }

            String var8 = var4.substring(0, var5) + "/" + var7;
            libDir = new File((new URI(var8)).getPath());
            if (var6.startsWith("Windows")) {
               libPrefix = "";
               libSuffix = ".dll";
            } else if (var6.startsWith("Mac")) {
               libPrefix = "lib";
               libSuffix = ".dylib";
            } else if (var6.startsWith("Linux")) {
               libPrefix = "lib";
               libSuffix = ".so";
            }
         }

         File var11 = new File(libDir, libPrefix + var0 + libSuffix);
         String var12 = var11.getCanonicalPath();

         try {
            System.load(var12);
            if (verbose) {
               System.err.println("Loaded " + var11.getAbsolutePath() + " from relative path");
            }

         } catch (UnsatisfiedLinkError var9) {
            throw var9;
         }
      } catch (Exception var10) {
         throw (UnsatisfiedLinkError)(new UnsatisfiedLinkError()).initCause(var10);
      }
   }

   static {
      AccessController.doPrivileged(() -> {
         verbose = Boolean.getBoolean("javafx.verbose");
         return null;
      });
   }
}
