/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.client;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

@Deprecated
public interface RequestDirector {
    public HttpResponse execute(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException, IOException;
}

