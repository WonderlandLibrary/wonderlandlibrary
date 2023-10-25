package org.apache.http.impl.client;

import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class DefaultConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {
  public static final DefaultConnectionKeepAliveStrategy INSTANCE = new DefaultConnectionKeepAliveStrategy();
  
  public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
    Args.notNull(response, "HTTP response");
    BasicHeaderElementIterator basicHeaderElementIterator = new BasicHeaderElementIterator(response.headerIterator("Keep-Alive"));
    while (basicHeaderElementIterator.hasNext()) {
      HeaderElement he = basicHeaderElementIterator.nextElement();
      String param = he.getName();
      String value = he.getValue();
      if (value != null && param.equalsIgnoreCase("timeout"))
        try {
          return Long.parseLong(value) * 1000L;
        } catch (NumberFormatException ignore) {} 
    } 
    return -1L;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\client\DefaultConnectionKeepAliveStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */