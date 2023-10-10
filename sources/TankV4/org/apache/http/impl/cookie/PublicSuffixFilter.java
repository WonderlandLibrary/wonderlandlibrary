package org.apache.http.impl.cookie;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;

public class PublicSuffixFilter implements CookieAttributeHandler {
   private final CookieAttributeHandler wrapped;
   private Set exceptions;
   private Set suffixes;

   public PublicSuffixFilter(CookieAttributeHandler var1) {
      this.wrapped = var1;
   }

   public void setPublicSuffixes(Collection var1) {
      this.suffixes = new HashSet(var1);
   }

   public void setExceptions(Collection var1) {
      this.exceptions = new HashSet(var1);
   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      return var1 != false ? false : this.wrapped.match(var1, var2);
   }

   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      this.wrapped.parse(var1, var2);
   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      this.wrapped.validate(var1, var2);
   }
}
