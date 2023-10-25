/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.protocol;

import java.util.List;
import org.apache.http.HttpResponseInterceptor;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Deprecated
public interface HttpResponseInterceptorList {
    public void addResponseInterceptor(HttpResponseInterceptor var1);

    public void addResponseInterceptor(HttpResponseInterceptor var1, int var2);

    public int getResponseInterceptorCount();

    public HttpResponseInterceptor getResponseInterceptor(int var1);

    public void clearResponseInterceptors();

    public void removeResponseInterceptorByClass(Class<? extends HttpResponseInterceptor> var1);

    public void setInterceptors(List<?> var1);
}

