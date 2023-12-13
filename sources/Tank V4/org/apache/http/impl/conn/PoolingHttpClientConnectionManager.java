package org.apache.http.impl.conn;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.pool.ConnFactory;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.pool.PoolStats;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@ThreadSafe
public class PoolingHttpClientConnectionManager implements HttpClientConnectionManager, ConnPoolControl, Closeable {
   private final Log log;
   private final PoolingHttpClientConnectionManager.ConfigData configData;
   private final CPool pool;
   private final HttpClientConnectionOperator connectionOperator;

   private static Registry getDefaultRegistry() {
      return RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
   }

   public PoolingHttpClientConnectionManager() {
      this(getDefaultRegistry());
   }

   public PoolingHttpClientConnectionManager(long var1, TimeUnit var3) {
      this(getDefaultRegistry(), (HttpConnectionFactory)null, (SchemePortResolver)null, (DnsResolver)null, var1, var3);
   }

   public PoolingHttpClientConnectionManager(Registry var1) {
      this(var1, (HttpConnectionFactory)null, (DnsResolver)null);
   }

   public PoolingHttpClientConnectionManager(Registry var1, DnsResolver var2) {
      this(var1, (HttpConnectionFactory)null, var2);
   }

   public PoolingHttpClientConnectionManager(Registry var1, HttpConnectionFactory var2) {
      this(var1, var2, (DnsResolver)null);
   }

   public PoolingHttpClientConnectionManager(HttpConnectionFactory var1) {
      this(getDefaultRegistry(), var1, (DnsResolver)null);
   }

   public PoolingHttpClientConnectionManager(Registry var1, HttpConnectionFactory var2, DnsResolver var3) {
      this(var1, var2, (SchemePortResolver)null, var3, -1L, TimeUnit.MILLISECONDS);
   }

   public PoolingHttpClientConnectionManager(Registry var1, HttpConnectionFactory var2, SchemePortResolver var3, DnsResolver var4, long var5, TimeUnit var7) {
      this.log = LogFactory.getLog(this.getClass());
      this.configData = new PoolingHttpClientConnectionManager.ConfigData();
      this.pool = new CPool(new PoolingHttpClientConnectionManager.InternalConnectionFactory(this.configData, var2), 2, 20, var5, var7);
      this.connectionOperator = new HttpClientConnectionOperator(var1, var3, var4);
   }

   PoolingHttpClientConnectionManager(CPool var1, Lookup var2, SchemePortResolver var3, DnsResolver var4) {
      this.log = LogFactory.getLog(this.getClass());
      this.configData = new PoolingHttpClientConnectionManager.ConfigData();
      this.pool = var1;
      this.connectionOperator = new HttpClientConnectionOperator(var2, var3, var4);
   }

   protected void finalize() throws Throwable {
      this.shutdown();
      super.finalize();
   }

   public void close() {
      this.shutdown();
   }

   private String format(HttpRoute var1, Object var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append("[route: ").append(var1).append("]");
      if (var2 != null) {
         var3.append("[state: ").append(var2).append("]");
      }

      return var3.toString();
   }

   private String formatStats(HttpRoute var1) {
      StringBuilder var2 = new StringBuilder();
      PoolStats var3 = this.pool.getTotalStats();
      PoolStats var4 = this.pool.getStats(var1);
      var2.append("[total kept alive: ").append(var3.getAvailable()).append("; ");
      var2.append("route allocated: ").append(var4.getLeased() + var4.getAvailable());
      var2.append(" of ").append(var4.getMax()).append("; ");
      var2.append("total allocated: ").append(var3.getLeased() + var3.getAvailable());
      var2.append(" of ").append(var3.getMax()).append("]");
      return var2.toString();
   }

   private String format(CPoolEntry var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("[id: ").append(var1.getId()).append("]");
      var2.append("[route: ").append(var1.getRoute()).append("]");
      Object var3 = var1.getState();
      if (var3 != null) {
         var2.append("[state: ").append(var3).append("]");
      }

      return var2.toString();
   }

   public ConnectionRequest requestConnection(HttpRoute var1, Object var2) {
      Args.notNull(var1, "HTTP route");
      if (this.log.isDebugEnabled()) {
         this.log.debug("Connection request: " + this.format(var1, var2) + this.formatStats(var1));
      }

      Future var3 = this.pool.lease(var1, var2, (FutureCallback)null);
      return new ConnectionRequest(this, var3) {
         final Future val$future;
         final PoolingHttpClientConnectionManager this$0;

         {
            this.this$0 = var1;
            this.val$future = var2;
         }

         public boolean cancel() {
            return this.val$future.cancel(true);
         }

         public HttpClientConnection get(long var1, TimeUnit var3) throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException {
            return this.this$0.leaseConnection(this.val$future, var1, var3);
         }
      };
   }

   protected HttpClientConnection leaseConnection(Future var1, long var2, TimeUnit var4) throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException {
      try {
         CPoolEntry var5 = (CPoolEntry)var1.get(var2, var4);
         if (var5 != null && !var1.isCancelled()) {
            Asserts.check(var5.getConnection() != null, "Pool entry with no connection");
            if (this.log.isDebugEnabled()) {
               this.log.debug("Connection leased: " + this.format(var5) + this.formatStats((HttpRoute)var5.getRoute()));
            }

            return CPoolProxy.newProxy(var5);
         } else {
            throw new InterruptedException();
         }
      } catch (TimeoutException var7) {
         throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
      }
   }

   public void releaseConnection(HttpClientConnection var1, Object var2, long var3, TimeUnit var5) {
      Args.notNull(var1, "Managed connection");
      synchronized(var1){}
      CPoolEntry var7 = CPoolProxy.detach(var1);
      if (var7 != null) {
         ManagedHttpClientConnection var8 = (ManagedHttpClientConnection)var7.getConnection();
         if (var8.isOpen()) {
            var7.setState(var2);
            var7.updateExpiry(var3, var5 != null ? var5 : TimeUnit.MILLISECONDS);
            if (this.log.isDebugEnabled()) {
               String var9;
               if (var3 > 0L) {
                  var9 = "for " + (double)var3 / 1000.0D + " seconds";
               } else {
                  var9 = "indefinitely";
               }

               this.log.debug("Connection " + this.format(var7) + " can be kept alive " + var9);
            }
         }

         this.pool.release(var7, var8.isOpen() && var7.isRouteComplete());
         if (this.log.isDebugEnabled()) {
            this.log.debug("Connection released: " + this.format(var7) + this.formatStats((HttpRoute)var7.getRoute()));
         }

      }
   }

   public void connect(HttpClientConnection var1, HttpRoute var2, int var3, HttpContext var4) throws IOException {
      Args.notNull(var1, "Managed Connection");
      Args.notNull(var2, "HTTP route");
      synchronized(var1){}
      CPoolEntry var7 = CPoolProxy.getPoolEntry(var1);
      ManagedHttpClientConnection var5 = (ManagedHttpClientConnection)var7.getConnection();
      HttpHost var6;
      if (var2.getProxyHost() != null) {
         var6 = var2.getProxyHost();
      } else {
         var6 = var2.getTargetHost();
      }

      InetSocketAddress var9 = var2.getLocalSocketAddress();
      SocketConfig var8 = this.configData.getSocketConfig(var6);
      if (var8 == null) {
         var8 = this.configData.getDefaultSocketConfig();
      }

      if (var8 == null) {
         var8 = SocketConfig.DEFAULT;
      }

      this.connectionOperator.connect(var5, var6, var9, var3, var8, var4);
   }

   public void upgrade(HttpClientConnection var1, HttpRoute var2, HttpContext var3) throws IOException {
      Args.notNull(var1, "Managed Connection");
      Args.notNull(var2, "HTTP route");
      synchronized(var1){}
      CPoolEntry var6 = CPoolProxy.getPoolEntry(var1);
      ManagedHttpClientConnection var4 = (ManagedHttpClientConnection)var6.getConnection();
      this.connectionOperator.upgrade(var4, var2.getTargetHost(), var3);
   }

   public void routeComplete(HttpClientConnection var1, HttpRoute var2, HttpContext var3) throws IOException {
      Args.notNull(var1, "Managed Connection");
      Args.notNull(var2, "HTTP route");
      synchronized(var1){}
      CPoolEntry var5 = CPoolProxy.getPoolEntry(var1);
      var5.markRouteComplete();
   }

   public void shutdown() {
      this.log.debug("Connection manager is shutting down");

      try {
         this.pool.shutdown();
      } catch (IOException var2) {
         this.log.debug("I/O exception shutting down connection manager", var2);
      }

      this.log.debug("Connection manager shut down");
   }

   public void closeIdleConnections(long var1, TimeUnit var3) {
      if (this.log.isDebugEnabled()) {
         this.log.debug("Closing connections idle longer than " + var1 + " " + var3);
      }

      this.pool.closeIdle(var1, var3);
   }

   public void closeExpiredConnections() {
      this.log.debug("Closing expired connections");
      this.pool.closeExpired();
   }

   public int getMaxTotal() {
      return this.pool.getMaxTotal();
   }

   public void setMaxTotal(int var1) {
      this.pool.setMaxTotal(var1);
   }

   public int getDefaultMaxPerRoute() {
      return this.pool.getDefaultMaxPerRoute();
   }

   public void setDefaultMaxPerRoute(int var1) {
      this.pool.setDefaultMaxPerRoute(var1);
   }

   public int getMaxPerRoute(HttpRoute var1) {
      return this.pool.getMaxPerRoute(var1);
   }

   public void setMaxPerRoute(HttpRoute var1, int var2) {
      this.pool.setMaxPerRoute(var1, var2);
   }

   public PoolStats getTotalStats() {
      return this.pool.getTotalStats();
   }

   public PoolStats getStats(HttpRoute var1) {
      return this.pool.getStats(var1);
   }

   public SocketConfig getDefaultSocketConfig() {
      return this.configData.getDefaultSocketConfig();
   }

   public void setDefaultSocketConfig(SocketConfig var1) {
      this.configData.setDefaultSocketConfig(var1);
   }

   public ConnectionConfig getDefaultConnectionConfig() {
      return this.configData.getDefaultConnectionConfig();
   }

   public void setDefaultConnectionConfig(ConnectionConfig var1) {
      this.configData.setDefaultConnectionConfig(var1);
   }

   public SocketConfig getSocketConfig(HttpHost var1) {
      return this.configData.getSocketConfig(var1);
   }

   public void setSocketConfig(HttpHost var1, SocketConfig var2) {
      this.configData.setSocketConfig(var1, var2);
   }

   public ConnectionConfig getConnectionConfig(HttpHost var1) {
      return this.configData.getConnectionConfig(var1);
   }

   public void setConnectionConfig(HttpHost var1, ConnectionConfig var2) {
      this.configData.setConnectionConfig(var1, var2);
   }

   public PoolStats getStats(Object var1) {
      return this.getStats((HttpRoute)var1);
   }

   public int getMaxPerRoute(Object var1) {
      return this.getMaxPerRoute((HttpRoute)var1);
   }

   public void setMaxPerRoute(Object var1, int var2) {
      this.setMaxPerRoute((HttpRoute)var1, var2);
   }

   static class InternalConnectionFactory implements ConnFactory {
      private final PoolingHttpClientConnectionManager.ConfigData configData;
      private final HttpConnectionFactory connFactory;

      InternalConnectionFactory(PoolingHttpClientConnectionManager.ConfigData var1, HttpConnectionFactory var2) {
         this.configData = var1 != null ? var1 : new PoolingHttpClientConnectionManager.ConfigData();
         this.connFactory = (HttpConnectionFactory)(var2 != null ? var2 : ManagedHttpClientConnectionFactory.INSTANCE);
      }

      public ManagedHttpClientConnection create(HttpRoute var1) throws IOException {
         ConnectionConfig var2 = null;
         if (var1.getProxyHost() != null) {
            var2 = this.configData.getConnectionConfig(var1.getProxyHost());
         }

         if (var2 == null) {
            var2 = this.configData.getConnectionConfig(var1.getTargetHost());
         }

         if (var2 == null) {
            var2 = this.configData.getDefaultConnectionConfig();
         }

         if (var2 == null) {
            var2 = ConnectionConfig.DEFAULT;
         }

         return (ManagedHttpClientConnection)this.connFactory.create(var1, var2);
      }

      public Object create(Object var1) throws IOException {
         return this.create((HttpRoute)var1);
      }
   }

   static class ConfigData {
      private final Map socketConfigMap = new ConcurrentHashMap();
      private final Map connectionConfigMap = new ConcurrentHashMap();
      private volatile SocketConfig defaultSocketConfig;
      private volatile ConnectionConfig defaultConnectionConfig;

      public SocketConfig getDefaultSocketConfig() {
         return this.defaultSocketConfig;
      }

      public void setDefaultSocketConfig(SocketConfig var1) {
         this.defaultSocketConfig = var1;
      }

      public ConnectionConfig getDefaultConnectionConfig() {
         return this.defaultConnectionConfig;
      }

      public void setDefaultConnectionConfig(ConnectionConfig var1) {
         this.defaultConnectionConfig = var1;
      }

      public SocketConfig getSocketConfig(HttpHost var1) {
         return (SocketConfig)this.socketConfigMap.get(var1);
      }

      public void setSocketConfig(HttpHost var1, SocketConfig var2) {
         this.socketConfigMap.put(var1, var2);
      }

      public ConnectionConfig getConnectionConfig(HttpHost var1) {
         return (ConnectionConfig)this.connectionConfigMap.get(var1);
      }

      public void setConnectionConfig(HttpHost var1, ConnectionConfig var2) {
         this.connectionConfigMap.put(var1, var2);
      }
   }
}
