package org.apache.http.conn.params;

import java.net.InetAddress;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@Immutable
public class ConnRouteParams implements ConnRoutePNames {
   public static final HttpHost NO_HOST = new HttpHost("127.0.0.255", 0, "no-host");
   public static final HttpRoute NO_ROUTE;

   private ConnRouteParams() {
   }

   public static HttpHost getDefaultProxy(HttpParams var0) {
      Args.notNull(var0, "Parameters");
      HttpHost var1 = (HttpHost)var0.getParameter("http.route.default-proxy");
      if (var1 != null && NO_HOST.equals(var1)) {
         var1 = null;
      }

      return var1;
   }

   public static void setDefaultProxy(HttpParams var0, HttpHost var1) {
      Args.notNull(var0, "Parameters");
      var0.setParameter("http.route.default-proxy", var1);
   }

   public static HttpRoute getForcedRoute(HttpParams var0) {
      Args.notNull(var0, "Parameters");
      HttpRoute var1 = (HttpRoute)var0.getParameter("http.route.forced-route");
      if (var1 != null && NO_ROUTE.equals(var1)) {
         var1 = null;
      }

      return var1;
   }

   public static void setForcedRoute(HttpParams var0, HttpRoute var1) {
      Args.notNull(var0, "Parameters");
      var0.setParameter("http.route.forced-route", var1);
   }

   public static InetAddress getLocalAddress(HttpParams var0) {
      Args.notNull(var0, "Parameters");
      InetAddress var1 = (InetAddress)var0.getParameter("http.route.local-address");
      return var1;
   }

   public static void setLocalAddress(HttpParams var0, InetAddress var1) {
      Args.notNull(var0, "Parameters");
      var0.setParameter("http.route.local-address", var1);
   }

   static {
      NO_ROUTE = new HttpRoute(NO_HOST);
   }
}
