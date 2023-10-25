package org.apache.http.impl.cookie;

import java.util.Collections;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;

@NotThreadSafe
public class IgnoreSpec extends CookieSpecBase {
  public int getVersion() {
    return 0;
  }
  
  public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
    return Collections.emptyList();
  }
  
  public List<Header> formatCookies(List<Cookie> cookies) {
    return Collections.emptyList();
  }
  
  public Header getVersionHeader() {
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\cookie\IgnoreSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */