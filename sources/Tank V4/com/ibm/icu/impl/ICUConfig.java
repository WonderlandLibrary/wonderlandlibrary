package com.ibm.icu.impl;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.MissingResourceException;
import java.util.Properties;

public class ICUConfig {
   public static final String CONFIG_PROPS_FILE = "/com/ibm/icu/ICUConfig.properties";
   private static final Properties CONFIG_PROPS = new Properties();

   public static String get(String var0) {
      return get(var0, (String)null);
   }

   public static String get(String var0, String var1) {
      String var2 = null;
      String var3 = var0;
      if (System.getSecurityManager() != null) {
         try {
            var2 = (String)AccessController.doPrivileged(new PrivilegedAction(var3) {
               final String val$fname;

               {
                  this.val$fname = var1;
               }

               public String run() {
                  return System.getProperty(this.val$fname);
               }

               public Object run() {
                  return this.run();
               }
            });
         } catch (AccessControlException var5) {
         }
      } else {
         var2 = System.getProperty(var0);
      }

      if (var2 == null) {
         var2 = CONFIG_PROPS.getProperty(var0, var1);
      }

      return var2;
   }

   static {
      try {
         InputStream var0 = ICUData.getStream("/com/ibm/icu/ICUConfig.properties");
         if (var0 != null) {
            CONFIG_PROPS.load(var0);
         }
      } catch (MissingResourceException var1) {
      } catch (IOException var2) {
      }

   }
}
