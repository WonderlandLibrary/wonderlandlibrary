/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.pool;

import java.io.Serializable;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;

@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class PoolStats
implements Serializable {
    private static final long serialVersionUID = -2807686144795228544L;
    private final int leased;
    private final int pending;
    private final int available;
    private final int max;

    public PoolStats(int leased, int pending, int free, int max) {
        this.leased = leased;
        this.pending = pending;
        this.available = free;
        this.max = max;
    }

    public int getLeased() {
        return this.leased;
    }

    public int getPending() {
        return this.pending;
    }

    public int getAvailable() {
        return this.available;
    }

    public int getMax() {
        return this.max;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[leased: ");
        buffer.append(this.leased);
        buffer.append("; pending: ");
        buffer.append(this.pending);
        buffer.append("; available: ");
        buffer.append(this.available);
        buffer.append("; max: ");
        buffer.append(this.max);
        buffer.append("]");
        return buffer.toString();
    }
}

