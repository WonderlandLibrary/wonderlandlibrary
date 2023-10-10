package org.apache.http.conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.util.Arrays;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;

@Immutable
public class ConnectTimeoutException extends InterruptedIOException {
   private static final long serialVersionUID = -4816682903149535989L;
   private final HttpHost host;

   public ConnectTimeoutException() {
      this.host = null;
   }

   public ConnectTimeoutException(String var1) {
      super(var1);
      this.host = null;
   }

   public ConnectTimeoutException(IOException var1, HttpHost var2, InetAddress... var3) {
      super("Connect to " + (var2 != null ? var2.toHostString() : "remote host") + (var3 != null && var3.length > 0 ? " " + Arrays.asList(var3) : "") + (var1 != null && var1.getMessage() != null ? " failed: " + var1.getMessage() : " timed out"));
      this.host = var2;
      this.initCause(var1);
   }

   public HttpHost getHost() {
      return this.host;
   }
}
