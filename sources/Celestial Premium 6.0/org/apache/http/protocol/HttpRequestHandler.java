/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

public interface HttpRequestHandler {
    public void handle(HttpRequest var1, HttpResponse var2, HttpContext var3) throws HttpException, IOException;
}

