package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.util.Args;

@NotThreadSafe
public abstract class CookieSpecBase extends AbstractCookieSpec {
  protected static String getDefaultPath(CookieOrigin origin) {
    String defaultPath = origin.getPath();
    int lastSlashIndex = defaultPath.lastIndexOf('/');
    if (lastSlashIndex >= 0) {
      if (lastSlashIndex == 0)
        lastSlashIndex = 1; 
      defaultPath = defaultPath.substring(0, lastSlashIndex);
    } 
    return defaultPath;
  }
  
  protected static String getDefaultDomain(CookieOrigin origin) {
    return origin.getHost();
  }
  
  protected List<Cookie> parse(HeaderElement[] elems, CookieOrigin origin) throws MalformedCookieException {
    List<Cookie> cookies = new ArrayList<Cookie>(elems.length);
    for (HeaderElement headerelement : elems) {
      String name = headerelement.getName();
      String value = headerelement.getValue();
      if (name == null || name.length() == 0)
        throw new MalformedCookieException("Cookie name may not be empty"); 
      BasicClientCookie cookie = new BasicClientCookie(name, value);
      cookie.setPath(getDefaultPath(origin));
      cookie.setDomain(getDefaultDomain(origin));
      NameValuePair[] attribs = headerelement.getParameters();
      for (int j = attribs.length - 1; j >= 0; j--) {
        NameValuePair attrib = attribs[j];
        String s = attrib.getName().toLowerCase(Locale.ENGLISH);
        cookie.setAttribute(s, attrib.getValue());
        CookieAttributeHandler handler = findAttribHandler(s);
        if (handler != null)
          handler.parse(cookie, attrib.getValue()); 
      } 
      cookies.add(cookie);
    } 
    return cookies;
  }
  
  public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
    Args.notNull(cookie, "Cookie");
    Args.notNull(origin, "Cookie origin");
    for (CookieAttributeHandler handler : getAttribHandlers())
      handler.validate(cookie, origin); 
  }
  
  public boolean match(Cookie cookie, CookieOrigin origin) {
    Args.notNull(cookie, "Cookie");
    Args.notNull(origin, "Cookie origin");
    for (CookieAttributeHandler handler : getAttribHandlers()) {
      if (!handler.match(cookie, origin))
        return false; 
    } 
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\cookie\CookieSpecBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */