/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class RequestUserAgent
implements HttpRequestInterceptor {
    private final String userAgent;

    public RequestUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public RequestUserAgent() {
        this(null);
    }

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        if (!request.containsHeader("User-Agent")) {
            String s2 = null;
            HttpParams params = request.getParams();
            if (params != null) {
                s2 = (String)params.getParameter("http.useragent");
            }
            if (s2 == null) {
                s2 = this.userAgent;
            }
            if (s2 != null) {
                request.addHeader("User-Agent", s2);
            }
        }
    }
}

