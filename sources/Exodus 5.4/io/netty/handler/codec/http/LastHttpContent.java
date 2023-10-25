/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;

public interface LastHttpContent
extends HttpContent {
    public static final LastHttpContent EMPTY_LAST_CONTENT = new LastHttpContent(){

        @Override
        public boolean release() {
            return false;
        }

        @Override
        public ByteBuf content() {
            return Unpooled.EMPTY_BUFFER;
        }

        public String toString() {
            return "EmptyLastHttpContent";
        }

        @Override
        public LastHttpContent duplicate() {
            return this;
        }

        @Override
        public HttpHeaders trailingHeaders() {
            return HttpHeaders.EMPTY_HEADERS;
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
        public LastHttpContent copy() {
            return EMPTY_LAST_CONTENT;
        }

        @Override
        public LastHttpContent retain() {
            return this;
        }

        @Override
        public DecoderResult getDecoderResult() {
            return DecoderResult.SUCCESS;
        }

        @Override
        public boolean release(int n) {
            return false;
        }

        @Override
        public void setDecoderResult(DecoderResult decoderResult) {
            throw new UnsupportedOperationException("read only");
        }
    };

    public HttpHeaders trailingHeaders();

    @Override
    public LastHttpContent copy();

    @Override
    public LastHttpContent retain();

    @Override
    public LastHttpContent retain(int var1);
}

