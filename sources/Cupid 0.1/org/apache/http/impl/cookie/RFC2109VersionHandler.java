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
  public void parse(SetCookie cookie, String value) throws MalformedCookieException {
    Args.notNull(cookie, "Cookie");
    if (value == null)
      throw new MalformedCookieException("Missing value for version attribute"); 
    if (value.trim().length() == 0)
      throw new MalformedCookieException("Blank value for version attribute"); 
    try {
      cookie.setVersion(Integer.parseInt(value));
    } catch (NumberFormatException e) {
      throw new MalformedCookieException("Invalid version: " + e.getMessage());
    } 
  }
  
  public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
    Args.notNull(cookie, "Cookie");
    if (cookie.getVersion() < 0)
      throw new CookieRestrictionViolationException("Cookie version may not be negative"); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\cookie\RFC2109VersionHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */