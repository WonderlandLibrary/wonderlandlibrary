/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.conn;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpClientConnection;
import org.apache.http.concurrent.Cancellable;
import org.apache.http.conn.ConnectionPoolTimeoutException;

public interface ConnectionRequest
extends Cancellable {
    public HttpClientConnection get(long var1, TimeUnit var3) throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException;
}

