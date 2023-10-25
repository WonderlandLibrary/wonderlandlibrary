package org.apache.http.impl.client;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.protocol.HttpContext;

@Immutable
public class ProxyAuthenticationStrategy extends AuthenticationStrategyImpl {
  public static final ProxyAuthenticationStrategy INSTANCE = new ProxyAuthenticationStrategy();
  
  public ProxyAuthenticationStrategy() {
    super(407, "Proxy-Authenticate");
  }
  
  Collection<String> getPreferredAuthSchemes(RequestConfig config) {
    return config.getProxyPreferredAuthSchemes();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\client\ProxyAuthenticationStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */