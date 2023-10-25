package org.apache.http.impl.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.ConnectionBackoffStrategy;

public class DefaultBackoffStrategy implements ConnectionBackoffStrategy {
  public boolean shouldBackoff(Throwable t) {
    return (t instanceof java.net.SocketTimeoutException || t instanceof java.net.ConnectException);
  }
  
  public boolean shouldBackoff(HttpResponse resp) {
    return (resp.getStatusLine().getStatusCode() == 503);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\client\DefaultBackoffStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */