/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.protocol;

import java.util.List;
import org.apache.http.HttpRequestInterceptor;

@Deprecated
public interface HttpRequestInterceptorList {
    public void addRequestInterceptor(HttpRequestInterceptor var1);

    public void addRequestInterceptor(HttpRequestInterceptor var1, int var2);

    public int getRequestInterceptorCount();

    public HttpRequestInterceptor getRequestInterceptor(int var1);

    public void clearRequestInterceptors();

    public void removeRequestInterceptorByClass(Class<? extends HttpRequestInterceptor> var1);

    public void setInterceptors(List<?> var1);
}

