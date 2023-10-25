package org.apache.http.cookie;

import java.util.List;
import org.apache.http.Header;

public interface CookieSpec {
  int getVersion();
  
  List<Cookie> parse(Header paramHeader, CookieOrigin paramCookieOrigin) throws MalformedCookieException;
  
  void validate(Cookie paramCookie, CookieOrigin paramCookieOrigin) throws MalformedCookieException;
  
  boolean match(Cookie paramCookie, CookieOrigin paramCookieOrigin);
  
  List<Header> formatCookies(List<Cookie> paramList);
  
  Header getVersionHeader();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\cookie\CookieSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */