/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.protocol;

import java.io.IOException;
import java.net.InetAddress;
import org.apache.http.HttpConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpInetConnection;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.util.Args;

@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class RequestTargetHost
implements HttpRequestInterceptor {
    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        HttpCoreContext coreContext = HttpCoreContext.adapt(context);
        ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
        String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT") && ver.lessEquals(HttpVersion.HTTP_1_0)) {
            return;
        }
        if (!request.containsHeader("Host")) {
            HttpHost targetHost = coreContext.getTargetHost();
            if (targetHost == null) {
                HttpConnection conn = coreContext.getConnection();
                if (conn instanceof HttpInetConnection) {
                    InetAddress address = ((HttpInetConnection)conn).getRemoteAddress();
                    int port = ((HttpInetConnection)conn).getRemotePort();
                    if (address != null) {
                        targetHost = new HttpHost(address.getHostName(), port);
                    }
                }
                if (targetHost == null) {
                    if (ver.lessEquals(HttpVersion.HTTP_1_0)) {
                        return;
                    }
                    throw new ProtocolException("Target host missing");
                }
            }
            request.addHeader("Host", targetHost.toHostString());
        }
    }
}

