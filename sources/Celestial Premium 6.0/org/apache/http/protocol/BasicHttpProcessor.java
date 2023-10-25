/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.protocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestInterceptorList;
import org.apache.http.protocol.HttpResponseInterceptorList;
import org.apache.http.util.Args;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Deprecated
@NotThreadSafe
public final class BasicHttpProcessor
implements HttpProcessor,
HttpRequestInterceptorList,
HttpResponseInterceptorList,
Cloneable {
    protected final List<HttpRequestInterceptor> requestInterceptors = new ArrayList<HttpRequestInterceptor>();
    protected final List<HttpResponseInterceptor> responseInterceptors = new ArrayList<HttpResponseInterceptor>();

    @Override
    public void addRequestInterceptor(HttpRequestInterceptor itcp) {
        if (itcp == null) {
            return;
        }
        this.requestInterceptors.add(itcp);
    }

    @Override
    public void addRequestInterceptor(HttpRequestInterceptor itcp, int index) {
        if (itcp == null) {
            return;
        }
        this.requestInterceptors.add(index, itcp);
    }

    @Override
    public void addResponseInterceptor(HttpResponseInterceptor itcp, int index) {
        if (itcp == null) {
            return;
        }
        this.responseInterceptors.add(index, itcp);
    }

    @Override
    public void removeRequestInterceptorByClass(Class<? extends HttpRequestInterceptor> clazz) {
        Iterator<HttpRequestInterceptor> it = this.requestInterceptors.iterator();
        while (it.hasNext()) {
            HttpRequestInterceptor request = it.next();
            if (!request.getClass().equals(clazz)) continue;
            it.remove();
        }
    }

    @Override
    public void removeResponseInterceptorByClass(Class<? extends HttpResponseInterceptor> clazz) {
        Iterator<HttpResponseInterceptor> it = this.responseInterceptors.iterator();
        while (it.hasNext()) {
            HttpResponseInterceptor request = it.next();
            if (!request.getClass().equals(clazz)) continue;
            it.remove();
        }
    }

    public final void addInterceptor(HttpRequestInterceptor interceptor) {
        this.addRequestInterceptor(interceptor);
    }

    public final void addInterceptor(HttpRequestInterceptor interceptor, int index) {
        this.addRequestInterceptor(interceptor, index);
    }

    @Override
    public int getRequestInterceptorCount() {
        return this.requestInterceptors.size();
    }

    @Override
    public HttpRequestInterceptor getRequestInterceptor(int index) {
        if (index < 0 || index >= this.requestInterceptors.size()) {
            return null;
        }
        return this.requestInterceptors.get(index);
    }

    @Override
    public void clearRequestInterceptors() {
        this.requestInterceptors.clear();
    }

    @Override
    public void addResponseInterceptor(HttpResponseInterceptor itcp) {
        if (itcp == null) {
            return;
        }
        this.responseInterceptors.add(itcp);
    }

    public final void addInterceptor(HttpResponseInterceptor interceptor) {
        this.addResponseInterceptor(interceptor);
    }

    public final void addInterceptor(HttpResponseInterceptor interceptor, int index) {
        this.addResponseInterceptor(interceptor, index);
    }

    @Override
    public int getResponseInterceptorCount() {
        return this.responseInterceptors.size();
    }

    @Override
    public HttpResponseInterceptor getResponseInterceptor(int index) {
        if (index < 0 || index >= this.responseInterceptors.size()) {
            return null;
        }
        return this.responseInterceptors.get(index);
    }

    @Override
    public void clearResponseInterceptors() {
        this.responseInterceptors.clear();
    }

    @Override
    public void setInterceptors(List<?> list) {
        Args.notNull(list, "Inteceptor list");
        this.requestInterceptors.clear();
        this.responseInterceptors.clear();
        for (Object obj : list) {
            if (obj instanceof HttpRequestInterceptor) {
                this.addInterceptor((HttpRequestInterceptor)obj);
            }
            if (!(obj instanceof HttpResponseInterceptor)) continue;
            this.addInterceptor((HttpResponseInterceptor)obj);
        }
    }

    public void clearInterceptors() {
        this.clearRequestInterceptors();
        this.clearResponseInterceptors();
    }

    @Override
    public void process(HttpRequest request, HttpContext context) throws IOException, HttpException {
        for (HttpRequestInterceptor interceptor : this.requestInterceptors) {
            interceptor.process(request, context);
        }
    }

    @Override
    public void process(HttpResponse response, HttpContext context) throws IOException, HttpException {
        for (HttpResponseInterceptor interceptor : this.responseInterceptors) {
            interceptor.process(response, context);
        }
    }

    protected void copyInterceptors(BasicHttpProcessor target) {
        target.requestInterceptors.clear();
        target.requestInterceptors.addAll(this.requestInterceptors);
        target.responseInterceptors.clear();
        target.responseInterceptors.addAll(this.responseInterceptors);
    }

    public BasicHttpProcessor copy() {
        BasicHttpProcessor clone = new BasicHttpProcessor();
        this.copyInterceptors(clone);
        return clone;
    }

    public Object clone() throws CloneNotSupportedException {
        BasicHttpProcessor clone = (BasicHttpProcessor)super.clone();
        this.copyInterceptors(clone);
        return clone;
    }
}

