package org.apache.http.protocol;

import java.util.Map;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.util.Args;

@Deprecated
@ThreadSafe
public class HttpRequestHandlerRegistry implements HttpRequestHandlerResolver {
  private final UriPatternMatcher<HttpRequestHandler> matcher = new UriPatternMatcher<HttpRequestHandler>();
  
  public void register(String pattern, HttpRequestHandler handler) {
    Args.notNull(pattern, "URI request pattern");
    Args.notNull(handler, "Request handler");
    this.matcher.register(pattern, handler);
  }
  
  public void unregister(String pattern) {
    this.matcher.unregister(pattern);
  }
  
  public void setHandlers(Map<String, HttpRequestHandler> map) {
    this.matcher.setObjects(map);
  }
  
  public Map<String, HttpRequestHandler> getHandlers() {
    return this.matcher.getObjects();
  }
  
  public HttpRequestHandler lookup(String requestURI) {
    return this.matcher.lookup(requestURI);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\protocol\HttpRequestHandlerRegistry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */