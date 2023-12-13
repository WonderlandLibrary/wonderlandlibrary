package org.apache.http.params;

/** @deprecated */
@Deprecated
public class HttpConnectionParamBean extends HttpAbstractParamBean {
   public HttpConnectionParamBean(HttpParams var1) {
      super(var1);
   }

   public void setSoTimeout(int var1) {
      HttpConnectionParams.setSoTimeout(this.params, var1);
   }

   public void setTcpNoDelay(boolean var1) {
      HttpConnectionParams.setTcpNoDelay(this.params, var1);
   }

   public void setSocketBufferSize(int var1) {
      HttpConnectionParams.setSocketBufferSize(this.params, var1);
   }

   public void setLinger(int var1) {
      HttpConnectionParams.setLinger(this.params, var1);
   }

   public void setConnectionTimeout(int var1) {
      HttpConnectionParams.setConnectionTimeout(this.params, var1);
   }

   public void setStaleCheckingEnabled(boolean var1) {
      HttpConnectionParams.setStaleCheckingEnabled(this.params, var1);
   }
}
