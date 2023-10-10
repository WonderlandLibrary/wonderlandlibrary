package org.apache.http.impl.client;

import java.net.ProxySelector;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.params.HttpParams;

/** @deprecated */
@Deprecated
@ThreadSafe
public class SystemDefaultHttpClient extends DefaultHttpClient {
   public SystemDefaultHttpClient(HttpParams var1) {
      super((ClientConnectionManager)null, var1);
   }

   public SystemDefaultHttpClient() {
      super((ClientConnectionManager)null, (HttpParams)null);
   }

   protected ClientConnectionManager createClientConnectionManager() {
      PoolingClientConnectionManager var1 = new PoolingClientConnectionManager(SchemeRegistryFactory.createSystemDefault());
      String var2 = System.getProperty("http.keepAlive", "true");
      if ("true".equalsIgnoreCase(var2)) {
         var2 = System.getProperty("http.maxConnections", "5");
         int var3 = Integer.parseInt(var2);
         var1.setDefaultMaxPerRoute(var3);
         var1.setMaxTotal(2 * var3);
      }

      return var1;
   }

   protected HttpRoutePlanner createHttpRoutePlanner() {
      return new ProxySelectorRoutePlanner(this.getConnectionManager().getSchemeRegistry(), ProxySelector.getDefault());
   }

   protected ConnectionReuseStrategy createConnectionReuseStrategy() {
      String var1 = System.getProperty("http.keepAlive", "true");
      return (ConnectionReuseStrategy)("true".equalsIgnoreCase(var1) ? new DefaultConnectionReuseStrategy() : new NoConnectionReuseStrategy());
   }
}
