package org.apache.http.conn.params;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@ThreadSafe
public final class ConnPerRouteBean implements ConnPerRoute {
   public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 2;
   private final ConcurrentHashMap maxPerHostMap;
   private volatile int defaultMax;

   public ConnPerRouteBean(int var1) {
      this.maxPerHostMap = new ConcurrentHashMap();
      this.setDefaultMaxPerRoute(var1);
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

   public void setDefaultMaxPerRoute(int var1) {
      Args.positive(var1, "Defautl max per route");
      this.defaultMax = var1;
   }

   public void setMaxForRoute(HttpRoute var1, int var2) {
      Args.notNull(var1, "HTTP route");
      Args.positive(var2, "Max per route");
      this.maxPerHostMap.put(var1, var2);
   }

   public int getMaxForRoute(HttpRoute var1) {
      Args.notNull(var1, "HTTP route");
      Integer var2 = (Integer)this.maxPerHostMap.get(var1);
      return var2 != null ? var2 : this.defaultMax;
   }

   public void setMaxForRoutes(Map var1) {
      if (var1 != null) {
         this.maxPerHostMap.clear();
         this.maxPerHostMap.putAll(var1);
      }
   }

   public String toString() {
      return this.maxPerHostMap.toString();
   }
}
