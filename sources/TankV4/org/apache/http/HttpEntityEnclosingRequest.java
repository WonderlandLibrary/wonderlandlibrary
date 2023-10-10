package org.apache.http;

public interface HttpEntityEnclosingRequest extends HttpRequest {
   boolean expectContinue();

   void setEntity(HttpEntity var1);

   HttpEntity getEntity();
}
