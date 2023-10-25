/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class DefaultFullHttpResponse
extends DefaultHttpResponse
implements FullHttpResponse {
    private final HttpHeaders trailingHeaders;
    private final ByteBuf content;
    private final boolean validateHeaders;

    @Override
    public FullHttpResponse duplicate() {
        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(this.getProtocolVersion(), this.getStatus(), this.content().duplicate(), this.validateHeaders);
        defaultFullHttpResponse.headers().set(this.headers());
        defaultFullHttpResponse.trailingHeaders().set(this.trailingHeaders());
        return defaultFullHttpResponse;
    }

    @Override
    public FullHttpResponse copy() {
        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(this.getProtocolVersion(), this.getStatus(), this.content().copy(), this.validateHeaders);
        defaultFullHttpResponse.headers().set(this.headers());
        defaultFullHttpResponse.trailingHeaders().set(this.trailingHeaders());
        return defaultFullHttpResponse;
    }

    public DefaultFullHttpResponse(HttpVersion httpVersion, HttpResponseStatus httpResponseStatus) {
        this(httpVersion, httpResponseStatus, Unpooled.buffer(0));
    }

    @Override
    public boolean release() {
        return this.content.release();
    }

    @Override
    public ByteBuf content() {
        return this.content;
    }

    @Override
    public boolean release(int n) {
        return this.content.release(n);
    }

    @Override
    public FullHttpResponse setStatus(HttpResponseStatus httpResponseStatus) {
        super.setStatus(httpResponseStatus);
        return this;
    }

    @Override
    public FullHttpResponse setProtocolVersion(HttpVersion httpVersion) {
        super.setProtocolVersion(httpVersion);
        return this;
    }

    @Override
    public FullHttpResponse retain() {
        this.content.retain();
        return this;
    }

    public DefaultFullHttpResponse(HttpVersion httpVersion, HttpResponseStatus httpResponseStatus, ByteBuf byteBuf, boolean bl) {
        super(httpVersion, httpResponseStatus, bl);
        if (byteBuf == null) {
            throw new NullPointerException("content");
        }
        this.content = byteBuf;
        this.trailingHeaders = new DefaultHttpHeaders(bl);
        this.validateHeaders = bl;
    }

    @Override
    public FullHttpResponse retain(int n) {
        this.content.retain(n);
        return this;
    }

    @Override
    public HttpHeaders trailingHeaders() {
        return this.trailingHeaders;
    }

    public DefaultFullHttpResponse(HttpVersion httpVersion, HttpResponseStatus httpResponseStatus, ByteBuf byteBuf) {
        this(httpVersion, httpResponseStatus, byteBuf, true);
    }

    @Override
    public int refCnt() {
        return this.content.refCnt();
    }
}

