package org.apache.logging.log4j.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.Logger;

public final class ProviderUtil {
   private static final String PROVIDER_RESOURCE = "META-INF/log4j-provider.properties";
   private static final String API_VERSION = "Log4jAPIVersion";
   private static final String[] COMPATIBLE_API_VERSIONS;
   private static final Logger LOGGER;
   private static final List PROVIDERS;

   private ProviderUtil() {
   }

   public static Iterator getProviders() {
      return PROVIDERS.iterator();
   }

   public static boolean hasProviders() {
      return PROVIDERS.size() > 0;
   }

   public static ClassLoader findClassLoader() {
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

      if (var0 == null) {
         var0 = ProviderUtil.class.getClassLoader();
      }

      return var0;
   }

   static {
      // $FF: Couldn't be decompiled
   }
}
