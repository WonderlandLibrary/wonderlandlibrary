package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpClientConnection;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

/** @deprecated */
@Deprecated
@ThreadSafe
public class BasicClientConnectionManager implements ClientConnectionManager {
   private final Log log;
   private static final AtomicLong COUNTER = new AtomicLong();
   public static final String MISUSE_MESSAGE = "Invalid use of BasicClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
   private final SchemeRegistry schemeRegistry;
   private final ClientConnectionOperator connOperator;
   @GuardedBy("this")
   private HttpPoolEntry poolEntry;
   @GuardedBy("this")
   private ManagedClientConnectionImpl conn;
   @GuardedBy("this")
   private volatile boolean shutdown;

   public BasicClientConnectionManager(SchemeRegistry var1) {
      this.log = LogFactory.getLog(this.getClass());
      Args.notNull(var1, "Scheme registry");
      this.schemeRegistry = var1;
      this.connOperator = this.createConnectionOperator(var1);
   }

   public BasicClientConnectionManager() {
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

   public final ClientConnectionRequest requestConnection(HttpRoute var1, Object var2) {
      return new ClientConnectionRequest(this, var1, var2) {
         final HttpRoute val$route;
         final Object val$state;
         final BasicClientConnectionManager this$0;

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

   private void assertNotShutdown() {
      Asserts.check(!this.shutdown, "Connection manager has been shut down");
   }

   ManagedClientConnection getConnection(HttpRoute var1, Object var2) {
      Args.notNull(var1, "Route");
      synchronized(this){}
      this.assertNotShutdown();
      if (this.log.isDebugEnabled()) {
         this.log.debug("Get connection for route " + var1);
      }

      Asserts.check(this.conn == null, "Invalid use of BasicClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.");
      if (this.poolEntry != null && !this.poolEntry.getPlannedRoute().equals(var1)) {
         this.poolEntry.close();
         this.poolEntry = null;
      }

      if (this.poolEntry == null) {
         String var4 = Long.toString(COUNTER.getAndIncrement());
         OperatedClientConnection var5 = this.connOperator.createConnection();
         this.poolEntry = new HttpPoolEntry(this.log, var4, var1, var5, 0L, TimeUnit.MILLISECONDS);
      }

      long var7 = System.currentTimeMillis();
      if (this.poolEntry.isExpired(var7)) {
         this.poolEntry.close();
         this.poolEntry.getTracker().reset();
      }

      this.conn = new ManagedClientConnectionImpl(this, this.connOperator, this.poolEntry);
      return this.conn;
   }

   private void shutdownConnection(HttpClientConnection var1) {
      try {
         var1.shutdown();
      } catch (IOException var3) {
         if (this.log.isDebugEnabled()) {
            this.log.debug("I/O exception shutting down connection", var3);
         }
      }

   }

   public void releaseConnection(ManagedClientConnection var1, long var2, TimeUnit var4) {
      Args.check(var1 instanceof ManagedClientConnectionImpl, "Connection class mismatch, connection not obtained from this manager");
      ManagedClientConnectionImpl var5 = (ManagedClientConnectionImpl)var1;
      synchronized(var5){}
      if (this.log.isDebugEnabled()) {
         this.log.debug("Releasing connection " + var1);
      }

      if (var5.getPoolEntry() != null) {
         ClientConnectionManager var7 = var5.getManager();
         Asserts.check(var7 == this, "Connection not obtained from this manager");
         synchronized(this){}
         if (this.shutdown) {
            this.shutdownConnection(var5);
         } else {
            if (var5.isOpen() && !var5.isMarkedReusable()) {
               this.shutdownConnection(var5);
            }

            if (var5.isMarkedReusable()) {
               this.poolEntry.updateExpiry(var2, var4 != null ? var4 : TimeUnit.MILLISECONDS);
               if (this.log.isDebugEnabled()) {
                  String var9;
                  if (var2 > 0L) {
                     var9 = "for " + var2 + " " + var4;
                  } else {
                     var9 = "indefinitely";
                  }

                  this.log.debug("Connection can be kept alive " + var9);
               }
            }

            var5.detach();
            this.conn = null;
            if (this.poolEntry.isClosed()) {
               this.poolEntry = null;
            }

         }
      }
   }

   public void closeExpiredConnections() {
      synchronized(this){}
      this.assertNotShutdown();
      long var2 = System.currentTimeMillis();
      if (this.poolEntry != null && this.poolEntry.isExpired(var2)) {
         this.poolEntry.close();
         this.poolEntry.getTracker().reset();
      }

   }

   public void closeIdleConnections(long var1, TimeUnit var3) {
      Args.notNull(var3, "Time unit");
      synchronized(this){}
      this.assertNotShutdown();
      long var5 = var3.toMillis(var1);
      if (var5 < 0L) {
         var5 = 0L;
      }

      long var7 = System.currentTimeMillis() - var5;
      if (this.poolEntry != null && this.poolEntry.getUpdated() <= var7) {
         this.poolEntry.close();
         this.poolEntry.getTracker().reset();
      }

   }

   public void shutdown() {
      synchronized(this){}
      this.shutdown = true;
      if (this.poolEntry != null) {
         this.poolEntry.close();
      }

      this.poolEntry = null;
      this.conn = null;
   }
}
