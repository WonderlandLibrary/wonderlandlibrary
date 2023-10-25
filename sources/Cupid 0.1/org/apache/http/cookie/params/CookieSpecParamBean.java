package org.apache.http.cookie.params;

import java.util.Collection;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.params.HttpAbstractParamBean;
import org.apache.http.params.HttpParams;

@Deprecated
@NotThreadSafe
public class CookieSpecParamBean extends HttpAbstractParamBean {
  public CookieSpecParamBean(HttpParams params) {
    super(params);
  }
  
  public void setDatePatterns(Collection<String> patterns) {
    this.params.setParameter("http.protocol.cookie-datepatterns", patterns);
  }
  
  public void setSingleHeader(boolean singleHeader) {
    this.params.setBooleanParameter("http.protocol.single-cookie-header", singleHeader);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\cookie\params\CookieSpecParamBean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */