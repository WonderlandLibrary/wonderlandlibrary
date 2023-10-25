/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpParams;

@Deprecated
public interface SchemeSocketFactory {
    public Socket createSocket(HttpParams var1) throws IOException;

    public Socket connectSocket(Socket var1, InetSocketAddress var2, InetSocketAddress var3, HttpParams var4) throws IOException, UnknownHostException, ConnectTimeoutException;

    public boolean isSecure(Socket var1) throws IllegalArgumentException;
}

