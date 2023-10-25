package org.apache.http.client;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

public interface ServiceUnavailableRetryStrategy {
  boolean retryRequest(HttpResponse paramHttpResponse, int paramInt, HttpContext paramHttpContext);
  
  long getRetryInterval();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\ServiceUnavailableRetryStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */