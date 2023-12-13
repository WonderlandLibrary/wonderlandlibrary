package org.apache.http.pool;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@ThreadSafe
public abstract class AbstractConnPool implements ConnPool, ConnPoolControl {
   private final Lock lock;
   private final ConnFactory connFactory;
   private final Map routeToPool;
   private final Set leased;
   private final LinkedList available;
   private final LinkedList pending;
   private final Map maxPerRoute;
   private volatile boolean isShutDown;
   private volatile int defaultMaxPerRoute;
   private volatile int maxTotal;

   public AbstractConnPool(ConnFactory var1, int var2, int var3) {
      this.connFactory = (ConnFactory)Args.notNull(var1, "Connection factory");
      this.defaultMaxPerRoute = Args.notNegative(var2, "Max per route value");
      this.maxTotal = Args.notNegative(var3, "Max total value");
      this.lock = new ReentrantLock();
      this.routeToPool = new HashMap();
      this.leased = new HashSet();
      this.available = new LinkedList();
      this.pending = new LinkedList();
      this.maxPerRoute = new HashMap();
   }

   protected abstract PoolEntry createEntry(Object var1, Object var2);

   protected void onLease(PoolEntry var1) {
   }

   protected void onRelease(PoolEntry var1) {
   }

   public boolean isShutdown() {
      return this.isShutDown;
   }

   public void shutdown() throws IOException {
      if (!this.isShutDown) {
         this.isShutDown = true;
         this.lock.lock();
         Iterator var1 = this.available.iterator();

         PoolEntry var2;
         while(var1.hasNext()) {
            var2 = (PoolEntry)var1.next();
            var2.close();
         }

         var1 = this.leased.iterator();

         while(var1.hasNext()) {
            var2 = (PoolEntry)var1.next();
            var2.close();
         }

         var1 = this.routeToPool.values().iterator();

         while(var1.hasNext()) {
            RouteSpecificPool var4 = (RouteSpecificPool)var1.next();
            var4.shutdown();
         }

         this.routeToPool.clear();
         this.leased.clear();
         this.available.clear();
         this.lock.unlock();
      }
   }

   private RouteSpecificPool getPool(Object var1) {
      RouteSpecificPool var2 = (RouteSpecificPool)this.routeToPool.get(var1);
      if (var2 == null) {
         var2 = new RouteSpecificPool(this, var1, var1) {
            final Object val$route;
            final AbstractConnPool this$0;

            {
               this.this$0 = var1;
               this.val$route = var3;
            }

            protected PoolEntry createEntry(Object var1) {
               return this.this$0.createEntry(this.val$route, var1);
            }
         };
         this.routeToPool.put(var1, var2);
      }

      return var2;
   }

   public Future lease(Object var1, Object var2, FutureCallback var3) {
      Args.notNull(var1, "Route");
      Asserts.check(!this.isShutDown, "Connection pool shut down");
      return new PoolEntryFuture(this, this.lock, var3, var1, var2) {
         final Object val$route;
         final Object val$state;
         final AbstractConnPool this$0;

         {
            this.this$0 = var1;
            this.val$route = var4;
            this.val$state = var5;
         }

         public PoolEntry getPoolEntry(long var1, TimeUnit var3) throws InterruptedException, TimeoutException, IOException {
            PoolEntry var4 = AbstractConnPool.access$000(this.this$0, this.val$route, this.val$state, var1, var3, this);
            this.this$0.onLease(var4);
            return var4;
         }

         public Object getPoolEntry(long var1, TimeUnit var3) throws IOException, InterruptedException, TimeoutException {
            return this.getPoolEntry(var1, var3);
         }
      };
   }

   public Future lease(Object var1, Object var2) {
      return this.lease(var1, var2, (FutureCallback)null);
   }

   private PoolEntry getPoolEntryBlocking(Object var1, Object var2, long var3, TimeUnit var5, PoolEntryFuture var6) throws IOException, InterruptedException, TimeoutException {
      Date var7 = null;
      if (var3 > 0L) {
         var7 = new Date(System.currentTimeMillis() + var5.toMillis(var3));
      }

      this.lock.lock();
      RouteSpecificPool var8 = this.getPool(var1);
      PoolEntry var9 = null;

      while(var9 == null) {
         Asserts.check(!this.isShutDown, "Connection pool shut down");

         while(true) {
            var9 = var8.getFree(var2);
            if (var9 == null || !var9.isClosed() && !var9.isExpired(System.currentTimeMillis())) {
               if (var9 != null) {
                  this.available.remove(var9);
                  this.leased.add(var9);
                  this.lock.unlock();
                  return var9;
               }

               int var10 = this.getMax(var1);
               int var11 = Math.max(0, var8.getAllocatedCount() + 1 - var10);
               int var12;
               if (var11 > 0) {
                  for(var12 = 0; var12 < var11; ++var12) {
                     PoolEntry var13 = var8.getLastUsed();
                     if (var13 == null) {
                        break;
                     }

                     var13.close();
                     this.available.remove(var13);
                     var8.remove(var13);
                  }
               }

               if (var8.getAllocatedCount() < var10) {
                  var12 = this.leased.size();
                  int var20 = Math.max(this.maxTotal - var12, 0);
                  if (var20 > 0) {
                     int var14 = this.available.size();
                     if (var14 > var20 - 1 && !this.available.isEmpty()) {
                        PoolEntry var15 = (PoolEntry)this.available.removeLast();
                        var15.close();
                        RouteSpecificPool var16 = this.getPool(var15.getRoute());
                        var16.remove(var15);
                     }

                     Object var21 = this.connFactory.create(var1);
                     var9 = var8.add(var21);
                     this.leased.add(var9);
                     this.lock.unlock();
                     return var9;
                  }
               }

               boolean var19 = false;
               var8.queue(var6);
               this.pending.add(var6);
               var19 = var6.await(var7);
               var8.unqueue(var6);
               this.pending.remove(var6);
               if (!var19 && var7 != null && var7.getTime() <= System.currentTimeMillis()) {
                  throw new TimeoutException("Timeout waiting for connection");
               }
               break;
            }

            var9.close();
            this.available.remove(var9);
            var8.free(var9, false);
         }
      }

      throw new TimeoutException("Timeout waiting for connection");
   }

   public void release(PoolEntry var1, boolean var2) {
      this.lock.lock();
      if (this.leased.remove(var1)) {
         RouteSpecificPool var3 = this.getPool(var1.getRoute());
         var3.free(var1, var2);
         if (var2 && !this.isShutDown) {
            this.available.addFirst(var1);
            this.onRelease(var1);
         } else {
            var1.close();
         }

         PoolEntryFuture var4 = var3.nextPending();
         if (var4 != null) {
            this.pending.remove(var4);
         } else {
            var4 = (PoolEntryFuture)this.pending.poll();
         }

         if (var4 != null) {
            var4.wakeup();
         }
      }

      this.lock.unlock();
   }

   private int getMax(Object var1) {
      Integer var2 = (Integer)this.maxPerRoute.get(var1);
      return var2 != null ? var2 : this.defaultMaxPerRoute;
   }

   public void setMaxTotal(int var1) {
      Args.notNegative(var1, "Max value");
      this.lock.lock();
      this.maxTotal = var1;
      this.lock.unlock();
   }

   public int getMaxTotal() {
      this.lock.lock();
      int var1 = this.maxTotal;
      this.lock.unlock();
      return var1;
   }

   public void setDefaultMaxPerRoute(int var1) {
      Args.notNegative(var1, "Max per route value");
      this.lock.lock();
      this.defaultMaxPerRoute = var1;
      this.lock.unlock();
   }

   public int getDefaultMaxPerRoute() {
      this.lock.lock();
      int var1 = this.defaultMaxPerRoute;
      this.lock.unlock();
      return var1;
   }

   public void setMaxPerRoute(Object var1, int var2) {
      Args.notNull(var1, "Route");
      Args.notNegative(var2, "Max per route value");
      this.lock.lock();
      this.maxPerRoute.put(var1, var2);
      this.lock.unlock();
   }

   public int getMaxPerRoute(Object var1) {
      Args.notNull(var1, "Route");
      this.lock.lock();
      int var2 = this.getMax(var1);
      this.lock.unlock();
      return var2;
   }

   public PoolStats getTotalStats() {
      this.lock.lock();
      PoolStats var1 = new PoolStats(this.leased.size(), this.pending.size(), this.available.size(), this.maxTotal);
      this.lock.unlock();
      return var1;
   }

   public PoolStats getStats(Object var1) {
      Args.notNull(var1, "Route");
      this.lock.lock();
      RouteSpecificPool var2 = this.getPool(var1);
      PoolStats var3 = new PoolStats(var2.getLeasedCount(), var2.getPendingCount(), var2.getAvailableCount(), this.getMax(var1));
      this.lock.unlock();
      return var3;
   }

   protected void enumAvailable(PoolEntryCallback var1) {
      this.lock.lock();
      Iterator var2 = this.available.iterator();

      while(var2.hasNext()) {
         PoolEntry var3 = (PoolEntry)var2.next();
         var1.process(var3);
         if (var3.isClosed()) {
            RouteSpecificPool var4 = this.getPool(var3.getRoute());
            var4.remove(var3);
            var2.remove();
         }
      }

      this.purgePoolMap();
      this.lock.unlock();
   }

   protected void enumLeased(PoolEntryCallback var1) {
      this.lock.lock();
      Iterator var2 = this.leased.iterator();

      while(var2.hasNext()) {
         PoolEntry var3 = (PoolEntry)var2.next();
         var1.process(var3);
      }

      this.lock.unlock();
   }

   private void purgePoolMap() {
      Iterator var1 = this.routeToPool.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         RouteSpecificPool var3 = (RouteSpecificPool)var2.getValue();
         if (var3.getPendingCount() + var3.getAllocatedCount() == 0) {
            var1.remove();
         }
      }

   }

   public void closeIdle(long var1, TimeUnit var3) {
      Args.notNull(var3, "Time unit");
      long var4 = var3.toMillis(var1);
      if (var4 < 0L) {
         var4 = 0L;
      }

      long var6 = System.currentTimeMillis() - var4;
      this.enumAvailable(new PoolEntryCallback(this, var6) {
         final long val$deadline;
         final AbstractConnPool this$0;

         {
            this.this$0 = var1;
            this.val$deadline = var2;
         }

         public void process(PoolEntry var1) {
            if (var1.getUpdated() <= this.val$deadline) {
               var1.close();
            }

         }
      });
   }

   public void closeExpired() {
      long var1 = System.currentTimeMillis();
      this.enumAvailable(new PoolEntryCallback(this, var1) {
         final long val$now;
         final AbstractConnPool this$0;

         {
            this.this$0 = var1;
            this.val$now = var2;
         }

         public void process(PoolEntry var1) {
            if (var1.isExpired(this.val$now)) {
               var1.close();
            }

         }
      });
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[leased: ");
      var1.append(this.leased);
      var1.append("][available: ");
      var1.append(this.available);
      var1.append("][pending: ");
      var1.append(this.pending);
      var1.append("]");
      return var1.toString();
   }

   public void release(Object var1, boolean var2) {
      this.release((PoolEntry)var1, var2);
   }

   static PoolEntry access$000(AbstractConnPool var0, Object var1, Object var2, long var3, TimeUnit var5, PoolEntryFuture var6) throws IOException, InterruptedException, TimeoutException {
      return var0.getPoolEntryBlocking(var1, var2, var3, var5, var6);
   }
}
