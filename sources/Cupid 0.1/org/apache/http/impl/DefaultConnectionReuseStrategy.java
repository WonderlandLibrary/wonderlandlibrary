package org.apache.http.impl;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.TokenIterator;
import org.apache.http.annotation.Immutable;
import org.apache.http.message.BasicTokenIterator;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class DefaultConnectionReuseStrategy implements ConnectionReuseStrategy {
  public static final DefaultConnectionReuseStrategy INSTANCE = new DefaultConnectionReuseStrategy();
  
  public boolean keepAlive(HttpResponse response, HttpContext context) {
    Args.notNull(response, "HTTP response");
    Args.notNull(context, "HTTP context");
    ProtocolVersion ver = response.getStatusLine().getProtocolVersion();
    Header teh = response.getFirstHeader("Transfer-Encoding");
    if (teh != null) {
      if (!"chunked".equalsIgnoreCase(teh.getValue()))
        return false; 
    } else if (canResponseHaveBody(response)) {
      Header[] clhs = response.getHeaders("Content-Length");
      if (clhs.length == 1) {
        Header clh = clhs[0];
        try {
          int contentLen = Integer.parseInt(clh.getValue());
          if (contentLen < 0)
            return false; 
        } catch (NumberFormatException ex) {
          return false;
        } 
      } else {
        return false;
      } 
    } 
    HeaderIterator hit = response.headerIterator("Connection");
    if (!hit.hasNext())
      hit = response.headerIterator("Proxy-Connection"); 
    if (hit.hasNext())
      try {
        TokenIterator ti = createTokenIterator(hit);
        boolean keepalive = false;
        while (ti.hasNext()) {
          String token = ti.nextToken();
          if ("Close".equalsIgnoreCase(token))
            return false; 
          if ("Keep-Alive".equalsIgnoreCase(token))
            keepalive = true; 
        } 
        if (keepalive)
          return true; 
      } catch (ParseException px) {
        return false;
      }  
    return !ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0);
  }
  
  protected TokenIterator createTokenIterator(HeaderIterator hit) {
    return (TokenIterator)new BasicTokenIterator(hit);
  }
  
  private boolean canResponseHaveBody(HttpResponse response) {
    int status = response.getStatusLine().getStatusCode();
    return (status >= 200 && status != 204 && status != 304 && status != 205);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\DefaultConnectionReuseStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */