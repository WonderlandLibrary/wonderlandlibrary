package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.HttpRequest;

public interface HttpUriRequest extends HttpRequest {
  String getMethod();
  
  URI getURI();
  
  void abort() throws UnsupportedOperationException;
  
  boolean isAborted();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\methods\HttpUriRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */