/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.pool;

import org.apache.http.pool.PoolEntry;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface PoolEntryCallback<T, C> {
    public void process(PoolEntry<T, C> var1);
}

