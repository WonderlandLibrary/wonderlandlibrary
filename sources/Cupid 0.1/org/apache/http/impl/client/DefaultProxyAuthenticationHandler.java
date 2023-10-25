package org.apache.http.impl.client;

import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Deprecated
@Immutable
public class DefaultProxyAuthenticationHandler extends AbstractAuthenticationHandler {
  public boolean isAuthenticationRequested(HttpResponse response, HttpContext context) {
    Args.notNull(response, "HTTP response");
    int status = response.getStatusLine().getStatusCode();
    return (status == 407);
  }
  
  public Map<String, Header> getChallenges(HttpResponse response, HttpContext context) throws MalformedChallengeException {
    Args.notNull(response, "HTTP response");
    Header[] headers = response.getHeaders("Proxy-Authenticate");
    return parseChallenges(headers);
  }
  
  protected List<String> getAuthPreferences(HttpResponse response, HttpContext context) {
    List<String> authpref = (List<String>)response.getParams().getParameter("http.auth.proxy-scheme-pref");
    if (authpref != null)
      return authpref; 
    return super.getAuthPreferences(response, context);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\client\DefaultProxyAuthenticationHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */