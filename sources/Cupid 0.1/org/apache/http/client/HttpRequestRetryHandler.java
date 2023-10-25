package org.apache.http.client;

import java.io.IOException;
import org.apache.http.protocol.HttpContext;

public interface HttpRequestRetryHandler {
  boolean retryRequest(IOException paramIOException, int paramInt, HttpContext paramHttpContext);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\HttpRequestRetryHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */