package org.apache.http.impl.client;

import org.apache.commons.logging.Log;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthState;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.impl.auth.HttpAuthenticator;
import org.apache.http.protocol.HttpContext;

@Deprecated
public class HttpAuthenticator extends HttpAuthenticator {
  public HttpAuthenticator(Log log) {
    super(log);
  }
  
  public HttpAuthenticator() {}
  
  public boolean authenticate(HttpHost host, HttpResponse response, AuthenticationStrategy authStrategy, AuthState authState, HttpContext context) {
    return handleAuthChallenge(host, response, authStrategy, authState, context);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\client\HttpAuthenticator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */