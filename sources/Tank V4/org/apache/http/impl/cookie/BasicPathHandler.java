package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

@Immutable
public class BasicPathHandler implements CookieAttributeHandler {
   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      var1.setPath(!TextUtils.isBlank(var2) ? var2 : "/");
   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      if (var2 == null) {
         throw new CookieRestrictionViolationException("Illegal path attribute \"" + var1.getPath() + "\". Path of origin: \"" + var2.getPath() + "\"");
      }
   }
}
