/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.conn;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.HttpInetConnection;
import org.apache.http.params.HttpParams;

@Deprecated
public interface OperatedClientConnection
extends HttpClientConnection,
HttpInetConnection {
    public HttpHost getTargetHost();

    public boolean isSecure();

    public Socket getSocket();

    public void opening(Socket var1, HttpHost var2) throws IOException;

    public void openCompleted(boolean var1, HttpParams var2) throws IOException;

    public void update(Socket var1, HttpHost var2, boolean var3, HttpParams var4) throws IOException;
}

