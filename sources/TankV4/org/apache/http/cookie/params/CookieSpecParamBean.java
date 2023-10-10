package org.apache.http.cookie.params;

import java.util.Collection;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.params.HttpAbstractParamBean;
import org.apache.http.params.HttpParams;

/** @deprecated */
@Deprecated
@NotThreadSafe
public class CookieSpecParamBean extends HttpAbstractParamBean {
   public CookieSpecParamBean(HttpParams var1) {
      super(var1);
   }

   public void setDatePatterns(Collection var1) {
      this.params.setParameter("http.protocol.cookie-datepatterns", var1);
   }

   public void setSingleHeader(boolean var1) {
      this.params.setBooleanParameter("http.protocol.single-cookie-header", var1);
   }
}
