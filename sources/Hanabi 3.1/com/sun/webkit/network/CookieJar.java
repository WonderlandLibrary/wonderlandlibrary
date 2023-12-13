package com.sun.webkit.network;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

final class CookieJar {
   private CookieJar() {
   }

   private static void fwkPut(String var0, String var1) {
      CookieHandler var2 = CookieHandler.getDefault();
      if (var2 != null) {
         URI var3 = null;

         try {
            var3 = new URI(var0);
            var3 = rewriteToFilterOutHttpOnlyCookies(var3);
         } catch (URISyntaxException var8) {
            return;
         }

         HashMap var4 = new HashMap();
         ArrayList var5 = new ArrayList();
         var5.add(var1);
         var4.put("Set-Cookie", var5);

         try {
            var2.put(var3, var4);
         } catch (IOException var7) {
         }
      }

   }

   private static String fwkGet(String var0, boolean var1) {
      CookieHandler var2 = CookieHandler.getDefault();
      if (var2 != null) {
         URI var3 = null;

         try {
            var3 = new URI(var0);
            if (!var1) {
               var3 = rewriteToFilterOutHttpOnlyCookies(var3);
            }
         } catch (URISyntaxException var13) {
            return null;
         }

         HashMap var4 = new HashMap();
         Map var5 = null;

         try {
            var5 = var2.get(var3, var4);
         } catch (IOException var12) {
            return null;
         }

         if (var5 != null) {
            StringBuilder var6 = new StringBuilder();
            Iterator var7 = var5.entrySet().iterator();

            while(true) {
               Map.Entry var8;
               String var9;
               do {
                  if (!var7.hasNext()) {
                     return var6.toString();
                  }

                  var8 = (Map.Entry)var7.next();
                  var9 = (String)var8.getKey();
               } while(!"Cookie".equalsIgnoreCase(var9));

               String var11;
               for(Iterator var10 = ((List)var8.getValue()).iterator(); var10.hasNext(); var6.append(var11)) {
                  var11 = (String)var10.next();
                  if (var6.length() > 0) {
                     var6.append("; ");
                  }
               }
            }
         }
      }

      return null;
   }

   private static URI rewriteToFilterOutHttpOnlyCookies(URI var0) throws URISyntaxException {
      return new URI(var0.getScheme().equalsIgnoreCase("https") ? "javascripts" : "javascript", var0.getRawSchemeSpecificPart(), var0.getRawFragment());
   }
}
