package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.Log;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.AbstractConnPool;
import org.apache.http.pool.ConnFactory;
import org.apache.http.pool.PoolEntry;

/** @deprecated */
@Deprecated
class HttpConnPool extends AbstractConnPool {
   private static final AtomicLong COUNTER = new AtomicLong();
   private final Log log;
   private final long timeToLive;
   private final TimeUnit tunit;

   public HttpConnPool(Log var1, ClientConnectionOperator var2, int var3, int var4, long var5, TimeUnit var7) {
      super(new HttpConnPool.InternalConnFactory(var2), var3, var4);
      this.log = var1;
      this.timeToLive = var5;
      this.tunit = var7;
   }

   protected HttpPoolEntry createEntry(HttpRoute var1, OperatedClientConnection var2) {
      String var3 = Long.toString(COUNTER.getAndIncrement());
      return new HttpPoolEntry(this.log, var3, var1, var2, this.timeToLive, this.tunit);
   }

   protected PoolEntry createEntry(Object var1, Object var2) {
      return this.createEntry((HttpRoute)var1, (OperatedClientConnection)var2);
   }

   static class InternalConnFactory implements ConnFactory {
      private final ClientConnectionOperator connOperator;

      InternalConnFactory(ClientConnectionOperator var1) {
         this.connOperator = var1;
      }

      public OperatedClientConnection create(HttpRoute var1) throws IOException {
         return this.connOperator.createConnection();
      }

      public Object create(Object var1) throws IOException {
         return this.create((HttpRoute)var1);
      }
   }
}
