/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.pool;

import org.apache.http.pool.PoolStats;

public interface ConnPoolControl<T> {
    public void setMaxTotal(int var1);

    public int getMaxTotal();

    public void setDefaultMaxPerRoute(int var1);

    public int getDefaultMaxPerRoute();

    public void setMaxPerRoute(T var1, int var2);

    public int getMaxPerRoute(T var1);

    public PoolStats getTotalStats();

    public PoolStats getStats(T var1);
}

