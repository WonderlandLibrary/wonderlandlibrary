/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpClientConnection;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.protocol.HttpContext;

public interface HttpClientConnectionManager {
    public ConnectionRequest requestConnection(HttpRoute var1, Object var2);

    public void releaseConnection(HttpClientConnection var1, Object var2, long var3, TimeUnit var5);

    public void connect(HttpClientConnection var1, HttpRoute var2, int var3, HttpContext var4) throws IOException;

    public void upgrade(HttpClientConnection var1, HttpRoute var2, HttpContext var3) throws IOException;

    public void routeComplete(HttpClientConnection var1, HttpRoute var2, HttpContext var3) throws IOException;

    public void closeIdleConnections(long var1, TimeUnit var3);

    public void closeExpiredConnections();

    public void shutdown();
}

