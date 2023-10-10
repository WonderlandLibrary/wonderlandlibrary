package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;

@Immutable
public abstract class AbstractCookieAttributeHandler implements CookieAttributeHandler {
   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      return true;
   }
}
