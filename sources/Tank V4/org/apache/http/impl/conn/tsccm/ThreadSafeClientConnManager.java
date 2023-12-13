package org.apache.http.impl.conn.tsccm;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.conn.DefaultClientConnectionOperator;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

/** @deprecated */
@Deprecated
@ThreadSafe
public class ThreadSafeClientConnManager implements ClientConnectionManager {
   private final Log log;
   protected final SchemeRegistry schemeRegistry;
   protected final AbstractConnPool connectionPool;
   protected final ConnPoolByRoute pool;
   protected final ClientConnectionOperator connOperator;
   protected final ConnPerRouteBean connPerRoute;

   public ThreadSafeClientConnManager(SchemeRegistry var1) {
      this(var1, -1L, TimeUnit.MILLISECONDS);
   }

   public ThreadSafeClientConnManager() {
      this(SchemeRegistryFactory.createDefault());
   }

   public ThreadSafeClientConnManager(SchemeRegistry var1, long var2, TimeUnit var4) {
      this(var1, var2, var4, new ConnPerRouteBean());
   }

   public ThreadSafeClientConnManager(SchemeRegistry var1, long var2, TimeUnit var4, ConnPerRouteBean var5) {
      Args.notNull(var1, "Scheme registry");
      this.log = LogFactory.getLog(this.getClass());
      this.schemeRegistry = var1;
      this.connPerRoute = var5;
      this.connOperator = this.createConnectionOperator(var1);
      this.pool = this.createConnectionPool(var2, var4);
      this.connectionPool = this.pool;
   }

   /** @deprecated */
   @Deprecated
   public ThreadSafeClientConnManager(HttpParams var1, SchemeRegistry var2) {
      Args.notNull(var2, "Scheme registry");
      this.log = LogFactory.getLog(this.getClass());
      this.schemeRegistry = var2;
      this.connPerRoute = new ConnPerRouteBean();
      this.connOperator = this.createConnectionOperator(var2);
      this.pool = (ConnPoolByRoute)this.createConnectionPool(var1);
      this.connectionPool = this.pool;
   }

   protected void finalize() throws Throwable {
      this.shutdown();
      super.finalize();
   }

   /** @deprecated */
   @Deprecated
   protected AbstractConnPool createConnectionPool(HttpParams var1) {
      return new ConnPoolByRoute(this.connOperator, var1);
   }

   protected ConnPoolByRoute createConnectionPool(long var1, TimeUnit var3) {
      return new ConnPoolByRoute(this.connOperator, this.connPerRoute, 20, var1, var3);
   }

   protected ClientConnectionOperator createConnectionOperator(SchemeRegistry var1) {
      return new DefaultClientConnectionOperator(var1);
   }

   public SchemeRegistry getSchemeRegistry() {
      return this.schemeRegistry;
   }

   public ClientConnectionRequest requestConnection(HttpRoute var1, Object var2) {
      PoolEntryRequest var3 = this.pool.requestPoolEntry(var1, var2);
      return new ClientConnectionRequest(this, var3, var1) {
         final PoolEntryRequest val$poolRequest;
         final HttpRoute val$route;
         final ThreadSafeClientConnManager this$0;

         {
            this.this$0 = var1;
            this.val$poolRequest = var2;
            this.val$route = var3;
         }

         public void abortRequest() {
            this.val$poolRequest.abortRequest();
         }

         public ManagedClientConnection getConnection(long var1, TimeUnit var3) throws InterruptedException, ConnectionPoolTimeoutException {
            Args.notNull(this.val$route, "Route");
            if (ThreadSafeClientConnManager.access$000(this.this$0).isDebugEnabled()) {
               ThreadSafeClientConnManager.access$000(this.this$0).debug("Get connection: " + this.val$route + ", timeout = " + var1);
            }

            BasicPoolEntry var4 = this.val$poolRequest.getPoolEntry(var1, var3);
            return new BasicPooledConnAdapter(this.this$0, var4);
         }
      };
   }

   public void releaseConnection(ManagedClientConnection var1, long var2, TimeUnit var4) {
      Args.check(var1 instanceof BasicPooledConnAdapter, "Connection class mismatch, connection not obtained from this manager");
      BasicPooledConnAdapter var5 = (BasicPooledConnAdapter)var1;
      if (var5.getPoolEntry() != null) {
         Asserts.check(var5.getManager() == this, "Connection not obtained from this manager");
      }

      synchronized(var5){}
      BasicPoolEntry var7 = (BasicPoolEntry)var5.getPoolEntry();
      if (var7 != null) {
         boolean var8;
         try {
            if (var5.isOpen() && !var5.isMarkedReusable()) {
               var5.shutdown();
            }
         } catch (IOException var12) {
            if (this.log.isDebugEnabled()) {
               this.log.debug("Exception shutting down released connection.", var12);
            }

            var8 = var5.isMarkedReusable();
            if (this.log.isDebugEnabled()) {
               if (var8) {
                  this.log.debug("Released connection is reusable.");
               } else {
                  this.log.debug("Released connection is not reusable.");
               }
            }

            var5.detach();
            this.pool.freeEntry(var7, var8, var2, var4);
            return;
         }

         var8 = var5.isMarkedReusable();
         if (this.log.isDebugEnabled()) {
            if (var8) {
               this.log.debug("Released connection is reusable.");
            } else {
               this.log.debug("Released connection is not reusable.");
            }
         }

         var5.detach();
         this.pool.freeEntry(var7, var8, var2, var4);
      }
   }

   public void shutdown() {
      this.log.debug("Shutting down");
      this.pool.shutdown();
   }

   public int getConnectionsInPool(HttpRoute var1) {
      return this.pool.getConnectionsInPool(var1);
   }

   public int getConnectionsInPool() {
      return this.pool.getConnectionsInPool();
   }

   public void closeIdleConnections(long var1, TimeUnit var3) {
      if (this.log.isDebugEnabled()) {
         this.log.debug("Closing connections idle longer than " + var1 + " " + var3);
      }

      this.pool.closeIdleConnections(var1, var3);
   }

   public void closeExpiredConnections() {
      this.log.debug("Closing expired connections");
      this.pool.closeExpiredConnections();
   }

   public int getMaxTotal() {
      return this.pool.getMaxTotalConnections();
   }

   public void setMaxTotal(int var1) {
      this.pool.setMaxTotalConnections(var1);
   }

   public int getDefaultMaxPerRoute() {
      return this.connPerRoute.getDefaultMaxPerRoute();
   }

   public void setDefaultMaxPerRoute(int var1) {
      this.connPerRoute.setDefaultMaxPerRoute(var1);
   }

   public int getMaxForRoute(HttpRoute var1) {
      return this.connPerRoute.getMaxForRoute(var1);
   }

   public void setMaxForRoute(HttpRoute var1, int var2) {
      this.connPerRoute.setMaxForRoute(var1, var2);
   }

   static Log access$000(ThreadSafeClientConnManager var0) {
      return var0.log;
   }
}
