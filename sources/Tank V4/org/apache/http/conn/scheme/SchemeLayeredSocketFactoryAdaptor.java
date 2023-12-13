package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.http.params.HttpParams;

/** @deprecated */
@Deprecated
class SchemeLayeredSocketFactoryAdaptor extends SchemeSocketFactoryAdaptor implements SchemeLayeredSocketFactory {
   private final LayeredSocketFactory factory;

   SchemeLayeredSocketFactoryAdaptor(LayeredSocketFactory var1) {
      super(var1);
      this.factory = var1;
   }

   public Socket createLayeredSocket(Socket var1, String var2, int var3, HttpParams var4) throws IOException, UnknownHostException {
      return this.factory.createSocket(var1, var2, var3, true);
   }
}
