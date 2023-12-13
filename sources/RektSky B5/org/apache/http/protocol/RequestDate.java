/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpDateGenerator;
import org.apache.http.util.Args;

@Contract(threading=ThreadingBehavior.SAFE)
public class RequestDate
implements HttpRequestInterceptor {
    private static final HttpDateGenerator DATE_GENERATOR = new HttpDateGenerator();

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        if (request instanceof HttpEntityEnclosingRequest && !request.containsHeader("Date")) {
            String httpdate = DATE_GENERATOR.getCurrentDate();
            request.setHeader("Date", httpdate);
        }
    }
}

