package org.apache.http.params;

import org.apache.http.util.Args;

@Deprecated
public abstract class HttpAbstractParamBean {
  protected final HttpParams params;
  
  public HttpAbstractParamBean(HttpParams params) {
    this.params = (HttpParams)Args.notNull(params, "HTTP parameters");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\params\HttpAbstractParamBean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */