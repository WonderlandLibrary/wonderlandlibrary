package org.apache.http.pool;

import java.util.concurrent.TimeUnit;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.util.Args;

@ThreadSafe
public abstract class PoolEntry {
   private final String id;
   private final Object route;
   private final Object conn;
   private final long created;
   private final long validUnit;
   @GuardedBy("this")
   private long updated;
   @GuardedBy("this")
   private long expiry;
   private volatile Object state;

   public PoolEntry(String var1, Object var2, Object var3, long var4, TimeUnit var6) {
      Args.notNull(var2, "Route");
      Args.notNull(var3, "Connection");
      Args.notNull(var6, "Time unit");
      this.id = var1;
      this.route = var2;
      this.conn = var3;
      this.created = System.currentTimeMillis();
      if (var4 > 0L) {
         this.validUnit = this.created + var6.toMillis(var4);
      } else {
         this.validUnit = Long.MAX_VALUE;
      }

      this.expiry = this.validUnit;
   }

   public PoolEntry(String var1, Object var2, Object var3) {
      this(var1, var2, var3, 0L, TimeUnit.MILLISECONDS);
   }

   public String getId() {
      return this.id;
   }

   public Object getRoute() {
      return this.route;
   }

   public Object getConnection() {
      return this.conn;
   }

   public long getCreated() {
      return this.created;
   }

   public long getValidUnit() {
      return this.validUnit;
   }

   public Object getState() {
      return this.state;
   }

   public void setState(Object var1) {
      this.state = var1;
   }

   public synchronized long getUpdated() {
      return this.updated;
   }

   public synchronized long getExpiry() {
      return this.expiry;
   }

   public synchronized void updateExpiry(long var1, TimeUnit var3) {
      Args.notNull(var3, "Time unit");
      this.updated = System.currentTimeMillis();
      long var4;
      if (var1 > 0L) {
         var4 = this.updated + var3.toMillis(var1);
      } else {
         var4 = Long.MAX_VALUE;
      }

      this.expiry = Math.min(var4, this.validUnit);
   }

   public synchronized boolean isExpired(long var1) {
      return var1 >= this.expiry;
   }

   public abstract void close();

   public abstract boolean isClosed();

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[id:");
      var1.append(this.id);
      var1.append("][route:");
      var1.append(this.route);
      var1.append("][state:");
      var1.append(this.state);
      var1.append("]");
      return var1.toString();
   }
}
