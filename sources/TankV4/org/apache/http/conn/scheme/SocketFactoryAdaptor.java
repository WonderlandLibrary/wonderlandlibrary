package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

/** @deprecated */
@Deprecated
class SocketFactoryAdaptor implements SocketFactory {
   private final SchemeSocketFactory factory;

   SocketFactoryAdaptor(SchemeSocketFactory var1) {
      this.factory = var1;
   }

   public Socket createSocket() throws IOException {
      BasicHttpParams var1 = new BasicHttpParams();
      return this.factory.createSocket(var1);
   }

   public Socket connectSocket(Socket var1, String var2, int var3, InetAddress var4, int var5, HttpParams var6) throws IOException, UnknownHostException, ConnectTimeoutException {
      InetSocketAddress var7 = null;
      if (var4 != null || var5 > 0) {
         var7 = new InetSocketAddress(var4, var5 > 0 ? var5 : 0);
      }

      InetAddress var8 = InetAddress.getByName(var2);
      InetSocketAddress var9 = new InetSocketAddress(var8, var3);
      return this.factory.connectSocket(var1, var9, var7, var6);
   }

   public boolean isSecure(Socket var1) throws IllegalArgumentException {
      return this.factory.isSecure(var1);
   }

   public SchemeSocketFactory getFactory() {
      return this.factory;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this == var1) {
         return true;
      } else {
         return var1 instanceof SocketFactoryAdaptor ? this.factory.equals(((SocketFactoryAdaptor)var1).factory) : this.factory.equals(var1);
      }
   }

   public int hashCode() {
      return this.factory.hashCode();
   }
}
