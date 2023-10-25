/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.protocol;

import java.util.List;
import org.apache.http.HttpRequestInterceptor;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
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

