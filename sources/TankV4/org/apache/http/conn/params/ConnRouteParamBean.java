package org.apache.http.conn.params;

import java.net.InetAddress;
import org.apache.http.HttpHost;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpAbstractParamBean;
import org.apache.http.params.HttpParams;

/** @deprecated */
@Deprecated
@NotThreadSafe
public class ConnRouteParamBean extends HttpAbstractParamBean {
   public ConnRouteParamBean(HttpParams var1) {
      super(var1);
   }

   public void setDefaultProxy(HttpHost var1) {
      this.params.setParameter("http.route.default-proxy", var1);
   }

   public void setLocalAddress(InetAddress var1) {
      this.params.setParameter("http.route.local-address", var1);
   }

   public void setForcedRoute(HttpRoute var1) {
      this.params.setParameter("http.route.forced-route", var1);
   }
}
