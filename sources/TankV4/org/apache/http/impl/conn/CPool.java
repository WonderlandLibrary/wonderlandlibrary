package org.apache.http.impl.conn;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.AbstractConnPool;
import org.apache.http.pool.ConnFactory;
import org.apache.http.pool.PoolEntry;

@ThreadSafe
class CPool extends AbstractConnPool {
   private static final AtomicLong COUNTER = new AtomicLong();
   private final Log log = LogFactory.getLog(CPool.class);
   private final long timeToLive;
   private final TimeUnit tunit;

   public CPool(ConnFactory var1, int var2, int var3, long var4, TimeUnit var6) {
      super(var1, var2, var3);
      this.timeToLive = var4;
      this.tunit = var6;
   }

   protected CPoolEntry createEntry(HttpRoute var1, ManagedHttpClientConnection var2) {
      String var3 = Long.toString(COUNTER.getAndIncrement());
      return new CPoolEntry(this.log, var3, var1, var2, this.timeToLive, this.tunit);
   }

   protected PoolEntry createEntry(Object var1, Object var2) {
      return this.createEntry((HttpRoute)var1, (ManagedHttpClientConnection)var2);
   }
}
