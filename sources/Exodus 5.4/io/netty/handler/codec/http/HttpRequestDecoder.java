/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectDecoder;
import io.netty.handler.codec.http.HttpVersion;

public class HttpRequestDecoder
extends HttpObjectDecoder {
    @Override
    protected HttpMessage createMessage(String[] stringArray) throws Exception {
        return new DefaultHttpRequest(HttpVersion.valueOf(stringArray[2]), HttpMethod.valueOf(stringArray[0]), stringArray[1], this.validateHeaders);
    }

    @Override
    protected boolean isDecodingRequest() {
        return true;
    }

    public HttpRequestDecoder() {
    }

    @Override
    protected HttpMessage createInvalidMessage() {
        return new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/bad-request", this.validateHeaders);
    }

    public HttpRequestDecoder(int n, int n2, int n3) {
        super(n, n2, n3, true);
    }

    public HttpRequestDecoder(int n, int n2, int n3, boolean bl) {
        super(n, n2, n3, true, bl);
    }
}

