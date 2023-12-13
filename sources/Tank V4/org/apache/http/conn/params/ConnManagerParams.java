package org.apache.http.conn.params;

import org.apache.http.annotation.Immutable;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@Immutable
public final class ConnManagerParams implements ConnManagerPNames {
   public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 20;
   private static final ConnPerRoute DEFAULT_CONN_PER_ROUTE = new ConnPerRoute() {
      public int getMaxForRoute(HttpRoute var1) {
         return 2;
      }
   };

   /** @deprecated */
   @Deprecated
   public static long getTimeout(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      return var0.getLongParameter("http.conn-manager.timeout", 0L);
   }

   /** @deprecated */
   @Deprecated
   public static void setTimeout(HttpParams var0, long var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setLongParameter("http.conn-manager.timeout", var1);
   }

   public static void setMaxConnectionsPerRoute(HttpParams var0, ConnPerRoute var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setParameter("http.conn-manager.max-per-route", var1);
   }

   public static ConnPerRoute getMaxConnectionsPerRoute(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      ConnPerRoute var1 = (ConnPerRoute)var0.getParameter("http.conn-manager.max-per-route");
      if (var1 == null) {
         var1 = DEFAULT_CONN_PER_ROUTE;
      }

      return var1;
   }

   public static void setMaxTotalConnections(HttpParams var0, int var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setIntParameter("http.conn-manager.max-total", var1);
   }

   public static int getMaxTotalConnections(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      return var0.getIntParameter("http.conn-manager.max-total", 20);
   }
}
