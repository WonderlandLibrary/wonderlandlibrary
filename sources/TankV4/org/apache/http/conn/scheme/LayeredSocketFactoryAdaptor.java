package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/** @deprecated */
@Deprecated
class LayeredSocketFactoryAdaptor extends SocketFactoryAdaptor implements LayeredSocketFactory {
   private final LayeredSchemeSocketFactory factory;

   LayeredSocketFactoryAdaptor(LayeredSchemeSocketFactory var1) {
      super(var1);
      this.factory = var1;
   }

   public Socket createSocket(Socket var1, String var2, int var3, boolean var4) throws IOException, UnknownHostException {
      return this.factory.createLayeredSocket(var1, var2, var3, var4);
   }
}
