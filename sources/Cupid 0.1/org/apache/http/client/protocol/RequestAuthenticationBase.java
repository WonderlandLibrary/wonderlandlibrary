package org.apache.http.client.protocol;

import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.auth.Credentials;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Asserts;

@Deprecated
abstract class RequestAuthenticationBase implements HttpRequestInterceptor {
  final Log log = LogFactory.getLog(getClass());
  
  void process(AuthState authState, HttpRequest request, HttpContext context) {
    Queue<AuthOption> authOptions;
    AuthScheme authScheme = authState.getAuthScheme();
    Credentials creds = authState.getCredentials();
    switch (authState.getState()) {
      case FAILURE:
        return;
      case SUCCESS:
        ensureAuthScheme(authScheme);
        if (authScheme.isConnectionBased())
          return; 
        break;
      case CHALLENGED:
        authOptions = authState.getAuthOptions();
        if (authOptions != null) {
          while (!authOptions.isEmpty()) {
            AuthOption authOption = authOptions.remove();
            authScheme = authOption.getAuthScheme();
            creds = authOption.getCredentials();
            authState.update(authScheme, creds);
            if (this.log.isDebugEnabled())
              this.log.debug("Generating response to an authentication challenge using " + authScheme.getSchemeName() + " scheme"); 
            try {
              Header header = authenticate(authScheme, creds, request, context);
              request.addHeader(header);
              break;
            } catch (AuthenticationException ex) {
              if (this.log.isWarnEnabled())
                this.log.warn(authScheme + " authentication error: " + ex.getMessage()); 
            } 
          } 
          return;
        } 
        ensureAuthScheme(authScheme);
        break;
    } 
    if (authScheme != null)
      try {
        Header header = authenticate(authScheme, creds, request, context);
        request.addHeader(header);
      } catch (AuthenticationException ex) {
        if (this.log.isErrorEnabled())
          this.log.error(authScheme + " authentication error: " + ex.getMessage()); 
      }  
  }
  
  private void ensureAuthScheme(AuthScheme authScheme) {
    Asserts.notNull(authScheme, "Auth scheme");
  }
  
  private Header authenticate(AuthScheme authScheme, Credentials creds, HttpRequest request, HttpContext context) throws AuthenticationException {
    Asserts.notNull(authScheme, "Auth scheme");
    if (authScheme instanceof ContextAwareAuthScheme)
      return ((ContextAwareAuthScheme)authScheme).authenticate(creds, request, context); 
    return authScheme.authenticate(creds, request);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\protocol\RequestAuthenticationBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */