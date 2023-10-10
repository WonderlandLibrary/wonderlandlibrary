package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

@Immutable
public class RFC2109VersionHandler extends AbstractCookieAttributeHandler {
   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      if (var2 == null) {
         throw new MalformedCookieException("Missing value for version attribute");
      } else if (var2.trim().length() == 0) {
         throw new MalformedCookieException("Blank value for version attribute");
      } else {
         try {
            var1.setVersion(Integer.parseInt(var2));
         } catch (NumberFormatException var4) {
            throw new MalformedCookieException("Invalid version: " + var4.getMessage());
         }
      }
   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      if (var1.getVersion() < 0) {
         throw new CookieRestrictionViolationException("Cookie version may not be negative");
      }
   }
}
