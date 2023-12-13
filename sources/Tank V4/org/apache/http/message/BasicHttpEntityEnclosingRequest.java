package org.apache.http.message;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class BasicHttpEntityEnclosingRequest extends BasicHttpRequest implements HttpEntityEnclosingRequest {
   private HttpEntity entity;

   public BasicHttpEntityEnclosingRequest(String var1, String var2) {
      super(var1, var2);
   }

   public BasicHttpEntityEnclosingRequest(String var1, String var2, ProtocolVersion var3) {
      super(var1, var2, var3);
   }

   public BasicHttpEntityEnclosingRequest(RequestLine var1) {
      super(var1);
   }

   public HttpEntity getEntity() {
      return this.entity;
   }

   public void setEntity(HttpEntity var1) {
      this.entity = var1;
   }

   public boolean expectContinue() {
      Header var1 = this.getFirstHeader("Expect");
      return var1 != null && "100-continue".equalsIgnoreCase(var1.getValue());
   }
}
