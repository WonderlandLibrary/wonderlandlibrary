package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public class RequestConnControl implements HttpRequestInterceptor {
  public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
    Args.notNull(request, "HTTP request");
    String method = request.getRequestLine().getMethod();
    if (method.equalsIgnoreCase("CONNECT"))
      return; 
    if (!request.containsHeader("Connection"))
      request.addHeader("Connection", "Keep-Alive"); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\protocol\RequestConnControl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */