/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.conn;

import java.io.IOException;
import java.net.Socket;
import javax.net.ssl.SSLSession;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpInetConnection;

public interface ManagedHttpClientConnection
extends HttpClientConnection,
HttpInetConnection {
    public String getId();

    public void bind(Socket var1) throws IOException;

    public Socket getSocket();

    public SSLSession getSSLSession();
}

