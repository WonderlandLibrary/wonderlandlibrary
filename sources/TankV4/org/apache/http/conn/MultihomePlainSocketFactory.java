package org.apache.http.conn;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

/** @deprecated */
@Deprecated
@Immutable
public final class MultihomePlainSocketFactory implements SocketFactory {
   private static final MultihomePlainSocketFactory DEFAULT_FACTORY = new MultihomePlainSocketFactory();

   public static MultihomePlainSocketFactory getSocketFactory() {
      return DEFAULT_FACTORY;
   }

   private MultihomePlainSocketFactory() {
   }

   public Socket createSocket() {
      return new Socket();
   }

   public Socket connectSocket(Socket var1, String var2, int var3, InetAddress var4, int var5, HttpParams var6) throws IOException {
      Args.notNull(var2, "Target host");
      Args.notNull(var6, "HTTP parameters");
      Socket var7 = var1;
      if (var1 == null) {
         var7 = this.createSocket();
      }

      if (var4 != null || var5 > 0) {
         InetSocketAddress var8 = new InetSocketAddress(var4, var5 > 0 ? var5 : 0);
         var7.bind(var8);
      }

      int var17 = HttpConnectionParams.getConnectionTimeout(var6);
      InetAddress[] var9 = InetAddress.getAllByName(var2);
      ArrayList var10 = new ArrayList(var9.length);
      var10.addAll(Arrays.asList(var9));
      Collections.shuffle(var10);
      IOException var11 = null;
      Iterator var12 = var10.iterator();

      while(var12.hasNext()) {
         InetAddress var13 = (InetAddress)var12.next();

         try {
            var7.connect(new InetSocketAddress(var13, var3), var17);
            break;
         } catch (SocketTimeoutException var15) {
            throw new ConnectTimeoutException("Connect to " + var13 + " timed out");
         } catch (IOException var16) {
            var7 = new Socket();
            var11 = var16;
         }
      }

      if (var11 != null) {
         throw var11;
      } else {
         return var7;
      }
   }

   public final boolean isSecure(Socket var1) throws IllegalArgumentException {
      Args.notNull(var1, "Socket");
      Asserts.check(!var1.isClosed(), "Socket is closed");
      return false;
   }
}
