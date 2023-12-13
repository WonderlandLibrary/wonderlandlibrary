package org.apache.http.impl.conn.tsccm;

import java.lang.ref.ReferenceQueue;
import java.util.concurrent.TimeUnit;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.AbstractPoolEntry;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
public class BasicPoolEntry extends AbstractPoolEntry {
   private final long created;
   private long updated;
   private final long validUntil;
   private long expiry;

   public BasicPoolEntry(ClientConnectionOperator var1, HttpRoute var2, ReferenceQueue var3) {
      super(var1, var2);
      Args.notNull(var2, "HTTP route");
      this.created = System.currentTimeMillis();
      this.validUntil = Long.MAX_VALUE;
      this.expiry = this.validUntil;
   }

   public BasicPoolEntry(ClientConnectionOperator var1, HttpRoute var2) {
      this(var1, var2, -1L, TimeUnit.MILLISECONDS);
   }

   public BasicPoolEntry(ClientConnectionOperator var1, HttpRoute var2, long var3, TimeUnit var5) {
      super(var1, var2);
      Args.notNull(var2, "HTTP route");
      this.created = System.currentTimeMillis();
      if (var3 > 0L) {
         this.validUntil = this.created + var5.toMillis(var3);
      } else {
         this.validUntil = Long.MAX_VALUE;
      }

      this.expiry = this.validUntil;
   }

   protected final OperatedClientConnection getConnection() {
      return super.connection;
   }

   protected final HttpRoute getPlannedRoute() {
      return super.route;
   }

   protected final BasicPoolEntryRef getWeakRef() {
      return null;
   }

   protected void shutdownEntry() {
      super.shutdownEntry();
   }

   public long getCreated() {
      return this.created;
   }

   public long getUpdated() {
      return this.updated;
   }

   public long getExpiry() {
      return this.expiry;
   }

   public long getValidUntil() {
      return this.validUntil;
   }

   public void updateExpiry(long var1, TimeUnit var3) {
      this.updated = System.currentTimeMillis();
      long var4;
      if (var1 > 0L) {
         var4 = this.updated + var3.toMillis(var1);
      } else {
         var4 = Long.MAX_VALUE;
      }

      this.expiry = Math.min(this.validUntil, var4);
   }

   public boolean isExpired(long var1) {
      return var1 >= this.expiry;
   }
}
