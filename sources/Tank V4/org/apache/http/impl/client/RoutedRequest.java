package org.apache.http.impl.client;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.routing.HttpRoute;

/** @deprecated */
@Deprecated
@NotThreadSafe
public class RoutedRequest {
   protected final RequestWrapper request;
   protected final HttpRoute route;

   public RoutedRequest(RequestWrapper var1, HttpRoute var2) {
      this.request = var1;
      this.route = var2;
   }

   public final RequestWrapper getRequest() {
      return this.request;
   }

   public final HttpRoute getRoute() {
      return this.route;
   }
}
