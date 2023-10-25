package org.apache.http.impl.conn;

import java.net.InetAddress;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class DefaultRoutePlanner implements HttpRoutePlanner {
  private final SchemePortResolver schemePortResolver;
  
  public DefaultRoutePlanner(SchemePortResolver schemePortResolver) {
    this.schemePortResolver = (schemePortResolver != null) ? schemePortResolver : DefaultSchemePortResolver.INSTANCE;
  }
  
  public HttpRoute determineRoute(HttpHost host, HttpRequest request, HttpContext context) throws HttpException {
    HttpHost httpHost1;
    Args.notNull(host, "Target host");
    Args.notNull(request, "Request");
    HttpClientContext clientContext = HttpClientContext.adapt(context);
    RequestConfig config = clientContext.getRequestConfig();
    InetAddress local = config.getLocalAddress();
    HttpHost proxy = config.getProxy();
    if (proxy == null)
      proxy = determineProxy(host, request, context); 
    if (host.getPort() <= 0) {
      try {
        httpHost1 = new HttpHost(host.getHostName(), this.schemePortResolver.resolve(host), host.getSchemeName());
      } catch (UnsupportedSchemeException ex) {
        throw new HttpException(ex.getMessage());
      } 
    } else {
      httpHost1 = host;
    } 
    boolean secure = httpHost1.getSchemeName().equalsIgnoreCase("https");
    if (proxy == null)
      return new HttpRoute(httpHost1, local, secure); 
    return new HttpRoute(httpHost1, local, proxy, secure);
  }
  
  protected HttpHost determineProxy(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\conn\DefaultRoutePlanner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */