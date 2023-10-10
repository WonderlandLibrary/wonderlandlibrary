package org.apache.http.client.params;

import org.apache.http.annotation.Immutable;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@Immutable
public class HttpClientParams {
   private HttpClientParams() {
   }

   public static boolean isRedirecting(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      return var0.getBooleanParameter("http.protocol.handle-redirects", true);
   }

   public static void setRedirecting(HttpParams var0, boolean var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setBooleanParameter("http.protocol.handle-redirects", var1);
   }

   public static boolean isAuthenticating(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      return var0.getBooleanParameter("http.protocol.handle-authentication", true);
   }

   public static void setAuthenticating(HttpParams var0, boolean var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setBooleanParameter("http.protocol.handle-authentication", var1);
   }

   public static String getCookiePolicy(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      String var1 = (String)var0.getParameter("http.protocol.cookie-policy");
      return var1 == null ? "best-match" : var1;
   }

   public static void setCookiePolicy(HttpParams var0, String var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setParameter("http.protocol.cookie-policy", var1);
   }

   public static void setConnectionManagerTimeout(HttpParams var0, long var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setLongParameter("http.conn-manager.timeout", var1);
   }

   public static long getConnectionManagerTimeout(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      Long var1 = (Long)var0.getParameter("http.conn-manager.timeout");
      return var1 != null ? var1 : (long)HttpConnectionParams.getConnectionTimeout(var0);
   }
}
