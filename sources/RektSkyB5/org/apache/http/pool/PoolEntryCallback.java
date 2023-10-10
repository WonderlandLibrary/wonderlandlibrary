/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.pool;

import org.apache.http.pool.PoolEntry;

public interface PoolEntryCallback<T, C> {
    public void process(PoolEntry<T, C> var1);
}

