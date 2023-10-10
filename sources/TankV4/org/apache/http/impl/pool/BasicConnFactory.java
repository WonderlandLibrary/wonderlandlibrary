package org.apache.http.impl.pool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultBHttpClientConnectionFactory;
import org.apache.http.params.HttpParamConfig;
import org.apache.http.params.HttpParams;
import org.apache.http.pool.ConnFactory;
import org.apache.http.util.Args;

@Immutable
public class BasicConnFactory implements ConnFactory {
   private final SocketFactory plainfactory;
   private final SSLSocketFactory sslfactory;
   private final int connectTimeout;
   private final SocketConfig sconfig;
   private final HttpConnectionFactory connFactory;

   /** @deprecated */
   @Deprecated
   public BasicConnFactory(SSLSocketFactory var1, HttpParams var2) {
      Args.notNull(var2, "HTTP params");
      this.plainfactory = null;
      this.sslfactory = var1;
      this.connectTimeout = var2.getIntParameter("http.connection.timeout", 0);
      this.sconfig = HttpParamConfig.getSocketConfig(var2);
      this.connFactory = new DefaultBHttpClientConnectionFactory(HttpParamConfig.getConnectionConfig(var2));
   }

   /** @deprecated */
   @Deprecated
   public BasicConnFactory(HttpParams var1) {
      this((SSLSocketFactory)null, (HttpParams)var1);
   }

   public BasicConnFactory(SocketFactory var1, SSLSocketFactory var2, int var3, SocketConfig var4, ConnectionConfig var5) {
      this.plainfactory = var1;
      this.sslfactory = var2;
      this.connectTimeout = var3;
      this.sconfig = var4 != null ? var4 : SocketConfig.DEFAULT;
      this.connFactory = new DefaultBHttpClientConnectionFactory(var5 != null ? var5 : ConnectionConfig.DEFAULT);
   }

   public BasicConnFactory(int var1, SocketConfig var2, ConnectionConfig var3) {
      this((SocketFactory)null, (SSLSocketFactory)null, var1, var2, var3);
   }

   public BasicConnFactory(SocketConfig var1, ConnectionConfig var2) {
      this((SocketFactory)null, (SSLSocketFactory)null, 0, var1, var2);
   }

   public BasicConnFactory() {
      this((SocketFactory)null, (SSLSocketFactory)null, 0, SocketConfig.DEFAULT, ConnectionConfig.DEFAULT);
   }

   /** @deprecated */
   @Deprecated
   protected HttpClientConnection create(Socket var1, HttpParams var2) throws IOException {
      int var3 = var2.getIntParameter("http.socket.buffer-size", 8192);
      DefaultBHttpClientConnection var4 = new DefaultBHttpClientConnection(var3);
      var4.bind(var1);
      return var4;
   }

   public HttpClientConnection create(HttpHost var1) throws IOException {
      String var2 = var1.getSchemeName();
      Socket var3 = null;
      if ("http".equalsIgnoreCase(var2)) {
         var3 = this.plainfactory != null ? this.plainfactory.createSocket() : new Socket();
      }

      if ("https".equalsIgnoreCase(var2)) {
         var3 = ((SocketFactory)(this.sslfactory != null ? this.sslfactory : SSLSocketFactory.getDefault())).createSocket();
      }

      if (var3 == null) {
         throw new IOException(var2 + " scheme is not supported");
      } else {
         String var4 = var1.getHostName();
         int var5 = var1.getPort();
         if (var5 == -1) {
            if (var1.getSchemeName().equalsIgnoreCase("http")) {
               var5 = 80;
            } else if (var1.getSchemeName().equalsIgnoreCase("https")) {
               var5 = 443;
            }
         }

         var3.setSoTimeout(this.sconfig.getSoTimeout());
         var3.connect(new InetSocketAddress(var4, var5), this.connectTimeout);
         var3.setTcpNoDelay(this.sconfig.isTcpNoDelay());
         int var6 = this.sconfig.getSoLinger();
         if (var6 >= 0) {
            var3.setSoLinger(var6 > 0, var6);
         }

         var3.setKeepAlive(this.sconfig.isSoKeepAlive());
         return (HttpClientConnection)this.connFactory.createConnection(var3);
      }
   }

   public Object create(Object var1) throws IOException {
      return this.create((HttpHost)var1);
   }
}
