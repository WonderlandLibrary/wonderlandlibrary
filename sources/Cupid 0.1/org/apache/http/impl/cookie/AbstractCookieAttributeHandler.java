package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;

@Immutable
public abstract class AbstractCookieAttributeHandler implements CookieAttributeHandler {
  public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {}
  
  public boolean match(Cookie cookie, CookieOrigin origin) {
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\cookie\AbstractCookieAttributeHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */