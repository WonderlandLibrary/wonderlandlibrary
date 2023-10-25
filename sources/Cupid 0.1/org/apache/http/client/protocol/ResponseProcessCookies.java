package org.apache.http.client.protocol;

import java.io.IOException;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class ResponseProcessCookies implements HttpResponseInterceptor {
  private final Log log = LogFactory.getLog(getClass());
  
  public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
    Args.notNull(response, "HTTP request");
    Args.notNull(context, "HTTP context");
    HttpClientContext clientContext = HttpClientContext.adapt(context);
    CookieSpec cookieSpec = clientContext.getCookieSpec();
    if (cookieSpec == null) {
      this.log.debug("Cookie spec not specified in HTTP context");
      return;
    } 
    CookieStore cookieStore = clientContext.getCookieStore();
    if (cookieStore == null) {
      this.log.debug("Cookie store not specified in HTTP context");
      return;
    } 
    CookieOrigin cookieOrigin = clientContext.getCookieOrigin();
    if (cookieOrigin == null) {
      this.log.debug("Cookie origin not specified in HTTP context");
      return;
    } 
    HeaderIterator it = response.headerIterator("Set-Cookie");
    processCookies(it, cookieSpec, cookieOrigin, cookieStore);
    if (cookieSpec.getVersion() > 0) {
      it = response.headerIterator("Set-Cookie2");
      processCookies(it, cookieSpec, cookieOrigin, cookieStore);
    } 
  }
  
  private void processCookies(HeaderIterator iterator, CookieSpec cookieSpec, CookieOrigin cookieOrigin, CookieStore cookieStore) {
    while (iterator.hasNext()) {
      Header header = iterator.nextHeader();
      try {
        List<Cookie> cookies = cookieSpec.parse(header, cookieOrigin);
        for (Cookie cookie : cookies) {
          try {
            cookieSpec.validate(cookie, cookieOrigin);
            cookieStore.addCookie(cookie);
            if (this.log.isDebugEnabled())
              this.log.debug("Cookie accepted [" + formatCooke(cookie) + "]"); 
          } catch (MalformedCookieException ex) {
            if (this.log.isWarnEnabled())
              this.log.warn("Cookie rejected [" + formatCooke(cookie) + "] " + ex.getMessage()); 
          } 
        } 
      } catch (MalformedCookieException ex) {
        if (this.log.isWarnEnabled())
          this.log.warn("Invalid cookie header: \"" + header + "\". " + ex.getMessage()); 
      } 
    } 
  }
  
  private static String formatCooke(Cookie cookie) {
    StringBuilder buf = new StringBuilder();
    buf.append(cookie.getName());
    buf.append("=\"");
    String v = cookie.getValue();
    if (v.length() > 100)
      v = v.substring(0, 100) + "..."; 
    buf.append(v);
    buf.append("\"");
    buf.append(", version:");
    buf.append(Integer.toString(cookie.getVersion()));
    buf.append(", domain:");
    buf.append(cookie.getDomain());
    buf.append(", path:");
    buf.append(cookie.getPath());
    buf.append(", expiry:");
    buf.append(cookie.getExpiryDate());
    return buf.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\protocol\ResponseProcessCookies.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */