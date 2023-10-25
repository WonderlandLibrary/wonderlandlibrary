package org.apache.http.impl.client;

import java.util.HashMap;
import org.apache.http.HttpHost;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthScheme;
import org.apache.http.client.AuthCache;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.util.Args;

@NotThreadSafe
public class BasicAuthCache implements AuthCache {
  private final HashMap<HttpHost, AuthScheme> map;
  
  private final SchemePortResolver schemePortResolver;
  
  public BasicAuthCache(SchemePortResolver schemePortResolver) {
    this.map = new HashMap<HttpHost, AuthScheme>();
    this.schemePortResolver = (schemePortResolver != null) ? schemePortResolver : (SchemePortResolver)DefaultSchemePortResolver.INSTANCE;
  }
  
  public BasicAuthCache() {
    this(null);
  }
  
  protected HttpHost getKey(HttpHost host) {
    if (host.getPort() <= 0) {
      int port;
      try {
        port = this.schemePortResolver.resolve(host);
      } catch (UnsupportedSchemeException ignore) {
        return host;
      } 
      return new HttpHost(host.getHostName(), port, host.getSchemeName());
    } 
    return host;
  }
  
  public void put(HttpHost host, AuthScheme authScheme) {
    Args.notNull(host, "HTTP host");
    this.map.put(getKey(host), authScheme);
  }
  
  public AuthScheme get(HttpHost host) {
    Args.notNull(host, "HTTP host");
    return this.map.get(getKey(host));
  }
  
  public void remove(HttpHost host) {
    Args.notNull(host, "HTTP host");
    this.map.remove(getKey(host));
  }
  
  public void clear() {
    this.map.clear();
  }
  
  public String toString() {
    return this.map.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\client\BasicAuthCache.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */