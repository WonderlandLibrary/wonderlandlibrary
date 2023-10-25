/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.impl.conn.tsccm;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.tsccm.AbstractConnPool;
import org.apache.http.impl.conn.tsccm.BasicPoolEntry;
import org.apache.http.impl.conn.tsccm.PoolEntryRequest;
import org.apache.http.impl.conn.tsccm.RouteSpecificPool;
import org.apache.http.impl.conn.tsccm.WaitingThread;
import org.apache.http.impl.conn.tsccm.WaitingThreadAborter;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Deprecated
public class ConnPoolByRoute
extends AbstractConnPool {
    private final Log log = LogFactory.getLog(this.getClass());
    private final Lock poolLock;
    protected final ClientConnectionOperator operator;
    protected final ConnPerRoute connPerRoute;
    protected final Set<BasicPoolEntry> leasedConnections;
    protected final Queue<BasicPoolEntry> freeConnections;
    protected final Queue<WaitingThread> waitingThreads;
    protected final Map<HttpRoute, RouteSpecificPool> routeToPool;
    private final long connTTL;
    private final TimeUnit connTTLTimeUnit;
    protected volatile boolean shutdown;
    protected volatile int maxTotalConnections;
    protected volatile int numConnections;

    public ConnPoolByRoute(ClientConnectionOperator operator, ConnPerRoute connPerRoute, int maxTotalConnections) {
        this(operator, connPerRoute, maxTotalConnections, -1L, TimeUnit.MILLISECONDS);
    }

    public ConnPoolByRoute(ClientConnectionOperator operator, ConnPerRoute connPerRoute, int maxTotalConnections, long connTTL, TimeUnit connTTLTimeUnit) {
        Args.notNull(operator, "Connection operator");
        Args.notNull(connPerRoute, "Connections per route");
        this.poolLock = ((AbstractConnPool)this).poolLock;
        this.leasedConnections = ((AbstractConnPool)this).leasedConnections;
        this.operator = operator;
        this.connPerRoute = connPerRoute;
        this.maxTotalConnections = maxTotalConnections;
        this.freeConnections = this.createFreeConnQueue();
        this.waitingThreads = this.createWaitingThreadQueue();
        this.routeToPool = this.createRouteToPoolMap();
        this.connTTL = connTTL;
        this.connTTLTimeUnit = connTTLTimeUnit;
    }

    protected Lock getLock() {
        return this.poolLock;
    }

    @Deprecated
    public ConnPoolByRoute(ClientConnectionOperator operator, HttpParams params) {
        this(operator, ConnManagerParams.getMaxConnectionsPerRoute(params), ConnManagerParams.getMaxTotalConnections(params));
    }

    protected Queue<BasicPoolEntry> createFreeConnQueue() {
        return new LinkedList<BasicPoolEntry>();
    }

    protected Queue<WaitingThread> createWaitingThreadQueue() {
        return new LinkedList<WaitingThread>();
    }

    protected Map<HttpRoute, RouteSpecificPool> createRouteToPoolMap() {
        return new HashMap<HttpRoute, RouteSpecificPool>();
    }

    protected RouteSpecificPool newRouteSpecificPool(HttpRoute route) {
        return new RouteSpecificPool(route, this.connPerRoute);
    }

    protected WaitingThread newWaitingThread(Condition cond, RouteSpecificPool rospl) {
        return new WaitingThread(cond, rospl);
    }

    private void closeConnection(BasicPoolEntry entry) {
        OperatedClientConnection conn = entry.getConnection();
        if (conn != null) {
            try {
                conn.close();
            }
            catch (IOException ex) {
                this.log.debug("I/O error closing connection", ex);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected RouteSpecificPool getRoutePool(HttpRoute route, boolean create) {
        RouteSpecificPool rospl = null;
        this.poolLock.lock();
        try {
            rospl = this.routeToPool.get(route);
            if (rospl == null && create) {
                rospl = this.newRouteSpecificPool(route);
                this.routeToPool.put(route, rospl);
            }
        }
        finally {
            this.poolLock.unlock();
        }
        return rospl;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int getConnectionsInPool(HttpRoute route) {
        this.poolLock.lock();
        try {
            RouteSpecificPool rospl = this.getRoutePool(route, false);
            int n = rospl != null ? rospl.getEntryCount() : 0;
            return n;
        }
        finally {
            this.poolLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int getConnectionsInPool() {
        this.poolLock.lock();
        try {
            int n = this.numConnections;
            return n;
        }
        finally {
            this.poolLock.unlock();
        }
    }

    @Override
    public PoolEntryRequest requestPoolEntry(final HttpRoute route, final Object state) {
        final WaitingThreadAborter aborter = new WaitingThreadAborter();
        return new PoolEntryRequest(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            public void abortRequest() {
                ConnPoolByRoute.this.poolLock.lock();
                try {
                    aborter.abort();
                }
                finally {
                    ConnPoolByRoute.this.poolLock.unlock();
                }
            }

            public BasicPoolEntry getPoolEntry(long timeout, TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
                return ConnPoolByRoute.this.getEntryBlocking(route, state, timeout, tunit, aborter);
            }
        };
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected BasicPoolEntry getEntryBlocking(HttpRoute route, Object state, long timeout, TimeUnit tunit, WaitingThreadAborter aborter) throws ConnectionPoolTimeoutException, InterruptedException {
        Date deadline = null;
        if (timeout > 0L) {
            deadline = new Date(System.currentTimeMillis() + tunit.toMillis(timeout));
        }
        BasicPoolEntry entry = null;
        this.poolLock.lock();
        try {
            RouteSpecificPool rospl = this.getRoutePool(route, true);
            WaitingThread waitingThread = null;
            while (entry == null) {
                boolean hasCapacity;
                Asserts.check(!this.shutdown, "Connection pool shut down");
                if (this.log.isDebugEnabled()) {
                    this.log.debug("[" + route + "] total kept alive: " + this.freeConnections.size() + ", total issued: " + this.leasedConnections.size() + ", total allocated: " + this.numConnections + " out of " + this.maxTotalConnections);
                }
                if ((entry = this.getFreeEntry(rospl, state)) != null) {
                    break;
                }
                boolean bl = hasCapacity = rospl.getCapacity() > 0;
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Available capacity: " + rospl.getCapacity() + " out of " + rospl.getMaxEntries() + " [" + route + "][" + state + "]");
                }
                if (hasCapacity && this.numConnections < this.maxTotalConnections) {
                    entry = this.createEntry(rospl, this.operator);
                    continue;
                }
                if (hasCapacity && !this.freeConnections.isEmpty()) {
                    this.deleteLeastUsedEntry();
                    rospl = this.getRoutePool(route, true);
                    entry = this.createEntry(rospl, this.operator);
                    continue;
                }
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Need to wait for connection [" + route + "][" + state + "]");
                }
                if (waitingThread == null) {
                    waitingThread = this.newWaitingThread(this.poolLock.newCondition(), rospl);
                    aborter.setWaitingThread(waitingThread);
                }
                boolean success = false;
                try {
                    rospl.queueThread(waitingThread);
                    this.waitingThreads.add(waitingThread);
                    success = waitingThread.await(deadline);
                }
                finally {
                    rospl.removeThread(waitingThread);
                    this.waitingThreads.remove(waitingThread);
                }
                if (success || deadline == null || deadline.getTime() > System.currentTimeMillis()) continue;
                throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
            }
        }
        finally {
            this.poolLock.unlock();
        }
        return entry;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void freeEntry(BasicPoolEntry entry, boolean reusable, long validDuration, TimeUnit timeUnit) {
        HttpRoute route = entry.getPlannedRoute();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Releasing connection [" + route + "][" + entry.getState() + "]");
        }
        this.poolLock.lock();
        try {
            if (this.shutdown) {
                this.closeConnection(entry);
                return;
            }
            this.leasedConnections.remove(entry);
            RouteSpecificPool rospl = this.getRoutePool(route, true);
            if (reusable && rospl.getCapacity() >= 0) {
                if (this.log.isDebugEnabled()) {
                    String s = validDuration > 0L ? "for " + validDuration + " " + (Object)((Object)timeUnit) : "indefinitely";
                    this.log.debug("Pooling connection [" + route + "][" + entry.getState() + "]; keep alive " + s);
                }
                rospl.freeEntry(entry);
                entry.updateExpiry(validDuration, timeUnit);
                this.freeConnections.add(entry);
            } else {
                this.closeConnection(entry);
                rospl.dropEntry();
                --this.numConnections;
            }
            this.notifyWaitingThread(rospl);
        }
        finally {
            this.poolLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected BasicPoolEntry getFreeEntry(RouteSpecificPool rospl, Object state) {
        BasicPoolEntry entry = null;
        this.poolLock.lock();
        try {
            boolean done = false;
            while (!done) {
                entry = rospl.allocEntry(state);
                if (entry != null) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Getting free connection [" + rospl.getRoute() + "][" + state + "]");
                    }
                    this.freeConnections.remove(entry);
                    if (entry.isExpired(System.currentTimeMillis())) {
                        if (this.log.isDebugEnabled()) {
                            this.log.debug("Closing expired free connection [" + rospl.getRoute() + "][" + state + "]");
                        }
                        this.closeConnection(entry);
                        rospl.dropEntry();
                        --this.numConnections;
                        continue;
                    }
                    this.leasedConnections.add(entry);
                    done = true;
                    continue;
                }
                done = true;
                if (!this.log.isDebugEnabled()) continue;
                this.log.debug("No free connections [" + rospl.getRoute() + "][" + state + "]");
            }
        }
        finally {
            this.poolLock.unlock();
        }
        return entry;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected BasicPoolEntry createEntry(RouteSpecificPool rospl, ClientConnectionOperator op) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("Creating new connection [" + rospl.getRoute() + "]");
        }
        BasicPoolEntry entry = new BasicPoolEntry(op, rospl.getRoute(), this.connTTL, this.connTTLTimeUnit);
        this.poolLock.lock();
        try {
            rospl.createdEntry(entry);
            ++this.numConnections;
            this.leasedConnections.add(entry);
        }
        finally {
            this.poolLock.unlock();
        }
        return entry;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void deleteEntry(BasicPoolEntry entry) {
        HttpRoute route = entry.getPlannedRoute();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Deleting connection [" + route + "][" + entry.getState() + "]");
        }
        this.poolLock.lock();
        try {
            this.closeConnection(entry);
            RouteSpecificPool rospl = this.getRoutePool(route, true);
            rospl.deleteEntry(entry);
            --this.numConnections;
            if (rospl.isUnused()) {
                this.routeToPool.remove(route);
            }
        }
        finally {
            this.poolLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void deleteLeastUsedEntry() {
        this.poolLock.lock();
        try {
            BasicPoolEntry entry = this.freeConnections.remove();
            if (entry != null) {
                this.deleteEntry(entry);
            } else if (this.log.isDebugEnabled()) {
                this.log.debug("No free connection to delete");
            }
        }
        finally {
            this.poolLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected void handleLostEntry(HttpRoute route) {
        this.poolLock.lock();
        try {
            RouteSpecificPool rospl = this.getRoutePool(route, true);
            rospl.dropEntry();
            if (rospl.isUnused()) {
                this.routeToPool.remove(route);
            }
            --this.numConnections;
            this.notifyWaitingThread(rospl);
        }
        finally {
            this.poolLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void notifyWaitingThread(RouteSpecificPool rospl) {
        WaitingThread waitingThread = null;
        this.poolLock.lock();
        try {
            if (rospl != null && rospl.hasThread()) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Notifying thread waiting on pool [" + rospl.getRoute() + "]");
                }
                waitingThread = rospl.nextThread();
            } else if (!this.waitingThreads.isEmpty()) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Notifying thread waiting on any pool");
                }
                waitingThread = this.waitingThreads.remove();
            } else if (this.log.isDebugEnabled()) {
                this.log.debug("Notifying no-one, there are no waiting threads");
            }
            if (waitingThread != null) {
                waitingThread.wakeup();
            }
        }
        finally {
            this.poolLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void deleteClosedConnections() {
        this.poolLock.lock();
        try {
            Iterator iter = this.freeConnections.iterator();
            while (iter.hasNext()) {
                BasicPoolEntry entry = (BasicPoolEntry)iter.next();
                if (entry.getConnection().isOpen()) continue;
                iter.remove();
                this.deleteEntry(entry);
            }
        }
        finally {
            this.poolLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void closeIdleConnections(long idletime, TimeUnit tunit) {
        long t;
        Args.notNull(tunit, "Time unit");
        long l = t = idletime > 0L ? idletime : 0L;
        if (this.log.isDebugEnabled()) {
            this.log.debug("Closing connections idle longer than " + t + " " + (Object)((Object)tunit));
        }
        long deadline = System.currentTimeMillis() - tunit.toMillis(t);
        this.poolLock.lock();
        try {
            Iterator iter = this.freeConnections.iterator();
            while (iter.hasNext()) {
                BasicPoolEntry entry = (BasicPoolEntry)iter.next();
                if (entry.getUpdated() > deadline) continue;
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Closing connection last used @ " + new Date(entry.getUpdated()));
                }
                iter.remove();
                this.deleteEntry(entry);
            }
        }
        finally {
            this.poolLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void closeExpiredConnections() {
        this.log.debug("Closing expired connections");
        long now = System.currentTimeMillis();
        this.poolLock.lock();
        try {
            Iterator iter = this.freeConnections.iterator();
            while (iter.hasNext()) {
                BasicPoolEntry entry = (BasicPoolEntry)iter.next();
                if (!entry.isExpired(now)) continue;
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Closing connection expired @ " + new Date(entry.getExpiry()));
                }
                iter.remove();
                this.deleteEntry(entry);
            }
        }
        finally {
            this.poolLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void shutdown() {
        this.poolLock.lock();
        try {
            if (this.shutdown) {
                return;
            }
            this.shutdown = true;
            Iterator<BasicPoolEntry> iter1 = this.leasedConnections.iterator();
            while (iter1.hasNext()) {
                BasicPoolEntry entry = iter1.next();
                iter1.remove();
                this.closeConnection(entry);
            }
            Iterator iter2 = this.freeConnections.iterator();
            while (iter2.hasNext()) {
                BasicPoolEntry entry = (BasicPoolEntry)iter2.next();
                iter2.remove();
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Closing connection [" + entry.getPlannedRoute() + "][" + entry.getState() + "]");
                }
                this.closeConnection(entry);
            }
            Iterator iwth = this.waitingThreads.iterator();
            while (iwth.hasNext()) {
                WaitingThread waiter = (WaitingThread)iwth.next();
                iwth.remove();
                waiter.wakeup();
            }
            this.routeToPool.clear();
        }
        finally {
            this.poolLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setMaxTotalConnections(int max) {
        this.poolLock.lock();
        try {
            this.maxTotalConnections = max;
        }
        finally {
            this.poolLock.unlock();
        }
    }

    public int getMaxTotalConnections() {
        return this.maxTotalConnections;
    }
}

