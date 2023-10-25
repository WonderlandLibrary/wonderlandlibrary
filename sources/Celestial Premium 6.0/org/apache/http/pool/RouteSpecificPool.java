/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.pool;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.pool.PoolEntry;
import org.apache.http.pool.PoolEntryFuture;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@NotThreadSafe
abstract class RouteSpecificPool<T, C, E extends PoolEntry<T, C>> {
    private final T route;
    private final Set<E> leased;
    private final LinkedList<E> available;
    private final LinkedList<PoolEntryFuture<E>> pending;

    RouteSpecificPool(T route) {
        this.route = route;
        this.leased = new HashSet();
        this.available = new LinkedList();
        this.pending = new LinkedList();
    }

    protected abstract E createEntry(C var1);

    public final T getRoute() {
        return this.route;
    }

    public int getLeasedCount() {
        return this.leased.size();
    }

    public int getPendingCount() {
        return this.pending.size();
    }

    public int getAvailableCount() {
        return this.available.size();
    }

    public int getAllocatedCount() {
        return this.available.size() + this.leased.size();
    }

    public E getFree(Object state) {
        if (!this.available.isEmpty()) {
            PoolEntry entry;
            Iterator it;
            if (state != null) {
                it = this.available.iterator();
                while (it.hasNext()) {
                    entry = (PoolEntry)it.next();
                    if (!state.equals(entry.getState())) continue;
                    it.remove();
                    this.leased.add(entry);
                    return (E)entry;
                }
            }
            it = this.available.iterator();
            while (it.hasNext()) {
                entry = (PoolEntry)it.next();
                if (entry.getState() != null) continue;
                it.remove();
                this.leased.add(entry);
                return (E)entry;
            }
        }
        return null;
    }

    public E getLastUsed() {
        if (!this.available.isEmpty()) {
            return (E)((PoolEntry)this.available.getLast());
        }
        return null;
    }

    public boolean remove(E entry) {
        Args.notNull(entry, "Pool entry");
        return this.available.remove(entry) || this.leased.remove(entry);
    }

    public void free(E entry, boolean reusable) {
        Args.notNull(entry, "Pool entry");
        boolean found = this.leased.remove(entry);
        Asserts.check(found, "Entry %s has not been leased from this pool", entry);
        if (reusable) {
            this.available.addFirst(entry);
        }
    }

    public E add(C conn) {
        E entry = this.createEntry(conn);
        this.leased.add(entry);
        return entry;
    }

    public void queue(PoolEntryFuture<E> future) {
        if (future == null) {
            return;
        }
        this.pending.add(future);
    }

    public PoolEntryFuture<E> nextPending() {
        return this.pending.poll();
    }

    public void unqueue(PoolEntryFuture<E> future) {
        if (future == null) {
            return;
        }
        this.pending.remove(future);
    }

    public void shutdown() {
        for (PoolEntryFuture poolEntryFuture : this.pending) {
            poolEntryFuture.cancel(true);
        }
        this.pending.clear();
        for (PoolEntry poolEntry : this.available) {
            poolEntry.close();
        }
        this.available.clear();
        for (PoolEntry poolEntry : this.leased) {
            poolEntry.close();
        }
        this.leased.clear();
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[route: ");
        buffer.append(this.route);
        buffer.append("][leased: ");
        buffer.append(this.leased.size());
        buffer.append("][available: ");
        buffer.append(this.available.size());
        buffer.append("][pending: ");
        buffer.append(this.pending.size());
        buffer.append("]");
        return buffer.toString();
    }
}

