package org.apache.http.impl.auth;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Asserts;

public class HttpAuthenticator {
  private final Log log;
  
  public HttpAuthenticator(Log log) {
    this.log = (log != null) ? log : LogFactory.getLog(getClass());
  }
  
  public HttpAuthenticator() {
    this(null);
  }
  
  public boolean isAuthenticationRequested(HttpHost host, HttpResponse response, AuthenticationStrategy authStrategy, AuthState authState, HttpContext context) {
    if (authStrategy.isAuthenticationRequested(host, response, context)) {
      this.log.debug("Authentication required");
      if (authState.getState() == AuthProtocolState.SUCCESS)
        authStrategy.authFailed(host, authState.getAuthScheme(), context); 
      return true;
    } 
    switch (authState.getState()) {
      case CHALLENGED:
      case HANDSHAKE:
        this.log.debug("Authentication succeeded");
        authState.setState(AuthProtocolState.SUCCESS);
        authStrategy.authSucceeded(host, authState.getAuthScheme(), context);
      case SUCCESS:
        return false;
    } 
    authState.setState(AuthProtocolState.UNCHALLENGED);
  }
  
  public boolean handleAuthChallenge(HttpHost host, HttpResponse response, AuthenticationStrategy authStrategy, AuthState authState, HttpContext context) {
    try {
      if (this.log.isDebugEnabled())
        this.log.debug(host.toHostString() + " requested authentication"); 
      Map<String, Header> challenges = authStrategy.getChallenges(host, response, context);
      if (challenges.isEmpty()) {
        this.log.debug("Response contains no authentication challenges");
        return false;
      } 
      AuthScheme authScheme = authState.getAuthScheme();
      switch (authState.getState()) {
        case FAILURE:
          return false;
        case SUCCESS:
          authState.reset();
          break;
        case CHALLENGED:
        case HANDSHAKE:
          if (authScheme == null) {
            this.log.debug("Auth scheme is null");
            authStrategy.authFailed(host, null, context);
            authState.reset();
            authState.setState(AuthProtocolState.FAILURE);
            return false;
          } 
        case UNCHALLENGED:
          if (authScheme != null) {
            String id = authScheme.getSchemeName();
            Header challenge = challenges.get(id.toLowerCase(Locale.US));
            if (challenge != null) {
              this.log.debug("Authorization challenge processed");
              authScheme.processChallenge(challenge);
              if (authScheme.isComplete()) {
                this.log.debug("Authentication failed");
                authStrategy.authFailed(host, authState.getAuthScheme(), context);
                authState.reset();
                authState.setState(AuthProtocolState.FAILURE);
                return false;
              } 
              authState.setState(AuthProtocolState.HANDSHAKE);
              return true;
            } 
            authState.reset();
          } 
          break;
      } 
      Queue<AuthOption> authOptions = authStrategy.select(challenges, host, response, context);
      if (authOptions != null && !authOptions.isEmpty()) {
        if (this.log.isDebugEnabled())
          this.log.debug("Selected authentication options: " + authOptions); 
        authState.setState(AuthProtocolState.CHALLENGED);
        authState.update(authOptions);
        return true;
      } 
      return false;
    } catch (MalformedChallengeException ex) {
      if (this.log.isWarnEnabled())
        this.log.warn("Malformed challenge: " + ex.getMessage()); 
      authState.reset();
      return false;
    } 
  }
  
  public void generateAuthResponse(HttpRequest request, AuthState authState, HttpContext context) throws HttpException, IOException {
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
              Header header = doAuth(authScheme, creds, request, context);
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
        Header header = doAuth(authScheme, creds, request, context);
        request.addHeader(header);
      } catch (AuthenticationException ex) {
        if (this.log.isErrorEnabled())
          this.log.error(authScheme + " authentication error: " + ex.getMessage()); 
      }  
  }
  
  private void ensureAuthScheme(AuthScheme authScheme) {
    Asserts.notNull(authScheme, "Auth scheme");
  }
  
  private Header doAuth(AuthScheme authScheme, Credentials creds, HttpRequest request, HttpContext context) throws AuthenticationException {
    if (authScheme instanceof ContextAwareAuthScheme)
      return ((ContextAwareAuthScheme)authScheme).authenticate(creds, request, context); 
    return authScheme.authenticate(creds, request);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\auth\HttpAuthenticator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */