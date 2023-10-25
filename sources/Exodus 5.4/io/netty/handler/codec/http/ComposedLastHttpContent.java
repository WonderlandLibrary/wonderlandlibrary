/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.LastHttpContent;

final class ComposedLastHttpContent
implements LastHttpContent {
    private DecoderResult result;
    private final HttpHeaders trailingHeaders;

    @Override
    public DecoderResult getDecoderResult() {
        return this.result;
    }

    @Override
    public void setDecoderResult(DecoderResult decoderResult) {
        this.result = decoderResult;
    }

    ComposedLastHttpContent(HttpHeaders httpHeaders) {
        this.trailingHeaders = httpHeaders;
    }

    @Override
    public boolean release(int n) {
        return false;
    }

    @Override
    public HttpHeaders trailingHeaders() {
        return this.trailingHeaders;
    }

    @Override
    public int refCnt() {
        return 1;
    }

    @Override
    public LastHttpContent retain(int n) {
        return this;
    }

    @Override
    public LastHttpContent retain() {
        return this;
    }

    @Override
    public LastHttpContent copy() {
        DefaultLastHttpContent defaultLastHttpContent = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER);
        defaultLastHttpContent.trailingHeaders().set(this.trailingHeaders());
        return defaultLastHttpContent;
    }

    @Override
    public ByteBuf content() {
        return Unpooled.EMPTY_BUFFER;
    }

    @Override
    public HttpContent duplicate() {
        return this.copy();
    }

    @Override
    public boolean release() {
        return false;
    }
}

