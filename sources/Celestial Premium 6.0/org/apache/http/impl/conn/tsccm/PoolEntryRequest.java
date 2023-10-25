/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.impl.conn.tsccm;

import java.util.concurrent.TimeUnit;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.impl.conn.tsccm.BasicPoolEntry;

@Deprecated
public interface PoolEntryRequest {
    public BasicPoolEntry getPoolEntry(long var1, TimeUnit var3) throws InterruptedException, ConnectionPoolTimeoutException;

    public void abortRequest();
}

