package org.apache.http.params;

import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
public final class HttpConnectionParams implements CoreConnectionPNames {
   private HttpConnectionParams() {
   }

   public static int getSoTimeout(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      return var0.getIntParameter("http.socket.timeout", 0);
   }

   public static void setSoTimeout(HttpParams var0, int var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setIntParameter("http.socket.timeout", var1);
   }

   public static boolean getSoReuseaddr(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      return var0.getBooleanParameter("http.socket.reuseaddr", false);
   }

   public static void setSoReuseaddr(HttpParams var0, boolean var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setBooleanParameter("http.socket.reuseaddr", var1);
   }

   public static boolean getTcpNoDelay(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      return var0.getBooleanParameter("http.tcp.nodelay", true);
   }

   public static void setTcpNoDelay(HttpParams var0, boolean var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setBooleanParameter("http.tcp.nodelay", var1);
   }

   public static int getSocketBufferSize(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      return var0.getIntParameter("http.socket.buffer-size", -1);
   }

   public static void setSocketBufferSize(HttpParams var0, int var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setIntParameter("http.socket.buffer-size", var1);
   }

   public static int getLinger(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      return var0.getIntParameter("http.socket.linger", -1);
   }

   public static void setLinger(HttpParams var0, int var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setIntParameter("http.socket.linger", var1);
   }

   public static int getConnectionTimeout(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      return var0.getIntParameter("http.connection.timeout", 0);
   }

   public static void setConnectionTimeout(HttpParams var0, int var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setIntParameter("http.connection.timeout", var1);
   }

   public static boolean isStaleCheckingEnabled(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      return var0.getBooleanParameter("http.connection.stalecheck", true);
   }

   public static void setStaleCheckingEnabled(HttpParams var0, boolean var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setBooleanParameter("http.connection.stalecheck", var1);
   }

   public static boolean getSoKeepalive(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      return var0.getBooleanParameter("http.socket.keepalive", false);
   }

   public static void setSoKeepalive(HttpParams var0, boolean var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setBooleanParameter("http.socket.keepalive", var1);
   }
}
