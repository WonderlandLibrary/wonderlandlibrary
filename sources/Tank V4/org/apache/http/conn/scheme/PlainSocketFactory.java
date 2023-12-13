package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@Immutable
public class PlainSocketFactory implements SocketFactory, SchemeSocketFactory {
   private final HostNameResolver nameResolver;

   public static PlainSocketFactory getSocketFactory() {
      return new PlainSocketFactory();
   }

   /** @deprecated */
   @Deprecated
   public PlainSocketFactory(HostNameResolver var1) {
      this.nameResolver = var1;
   }

   public PlainSocketFactory() {
      this.nameResolver = null;
   }

   public Socket createSocket(HttpParams var1) {
      return new Socket();
   }

   public Socket createSocket() {
      return new Socket();
   }

   public Socket connectSocket(Socket var1, InetSocketAddress var2, InetSocketAddress var3, HttpParams var4) throws IOException, ConnectTimeoutException {
      Args.notNull(var2, "Remote address");
      Args.notNull(var4, "HTTP parameters");
      Socket var5 = var1;
      if (var1 == null) {
         var5 = this.createSocket();
      }

      if (var3 != null) {
         var5.setReuseAddress(HttpConnectionParams.getSoReuseaddr(var4));
         var5.bind(var3);
      }

      int var6 = HttpConnectionParams.getConnectionTimeout(var4);
      int var7 = HttpConnectionParams.getSoTimeout(var4);

      try {
         var5.setSoTimeout(var7);
         var5.connect(var2, var6);
         return var5;
      } catch (SocketTimeoutException var9) {
         throw new ConnectTimeoutException("Connect to " + var2 + " timed out");
      }
   }

   public final boolean isSecure(Socket var1) {
      return false;
   }

   /** @deprecated */
   @Deprecated
   public Socket connectSocket(Socket var1, String var2, int var3, InetAddress var4, int var5, HttpParams var6) throws IOException, UnknownHostException, ConnectTimeoutException {
      InetSocketAddress var7 = null;
      if (var4 != null || var5 > 0) {
         var7 = new InetSocketAddress(var4, var5 > 0 ? var5 : 0);
      }

      InetAddress var8;
      if (this.nameResolver != null) {
         var8 = this.nameResolver.resolve(var2);
      } else {
         var8 = InetAddress.getByName(var2);
      }

      InetSocketAddress var9 = new InetSocketAddress(var8, var3);
      return this.connectSocket(var1, var9, var7, var6);
   }
}
