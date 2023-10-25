package org.apache.http.protocol;

import org.apache.http.HttpRequest;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.util.Args;

@ThreadSafe
public class UriHttpRequestHandlerMapper implements HttpRequestHandlerMapper {
  private final UriPatternMatcher<HttpRequestHandler> matcher;
  
  protected UriHttpRequestHandlerMapper(UriPatternMatcher<HttpRequestHandler> matcher) {
    this.matcher = (UriPatternMatcher<HttpRequestHandler>)Args.notNull(matcher, "Pattern matcher");
  }
  
  public UriHttpRequestHandlerMapper() {
    this(new UriPatternMatcher<HttpRequestHandler>());
  }
  
  public void register(String pattern, HttpRequestHandler handler) {
    Args.notNull(pattern, "Pattern");
    Args.notNull(handler, "Handler");
    this.matcher.register(pattern, handler);
  }
  
  public void unregister(String pattern) {
    this.matcher.unregister(pattern);
  }
  
  protected String getRequestPath(HttpRequest request) {
    String uriPath = request.getRequestLine().getUri();
    int index = uriPath.indexOf("?");
    if (index != -1) {
      uriPath = uriPath.substring(0, index);
    } else {
      index = uriPath.indexOf("#");
      if (index != -1)
        uriPath = uriPath.substring(0, index); 
    } 
    return uriPath;
  }
  
  public HttpRequestHandler lookup(HttpRequest request) {
    Args.notNull(request, "HTTP request");
    return this.matcher.lookup(getRequestPath(request));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\protocol\UriHttpRequestHandlerMapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */