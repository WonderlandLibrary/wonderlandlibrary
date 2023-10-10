package org.apache.http.conn.params;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.params.HttpAbstractParamBean;
import org.apache.http.params.HttpParams;

/** @deprecated */
@Deprecated
@NotThreadSafe
public class ConnManagerParamBean extends HttpAbstractParamBean {
   public ConnManagerParamBean(HttpParams var1) {
      super(var1);
   }

   public void setTimeout(long var1) {
      this.params.setLongParameter("http.conn-manager.timeout", var1);
   }

   public void setMaxTotalConnections(int var1) {
      this.params.setIntParameter("http.conn-manager.max-total", var1);
   }

   public void setConnectionsPerRoute(ConnPerRouteBean var1) {
      this.params.setParameter("http.conn-manager.max-per-route", var1);
   }
}
