package com.sun.webkit.network;

import java.net.CookieHandler;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CookieManager extends CookieHandler {
   private static final Logger logger = Logger.getLogger(CookieManager.class.getName());
   private final CookieStore store = new CookieStore();

   public Map get(URI var1, Map var2) {
      if (logger.isLoggable(Level.FINEST)) {
         logger.log(Level.FINEST, "uri: [{0}], requestHeaders: {1}", new Object[]{var1, toLogString(var2)});
      }

      if (var1 == null) {
         throw new IllegalArgumentException("uri is null");
      } else if (var2 == null) {
         throw new IllegalArgumentException("requestHeaders is null");
      } else {
         String var3 = this.get(var1);
         Object var4;
         if (var3 != null) {
            var4 = new HashMap();
            ((Map)var4).put("Cookie", Arrays.asList(var3));
         } else {
            var4 = Collections.emptyMap();
         }

         if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "result: {0}", toLogString((Map)var4));
         }

         return (Map)var4;
      }
   }

   private String get(URI var1) {
      String var2 = var1.getHost();
      if (var2 != null && var2.length() != 0) {
         var2 = canonicalize(var2);
         String var3 = var1.getScheme();
         boolean var4 = "https".equalsIgnoreCase(var3) || "javascripts".equalsIgnoreCase(var3);
         boolean var5 = "http".equalsIgnoreCase(var3) || "https".equalsIgnoreCase(var3);
         List var6;
         synchronized(this.store) {
            var6 = this.store.get(var2, var1.getPath(), var4, var5);
         }

         StringBuilder var7 = new StringBuilder();
         Iterator var8 = var6.iterator();

         while(var8.hasNext()) {
            Cookie var9 = (Cookie)var8.next();
            if (var7.length() > 0) {
               var7.append("; ");
            }

            var7.append(var9.getName());
            var7.append('=');
            var7.append(var9.getValue());
         }

         return var7.length() > 0 ? var7.toString() : null;
      } else {
         logger.log(Level.FINEST, "Null or empty URI host, returning null");
         return null;
      }
   }

   public void put(URI var1, Map var2) {
      if (logger.isLoggable(Level.FINEST)) {
         logger.log(Level.FINEST, "uri: [{0}], responseHeaders: {1}", new Object[]{var1, toLogString(var2)});
      }

      if (var1 == null) {
         throw new IllegalArgumentException("uri is null");
      } else if (var2 == null) {
         throw new IllegalArgumentException("responseHeaders is null");
      } else {
         Iterator var3 = var2.entrySet().iterator();

         while(true) {
            Map.Entry var4;
            String var5;
            do {
               if (!var3.hasNext()) {
                  return;
               }

               var4 = (Map.Entry)var3.next();
               var5 = (String)var4.getKey();
            } while(!"Set-Cookie".equalsIgnoreCase(var5));

            ExtendedTime var6 = ExtendedTime.currentTime();
            ListIterator var7 = ((List)var4.getValue()).listIterator(((List)var4.getValue()).size());

            while(var7.hasPrevious()) {
               Cookie var8 = Cookie.parse((String)var7.previous(), var6);
               if (var8 != null) {
                  this.put(var1, var8);
                  var6 = var6.incrementSubtime();
               }
            }
         }
      }
   }

   private void put(URI var1, Cookie var2) {
      logger.log(Level.FINEST, "cookie: {0}", var2);
      String var3 = var1.getHost();
      if (var3 != null && var3.length() != 0) {
         var3 = canonicalize(var3);
         if (!PublicSuffixes.pslFileExists()) {
            var2.setDomain("");
         } else if (PublicSuffixes.isPublicSuffix(var2.getDomain())) {
            if (!var2.getDomain().equals(var3)) {
               logger.log(Level.FINEST, "Domain is public suffix, ignoring cookie");
               return;
            }

            var2.setDomain("");
         }

         if (var2.getDomain().length() > 0) {
            if (!Cookie.domainMatches(var3, var2.getDomain())) {
               logger.log(Level.FINEST, "Hostname does not match domain, ignoring cookie");
               return;
            }

            var2.setHostOnly(false);
         } else {
            var2.setHostOnly(true);
            var2.setDomain(var3);
         }

         if (var2.getPath() == null) {
            var2.setPath(Cookie.defaultPath(var1));
         }

         boolean var4 = "http".equalsIgnoreCase(var1.getScheme()) || "https".equalsIgnoreCase(var1.getScheme());
         if (var2.getHttpOnly() && !var4) {
            logger.log(Level.FINEST, "HttpOnly cookie received from non-HTTP API, ignoring cookie");
         } else {
            synchronized(this.store) {
               Cookie var6 = this.store.get(var2);
               if (var6 != null) {
                  if (var6.getHttpOnly() && !var4) {
                     logger.log(Level.FINEST, "Non-HTTP API attempts to overwrite HttpOnly cookie, blocked");
                     return;
                  }

                  var2.setCreationTime(var6.getCreationTime());
               }

               this.store.put(var2);
            }

            logger.log(Level.FINEST, "Stored: {0}", var2);
         }
      } else {
         logger.log(Level.FINEST, "Null or empty URI host, ignoring cookie");
      }
   }

   private static String toLogString(Map var0) {
      if (var0 == null) {
         return null;
      } else if (var0.isEmpty()) {
         return "{}";
      } else {
         StringBuilder var1 = new StringBuilder();
         Iterator var2 = var0.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            String var4 = (String)var3.getKey();
            Iterator var5 = ((List)var3.getValue()).iterator();

            while(var5.hasNext()) {
               String var6 = (String)var5.next();
               var1.append(String.format("%n    "));
               var1.append(var4);
               var1.append(": ");
               var1.append(var6);
            }
         }

         return var1.toString();
      }
   }

   private static String canonicalize(String var0) {
      return var0.toLowerCase();
   }
}
