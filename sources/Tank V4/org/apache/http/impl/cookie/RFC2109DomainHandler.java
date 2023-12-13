package org.apache.http.impl.cookie;

import java.util.Locale;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

@Immutable
public class RFC2109DomainHandler implements CookieAttributeHandler {
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
         if (!var4.equals(var3)) {
            int var5 = var4.indexOf(46);
            if (var5 == -1) {
               throw new CookieRestrictionViolationException("Domain attribute \"" + var4 + "\" does not match the host \"" + var3 + "\"");
            }

            if (!var4.startsWith(".")) {
               throw new CookieRestrictionViolationException("Domain attribute \"" + var4 + "\" violates RFC 2109: domain must start with a dot");
            }

            var5 = var4.indexOf(46, 1);
            if (var5 < 0 || var5 == var4.length() - 1) {
               throw new CookieRestrictionViolationException("Domain attribute \"" + var4 + "\" violates RFC 2109: domain must contain an embedded dot");
            }

            var3 = var3.toLowerCase(Locale.ENGLISH);
            if (!var3.endsWith(var4)) {
               throw new CookieRestrictionViolationException("Illegal domain attribute \"" + var4 + "\". Domain of origin: \"" + var3 + "\"");
            }

            String var6 = var3.substring(0, var3.length() - var4.length());
            if (var6.indexOf(46) != -1) {
               throw new CookieRestrictionViolationException("Domain attribute \"" + var4 + "\" violates RFC 2109: host minus domain may not contain any dots");
            }
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
      } else {
         return var3.equals(var4) || var4.startsWith(".") && var3.endsWith(var4);
      }
   }
}
