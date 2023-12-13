package org.apache.http.cookie;

public interface CookieAttributeHandler {
   void parse(SetCookie var1, String var2) throws MalformedCookieException;

   void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException;

   boolean match(Cookie var1, CookieOrigin var2);
}
