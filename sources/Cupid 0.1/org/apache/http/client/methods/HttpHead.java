package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpHead extends HttpRequestBase {
  public static final String METHOD_NAME = "HEAD";
  
  public HttpHead() {}
  
  public HttpHead(URI uri) {
    setURI(uri);
  }
  
  public HttpHead(String uri) {
    setURI(URI.create(uri));
  }
  
  public String getMethod() {
    return "HEAD";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\methods\HttpHead.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */