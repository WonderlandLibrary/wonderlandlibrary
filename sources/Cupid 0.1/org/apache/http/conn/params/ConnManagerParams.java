package org.apache.http.conn.params;

import org.apache.http.annotation.Immutable;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
@Immutable
public final class ConnManagerParams implements ConnManagerPNames {
  public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 20;
  
  @Deprecated
  public static long getTimeout(HttpParams params) {
    Args.notNull(params, "HTTP parameters");
    return params.getLongParameter("http.conn-manager.timeout", 0L);
  }
  
  @Deprecated
  public static void setTimeout(HttpParams params, long timeout) {
    Args.notNull(params, "HTTP parameters");
    params.setLongParameter("http.conn-manager.timeout", timeout);
  }
  
  private static final ConnPerRoute DEFAULT_CONN_PER_ROUTE = new ConnPerRoute() {
      public int getMaxForRoute(HttpRoute route) {
        return 2;
      }
    };
  
  public static void setMaxConnectionsPerRoute(HttpParams params, ConnPerRoute connPerRoute) {
    Args.notNull(params, "HTTP parameters");
    params.setParameter("http.conn-manager.max-per-route", connPerRoute);
  }
  
  public static ConnPerRoute getMaxConnectionsPerRoute(HttpParams params) {
    Args.notNull(params, "HTTP parameters");
    ConnPerRoute connPerRoute = (ConnPerRoute)params.getParameter("http.conn-manager.max-per-route");
    if (connPerRoute == null)
      connPerRoute = DEFAULT_CONN_PER_ROUTE; 
    return connPerRoute;
  }
  
  public static void setMaxTotalConnections(HttpParams params, int maxTotalConnections) {
    Args.notNull(params, "HTTP parameters");
    params.setIntParameter("http.conn-manager.max-total", maxTotalConnections);
  }
  
  public static int getMaxTotalConnections(HttpParams params) {
    Args.notNull(params, "HTTP parameters");
    return params.getIntParameter("http.conn-manager.max-total", 20);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\params\ConnManagerParams.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */