/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

public interface HttpResponseInterceptor {
    public void process(HttpResponse var1, HttpContext var2) throws HttpException, IOException;
}

