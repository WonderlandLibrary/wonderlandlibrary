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
  
  public BasicHttpEntityEnclosingRequest(String method, String uri) {
    super(method, uri);
  }
  
  public BasicHttpEntityEnclosingRequest(String method, String uri, ProtocolVersion ver) {
    super(method, uri, ver);
  }
  
  public BasicHttpEntityEnclosingRequest(RequestLine requestline) {
    super(requestline);
  }
  
  public HttpEntity getEntity() {
    return this.entity;
  }
  
  public void setEntity(HttpEntity entity) {
    this.entity = entity;
  }
  
  public boolean expectContinue() {
    Header expect = getFirstHeader("Expect");
    return (expect != null && "100-continue".equalsIgnoreCase(expect.getValue()));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\message\BasicHttpEntityEnclosingRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */