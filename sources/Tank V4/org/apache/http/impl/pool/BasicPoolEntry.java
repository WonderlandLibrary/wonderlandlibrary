package org.apache.http.impl.pool;

import java.io.IOException;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.pool.PoolEntry;

@ThreadSafe
public class BasicPoolEntry extends PoolEntry {
   public BasicPoolEntry(String var1, HttpHost var2, HttpClientConnection var3) {
      super(var1, var2, var3);
   }

   public void close() {
      try {
         ((HttpClientConnection)this.getConnection()).close();
      } catch (IOException var2) {
      }

   }

   public boolean isClosed() {
      return !((HttpClientConnection)this.getConnection()).isOpen();
   }
}
