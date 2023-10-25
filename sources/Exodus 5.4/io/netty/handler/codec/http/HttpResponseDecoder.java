/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObjectDecoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class HttpResponseDecoder
extends HttpObjectDecoder {
    private static final HttpResponseStatus UNKNOWN_STATUS = new HttpResponseStatus(999, "Unknown");

    @Override
    protected HttpMessage createMessage(String[] stringArray) {
        return new DefaultHttpResponse(HttpVersion.valueOf(stringArray[0]), new HttpResponseStatus(Integer.parseInt(stringArray[1]), stringArray[2]), this.validateHeaders);
    }

    @Override
    protected HttpMessage createInvalidMessage() {
        return new DefaultHttpResponse(HttpVersion.HTTP_1_0, UNKNOWN_STATUS, this.validateHeaders);
    }

    public HttpResponseDecoder(int n, int n2, int n3, boolean bl) {
        super(n, n2, n3, true, bl);
    }

    @Override
    protected boolean isDecodingRequest() {
        return false;
    }

    public HttpResponseDecoder(int n, int n2, int n3) {
        super(n, n2, n3, true);
    }

    public HttpResponseDecoder() {
    }
}

