/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.impl.conn.tsccm;

import java.lang.ref.ReferenceQueue;
import java.util.concurrent.TimeUnit;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.AbstractPoolEntry;
import org.apache.http.impl.conn.tsccm.BasicPoolEntryRef;
import org.apache.http.util.Args;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Deprecated
public class BasicPoolEntry
extends AbstractPoolEntry {
    private final long created;
    private long updated;
    private final long validUntil;
    private long expiry;

    public BasicPoolEntry(ClientConnectionOperator op, HttpRoute route, ReferenceQueue<Object> queue) {
        super(op, route);
        Args.notNull(route, "HTTP route");
        this.created = System.currentTimeMillis();
        this.expiry = this.validUntil = Long.MAX_VALUE;
    }

    public BasicPoolEntry(ClientConnectionOperator op, HttpRoute route) {
        this(op, route, -1L, TimeUnit.MILLISECONDS);
    }

    public BasicPoolEntry(ClientConnectionOperator op, HttpRoute route, long connTTL, TimeUnit timeunit) {
        super(op, route);
        Args.notNull(route, "HTTP route");
        this.created = System.currentTimeMillis();
        this.validUntil = connTTL > 0L ? this.created + timeunit.toMillis(connTTL) : Long.MAX_VALUE;
        this.expiry = this.validUntil;
    }

    protected final OperatedClientConnection getConnection() {
        return this.connection;
    }

    protected final HttpRoute getPlannedRoute() {
        return this.route;
    }

    protected final BasicPoolEntryRef getWeakRef() {
        return null;
    }

    @Override
    protected void shutdownEntry() {
        super.shutdownEntry();
    }

    public long getCreated() {
        return this.created;
    }

    public long getUpdated() {
        return this.updated;
    }

    public long getExpiry() {
        return this.expiry;
    }

    public long getValidUntil() {
        return this.validUntil;
    }

    public void updateExpiry(long time, TimeUnit timeunit) {
        this.updated = System.currentTimeMillis();
        long newExpiry = time > 0L ? this.updated + timeunit.toMillis(time) : Long.MAX_VALUE;
        this.expiry = Math.min(this.validUntil, newExpiry);
    }

    public boolean isExpired(long now) {
        return now >= this.expiry;
    }
}

