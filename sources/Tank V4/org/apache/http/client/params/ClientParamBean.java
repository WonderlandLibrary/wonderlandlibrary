package org.apache.http.client.params;

import java.util.Collection;
import org.apache.http.HttpHost;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.params.HttpAbstractParamBean;
import org.apache.http.params.HttpParams;

/** @deprecated */
@Deprecated
@NotThreadSafe
public class ClientParamBean extends HttpAbstractParamBean {
   public ClientParamBean(HttpParams var1) {
      super(var1);
   }

   /** @deprecated */
   @Deprecated
   public void setConnectionManagerFactoryClassName(String var1) {
      this.params.setParameter("http.connection-manager.factory-class-name", var1);
   }

   public void setHandleRedirects(boolean var1) {
      this.params.setBooleanParameter("http.protocol.handle-redirects", var1);
   }

   public void setRejectRelativeRedirect(boolean var1) {
      this.params.setBooleanParameter("http.protocol.reject-relative-redirect", var1);
   }

   public void setMaxRedirects(int var1) {
      this.params.setIntParameter("http.protocol.max-redirects", var1);
   }

   public void setAllowCircularRedirects(boolean var1) {
      this.params.setBooleanParameter("http.protocol.allow-circular-redirects", var1);
   }

   public void setHandleAuthentication(boolean var1) {
      this.params.setBooleanParameter("http.protocol.handle-authentication", var1);
   }

   public void setCookiePolicy(String var1) {
      this.params.setParameter("http.protocol.cookie-policy", var1);
   }

   public void setVirtualHost(HttpHost var1) {
      this.params.setParameter("http.virtual-host", var1);
   }

   public void setDefaultHeaders(Collection var1) {
      this.params.setParameter("http.default-headers", var1);
   }

   public void setDefaultHost(HttpHost var1) {
      this.params.setParameter("http.default-host", var1);
   }

   public void setConnectionManagerTimeout(long var1) {
      this.params.setLongParameter("http.conn-manager.timeout", var1);
   }
}
