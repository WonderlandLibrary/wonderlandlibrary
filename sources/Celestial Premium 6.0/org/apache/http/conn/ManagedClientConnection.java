/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSession;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.conn.ConnectionReleaseTrigger;
import org.apache.http.conn.HttpRoutedConnection;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Deprecated
public interface ManagedClientConnection
extends HttpClientConnection,
HttpRoutedConnection,
ManagedHttpClientConnection,
ConnectionReleaseTrigger {
    public boolean isSecure();

    public HttpRoute getRoute();

    public SSLSession getSSLSession();

    public void open(HttpRoute var1, HttpContext var2, HttpParams var3) throws IOException;

    public void tunnelTarget(boolean var1, HttpParams var2) throws IOException;

    public void tunnelProxy(HttpHost var1, boolean var2, HttpParams var3) throws IOException;

    public void layerProtocol(HttpContext var1, HttpParams var2) throws IOException;

    public void markReusable();

    public void unmarkReusable();

    public boolean isMarkedReusable();

    public void setState(Object var1);

    public Object getState();

    public void setIdleDuration(long var1, TimeUnit var3);
}

