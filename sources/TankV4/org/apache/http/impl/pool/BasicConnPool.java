package org.apache.http.impl.pool;

import java.util.concurrent.atomic.AtomicLong;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.params.HttpParams;
import org.apache.http.pool.AbstractConnPool;
import org.apache.http.pool.ConnFactory;
import org.apache.http.pool.PoolEntry;

@ThreadSafe
public class BasicConnPool extends AbstractConnPool {
   private static final AtomicLong COUNTER = new AtomicLong();

   public BasicConnPool(ConnFactory var1) {
      super(var1, 2, 20);
   }

   /** @deprecated */
   @Deprecated
   public BasicConnPool(HttpParams var1) {
      super(new BasicConnFactory(var1), 2, 20);
   }

   public BasicConnPool(SocketConfig var1, ConnectionConfig var2) {
      super(new BasicConnFactory(var1, var2), 2, 20);
   }

   public BasicConnPool() {
      super(new BasicConnFactory(SocketConfig.DEFAULT, ConnectionConfig.DEFAULT), 2, 20);
   }

   protected BasicPoolEntry createEntry(HttpHost var1, HttpClientConnection var2) {
      return new BasicPoolEntry(Long.toString(COUNTER.getAndIncrement()), var1, var2);
   }

   protected PoolEntry createEntry(Object var1, Object var2) {
      return this.createEntry((HttpHost)var1, (HttpClientConnection)var2);
   }
}
