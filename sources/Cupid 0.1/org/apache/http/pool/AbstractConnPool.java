package org.apache.http.pool;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@ThreadSafe
public abstract class AbstractConnPool<T, C, E extends PoolEntry<T, C>> implements ConnPool<T, E>, ConnPoolControl<T> {
  private final Lock lock;
  
  private final ConnFactory<T, C> connFactory;
  
  private final Map<T, RouteSpecificPool<T, C, E>> routeToPool;
  
  private final Set<E> leased;
  
  private final LinkedList<E> available;
  
  private final LinkedList<PoolEntryFuture<E>> pending;
  
  private final Map<T, Integer> maxPerRoute;
  
  private volatile boolean isShutDown;
  
  private volatile int defaultMaxPerRoute;
  
  private volatile int maxTotal;
  
  public AbstractConnPool(ConnFactory<T, C> connFactory, int defaultMaxPerRoute, int maxTotal) {
    this.connFactory = (ConnFactory<T, C>)Args.notNull(connFactory, "Connection factory");
    this.defaultMaxPerRoute = Args.notNegative(defaultMaxPerRoute, "Max per route value");
    this.maxTotal = Args.notNegative(maxTotal, "Max total value");
    this.lock = new ReentrantLock();
    this.routeToPool = new HashMap<T, RouteSpecificPool<T, C, E>>();
    this.leased = new HashSet<E>();
    this.available = new LinkedList<E>();
    this.pending = new LinkedList<PoolEntryFuture<E>>();
    this.maxPerRoute = new HashMap<T, Integer>();
  }
  
  protected void onLease(E entry) {}
  
  protected void onRelease(E entry) {}
  
  public boolean isShutdown() {
    return this.isShutDown;
  }
  
  public void shutdown() throws IOException {
    if (this.isShutDown)
      return; 
    this.isShutDown = true;
    this.lock.lock();
    try {
      for (PoolEntry poolEntry : this.available)
        poolEntry.close(); 
      for (PoolEntry poolEntry : this.leased)
        poolEntry.close(); 
      for (RouteSpecificPool<T, C, E> pool : this.routeToPool.values())
        pool.shutdown(); 
      this.routeToPool.clear();
      this.leased.clear();
      this.available.clear();
    } finally {
      this.lock.unlock();
    } 
  }
  
  private RouteSpecificPool<T, C, E> getPool(final T route) {
    RouteSpecificPool<T, C, E> pool = this.routeToPool.get(route);
    if (pool == null) {
      pool = new RouteSpecificPool<T, C, E>(route) {
          protected E createEntry(C conn) {
            return AbstractConnPool.this.createEntry(route, conn);
          }
        };
      this.routeToPool.put(route, pool);
    } 
    return pool;
  }
  
  public Future<E> lease(final T route, final Object state, FutureCallback<E> callback) {
    Args.notNull(route, "Route");
    Asserts.check(!this.isShutDown, "Connection pool shut down");
    return new PoolEntryFuture<E>(this.lock, callback) {
        public E getPoolEntry(long timeout, TimeUnit tunit) throws InterruptedException, TimeoutException, IOException {
          PoolEntry poolEntry = (PoolEntry)AbstractConnPool.this.getPoolEntryBlocking((T)route, state, timeout, tunit, this);
          AbstractConnPool.this.onLease(poolEntry);
          return (E)poolEntry;
        }
      };
  }
  
  public Future<E> lease(T route, Object state) {
    return lease(route, state, null);
  }
  
  private E getPoolEntryBlocking(T route, Object state, long timeout, TimeUnit tunit, PoolEntryFuture<E> future) throws IOException, InterruptedException, TimeoutException {
    Date deadline = null;
    if (timeout > 0L)
      deadline = new Date(System.currentTimeMillis() + tunit.toMillis(timeout)); 
    this.lock.lock();
    try {
      RouteSpecificPool<T, C, E> pool = getPool(route);
      E entry = null;
      while (entry == null) {
        Asserts.check(!this.isShutDown, "Connection pool shut down");
        while (true) {
          entry = pool.getFree(state);
          if (entry == null)
            break; 
          if (entry.isClosed() || entry.isExpired(System.currentTimeMillis())) {
            entry.close();
            this.available.remove(entry);
            pool.free(entry, false);
            continue;
          } 
          break;
        } 
        if (entry != null) {
          this.available.remove(entry);
          this.leased.add(entry);
          return entry;
        } 
        int maxPerRoute = getMax(route);
        int excess = Math.max(0, pool.getAllocatedCount() + 1 - maxPerRoute);
        if (excess > 0)
          for (int i = 0; i < excess; i++) {
            E lastUsed = pool.getLastUsed();
            if (lastUsed == null)
              break; 
            lastUsed.close();
            this.available.remove(lastUsed);
            pool.remove(lastUsed);
          }  
        if (pool.getAllocatedCount() < maxPerRoute) {
          int totalUsed = this.leased.size();
          int freeCapacity = Math.max(this.maxTotal - totalUsed, 0);
          if (freeCapacity > 0) {
            int totalAvailable = this.available.size();
            if (totalAvailable > freeCapacity - 1 && 
              !this.available.isEmpty()) {
              PoolEntry poolEntry = (PoolEntry)this.available.removeLast();
              poolEntry.close();
              RouteSpecificPool<T, C, E> otherpool = getPool((T)poolEntry.getRoute());
              otherpool.remove((E)poolEntry);
            } 
            C conn = this.connFactory.create(route);
            entry = pool.add(conn);
            this.leased.add(entry);
            return entry;
          } 
        } 
        boolean success = false;
        try {
          pool.queue(future);
          this.pending.add(future);
          success = future.await(deadline);
        } finally {
          pool.unqueue(future);
          this.pending.remove(future);
        } 
        if (!success && deadline != null && deadline.getTime() <= System.currentTimeMillis())
          break; 
      } 
      throw new TimeoutException("Timeout waiting for connection");
    } finally {
      this.lock.unlock();
    } 
  }
  
  public void release(E entry, boolean reusable) {
    this.lock.lock();
    try {
      if (this.leased.remove(entry)) {
        RouteSpecificPool<T, C, E> pool = getPool((T)entry.getRoute());
        pool.free(entry, reusable);
        if (reusable && !this.isShutDown) {
          this.available.addFirst(entry);
          onRelease(entry);
        } else {
          entry.close();
        } 
        PoolEntryFuture<E> future = pool.nextPending();
        if (future != null) {
          this.pending.remove(future);
        } else {
          future = this.pending.poll();
        } 
        if (future != null)
          future.wakeup(); 
      } 
    } finally {
      this.lock.unlock();
    } 
  }
  
  private int getMax(T route) {
    Integer v = this.maxPerRoute.get(route);
    if (v != null)
      return v.intValue(); 
    return this.defaultMaxPerRoute;
  }
  
  public void setMaxTotal(int max) {
    Args.notNegative(max, "Max value");
    this.lock.lock();
    try {
      this.maxTotal = max;
    } finally {
      this.lock.unlock();
    } 
  }
  
  public int getMaxTotal() {
    this.lock.lock();
    try {
      return this.maxTotal;
    } finally {
      this.lock.unlock();
    } 
  }
  
  public void setDefaultMaxPerRoute(int max) {
    Args.notNegative(max, "Max per route value");
    this.lock.lock();
    try {
      this.defaultMaxPerRoute = max;
    } finally {
      this.lock.unlock();
    } 
  }
  
  public int getDefaultMaxPerRoute() {
    this.lock.lock();
    try {
      return this.defaultMaxPerRoute;
    } finally {
      this.lock.unlock();
    } 
  }
  
  public void setMaxPerRoute(T route, int max) {
    Args.notNull(route, "Route");
    Args.notNegative(max, "Max per route value");
    this.lock.lock();
    try {
      this.maxPerRoute.put(route, Integer.valueOf(max));
    } finally {
      this.lock.unlock();
    } 
  }
  
  public int getMaxPerRoute(T route) {
    Args.notNull(route, "Route");
    this.lock.lock();
    try {
      return getMax(route);
    } finally {
      this.lock.unlock();
    } 
  }
  
  public PoolStats getTotalStats() {
    this.lock.lock();
    try {
      return new PoolStats(this.leased.size(), this.pending.size(), this.available.size(), this.maxTotal);
    } finally {
      this.lock.unlock();
    } 
  }
  
  public PoolStats getStats(T route) {
    Args.notNull(route, "Route");
    this.lock.lock();
    try {
      RouteSpecificPool<T, C, E> pool = getPool(route);
      return new PoolStats(pool.getLeasedCount(), pool.getPendingCount(), pool.getAvailableCount(), getMax(route));
    } finally {
      this.lock.unlock();
    } 
  }
  
  protected void enumAvailable(PoolEntryCallback<T, C> callback) {
    this.lock.lock();
    try {
      Iterator<E> it = this.available.iterator();
      while (it.hasNext()) {
        PoolEntry<T, C> poolEntry = (PoolEntry)it.next();
        callback.process(poolEntry);
        if (poolEntry.isClosed()) {
          RouteSpecificPool<T, C, E> pool = getPool(poolEntry.getRoute());
          pool.remove((E)poolEntry);
          it.remove();
        } 
      } 
      purgePoolMap();
    } finally {
      this.lock.unlock();
    } 
  }
  
  protected void enumLeased(PoolEntryCallback<T, C> callback) {
    this.lock.lock();
    try {
      Iterator<E> it = this.leased.iterator();
      while (it.hasNext()) {
        PoolEntry<T, C> poolEntry = (PoolEntry)it.next();
        callback.process(poolEntry);
      } 
    } finally {
      this.lock.unlock();
    } 
  }
  
  private void purgePoolMap() {
    Iterator<Map.Entry<T, RouteSpecificPool<T, C, E>>> it = this.routeToPool.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<T, RouteSpecificPool<T, C, E>> entry = it.next();
      RouteSpecificPool<T, C, E> pool = entry.getValue();
      if (pool.getPendingCount() + pool.getAllocatedCount() == 0)
        it.remove(); 
    } 
  }
  
  public void closeIdle(long idletime, TimeUnit tunit) {
    Args.notNull(tunit, "Time unit");
    long time = tunit.toMillis(idletime);
    if (time < 0L)
      time = 0L; 
    final long deadline = System.currentTimeMillis() - time;
    enumAvailable(new PoolEntryCallback<T, C>() {
          public void process(PoolEntry<T, C> entry) {
            if (entry.getUpdated() <= deadline)
              entry.close(); 
          }
        });
  }
  
  public void closeExpired() {
    final long now = System.currentTimeMillis();
    enumAvailable(new PoolEntryCallback<T, C>() {
          public void process(PoolEntry<T, C> entry) {
            if (entry.isExpired(now))
              entry.close(); 
          }
        });
  }
  
  public String toString() {
    StringBuilder buffer = new StringBuilder();
    buffer.append("[leased: ");
    buffer.append(this.leased);
    buffer.append("][available: ");
    buffer.append(this.available);
    buffer.append("][pending: ");
    buffer.append(this.pending);
    buffer.append("]");
    return buffer.toString();
  }
  
  protected abstract E createEntry(T paramT, C paramC);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\pool\AbstractConnPool.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */