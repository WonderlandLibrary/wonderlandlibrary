package org.apache.http.params;

import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.SocketConfig;

/** @deprecated */
@Deprecated
public final class HttpParamConfig {
   private HttpParamConfig() {
   }

   public static SocketConfig getSocketConfig(HttpParams var0) {
      return SocketConfig.custom().setSoTimeout(var0.getIntParameter("http.socket.timeout", 0)).setSoReuseAddress(var0.getBooleanParameter("http.socket.reuseaddr", false)).setSoKeepAlive(var0.getBooleanParameter("http.socket.keepalive", false)).setSoLinger(var0.getIntParameter("http.socket.linger", -1)).setTcpNoDelay(var0.getBooleanParameter("http.tcp.nodelay", true)).build();
   }

   public static MessageConstraints getMessageConstraints(HttpParams var0) {
      return MessageConstraints.custom().setMaxHeaderCount(var0.getIntParameter("http.connection.max-header-count", -1)).setMaxLineLength(var0.getIntParameter("http.connection.max-line-length", -1)).build();
   }

   public static ConnectionConfig getConnectionConfig(HttpParams var0) {
      MessageConstraints var1 = getMessageConstraints(var0);
      String var2 = (String)var0.getParameter("http.protocol.element-charset");
      return ConnectionConfig.custom().setCharset(var2 != null ? Charset.forName(var2) : null).setMalformedInputAction((CodingErrorAction)var0.getParameter("http.malformed.input.action")).setMalformedInputAction((CodingErrorAction)var0.getParameter("http.unmappable.input.action")).setMessageConstraints(var1).build();
   }
}
