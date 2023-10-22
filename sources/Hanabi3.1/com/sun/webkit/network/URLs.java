package com.sun.webkit.network;

import com.sun.webkit.network.about.Handler;
import java.net.MalformedURLException;
import java.net.NetPermission;
import java.net.URL;
import java.net.URLStreamHandler;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permission;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class URLs {
   private static final Map handlerMap;
   private static final Permission streamHandlerPermission;

   private URLs() {
      throw new AssertionError();
   }

   public static URL newURL(String var0) throws MalformedURLException {
      return newURL((URL)null, var0);
   }

   public static URL newURL(URL var0, String var1) throws MalformedURLException {
      try {
         return new URL(var0, var1);
      } catch (MalformedURLException var7) {
         int var3 = var1.indexOf(58);
         URLStreamHandler var4 = var3 != -1 ? (URLStreamHandler)handlerMap.get(var1.substring(0, var3).toLowerCase()) : null;
         if (var4 == null) {
            throw var7;
         } else {
            try {
               return (URL)AccessController.doPrivileged(() -> {
                  try {
                     return new URL(var0, var1, var4);
                  } catch (MalformedURLException var4x) {
                     throw new RuntimeException(var4x);
                  }
               }, (AccessControlContext)null, streamHandlerPermission);
            } catch (RuntimeException var6) {
               if (var6.getCause() instanceof MalformedURLException) {
                  throw (MalformedURLException)var6.getCause();
               } else {
                  throw var6;
               }
            }
         }
      }
   }

   static {
      HashMap var0 = new HashMap(2);
      var0.put("about", new Handler());
      var0.put("data", new com.sun.webkit.network.data.Handler());
      handlerMap = Collections.unmodifiableMap(var0);
      streamHandlerPermission = new NetPermission("specifyStreamHandler");
   }
}
