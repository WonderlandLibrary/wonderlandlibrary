/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.conn;

import java.io.IOException;
import java.net.InetAddress;
import org.apache.http.HttpHost;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Deprecated
public interface ClientConnectionOperator {
    public OperatedClientConnection createConnection();

    public void openConnection(OperatedClientConnection var1, HttpHost var2, InetAddress var3, HttpContext var4, HttpParams var5) throws IOException;

    public void updateSecureConnection(OperatedClientConnection var1, HttpHost var2, HttpContext var3, HttpParams var4) throws IOException;
}

