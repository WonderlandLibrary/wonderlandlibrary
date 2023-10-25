package org.apache.http.pool;

import java.util.concurrent.TimeUnit;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.util.Args;

@ThreadSafe
public abstract class PoolEntry<T, C> {
  private final String id;
  
  private final T route;
  
  private final C conn;
  
  private final long created;
  
  private final long validUnit;
  
  @GuardedBy("this")
  private long updated;
  
  @GuardedBy("this")
  private long expiry;
  
  private volatile Object state;
  
  public PoolEntry(String id, T route, C conn, long timeToLive, TimeUnit tunit) {
    Args.notNull(route, "Route");
    Args.notNull(conn, "Connection");
    Args.notNull(tunit, "Time unit");
    this.id = id;
    this.route = route;
    this.conn = conn;
    this.created = System.currentTimeMillis();
    if (timeToLive > 0L) {
      this.validUnit = this.created + tunit.toMillis(timeToLive);
    } else {
      this.validUnit = Long.MAX_VALUE;
    } 
    this.expiry = this.validUnit;
  }
  
  public PoolEntry(String id, T route, C conn) {
    this(id, route, conn, 0L, TimeUnit.MILLISECONDS);
  }
  
  public String getId() {
    return this.id;
  }
  
  public T getRoute() {
    return this.route;
  }
  
  public C getConnection() {
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
  
  public void setState(Object state) {
    this.state = state;
  }
  
  public synchronized long getUpdated() {
    return this.updated;
  }
  
  public synchronized long getExpiry() {
    return this.expiry;
  }
  
  public synchronized void updateExpiry(long time, TimeUnit tunit) {
    long newExpiry;
    Args.notNull(tunit, "Time unit");
    this.updated = System.currentTimeMillis();
    if (time > 0L) {
      newExpiry = this.updated + tunit.toMillis(time);
    } else {
      newExpiry = Long.MAX_VALUE;
    } 
    this.expiry = Math.min(newExpiry, this.validUnit);
  }
  
  public synchronized boolean isExpired(long now) {
    return (now >= this.expiry);
  }
  
  public abstract void close();
  
  public abstract boolean isClosed();
  
  public String toString() {
    StringBuilder buffer = new StringBuilder();
    buffer.append("[id:");
    buffer.append(this.id);
    buffer.append("][route:");
    buffer.append(this.route);
    buffer.append("][state:");
    buffer.append(this.state);
    buffer.append("]");
    return buffer.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\pool\PoolEntry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */