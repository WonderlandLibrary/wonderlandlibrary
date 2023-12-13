package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.pool.PoolEntry;

/** @deprecated */
@Deprecated
class HttpPoolEntry extends PoolEntry {
   private final Log log;
   private final RouteTracker tracker;

   public HttpPoolEntry(Log var1, String var2, HttpRoute var3, OperatedClientConnection var4, long var5, TimeUnit var7) {
      super(var2, var3, var4, var5, var7);
      this.log = var1;
      this.tracker = new RouteTracker(var3);
   }

   public boolean isExpired(long var1) {
      boolean var3 = super.isExpired(var1);
      if (var3 && this.log.isDebugEnabled()) {
         this.log.debug("Connection " + this + " expired @ " + new Date(this.getExpiry()));
      }

      return var3;
   }

   RouteTracker getTracker() {
      return this.tracker;
   }

   HttpRoute getPlannedRoute() {
      return (HttpRoute)this.getRoute();
   }

   HttpRoute getEffectiveRoute() {
      return this.tracker.toRoute();
   }

   public boolean isClosed() {
      OperatedClientConnection var1 = (OperatedClientConnection)this.getConnection();
      return !var1.isOpen();
   }

   public void close() {
      OperatedClientConnection var1 = (OperatedClientConnection)this.getConnection();

      try {
         var1.close();
      } catch (IOException var3) {
         this.log.debug("I/O error closing connection", var3);
      }

   }
}
