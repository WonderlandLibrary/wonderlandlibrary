package org.apache.http;

public interface HttpEntityEnclosingRequest extends HttpRequest {
  boolean expectContinue();
  
  void setEntity(HttpEntity paramHttpEntity);
  
  HttpEntity getEntity();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\HttpEntityEnclosingRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */