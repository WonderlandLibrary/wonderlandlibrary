package org.apache.http.conn.params;

import java.net.InetAddress;
import org.apache.http.HttpHost;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpAbstractParamBean;
import org.apache.http.params.HttpParams;

@Deprecated
@NotThreadSafe
public class ConnRouteParamBean extends HttpAbstractParamBean {
  public ConnRouteParamBean(HttpParams params) {
    super(params);
  }
  
  public void setDefaultProxy(HttpHost defaultProxy) {
    this.params.setParameter("http.route.default-proxy", defaultProxy);
  }
  
  public void setLocalAddress(InetAddress address) {
    this.params.setParameter("http.route.local-address", address);
  }
  
  public void setForcedRoute(HttpRoute route) {
    this.params.setParameter("http.route.forced-route", route);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\params\ConnRouteParamBean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */