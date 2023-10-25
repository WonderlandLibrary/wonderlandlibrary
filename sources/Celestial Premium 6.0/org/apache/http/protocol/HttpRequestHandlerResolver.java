/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.protocol;

import org.apache.http.protocol.HttpRequestHandler;

@Deprecated
public interface HttpRequestHandlerResolver {
    public HttpRequestHandler lookup(String var1);
}

