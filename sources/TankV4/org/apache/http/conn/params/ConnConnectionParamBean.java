package org.apache.http.conn.params;

import org.apache.http.params.HttpAbstractParamBean;
import org.apache.http.params.HttpParams;

/** @deprecated */
@Deprecated
public class ConnConnectionParamBean extends HttpAbstractParamBean {
   public ConnConnectionParamBean(HttpParams var1) {
      super(var1);
   }

   /** @deprecated */
   @Deprecated
   public void setMaxStatusLineGarbage(int var1) {
      this.params.setIntParameter("http.connection.max-status-line-garbage", var1);
   }
}
