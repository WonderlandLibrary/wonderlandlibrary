package org.apache.http.impl.conn;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.LangUtils;

@ThreadSafe
public class BasicHttpClientConnectionManager implements HttpClientConnectionManager, Closeable {
   private final Log log;
   private final HttpClientConnectionOperator connectionOperator;
   private final HttpConnectionFactory connFactory;
   @GuardedBy("this")
   private ManagedHttpClientConnection conn;
   @GuardedBy("this")
   private HttpRoute route;
   @GuardedBy("this")
   private Object state;
   @GuardedBy("this")
   private long updated;
   @GuardedBy("this")
   private long expiry;
   @GuardedBy("this")
   private boolean leased;
   @GuardedBy("this")
   private SocketConfig socketConfig;
   @GuardedBy("this")
   private ConnectionConfig connConfig;
   @GuardedBy("this")
   private volatile boolean shutdown;

   private static Registry getDefaultRegistry() {
      return RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
   }

   public BasicHttpClientConnectionManager(Lookup var1, HttpConnectionFactory var2, SchemePortResolver var3, DnsResolver var4) {
      this.log = LogFactory.getLog(this.getClass());
      this.connectionOperator = new HttpClientConnectionOperator(var1, var3, var4);
      this.connFactory = (HttpConnectionFactory)(var2 != null ? var2 : ManagedHttpClientConnectionFactory.INSTANCE);
      this.expiry = Long.MAX_VALUE;
      this.socketConfig = SocketConfig.DEFAULT;
      this.connConfig = ConnectionConfig.DEFAULT;
   }

   public BasicHttpClientConnectionManager(Lookup var1, HttpConnectionFactory var2) {
      this(var1, var2, (SchemePortResolver)null, (DnsResolver)null);
   }

   public BasicHttpClientConnectionManager(Lookup var1) {
      this(var1, (HttpConnectionFactory)null, (SchemePortResolver)null, (DnsResolver)null);
   }

   public BasicHttpClientConnectionManager() {
      this(getDefaultRegistry(), (HttpConnectionFactory)null, (SchemePortResolver)null, (DnsResolver)null);
   }

   protected void finalize() throws Throwable {
      this.shutdown();
      super.finalize();
   }

   public void close() {
      this.shutdown();
   }

   HttpRoute getRoute() {
      return this.route;
   }

   Object getState() {
      return this.state;
   }

   public synchronized SocketConfig getSocketConfig() {
      return this.socketConfig;
   }

   public synchronized void setSocketConfig(SocketConfig var1) {
      this.socketConfig = var1 != null ? var1 : SocketConfig.DEFAULT;
   }

   public synchronized ConnectionConfig getConnectionConfig() {
      return this.connConfig;
   }

   public synchronized void setConnectionConfig(ConnectionConfig var1) {
      this.connConfig = var1 != null ? var1 : ConnectionConfig.DEFAULT;
   }

   public final ConnectionRequest requestConnection(HttpRoute var1, Object var2) {
      Args.notNull(var1, "Route");
      return new ConnectionRequest(this, var1, var2) {
         final HttpRoute val$route;
         final Object val$state;
         final BasicHttpClientConnectionManager this$0;

         {
            this.this$0 = var1;
            this.val$route = var2;
            this.val$state = var3;
         }

         public boolean cancel() {
            return false;
         }

         public HttpClientConnection get(long var1, TimeUnit var3) {
            return this.this$0.getConnection(this.val$route, this.val$state);
         }
      };
   }

   private void closeConnection() {
      if (this.conn != null) {
         this.log.debug("Closing connection");

         try {
            this.conn.close();
         } catch (IOException var2) {
            if (this.log.isDebugEnabled()) {
               this.log.debug("I/O exception closing connection", var2);
            }
         }

         this.conn = null;
      }

   }

   private void shutdownConnection() {
      if (this.conn != null) {
         this.log.debug("Shutting down connection");

         try {
            this.conn.shutdown();
         } catch (IOException var2) {
            if (this.log.isDebugEnabled()) {
               this.log.debug("I/O exception shutting down connection", var2);
            }
         }

         this.conn = null;
      }

   }

   private void checkExpiry() {
      if (this.conn != null && System.currentTimeMillis() >= this.expiry) {
         if (this.log.isDebugEnabled()) {
            this.log.debug("Connection expired @ " + new Date(this.expiry));
         }

         this.closeConnection();
      }

   }

   synchronized HttpClientConnection getConnection(HttpRoute var1, Object var2) {
      Asserts.check(!this.shutdown, "Connection manager has been shut down");
      if (this.log.isDebugEnabled()) {
         this.log.debug("Get connection for route " + var1);
      }

      Asserts.check(!this.leased, "Connection is still allocated");
      if (!LangUtils.equals(this.route, var1) || !LangUtils.equals(this.state, var2)) {
         this.closeConnection();
      }

      this.route = var1;
      this.state = var2;
      this.checkExpiry();
      if (this.conn == null) {
         this.conn = (ManagedHttpClientConnection)this.connFactory.create(var1, this.connConfig);
      }

      this.leased = true;
      return this.conn;
   }

   public synchronized void releaseConnection(HttpClientConnection var1, Object var2, long var3, TimeUnit var5) {
      Args.notNull(var1, "Connection");
      Asserts.check(var1 == this.conn, "Connection not obtained from this manager");
      if (this.log.isDebugEnabled()) {
         this.log.debug("Releasing connection " + var1);
      }

      if (this.shutdown) {
         this.shutdownConnection();
      } else {
         this.updated = System.currentTimeMillis();
         if (!this.conn.isOpen()) {
            this.conn = null;
            this.route = null;
            this.conn = null;
            this.expiry = Long.MAX_VALUE;
         } else {
            this.state = var2;
            if (this.log.isDebugEnabled()) {
               String var6;
               if (var3 > 0L) {
                  var6 = "for " + var3 + " " + var5;
               } else {
                  var6 = "indefinitely";
               }

               this.log.debug("Connection can be kept alive " + var6);
            }

            if (var3 > 0L) {
               this.expiry = this.updated + var5.toMillis(var3);
            } else {
               this.expiry = Long.MAX_VALUE;
            }
         }

         this.leased = false;
      }
   }

   public void connect(HttpClientConnection var1, HttpRoute var2, int var3, HttpContext var4) throws IOException {
      Args.notNull(var1, "Connection");
      Args.notNull(var2, "HTTP route");
      Asserts.check(var1 == this.conn, "Connection not obtained from this manager");
      HttpHost var5;
      if (var2.getProxyHost() != null) {
         var5 = var2.getProxyHost();
      } else {
         var5 = var2.getTargetHost();
      }

      InetSocketAddress var6 = var2.getLocalSocketAddress();
      this.connectionOperator.connect(this.conn, var5, var6, var3, this.socketConfig, var4);
   }

   public void upgrade(HttpClientConnection var1, HttpRoute var2, HttpContext var3) throws IOException {
      Args.notNull(var1, "Connection");
      Args.notNull(var2, "HTTP route");
      Asserts.check(var1 == this.conn, "Connection not obtained from this manager");
      this.connectionOperator.upgrade(this.conn, var2.getTargetHost(), var3);
   }

   public void routeComplete(HttpClientConnection var1, HttpRoute var2, HttpContext var3) throws IOException {
   }

   public synchronized void closeExpiredConnections() {
      if (!this.shutdown) {
         if (!this.leased) {
            this.checkExpiry();
         }

      }
   }

   public synchronized void closeIdleConnections(long var1, TimeUnit var3) {
      Args.notNull(var3, "Time unit");
      if (!this.shutdown) {
         if (!this.leased) {
            long var4 = var3.toMillis(var1);
            if (var4 < 0L) {
               var4 = 0L;
            }

            long var6 = System.currentTimeMillis() - var4;
            if (this.updated <= var6) {
               this.closeConnection();
            }
         }

      }
   }

   public synchronized void shutdown() {
      if (!this.shutdown) {
         this.shutdown = true;
         this.shutdownConnection();
      }
   }
}
