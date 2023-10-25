/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.client;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

public interface ServiceUnavailableRetryStrategy {
    public boolean retryRequest(HttpResponse var1, int var2, HttpContext var3);

    public long getRetryInterval();
}

