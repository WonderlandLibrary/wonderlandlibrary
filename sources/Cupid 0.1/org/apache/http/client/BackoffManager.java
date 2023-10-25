package org.apache.http.client;

import org.apache.http.conn.routing.HttpRoute;

public interface BackoffManager {
  void backOff(HttpRoute paramHttpRoute);
  
  void probe(HttpRoute paramHttpRoute);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\BackoffManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */