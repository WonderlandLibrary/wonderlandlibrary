package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpPut extends HttpEntityEnclosingRequestBase {
  public static final String METHOD_NAME = "PUT";
  
  public HttpPut() {}
  
  public HttpPut(URI uri) {
    setURI(uri);
  }
  
  public HttpPut(String uri) {
    setURI(URI.create(uri));
  }
  
  public String getMethod() {
    return "PUT";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\methods\HttpPut.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */