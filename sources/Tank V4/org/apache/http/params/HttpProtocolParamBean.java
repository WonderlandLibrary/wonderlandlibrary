package org.apache.http.params;

import org.apache.http.HttpVersion;

/** @deprecated */
@Deprecated
public class HttpProtocolParamBean extends HttpAbstractParamBean {
   public HttpProtocolParamBean(HttpParams var1) {
      super(var1);
   }

   public void setHttpElementCharset(String var1) {
      HttpProtocolParams.setHttpElementCharset(this.params, var1);
   }

   public void setContentCharset(String var1) {
      HttpProtocolParams.setContentCharset(this.params, var1);
   }

   public void setVersion(HttpVersion var1) {
      HttpProtocolParams.setVersion(this.params, var1);
   }

   public void setUserAgent(String var1) {
      HttpProtocolParams.setUserAgent(this.params, var1);
   }

   public void setUseExpectContinue(boolean var1) {
      HttpProtocolParams.setUseExpectContinue(this.params, var1);
   }
}
