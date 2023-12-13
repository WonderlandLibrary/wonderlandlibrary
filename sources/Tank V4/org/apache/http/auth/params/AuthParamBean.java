package org.apache.http.auth.params;

import org.apache.http.params.HttpAbstractParamBean;
import org.apache.http.params.HttpParams;

/** @deprecated */
@Deprecated
public class AuthParamBean extends HttpAbstractParamBean {
   public AuthParamBean(HttpParams var1) {
      super(var1);
   }

   public void setCredentialCharset(String var1) {
      AuthParams.setCredentialCharset(this.params, var1);
   }
}
