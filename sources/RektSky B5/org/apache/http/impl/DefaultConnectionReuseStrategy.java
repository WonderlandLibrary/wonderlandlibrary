/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.impl;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.TokenIterator;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.message.BasicTokenIterator;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class DefaultConnectionReuseStrategy
implements ConnectionReuseStrategy {
    public static final DefaultConnectionReuseStrategy INSTANCE = new DefaultConnectionReuseStrategy();

    @Override
    public boolean keepAlive(HttpResponse response, HttpContext context) {
        HeaderIterator headerIterator;
        ProtocolVersion ver;
        block25: {
            HttpRequest request;
            Args.notNull(response, "HTTP response");
            Args.notNull(context, "HTTP context");
            if (response.getStatusLine().getStatusCode() == 204) {
                Header teh;
                Header clh = response.getFirstHeader("Content-Length");
                if (clh != null) {
                    try {
                        int contentLen = Integer.parseInt(clh.getValue());
                        if (contentLen > 0) {
                            return false;
                        }
                    }
                    catch (NumberFormatException ex) {
                        // empty catch block
                    }
                }
                if ((teh = response.getFirstHeader("Transfer-Encoding")) != null) {
                    return false;
                }
            }
            if ((request = (HttpRequest)context.getAttribute("http.request")) != null) {
                try {
                    BasicTokenIterator ti = new BasicTokenIterator(request.headerIterator("Connection"));
                    while (ti.hasNext()) {
                        String token = ti.nextToken();
                        if (!"Close".equalsIgnoreCase(token)) continue;
                        return false;
                    }
                }
                catch (ParseException px) {
                    return false;
                }
            }
            ver = response.getStatusLine().getProtocolVersion();
            Header teh = response.getFirstHeader("Transfer-Encoding");
            if (teh != null) {
                if (!"chunked".equalsIgnoreCase(teh.getValue())) {
                    return false;
                }
            } else if (this.canResponseHaveBody(request, response)) {
                Header[] clhs = response.getHeaders("Content-Length");
                if (clhs.length == 1) {
                    Header clh = clhs[0];
                    try {
                        int contentLen = Integer.parseInt(clh.getValue());
                        if (contentLen < 0) {
                            return false;
                        }
                        break block25;
                    }
                    catch (NumberFormatException ex) {
                        return false;
                    }
                }
                return false;
            }
        }
        if (!(headerIterator = response.headerIterator("Connection")).hasNext()) {
            headerIterator = response.headerIterator("Proxy-Connection");
        }
        if (headerIterator.hasNext()) {
            try {
                BasicTokenIterator ti = new BasicTokenIterator(headerIterator);
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

    private boolean canResponseHaveBody(HttpRequest request, HttpResponse response) {
        if (request != null && request.getRequestLine().getMethod().equalsIgnoreCase("HEAD")) {
            return false;
        }
        int status = response.getStatusLine().getStatusCode();
        return status >= 200 && status != 204 && status != 304 && status != 205;
    }
}

