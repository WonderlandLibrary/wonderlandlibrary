package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpDelete extends HttpRequestBase {
  public static final String METHOD_NAME = "DELETE";
  
  public HttpDelete() {}
  
  public HttpDelete(URI uri) {
    setURI(uri);
  }
  
  public HttpDelete(String uri) {
    setURI(URI.create(uri));
  }
  
  public String getMethod() {
    return "DELETE";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\methods\HttpDelete.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */