/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.protocol.HttpContext;

public interface HttpResponseFactory {
    public HttpResponse newHttpResponse(ProtocolVersion var1, int var2, HttpContext var3);

    public HttpResponse newHttpResponse(StatusLine var1, HttpContext var2);
}

