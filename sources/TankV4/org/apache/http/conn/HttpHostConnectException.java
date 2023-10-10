package org.apache.http.conn;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.util.Arrays;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;

@Immutable
public class HttpHostConnectException extends ConnectException {
   private static final long serialVersionUID = -3194482710275220224L;
   private final HttpHost host;

   /** @deprecated */
   @Deprecated
   public HttpHostConnectException(HttpHost var1, ConnectException var2) {
      this(var2, var1, (InetAddress[])null);
   }

   public HttpHostConnectException(IOException var1, HttpHost var2, InetAddress... var3) {
      super("Connect to " + (var2 != null ? var2.toHostString() : "remote host") + (var3 != null && var3.length > 0 ? " " + Arrays.asList(var3) : "") + (var1 != null && var1.getMessage() != null ? " failed: " + var1.getMessage() : " refused"));
      this.host = var2;
      this.initCause(var1);
   }

   public HttpHost getHost() {
      return this.host;
   }
}
