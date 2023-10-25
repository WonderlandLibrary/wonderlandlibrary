package org.apache.http.conn.params;

import org.apache.http.params.HttpAbstractParamBean;
import org.apache.http.params.HttpParams;

@Deprecated
public class ConnConnectionParamBean extends HttpAbstractParamBean {
  public ConnConnectionParamBean(HttpParams params) {
    super(params);
  }
  
  @Deprecated
  public void setMaxStatusLineGarbage(int maxStatusLineGarbage) {
    this.params.setIntParameter("http.connection.max-status-line-garbage", maxStatusLineGarbage);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\params\ConnConnectionParamBean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */