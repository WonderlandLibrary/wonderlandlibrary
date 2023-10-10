/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.protocol;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.ChainBuilder;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.ImmutableHttpProcessor;

public class HttpProcessorBuilder {
    private ChainBuilder<HttpRequestInterceptor> requestChainBuilder;
    private ChainBuilder<HttpResponseInterceptor> responseChainBuilder;

    public static HttpProcessorBuilder create() {
        return new HttpProcessorBuilder();
    }

    HttpProcessorBuilder() {
    }

    private ChainBuilder<HttpRequestInterceptor> getRequestChainBuilder() {
        if (this.requestChainBuilder == null) {
            this.requestChainBuilder = new ChainBuilder();
        }
        return this.requestChainBuilder;
    }

    private ChainBuilder<HttpResponseInterceptor> getResponseChainBuilder() {
        if (this.responseChainBuilder == null) {
            this.responseChainBuilder = new ChainBuilder();
        }
        return this.responseChainBuilder;
    }

    public HttpProcessorBuilder addFirst(HttpRequestInterceptor e2) {
        if (e2 == null) {
            return this;
        }
        this.getRequestChainBuilder().addFirst(e2);
        return this;
    }

    public HttpProcessorBuilder addLast(HttpRequestInterceptor e2) {
        if (e2 == null) {
            return this;
        }
        this.getRequestChainBuilder().addLast(e2);
        return this;
    }

    public HttpProcessorBuilder add(HttpRequestInterceptor e2) {
        return this.addLast(e2);
    }

    public HttpProcessorBuilder addAllFirst(HttpRequestInterceptor ... e2) {
        if (e2 == null) {
            return this;
        }
        this.getRequestChainBuilder().addAllFirst((HttpRequestInterceptor[])e2);
        return this;
    }

    public HttpProcessorBuilder addAllLast(HttpRequestInterceptor ... e2) {
        if (e2 == null) {
            return this;
        }
        this.getRequestChainBuilder().addAllLast((HttpRequestInterceptor[])e2);
        return this;
    }

    public HttpProcessorBuilder addAll(HttpRequestInterceptor ... e2) {
        return this.addAllLast(e2);
    }

    public HttpProcessorBuilder addFirst(HttpResponseInterceptor e2) {
        if (e2 == null) {
            return this;
        }
        this.getResponseChainBuilder().addFirst(e2);
        return this;
    }

    public HttpProcessorBuilder addLast(HttpResponseInterceptor e2) {
        if (e2 == null) {
            return this;
        }
        this.getResponseChainBuilder().addLast(e2);
        return this;
    }

    public HttpProcessorBuilder add(HttpResponseInterceptor e2) {
        return this.addLast(e2);
    }

    public HttpProcessorBuilder addAllFirst(HttpResponseInterceptor ... e2) {
        if (e2 == null) {
            return this;
        }
        this.getResponseChainBuilder().addAllFirst((HttpResponseInterceptor[])e2);
        return this;
    }

    public HttpProcessorBuilder addAllLast(HttpResponseInterceptor ... e2) {
        if (e2 == null) {
            return this;
        }
        this.getResponseChainBuilder().addAllLast((HttpResponseInterceptor[])e2);
        return this;
    }

    public HttpProcessorBuilder addAll(HttpResponseInterceptor ... e2) {
        return this.addAllLast(e2);
    }

    public HttpProcessor build() {
        return new ImmutableHttpProcessor(this.requestChainBuilder != null ? this.requestChainBuilder.build() : null, this.responseChainBuilder != null ? this.responseChainBuilder.build() : null);
    }
}

