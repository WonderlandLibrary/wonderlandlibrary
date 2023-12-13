package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

/** @deprecated */
@Deprecated
@ThreadSafe
public class SingleClientConnManager implements ClientConnectionManager {
   private final Log log;
   public static final String MISUSE_MESSAGE = "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
   protected final SchemeRegistry schemeRegistry;
   protected final ClientConnectionOperator connOperator;
   protected final boolean alwaysShutDown;
   @GuardedBy("this")
   protected volatile SingleClientConnManager.PoolEntry uniquePoolEntry;
   @GuardedBy("this")
   protected volatile SingleClientConnManager.ConnAdapter managedConn;
   @GuardedBy("this")
   protected volatile long lastReleaseTime;
   @GuardedBy("this")
   protected volatile long connectionExpiresTime;
   protected volatile boolean isShutDown;

   /** @deprecated */
   @Deprecated
   public SingleClientConnManager(HttpParams var1, SchemeRegistry var2) {
      this(var2);
   }

   public SingleClientConnManager(SchemeRegistry var1) {
      this.log = LogFactory.getLog(this.getClass());
      Args.notNull(var1, "Scheme registry");
      this.schemeRegistry = var1;
      this.connOperator = this.createConnectionOperator(var1);
      this.uniquePoolEntry = new SingleClientConnManager.PoolEntry(this);
      this.managedConn = null;
      this.lastReleaseTime = -1L;
      this.alwaysShutDown = false;
      this.isShutDown = false;
   }

   public SingleClientConnManager() {
      this(SchemeRegistryFactory.createDefault());
   }

   protected void finalize() throws Throwable {
      this.shutdown();
      super.finalize();
   }

   public SchemeRegistry getSchemeRegistry() {
      return this.schemeRegistry;
   }

   protected ClientConnectionOperator createConnectionOperator(SchemeRegistry var1) {
      return new DefaultClientConnectionOperator(var1);
   }

   protected final void assertStillUp() throws IllegalStateException {
      Asserts.check(!this.isShutDown, "Manager is shut down");
   }

   public final ClientConnectionRequest requestConnection(HttpRoute var1, Object var2) {
      return new ClientConnectionRequest(this, var1, var2) {
         final HttpRoute val$route;
         final Object val$state;
         final SingleClientConnManager this$0;

         {
            this.this$0 = var1;
            this.val$route = var2;
            this.val$state = var3;
         }

         public void abortRequest() {
         }

         public ManagedClientConnection getConnection(long var1, TimeUnit var3) {
            return this.this$0.getConnection(this.val$route, this.val$state);
         }
      };
   }

   public ManagedClientConnection getConnection(HttpRoute var1, Object var2) {
      Args.notNull(var1, "Route");
      this.assertStillUp();
      if (this.log.isDebugEnabled()) {
         this.log.debug("Get connection for route " + var1);
      }

      synchronized(this){}
      Asserts.check(this.managedConn == null, "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.");
      boolean var4 = false;
      boolean var5 = false;
      this.closeExpiredConnections();
      if (this.uniquePoolEntry.connection.isOpen()) {
         RouteTracker var6 = this.uniquePoolEntry.tracker;
         var5 = var6 == null || !var6.toRoute().equals(var1);
      } else {
         var4 = true;
      }

      if (var5) {
         var4 = true;

         try {
            this.uniquePoolEntry.shutdown();
         } catch (IOException var8) {
            this.log.debug("Problem shutting down connection.", var8);
         }
      }

      if (var4) {
         this.uniquePoolEntry = new SingleClientConnManager.PoolEntry(this);
      }

      this.managedConn = new SingleClientConnManager.ConnAdapter(this, this.uniquePoolEntry, var1);
      return this.managedConn;
   }

   public void releaseConnection(ManagedClientConnection var1, long var2, TimeUnit var4) {
      Args.check(var1 instanceof SingleClientConnManager.ConnAdapter, "Connection class mismatch, connection not obtained from this manager");
      this.assertStillUp();
      if (this.log.isDebugEnabled()) {
         this.log.debug("Releasing connection " + var1);
      }

      SingleClientConnManager.ConnAdapter var5 = (SingleClientConnManager.ConnAdapter)var1;
      synchronized(var5){}
      if (var5.poolEntry != null) {
         ClientConnectionManager var7 = var5.getManager();
         Asserts.check(var7 == this, "Connection not obtained from this manager");

         try {
            if (var5.isOpen() && (this.alwaysShutDown || !var5.isMarkedReusable())) {
               if (this.log.isDebugEnabled()) {
                  this.log.debug("Released connection open but not reusable.");
               }

               var5.shutdown();
            }
         } catch (IOException var15) {
            if (this.log.isDebugEnabled()) {
               this.log.debug("Exception shutting down released connection.", var15);
            }

            var5.detach();
            synchronized(this){}
            this.managedConn = null;
            this.lastReleaseTime = System.currentTimeMillis();
            if (var2 > 0L) {
               this.connectionExpiresTime = var4.toMillis(var2) + this.lastReleaseTime;
            } else {
               this.connectionExpiresTime = Long.MAX_VALUE;
            }

            return;
         }

         var5.detach();
         synchronized(this){}
         this.managedConn = null;
         this.lastReleaseTime = System.currentTimeMillis();
         if (var2 > 0L) {
            this.connectionExpiresTime = var4.toMillis(var2) + this.lastReleaseTime;
         } else {
            this.connectionExpiresTime = Long.MAX_VALUE;
         }

      }
   }

   public void closeExpiredConnections() {
      long var1 = this.connectionExpiresTime;
      if (System.currentTimeMillis() >= var1) {
         this.closeIdleConnections(0L, TimeUnit.MILLISECONDS);
      }

   }

   public void closeIdleConnections(long var1, TimeUnit var3) {
      this.assertStillUp();
      Args.notNull(var3, "Time unit");
      synchronized(this){}
      if (this.managedConn == null && this.uniquePoolEntry.connection.isOpen()) {
         long var5 = System.currentTimeMillis() - var3.toMillis(var1);
         if (this.lastReleaseTime <= var5) {
            try {
               this.uniquePoolEntry.close();
            } catch (IOException var9) {
               this.log.debug("Problem closing idle connection.", var9);
            }
         }
      }

   }

   public void shutdown() {
      this.isShutDown = true;
      synchronized(this){}

      try {
         if (this.uniquePoolEntry != null) {
            this.uniquePoolEntry.shutdown();
         }
      } catch (IOException var5) {
         this.log.debug("Problem while shutting down manager.", var5);
         this.uniquePoolEntry = null;
         this.managedConn = null;
         return;
      }

      this.uniquePoolEntry = null;
      this.managedConn = null;
   }

   protected void revokeConnection() {
      SingleClientConnManager.ConnAdapter var1 = this.managedConn;
      if (var1 != null) {
         var1.detach();
         synchronized(this){}

         try {
            this.uniquePoolEntry.shutdown();
         } catch (IOException var5) {
            this.log.debug("Problem while shutting down connection.", var5);
         }

      }
   }

   protected class ConnAdapter extends AbstractPooledConnAdapter {
      final SingleClientConnManager this$0;

      protected ConnAdapter(SingleClientConnManager var1, SingleClientConnManager.PoolEntry var2, HttpRoute var3) {
         super(var1, var2);
         this.this$0 = var1;
         this.markReusable();
         var2.route = var3;
      }
   }

   protected class PoolEntry extends AbstractPoolEntry {
      final SingleClientConnManager this$0;

      protected PoolEntry(SingleClientConnManager var1) {
         super(var1.connOperator, (HttpRoute)null);
         this.this$0 = var1;
      }

      protected void close() throws IOException {
         this.shutdownEntry();
         if (this.connection.isOpen()) {
            this.connection.close();
         }

      }

      protected void shutdown() throws IOException {
         this.shutdownEntry();
         if (this.connection.isOpen()) {
            this.connection.shutdown();
         }

      }
   }
}
