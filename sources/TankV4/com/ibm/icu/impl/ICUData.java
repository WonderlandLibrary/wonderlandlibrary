package com.ibm.icu.impl;

import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.MissingResourceException;

public final class ICUData {
   public static boolean exists(String var0) {
      URL var1 = null;
      if (System.getSecurityManager() != null) {
         var1 = (URL)AccessController.doPrivileged(new PrivilegedAction(var0) {
            final String val$resourceName;

            {
               this.val$resourceName = var1;
            }

            public URL run() {
               return ICUData.class.getResource(this.val$resourceName);
            }

            public Object run() {
               return this.run();
            }
         });
      } else {
         var1 = ICUData.class.getResource(var0);
      }

      return var1 != null;
   }

   private static InputStream getStream(Class var0, String var1, boolean var2) {
      InputStream var3 = null;
      if (System.getSecurityManager() != null) {
         var3 = (InputStream)AccessController.doPrivileged(new PrivilegedAction(var0, var1) {
            final Class val$root;
            final String val$resourceName;

            {
               this.val$root = var1;
               this.val$resourceName = var2;
            }

            public InputStream run() {
               return this.val$root.getResourceAsStream(this.val$resourceName);
            }

            public Object run() {
               return this.run();
            }
         });
      } else {
         var3 = var0.getResourceAsStream(var1);
      }

      if (var3 == null && var2) {
         throw new MissingResourceException("could not locate data " + var1, var0.getPackage().getName(), var1);
      } else {
         return var3;
      }
   }

   private static InputStream getStream(ClassLoader var0, String var1, boolean var2) {
      InputStream var3 = null;
      if (System.getSecurityManager() != null) {
         var3 = (InputStream)AccessController.doPrivileged(new PrivilegedAction(var0, var1) {
            final ClassLoader val$loader;
            final String val$resourceName;

            {
               this.val$loader = var1;
               this.val$resourceName = var2;
            }

            public InputStream run() {
               return this.val$loader.getResourceAsStream(this.val$resourceName);
            }

            public Object run() {
               return this.run();
            }
         });
      } else {
         var3 = var0.getResourceAsStream(var1);
      }

      if (var3 == null && var2) {
         throw new MissingResourceException("could not locate data", var0.toString(), var1);
      } else {
         return var3;
      }
   }

   public static InputStream getStream(ClassLoader var0, String var1) {
      return getStream(var0, var1, false);
   }

   public static InputStream getRequiredStream(ClassLoader var0, String var1) {
      return getStream(var0, var1, true);
   }

   public static InputStream getStream(String var0) {
      return getStream(ICUData.class, var0, false);
   }

   public static InputStream getRequiredStream(String var0) {
      return getStream(ICUData.class, var0, true);
   }

   public static InputStream getStream(Class var0, String var1) {
      return getStream(var0, var1, false);
   }

   public static InputStream getRequiredStream(Class var0, String var1) {
      return getStream(var0, var1, true);
   }
}
