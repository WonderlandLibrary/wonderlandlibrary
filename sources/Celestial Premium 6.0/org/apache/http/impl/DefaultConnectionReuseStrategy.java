/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.impl;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.TokenIterator;
import org.apache.http.annotation.Immutable;
import org.apache.http.message.BasicTokenIterator;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class DefaultConnectionReuseStrategy
implements ConnectionReuseStrategy {
    public static final DefaultConnectionReuseStrategy INSTANCE = new DefaultConnectionReuseStrategy();

    public boolean keepAlive(HttpResponse response, HttpContext context) {
        HeaderIterator hit;
        ProtocolVersion ver;
        block15: {
            Args.notNull(response, "HTTP response");
            Args.notNull(context, "HTTP context");
            ver = response.getStatusLine().getProtocolVersion();
            Header teh = response.getFirstHeader("Transfer-Encoding");
            if (teh != null) {
                if (!"chunked".equalsIgnoreCase(teh.getValue())) {
                    return false;
                }
            } else if (this.canResponseHaveBody(response)) {
                Header[] clhs = response.getHeaders("Content-Length");
                if (clhs.length == 1) {
                    Header clh = clhs[0];
                    try {
                        int contentLen = Integer.parseInt(clh.getValue());
                        if (contentLen < 0) {
                            return false;
                        }
                        break block15;
                    }
                    catch (NumberFormatException ex) {
                        return false;
                    }
                }
                return false;
            }
        }
        if (!(hit = response.headerIterator("Connection")).hasNext()) {
            hit = response.headerIterator("Proxy-Connection");
        }
        if (hit.hasNext()) {
            try {
                TokenIterator ti = this.createTokenIterator(hit);
                boolean keepalive = false;
                while (ti.hasNext()) {
                    String token = ti.nextToken();
                    if ("Close".equalsIgnoreCase(token)) {
                        return false;
                    }
                    if (!"Keep-Alive".equalsIgnoreCase(token)) continue;
                    keepalive = true;
                }
                if (keepalive) {
                    return true;
                }
            }
            catch (ParseException px) {
                return false;
            }
        }
        return !ver.lessEquals(HttpVersion.HTTP_1_0);
    }

    protected TokenIterator createTokenIterator(HeaderIterator hit) {
        return new BasicTokenIterator(hit);
    }

    private boolean canResponseHaveBody(HttpResponse response) {
        int status = response.getStatusLine().getStatusCode();
        return status >= 200 && status != 204 && status != 304 && status != 205;
    }
}

