/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.pool;

import org.apache.http.pool.PoolStats;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
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

