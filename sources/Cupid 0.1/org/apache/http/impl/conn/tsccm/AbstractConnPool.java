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

@Deprecated
public abstract class AbstractConnPool {
  private final Log log = LogFactory.getLog(getClass());
  
  @GuardedBy("poolLock")
  protected Set<BasicPoolEntry> leasedConnections = new HashSet<BasicPoolEntry>();
  
  protected IdleConnectionHandler idleConnHandler = new IdleConnectionHandler();
  
  protected final Lock poolLock = new ReentrantLock();
  
  @GuardedBy("poolLock")
  protected int numConnections;
  
  protected volatile boolean isShutDown;
  
  protected Set<BasicPoolEntryRef> issuedConnections;
  
  protected ReferenceQueue<Object> refQueue;
  
  public void enableConnectionGC() throws IllegalStateException {}
  
  public final BasicPoolEntry getEntry(HttpRoute route, Object state, long timeout, TimeUnit tunit) throws ConnectionPoolTimeoutException, InterruptedException {
    return requestPoolEntry(route, state).getPoolEntry(timeout, tunit);
  }
  
  public abstract PoolEntryRequest requestPoolEntry(HttpRoute paramHttpRoute, Object paramObject);
  
  public abstract void freeEntry(BasicPoolEntry paramBasicPoolEntry, boolean paramBoolean, long paramLong, TimeUnit paramTimeUnit);
  
  public void handleReference(Reference<?> ref) {}
  
  protected abstract void handleLostEntry(HttpRoute paramHttpRoute);
  
  public void closeIdleConnections(long idletime, TimeUnit tunit) {
    Args.notNull(tunit, "Time unit");
    this.poolLock.lock();
    try {
      this.idleConnHandler.closeIdleConnections(tunit.toMillis(idletime));
    } finally {
      this.poolLock.unlock();
    } 
  }
  
  public void closeExpiredConnections() {
    this.poolLock.lock();
    try {
      this.idleConnHandler.closeExpiredConnections();
    } finally {
      this.poolLock.unlock();
    } 
  }
  
  public abstract void deleteClosedConnections();
  
  public void shutdown() {
    this.poolLock.lock();
    try {
      if (this.isShutDown)
        return; 
      Iterator<BasicPoolEntry> iter = this.leasedConnections.iterator();
      while (iter.hasNext()) {
        BasicPoolEntry entry = iter.next();
        iter.remove();
        closeConnection(entry.getConnection());
      } 
      this.idleConnHandler.removeAll();
      this.isShutDown = true;
    } finally {
      this.poolLock.unlock();
    } 
  }
  
  protected void closeConnection(OperatedClientConnection conn) {
    if (conn != null)
      try {
        conn.close();
      } catch (IOException ex) {
        this.log.debug("I/O error closing connection", ex);
      }  
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\conn\tsccm\AbstractConnPool.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */