package org.apache.http.client;

import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.protocol.HttpContext;

@Deprecated
public interface AuthenticationHandler {
  boolean isAuthenticationRequested(HttpResponse paramHttpResponse, HttpContext paramHttpContext);
  
  Map<String, Header> getChallenges(HttpResponse paramHttpResponse, HttpContext paramHttpContext) throws MalformedChallengeException;
  
  AuthScheme selectScheme(Map<String, Header> paramMap, HttpResponse paramHttpResponse, HttpContext paramHttpContext) throws AuthenticationException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\AuthenticationHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */