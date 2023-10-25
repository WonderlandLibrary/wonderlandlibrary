package org.apache.http.conn.params;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;

@Deprecated
@ThreadSafe
public final class ConnPerRouteBean implements ConnPerRoute {
  public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 2;
  
  private final ConcurrentHashMap<HttpRoute, Integer> maxPerHostMap;
  
  private volatile int defaultMax;
  
  public ConnPerRouteBean(int defaultMax) {
    this.maxPerHostMap = new ConcurrentHashMap<HttpRoute, Integer>();
    setDefaultMaxPerRoute(defaultMax);
  }
  
  public ConnPerRouteBean() {
    this(2);
  }
  
  public int getDefaultMax() {
    return this.defaultMax;
  }
  
  public int getDefaultMaxPerRoute() {
    return this.defaultMax;
  }
  
  public void setDefaultMaxPerRoute(int max) {
    Args.positive(max, "Defautl max per route");
    this.defaultMax = max;
  }
  
  public void setMaxForRoute(HttpRoute route, int max) {
    Args.notNull(route, "HTTP route");
    Args.positive(max, "Max per route");
    this.maxPerHostMap.put(route, Integer.valueOf(max));
  }
  
  public int getMaxForRoute(HttpRoute route) {
    Args.notNull(route, "HTTP route");
    Integer max = this.maxPerHostMap.get(route);
    if (max != null)
      return max.intValue(); 
    return this.defaultMax;
  }
  
  public void setMaxForRoutes(Map<HttpRoute, Integer> map) {
    if (map == null)
      return; 
    this.maxPerHostMap.clear();
    this.maxPerHostMap.putAll(map);
  }
  
  public String toString() {
    return this.maxPerHostMap.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\params\ConnPerRouteBean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */