package org.lwjgl;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import org.lwjgl.input.Mouse;

public final class Sys {
   private static final String JNI_LIBRARY_NAME = "lwjgl";
   private static final String VERSION = "2.9.1";
   private static final String POSTFIX64BIT = "64";
   private static final SysImplementation implementation = createImplementation();
   private static final boolean is64Bit;

   private static void doLoadLibrary(String var0) {
      AccessController.doPrivileged(new PrivilegedAction(var0) {
         final String val$lib_name;

         {
            this.val$lib_name = var1;
         }

         public Object run() {
            String var1 = System.getProperty("org.lwjgl.librarypath");
            if (var1 != null) {
               System.load(var1 + File.separator + System.mapLibraryName(this.val$lib_name));
            } else {
               System.loadLibrary(this.val$lib_name);
            }

            return null;
         }
      });
   }

   private static void loadLibrary(String var0) {
      String var1 = System.getProperty("os.arch");
      boolean var2 = "amd64".equals(var1) || "x86_64".equals(var1);
      if (var2) {
         try {
            doLoadLibrary(var0 + "64");
            return;
         } catch (UnsatisfiedLinkError var7) {
            LWJGLUtil.log("Failed to load 64 bit library: " + var7.getMessage());
         }
      }

      try {
         doLoadLibrary(var0);
      } catch (UnsatisfiedLinkError var6) {
         if (implementation.has64Bit()) {
            try {
               doLoadLibrary(var0 + "64");
               return;
            } catch (UnsatisfiedLinkError var5) {
               LWJGLUtil.log("Failed to load 64 bit library: " + var5.getMessage());
            }
         }

         throw var6;
      }
   }

   private static SysImplementation createImplementation() {
      switch(LWJGLUtil.getPlatform()) {
      case 1:
         return new LinuxSysImplementation();
      case 2:
         return new MacOSXSysImplementation();
      case 3:
         return new WindowsSysImplementation();
      default:
         throw new IllegalStateException("Unsupported platform");
      }
   }

   private Sys() {
   }

   public static String getVersion() {
      return "2.9.1";
   }

   public static void initialize() {
   }

   public static boolean is64Bit() {
      return is64Bit;
   }

   public static long getTimerResolution() {
      return implementation.getTimerResolution();
   }

   public static long getTime() {
      return implementation.getTime() & Long.MAX_VALUE;
   }

   public static void alert(String var0, String var1) {
      boolean var2 = Mouse.isGrabbed();
      if (var2) {
         Mouse.setGrabbed(false);
      }

      if (var0 == null) {
         var0 = "";
      }

      if (var1 == null) {
         var1 = "";
      }

      implementation.alert(var0, var1);
      if (var2) {
         Mouse.setGrabbed(true);
      }

   }

   public static boolean openURL(String var0) {
      try {
         Class var1 = Class.forName("javax.jnlp.ServiceManager");
         Method var2 = (Method)AccessController.doPrivileged(new PrivilegedExceptionAction(var1) {
            final Class val$serviceManagerClass;

            {
               this.val$serviceManagerClass = var1;
            }

            public Method run() throws Exception {
               return this.val$serviceManagerClass.getMethod("lookup", String.class);
            }

            public Object run() throws Exception {
               return this.run();
            }
         });
         Object var3 = var2.invoke(var1, "javax.jnlp.BasicService");
         Class var4 = Class.forName("javax.jnlp.BasicService");
         Method var5 = (Method)AccessController.doPrivileged(new PrivilegedExceptionAction(var4) {
            final Class val$basicServiceClass;

            {
               this.val$basicServiceClass = var1;
            }

            public Method run() throws Exception {
               return this.val$basicServiceClass.getMethod("showDocument", URL.class);
            }

            public Object run() throws Exception {
               return this.run();
            }
         });

         try {
            Boolean var6 = (Boolean)var5.invoke(var3, new URL(var0));
            return var6;
         } catch (MalformedURLException var7) {
            var7.printStackTrace(System.err);
            return false;
         }
      } catch (Exception var8) {
         return implementation.openURL(var0);
      }
   }

   public static String getClipboard() {
      return implementation.getClipboard();
   }

   static {
      loadLibrary("lwjgl");
      is64Bit = implementation.getPointerSize() == 8;
      int var0 = implementation.getJNIVersion();
      int var1 = implementation.getRequiredJNIVersion();
      if (var0 != var1) {
         throw new LinkageError("Version mismatch: jar version is '" + var1 + "', native library version is '" + var0 + "'");
      } else {
         implementation.setDebug(LWJGLUtil.DEBUG);
      }
   }
}
