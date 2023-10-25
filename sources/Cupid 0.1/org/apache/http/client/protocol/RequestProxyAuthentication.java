package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthState;
import org.apache.http.conn.HttpRoutedConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Deprecated
@Immutable
public class RequestProxyAuthentication extends RequestAuthenticationBase {
  public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
    Args.notNull(request, "HTTP request");
    Args.notNull(context, "HTTP context");
    if (request.containsHeader("Proxy-Authorization"))
      return; 
    HttpRoutedConnection conn = (HttpRoutedConnection)context.getAttribute("http.connection");
    if (conn == null) {
      this.log.debug("HTTP connection not set in the context");
      return;
    } 
    HttpRoute route = conn.getRoute();
    if (route.isTunnelled())
      return; 
    AuthState authState = (AuthState)context.getAttribute("http.auth.proxy-scope");
    if (authState == null) {
      this.log.debug("Proxy auth state not set in the context");
      return;
    } 
    if (this.log.isDebugEnabled())
      this.log.debug("Proxy auth state: " + authState.getState()); 
    process(authState, request, context);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\protocol\RequestProxyAuthentication.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */