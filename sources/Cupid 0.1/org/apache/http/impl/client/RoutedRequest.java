package org.apache.http.impl.client;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.routing.HttpRoute;

@Deprecated
@NotThreadSafe
public class RoutedRequest {
  protected final RequestWrapper request;
  
  protected final HttpRoute route;
  
  public RoutedRequest(RequestWrapper req, HttpRoute route) {
    this.request = req;
    this.route = route;
  }
  
  public final RequestWrapper getRequest() {
    return this.request;
  }
  
  public final HttpRoute getRoute() {
    return this.route;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\client\RoutedRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */