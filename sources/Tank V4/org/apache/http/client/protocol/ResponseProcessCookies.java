package org.apache.http.client.protocol;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class ResponseProcessCookies implements HttpResponseInterceptor {
   private final Log log = LogFactory.getLog(this.getClass());

   public void process(HttpResponse var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      Args.notNull(var2, "HTTP context");
      HttpClientContext var3 = HttpClientContext.adapt(var2);
      CookieSpec var4 = var3.getCookieSpec();
      if (var4 == null) {
         this.log.debug("Cookie spec not specified in HTTP context");
      } else {
         CookieStore var5 = var3.getCookieStore();
         if (var5 == null) {
            this.log.debug("Cookie store not specified in HTTP context");
         } else {
            CookieOrigin var6 = var3.getCookieOrigin();
            if (var6 == null) {
               this.log.debug("Cookie origin not specified in HTTP context");
            } else {
               HeaderIterator var7 = var1.headerIterator("Set-Cookie");
               this.processCookies(var7, var4, var6, var5);
               if (var4.getVersion() > 0) {
                  var7 = var1.headerIterator("Set-Cookie2");
                  this.processCookies(var7, var4, var6, var5);
               }

            }
         }
      }
   }

   private void processCookies(HeaderIterator var1, CookieSpec var2, CookieOrigin var3, CookieStore var4) {
      while(var1.hasNext()) {
         Header var5 = var1.nextHeader();

         try {
            List var6 = var2.parse(var5, var3);
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               Cookie var8 = (Cookie)var7.next();

               try {
                  var2.validate(var8, var3);
                  var4.addCookie(var8);
                  if (this.log.isDebugEnabled()) {
                     this.log.debug("Cookie accepted [" + formatCooke(var8) + "]");
                  }
               } catch (MalformedCookieException var10) {
                  if (this.log.isWarnEnabled()) {
                     this.log.warn("Cookie rejected [" + formatCooke(var8) + "] " + var10.getMessage());
                  }
               }
            }
         } catch (MalformedCookieException var11) {
            if (this.log.isWarnEnabled()) {
               this.log.warn("Invalid cookie header: \"" + var5 + "\". " + var11.getMessage());
            }
         }
      }

   }

   private static String formatCooke(Cookie var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append(var0.getName());
      var1.append("=\"");
      String var2 = var0.getValue();
      if (var2.length() > 100) {
         var2 = var2.substring(0, 100) + "...";
      }

      var1.append(var2);
      var1.append("\"");
      var1.append(", version:");
      var1.append(Integer.toString(var0.getVersion()));
      var1.append(", domain:");
      var1.append(var0.getDomain());
      var1.append(", path:");
      var1.append(var0.getPath());
      var1.append(", expiry:");
      var1.append(var0.getExpiryDate());
      return var1.toString();
   }
}
