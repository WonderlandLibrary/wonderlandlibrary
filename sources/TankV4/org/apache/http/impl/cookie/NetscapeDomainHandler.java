package org.apache.http.impl.cookie;

import java.util.StringTokenizer;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.util.Args;

@Immutable
public class NetscapeDomainHandler extends BasicDomainHandler {
   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      super.validate(var1, var2);
      String var3 = var2.getHost();
      String var4 = var1.getDomain();
      if (var3.contains(".")) {
         int var5 = (new StringTokenizer(var4, ".")).countTokens();
         if (var4 == false) {
            if (var5 < 2) {
               throw new CookieRestrictionViolationException("Domain attribute \"" + var4 + "\" violates the Netscape cookie specification for " + "special domains");
            }
         } else if (var5 < 3) {
            throw new CookieRestrictionViolationException("Domain attribute \"" + var4 + "\" violates the Netscape cookie specification");
         }
      }

   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      String var3 = var2.getHost();
      String var4 = var1.getDomain();
      return var4 == null ? false : var3.endsWith(var4);
   }
}
