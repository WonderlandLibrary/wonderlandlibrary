package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.util.Args;

@NotThreadSafe
public abstract class CookieSpecBase extends AbstractCookieSpec {
   protected static String getDefaultPath(CookieOrigin var0) {
      String var1 = var0.getPath();
      int var2 = var1.lastIndexOf(47);
      if (var2 >= 0) {
         if (var2 == 0) {
            var2 = 1;
         }

         var1 = var1.substring(0, var2);
      }

      return var1;
   }

   protected static String getDefaultDomain(CookieOrigin var0) {
      return var0.getHost();
   }

   protected List parse(HeaderElement[] var1, CookieOrigin var2) throws MalformedCookieException {
      ArrayList var3 = new ArrayList(var1.length);
      HeaderElement[] var4 = var1;
      int var5 = var1.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         HeaderElement var7 = var4[var6];
         String var8 = var7.getName();
         String var9 = var7.getValue();
         if (var8 == null || var8.length() == 0) {
            throw new MalformedCookieException("Cookie name may not be empty");
         }

         BasicClientCookie var10 = new BasicClientCookie(var8, var9);
         var10.setPath(getDefaultPath(var2));
         var10.setDomain(getDefaultDomain(var2));
         NameValuePair[] var11 = var7.getParameters();

         for(int var12 = var11.length - 1; var12 >= 0; --var12) {
            NameValuePair var13 = var11[var12];
            String var14 = var13.getName().toLowerCase(Locale.ENGLISH);
            var10.setAttribute(var14, var13.getValue());
            CookieAttributeHandler var15 = this.findAttribHandler(var14);
            if (var15 != null) {
               var15.parse(var10, var13.getValue());
            }
         }

         var3.add(var10);
      }

      return var3;
   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      Iterator var3 = this.getAttribHandlers().iterator();

      while(var3.hasNext()) {
         CookieAttributeHandler var4 = (CookieAttributeHandler)var3.next();
         var4.validate(var1, var2);
      }

   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      Iterator var3 = this.getAttribHandlers().iterator();

      CookieAttributeHandler var4;
      do {
         if (!var3.hasNext()) {
            return true;
         }

         var4 = (CookieAttributeHandler)var3.next();
      } while(var4.match(var1, var2));

      return false;
   }
}
