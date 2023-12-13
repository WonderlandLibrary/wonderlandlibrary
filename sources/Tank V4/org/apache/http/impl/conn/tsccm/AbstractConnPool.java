package org.apache.http.impl.conn.tsccm;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.IdleConnectionHandler;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
public abstract class AbstractConnPool {
   private final Log log = LogFactory.getLog(this.getClass());
   protected final Lock poolLock = new ReentrantLock();
   @GuardedBy("poolLock")
   protected Set leasedConnections = new HashSet();
   @GuardedBy("poolLock")
   protected int numConnections;
   protected volatile boolean isShutDown;
   protected Set issuedConnections;
   protected ReferenceQueue refQueue;
   protected IdleConnectionHandler idleConnHandler = new IdleConnectionHandler();

   protected AbstractConnPool() {
   }

   public void enableConnectionGC() throws IllegalStateException {
   }

   public final BasicPoolEntry getEntry(HttpRoute var1, Object var2, long var3, TimeUnit var5) throws ConnectionPoolTimeoutException, InterruptedException {
      return this.requestPoolEntry(var1, var2).getPoolEntry(var3, var5);
   }

   public abstract PoolEntryRequest requestPoolEntry(HttpRoute var1, Object var2);

   public abstract void freeEntry(BasicPoolEntry var1, boolean var2, long var3, TimeUnit var5);

   public void handleReference(Reference var1) {
   }

   protected abstract void handleLostEntry(HttpRoute var1);

   public void closeIdleConnections(long var1, TimeUnit var3) {
      Args.notNull(var3, "Time unit");
      this.poolLock.lock();
      this.idleConnHandler.closeIdleConnections(var3.toMillis(var1));
      this.poolLock.unlock();
   }

   public void closeExpiredConnections() {
      this.poolLock.lock();
      this.idleConnHandler.closeExpiredConnections();
      this.poolLock.unlock();
   }

   public abstract void deleteClosedConnections();

   public void shutdown() {
      this.poolLock.lock();
      if (this.isShutDown) {
         this.poolLock.unlock();
      } else {
         Iterator var1 = this.leasedConnections.iterator();

         while(var1.hasNext()) {
            BasicPoolEntry var2 = (BasicPoolEntry)var1.next();
            var1.remove();
            this.closeConnection(var2.getConnection());
         }

         this.idleConnHandler.removeAll();
         this.isShutDown = true;
         this.poolLock.unlock();
      }
   }

   protected void closeConnection(OperatedClientConnection var1) {
      if (var1 != null) {
         try {
            var1.close();
         } catch (IOException var3) {
            this.log.debug("I/O error closing connection", var3);
         }
      }

   }
}
