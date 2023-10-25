package org.apache.http.auth.params;

import org.apache.http.params.HttpAbstractParamBean;
import org.apache.http.params.HttpParams;

@Deprecated
public class AuthParamBean extends HttpAbstractParamBean {
  public AuthParamBean(HttpParams params) {
    super(params);
  }
  
  public void setCredentialCharset(String charset) {
    AuthParams.setCredentialCharset(this.params, charset);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\auth\params\AuthParamBean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */