package org.apache.http.impl.conn.tsccm;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

/** @deprecated */
@Deprecated
public class ConnPoolByRoute extends AbstractConnPool {
   private final Log log;
   private final Lock poolLock;
   protected final ClientConnectionOperator operator;
   protected final ConnPerRoute connPerRoute;
   protected final Set leasedConnections;
   protected final Queue freeConnections;
   protected final Queue waitingThreads;
   protected final Map routeToPool;
   private final long connTTL;
   private final TimeUnit connTTLTimeUnit;
   protected volatile boolean shutdown;
   protected volatile int maxTotalConnections;
   protected volatile int numConnections;

   public ConnPoolByRoute(ClientConnectionOperator var1, ConnPerRoute var2, int var3) {
      this(var1, var2, var3, -1L, TimeUnit.MILLISECONDS);
   }

   public ConnPoolByRoute(ClientConnectionOperator var1, ConnPerRoute var2, int var3, long var4, TimeUnit var6) {
      this.log = LogFactory.getLog(this.getClass());
      Args.notNull(var1, "Connection operator");
      Args.notNull(var2, "Connections per route");
      this.poolLock = super.poolLock;
      this.leasedConnections = super.leasedConnections;
      this.operator = var1;
      this.connPerRoute = var2;
      this.maxTotalConnections = var3;
      this.freeConnections = this.createFreeConnQueue();
      this.waitingThreads = this.createWaitingThreadQueue();
      this.routeToPool = this.createRouteToPoolMap();
      this.connTTL = var4;
      this.connTTLTimeUnit = var6;
   }

   protected Lock getLock() {
      return this.poolLock;
   }

   /** @deprecated */
   @Deprecated
   public ConnPoolByRoute(ClientConnectionOperator var1, HttpParams var2) {
      this(var1, ConnManagerParams.getMaxConnectionsPerRoute(var2), ConnManagerParams.getMaxTotalConnections(var2));
   }

   protected Queue createFreeConnQueue() {
      return new LinkedList();
   }

   protected Queue createWaitingThreadQueue() {
      return new LinkedList();
   }

   protected Map createRouteToPoolMap() {
      return new HashMap();
   }

   protected RouteSpecificPool newRouteSpecificPool(HttpRoute var1) {
      return new RouteSpecificPool(var1, this.connPerRoute);
   }

   protected WaitingThread newWaitingThread(Condition var1, RouteSpecificPool var2) {
      return new WaitingThread(var1, var2);
   }

   private void closeConnection(BasicPoolEntry var1) {
      OperatedClientConnection var2 = var1.getConnection();
      if (var2 != null) {
         try {
            var2.close();
         } catch (IOException var4) {
            this.log.debug("I/O error closing connection", var4);
         }
      }

   }

   protected RouteSpecificPool getRoutePool(HttpRoute var1, boolean var2) {
      RouteSpecificPool var3 = null;
      this.poolLock.lock();
      var3 = (RouteSpecificPool)this.routeToPool.get(var1);
      if (var3 == null && var2) {
         var3 = this.newRouteSpecificPool(var1);
         this.routeToPool.put(var1, var3);
      }

      this.poolLock.unlock();
      return var3;
   }

   public int getConnectionsInPool(HttpRoute var1) {
      this.poolLock.lock();
      RouteSpecificPool var2 = this.getRoutePool(var1, false);
      int var3 = var2 != null ? var2.getEntryCount() : 0;
      this.poolLock.unlock();
      return var3;
   }

   public int getConnectionsInPool() {
      this.poolLock.lock();
      int var1 = this.numConnections;
      this.poolLock.unlock();
      return var1;
   }

   public PoolEntryRequest requestPoolEntry(HttpRoute var1, Object var2) {
      WaitingThreadAborter var3 = new WaitingThreadAborter();
      return new PoolEntryRequest(this, var3, var1, var2) {
         final WaitingThreadAborter val$aborter;
         final HttpRoute val$route;
         final Object val$state;
         final ConnPoolByRoute this$0;

         {
            this.this$0 = var1;
            this.val$aborter = var2;
            this.val$route = var3;
            this.val$state = var4;
         }

         public void abortRequest() {
            ConnPoolByRoute.access$000(this.this$0).lock();
            this.val$aborter.abort();
            ConnPoolByRoute.access$000(this.this$0).unlock();
         }

         public BasicPoolEntry getPoolEntry(long var1, TimeUnit var3) throws InterruptedException, ConnectionPoolTimeoutException {
            return this.this$0.getEntryBlocking(this.val$route, this.val$state, var1, var3, this.val$aborter);
         }
      };
   }

   protected BasicPoolEntry getEntryBlocking(HttpRoute var1, Object var2, long var3, TimeUnit var5, WaitingThreadAborter var6) throws ConnectionPoolTimeoutException, InterruptedException {
      Date var7 = null;
      if (var3 > 0L) {
         var7 = new Date(System.currentTimeMillis() + var5.toMillis(var3));
      }

      BasicPoolEntry var8 = null;
      this.poolLock.lock();
      RouteSpecificPool var9 = this.getRoutePool(var1, true);
      WaitingThread var10 = null;

      while(var8 == null) {
         Asserts.check(!this.shutdown, "Connection pool shut down");
         if (this.log.isDebugEnabled()) {
            this.log.debug("[" + var1 + "] total kept alive: " + this.freeConnections.size() + ", total issued: " + this.leasedConnections.size() + ", total allocated: " + this.numConnections + " out of " + this.maxTotalConnections);
         }

         var8 = this.getFreeEntry(var9, var2);
         if (var8 != null) {
            break;
         }

         boolean var11 = var9.getCapacity() > 0;
         if (this.log.isDebugEnabled()) {
            this.log.debug("Available capacity: " + var9.getCapacity() + " out of " + var9.getMaxEntries() + " [" + var1 + "][" + var2 + "]");
         }

         if (var11 && this.numConnections < this.maxTotalConnections) {
            var8 = this.createEntry(var9, this.operator);
         } else if (var11 && !this.freeConnections.isEmpty()) {
            this.deleteLeastUsedEntry();
            var9 = this.getRoutePool(var1, true);
            var8 = this.createEntry(var9, this.operator);
         } else {
            if (this.log.isDebugEnabled()) {
               this.log.debug("Need to wait for connection [" + var1 + "][" + var2 + "]");
            }

            if (var10 == null) {
               var10 = this.newWaitingThread(this.poolLock.newCondition(), var9);
               var6.setWaitingThread(var10);
            }

            boolean var12 = false;
            var9.queueThread(var10);
            this.waitingThreads.add(var10);
            var12 = var10.await(var7);
            var9.removeThread(var10);
            this.waitingThreads.remove(var10);
            if (!var12 && var7 != null && var7.getTime() <= System.currentTimeMillis()) {
               throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
            }
         }
      }

      this.poolLock.unlock();
      return var8;
   }

   public void freeEntry(BasicPoolEntry var1, boolean var2, long var3, TimeUnit var5) {
      HttpRoute var6 = var1.getPlannedRoute();
      if (this.log.isDebugEnabled()) {
         this.log.debug("Releasing connection [" + var6 + "][" + var1.getState() + "]");
      }

      this.poolLock.lock();
      if (this.shutdown) {
         this.closeConnection(var1);
         this.poolLock.unlock();
      } else {
         this.leasedConnections.remove(var1);
         RouteSpecificPool var7 = this.getRoutePool(var6, true);
         if (var2 && var7.getCapacity() >= 0) {
            if (this.log.isDebugEnabled()) {
               String var8;
               if (var3 > 0L) {
                  var8 = "for " + var3 + " " + var5;
               } else {
                  var8 = "indefinitely";
               }

               this.log.debug("Pooling connection [" + var6 + "][" + var1.getState() + "]; keep alive " + var8);
            }

            var7.freeEntry(var1);
            var1.updateExpiry(var3, var5);
            this.freeConnections.add(var1);
         } else {
            this.closeConnection(var1);
            var7.dropEntry();
            --this.numConnections;
         }

         this.notifyWaitingThread(var7);
         this.poolLock.unlock();
      }
   }

   protected BasicPoolEntry getFreeEntry(RouteSpecificPool var1, Object var2) {
      BasicPoolEntry var3 = null;
      this.poolLock.lock();
      boolean var4 = false;

      while(!var4) {
         var3 = var1.allocEntry(var2);
         if (var3 != null) {
            if (this.log.isDebugEnabled()) {
               this.log.debug("Getting free connection [" + var1.getRoute() + "][" + var2 + "]");
            }

            this.freeConnections.remove(var3);
            if (var3.isExpired(System.currentTimeMillis())) {
               if (this.log.isDebugEnabled()) {
                  this.log.debug("Closing expired free connection [" + var1.getRoute() + "][" + var2 + "]");
               }

               this.closeConnection(var3);
               var1.dropEntry();
               --this.numConnections;
            } else {
               this.leasedConnections.add(var3);
               var4 = true;
            }
         } else {
            var4 = true;
            if (this.log.isDebugEnabled()) {
               this.log.debug("No free connections [" + var1.getRoute() + "][" + var2 + "]");
            }
         }
      }

      this.poolLock.unlock();
      return var3;
   }

   protected BasicPoolEntry createEntry(RouteSpecificPool var1, ClientConnectionOperator var2) {
      if (this.log.isDebugEnabled()) {
         this.log.debug("Creating new connection [" + var1.getRoute() + "]");
      }

      BasicPoolEntry var3 = new BasicPoolEntry(var2, var1.getRoute(), this.connTTL, this.connTTLTimeUnit);
      this.poolLock.lock();
      var1.createdEntry(var3);
      ++this.numConnections;
      this.leasedConnections.add(var3);
      this.poolLock.unlock();
      return var3;
   }

   protected void deleteEntry(BasicPoolEntry var1) {
      HttpRoute var2 = var1.getPlannedRoute();
      if (this.log.isDebugEnabled()) {
         this.log.debug("Deleting connection [" + var2 + "][" + var1.getState() + "]");
      }

      this.poolLock.lock();
      this.closeConnection(var1);
      RouteSpecificPool var3 = this.getRoutePool(var2, true);
      var3.deleteEntry(var1);
      --this.numConnections;
      if (var3.isUnused()) {
         this.routeToPool.remove(var2);
      }

      this.poolLock.unlock();
   }

   protected void deleteLeastUsedEntry() {
      this.poolLock.lock();
      BasicPoolEntry var1 = (BasicPoolEntry)this.freeConnections.remove();
      if (var1 != null) {
         this.deleteEntry(var1);
      } else if (this.log.isDebugEnabled()) {
         this.log.debug("No free connection to delete");
      }

      this.poolLock.unlock();
   }

   protected void handleLostEntry(HttpRoute var1) {
      this.poolLock.lock();
      RouteSpecificPool var2 = this.getRoutePool(var1, true);
      var2.dropEntry();
      if (var2.isUnused()) {
         this.routeToPool.remove(var1);
      }

      --this.numConnections;
      this.notifyWaitingThread(var2);
      this.poolLock.unlock();
   }

   protected void notifyWaitingThread(RouteSpecificPool var1) {
      WaitingThread var2 = null;
      this.poolLock.lock();
      if (var1 != null && var1.hasThread()) {
         if (this.log.isDebugEnabled()) {
            this.log.debug("Notifying thread waiting on pool [" + var1.getRoute() + "]");
         }

         var2 = var1.nextThread();
      } else if (!this.waitingThreads.isEmpty()) {
         if (this.log.isDebugEnabled()) {
            this.log.debug("Notifying thread waiting on any pool");
         }

         var2 = (WaitingThread)this.waitingThreads.remove();
      } else if (this.log.isDebugEnabled()) {
         this.log.debug("Notifying no-one, there are no waiting threads");
      }

      if (var2 != null) {
         var2.wakeup();
      }

      this.poolLock.unlock();
   }

   public void deleteClosedConnections() {
      this.poolLock.lock();
      Iterator var1 = this.freeConnections.iterator();

      while(var1.hasNext()) {
         BasicPoolEntry var2 = (BasicPoolEntry)var1.next();
         if (!var2.getConnection().isOpen()) {
            var1.remove();
            this.deleteEntry(var2);
         }
      }

      this.poolLock.unlock();
   }

   public void closeIdleConnections(long var1, TimeUnit var3) {
      Args.notNull(var3, "Time unit");
      long var4 = var1 > 0L ? var1 : 0L;
      if (this.log.isDebugEnabled()) {
         this.log.debug("Closing connections idle longer than " + var4 + " " + var3);
      }

      long var6 = System.currentTimeMillis() - var3.toMillis(var4);
      this.poolLock.lock();
      Iterator var8 = this.freeConnections.iterator();

      while(var8.hasNext()) {
         BasicPoolEntry var9 = (BasicPoolEntry)var8.next();
         if (var9.getUpdated() <= var6) {
            if (this.log.isDebugEnabled()) {
               this.log.debug("Closing connection last used @ " + new Date(var9.getUpdated()));
            }

            var8.remove();
            this.deleteEntry(var9);
         }
      }

      this.poolLock.unlock();
   }

   public void closeExpiredConnections() {
      this.log.debug("Closing expired connections");
      long var1 = System.currentTimeMillis();
      this.poolLock.lock();
      Iterator var3 = this.freeConnections.iterator();

      while(var3.hasNext()) {
         BasicPoolEntry var4 = (BasicPoolEntry)var3.next();
         if (var4.isExpired(var1)) {
            if (this.log.isDebugEnabled()) {
               this.log.debug("Closing connection expired @ " + new Date(var4.getExpiry()));
            }

            var3.remove();
            this.deleteEntry(var4);
         }
      }

      this.poolLock.unlock();
   }

   public void shutdown() {
      this.poolLock.lock();
      if (this.shutdown) {
         this.poolLock.unlock();
      } else {
         this.shutdown = true;
         Iterator var1 = this.leasedConnections.iterator();

         while(var1.hasNext()) {
            BasicPoolEntry var2 = (BasicPoolEntry)var1.next();
            var1.remove();
            this.closeConnection(var2);
         }

         BasicPoolEntry var3;
         for(Iterator var6 = this.freeConnections.iterator(); var6.hasNext(); this.closeConnection(var3)) {
            var3 = (BasicPoolEntry)var6.next();
            var6.remove();
            if (this.log.isDebugEnabled()) {
               this.log.debug("Closing connection [" + var3.getPlannedRoute() + "][" + var3.getState() + "]");
            }
         }

         Iterator var7 = this.waitingThreads.iterator();

         while(var7.hasNext()) {
            WaitingThread var4 = (WaitingThread)var7.next();
            var7.remove();
            var4.wakeup();
         }

         this.routeToPool.clear();
         this.poolLock.unlock();
      }
   }

   public void setMaxTotalConnections(int var1) {
      this.poolLock.lock();
      this.maxTotalConnections = var1;
      this.poolLock.unlock();
   }

   public int getMaxTotalConnections() {
      return this.maxTotalConnections;
   }

   static Lock access$000(ConnPoolByRoute var0) {
      return var0.poolLock;
   }
}
