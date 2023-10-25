package org.apache.http.client.protocol;

import java.io.IOException;
import java.util.Collection;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class RequestDefaultHeaders implements HttpRequestInterceptor {
  private final Collection<? extends Header> defaultHeaders;
  
  public RequestDefaultHeaders(Collection<? extends Header> defaultHeaders) {
    this.defaultHeaders = defaultHeaders;
  }
  
  public RequestDefaultHeaders() {
    this(null);
  }
  
  public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
    Args.notNull(request, "HTTP request");
    String method = request.getRequestLine().getMethod();
    if (method.equalsIgnoreCase("CONNECT"))
      return; 
    Collection<? extends Header> defHeaders = (Collection<? extends Header>)request.getParams().getParameter("http.default-headers");
    if (defHeaders == null)
      defHeaders = this.defaultHeaders; 
    if (defHeaders != null)
      for (Header defHeader : defHeaders)
        request.addHeader(defHeader);  
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\protocol\RequestDefaultHeaders.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */