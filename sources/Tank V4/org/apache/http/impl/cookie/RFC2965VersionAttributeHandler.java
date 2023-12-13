package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.cookie.SetCookie2;
import org.apache.http.util.Args;

@Immutable
public class RFC2965VersionAttributeHandler implements CookieAttributeHandler {
   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      if (var2 == null) {
         throw new MalformedCookieException("Missing value for version attribute");
      } else {
         boolean var3 = true;

         int var6;
         try {
            var6 = Integer.parseInt(var2);
         } catch (NumberFormatException var5) {
            var6 = -1;
         }

         if (var6 < 0) {
            throw new MalformedCookieException("Invalid cookie version.");
         } else {
            var1.setVersion(var6);
         }
      }
   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      if (var1 instanceof SetCookie2 && var1 instanceof ClientCookie && !((ClientCookie)var1).containsAttribute("version")) {
         throw new CookieRestrictionViolationException("Violates RFC 2965. Version attribute is required.");
      }
   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      return true;
   }
}
