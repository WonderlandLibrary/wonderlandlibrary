package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpGet extends HttpRequestBase {
  public static final String METHOD_NAME = "GET";
  
  public HttpGet() {}
  
  public HttpGet(URI uri) {
    setURI(uri);
  }
  
  public HttpGet(String uri) {
    setURI(URI.create(uri));
  }
  
  public String getMethod() {
    return "GET";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\methods\HttpGet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */