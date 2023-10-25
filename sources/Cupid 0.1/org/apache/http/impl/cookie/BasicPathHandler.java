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
  public void parse(SetCookie cookie, String value) throws MalformedCookieException {
    Args.notNull(cookie, "Cookie");
    cookie.setPath(!TextUtils.isBlank(value) ? value : "/");
  }
  
  public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
    if (!match(cookie, origin))
      throw new CookieRestrictionViolationException("Illegal path attribute \"" + cookie.getPath() + "\". Path of origin: \"" + origin.getPath() + "\""); 
  }
  
  public boolean match(Cookie cookie, CookieOrigin origin) {
    Args.notNull(cookie, "Cookie");
    Args.notNull(origin, "Cookie origin");
    String targetpath = origin.getPath();
    String topmostPath = cookie.getPath();
    if (topmostPath == null)
      topmostPath = "/"; 
    if (topmostPath.length() > 1 && topmostPath.endsWith("/"))
      topmostPath = topmostPath.substring(0, topmostPath.length() - 1); 
    boolean match = targetpath.startsWith(topmostPath);
    if (match && targetpath.length() != topmostPath.length() && 
      !topmostPath.endsWith("/"))
      match = (targetpath.charAt(topmostPath.length()) == '/'); 
    return match;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\cookie\BasicPathHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */