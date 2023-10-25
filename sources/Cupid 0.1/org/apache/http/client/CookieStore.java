package org.apache.http.client;

import java.util.Date;
import java.util.List;
import org.apache.http.cookie.Cookie;

public interface CookieStore {
  void addCookie(Cookie paramCookie);
  
  List<Cookie> getCookies();
  
  boolean clearExpired(Date paramDate);
  
  void clear();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\CookieStore.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */