package org.apache.http.client.params;

import java.util.Collection;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.params.HttpAbstractParamBean;
import org.apache.http.params.HttpParams;

@Deprecated
@NotThreadSafe
public class ClientParamBean extends HttpAbstractParamBean {
  public ClientParamBean(HttpParams params) {
    super(params);
  }
  
  @Deprecated
  public void setConnectionManagerFactoryClassName(String factory) {
    this.params.setParameter("http.connection-manager.factory-class-name", factory);
  }
  
  public void setHandleRedirects(boolean handle) {
    this.params.setBooleanParameter("http.protocol.handle-redirects", handle);
  }
  
  public void setRejectRelativeRedirect(boolean reject) {
    this.params.setBooleanParameter("http.protocol.reject-relative-redirect", reject);
  }
  
  public void setMaxRedirects(int maxRedirects) {
    this.params.setIntParameter("http.protocol.max-redirects", maxRedirects);
  }
  
  public void setAllowCircularRedirects(boolean allow) {
    this.params.setBooleanParameter("http.protocol.allow-circular-redirects", allow);
  }
  
  public void setHandleAuthentication(boolean handle) {
    this.params.setBooleanParameter("http.protocol.handle-authentication", handle);
  }
  
  public void setCookiePolicy(String policy) {
    this.params.setParameter("http.protocol.cookie-policy", policy);
  }
  
  public void setVirtualHost(HttpHost host) {
    this.params.setParameter("http.virtual-host", host);
  }
  
  public void setDefaultHeaders(Collection<Header> headers) {
    this.params.setParameter("http.default-headers", headers);
  }
  
  public void setDefaultHost(HttpHost host) {
    this.params.setParameter("http.default-host", host);
  }
  
  public void setConnectionManagerTimeout(long timeout) {
    this.params.setLongParameter("http.conn-manager.timeout", timeout);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\params\ClientParamBean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */