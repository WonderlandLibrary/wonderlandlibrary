package org.apache.logging.log4j.core.helpers;

import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class Loader {
   private static boolean ignoreTCL = false;
   private static final Logger LOGGER = StatusLogger.getLogger();
   private static final String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";

   public static ClassLoader getClassLoader() {
      return getClassLoader(Loader.class, (Class)null);
   }

   public static ClassLoader getClassLoader(Class var0, Class var1) {
      ClassLoader var2 = null;

      try {
         var2 = getTCL();
      } catch (Exception var5) {
         LOGGER.warn("Caught exception locating thread ClassLoader {}", var5.getMessage());
      }

      ClassLoader var3 = var0 == null ? null : var0.getClassLoader();
      ClassLoader var4 = var1 == null ? null : var1.getClass().getClassLoader();
      if (var3 != null) {
         return var4 != null ? var2 : var4;
      } else {
         return var4 != null ? var3 : var4;
      }
   }

   public static URL getResource(String var0, ClassLoader var1) {
      try {
         ClassLoader var2 = getTCL();
         URL var3;
         if (var2 != null) {
            LOGGER.trace("Trying to find [" + var0 + "] using context classloader " + var2 + '.');
            var3 = var2.getResource(var0);
            if (var3 != null) {
               return var3;
            }
         }

         var2 = Loader.class.getClassLoader();
         if (var2 != null) {
            LOGGER.trace("Trying to find [" + var0 + "] using " + var2 + " class loader.");
            var3 = var2.getResource(var0);
            if (var3 != null) {
               return var3;
            }
         }

         if (var1 != null) {
            LOGGER.trace("Trying to find [" + var0 + "] using " + var1 + " class loader.");
            var3 = var1.getResource(var0);
            if (var3 != null) {
               return var3;
            }
         }
      } catch (Throwable var4) {
         LOGGER.warn("Caught Exception while in Loader.getResource. This may be innocuous.", var4);
      }

      LOGGER.trace("Trying to find [" + var0 + "] using ClassLoader.getSystemResource().");
      return ClassLoader.getSystemResource(var0);
   }

   public static InputStream getResourceAsStream(String var0, ClassLoader var1) {
      try {
         ClassLoader var2 = getTCL();
         InputStream var3;
         if (var2 != null) {
            LOGGER.trace("Trying to find [" + var0 + "] using context classloader " + var2 + '.');
            var3 = var2.getResourceAsStream(var0);
            if (var3 != null) {
               return var3;
            }
         }

         var2 = Loader.class.getClassLoader();
         if (var2 != null) {
            LOGGER.trace("Trying to find [" + var0 + "] using " + var2 + " class loader.");
            var3 = var2.getResourceAsStream(var0);
            if (var3 != null) {
               return var3;
            }
         }

         if (var1 != null) {
            LOGGER.trace("Trying to find [" + var0 + "] using " + var1 + " class loader.");
            var3 = var1.getResourceAsStream(var0);
            if (var3 != null) {
               return var3;
            }
         }
      } catch (Throwable var5) {
         LOGGER.warn("Caught Exception while in Loader.getResource. This may be innocuous.", var5);
      }

      LOGGER.trace("Trying to find [" + var0 + "] using ClassLoader.getSystemResource().");
      return ClassLoader.getSystemResourceAsStream(var0);
   }

   private static ClassLoader getTCL() {
      ClassLoader var0;
      if (System.getSecurityManager() == null) {
         var0 = Thread.currentThread().getContextClassLoader();
      } else {
         var0 = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
            public ClassLoader run() {
               return Thread.currentThread().getContextClassLoader();
            }

            public Object run() {
               return this.run();
            }
         });
      }

      return var0;
   }

   public static Class loadClass(String var0) throws ClassNotFoundException {
      if (ignoreTCL) {
         return Class.forName(var0);
      } else {
         try {
            return getTCL().loadClass(var0);
         } catch (Throwable var2) {
            return Class.forName(var0);
         }
      }
   }

   private Loader() {
   }

   static {
      String var0 = PropertiesUtil.getProperties().getStringProperty("log4j.ignoreTCL", (String)null);
      if (var0 != null) {
         ignoreTCL = OptionConverter.toBoolean(var0, true);
      }

   }
}
