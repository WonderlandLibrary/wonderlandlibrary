/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;

public interface HttpRequestInterceptor {
    public void process(HttpRequest var1, HttpContext var2) throws HttpException, IOException;
}

