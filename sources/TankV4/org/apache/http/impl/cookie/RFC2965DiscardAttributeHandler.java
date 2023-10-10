package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.cookie.SetCookie2;

@Immutable
public class RFC2965DiscardAttributeHandler implements CookieAttributeHandler {
   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      if (var1 instanceof SetCookie2) {
         SetCookie2 var3 = (SetCookie2)var1;
         var3.setDiscard(true);
      }

   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      return true;
   }
}
