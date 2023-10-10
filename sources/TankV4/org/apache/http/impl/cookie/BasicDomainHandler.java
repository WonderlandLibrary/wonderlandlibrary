package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

@Immutable
public class BasicDomainHandler implements CookieAttributeHandler {
   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      if (var2 == null) {
         throw new MalformedCookieException("Missing value for domain attribute");
      } else if (var2.trim().length() == 0) {
         throw new MalformedCookieException("Blank value for domain attribute");
      } else {
         var1.setDomain(var2);
      }
   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      String var3 = var2.getHost();
      String var4 = var1.getDomain();
      if (var4 == null) {
         throw new CookieRestrictionViolationException("Cookie domain may not be null");
      } else {
         if (var3.contains(".")) {
            if (!var3.endsWith(var4)) {
               if (var4.startsWith(".")) {
                  var4 = var4.substring(1, var4.length());
               }

               if (!var3.equals(var4)) {
                  throw new CookieRestrictionViolationException("Illegal domain attribute \"" + var4 + "\". Domain of origin: \"" + var3 + "\"");
               }
            }
         } else if (!var3.equals(var4)) {
            throw new CookieRestrictionViolationException("Illegal domain attribute \"" + var4 + "\". Domain of origin: \"" + var3 + "\"");
         }

      }
   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      String var3 = var2.getHost();
      String var4 = var1.getDomain();
      if (var4 == null) {
         return false;
      } else if (var3.equals(var4)) {
         return true;
      } else {
         if (!var4.startsWith(".")) {
            var4 = '.' + var4;
         }

         return var3.endsWith(var4) || var3.equals(var4.substring(1));
      }
   }
}
