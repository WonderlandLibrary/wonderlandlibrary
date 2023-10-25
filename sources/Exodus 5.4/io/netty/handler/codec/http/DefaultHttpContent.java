/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultHttpObject;
import io.netty.handler.codec.http.HttpContent;
import io.netty.util.internal.StringUtil;

public class DefaultHttpContent
extends DefaultHttpObject
implements HttpContent {
    private final ByteBuf content;

    @Override
    public HttpContent copy() {
        return new DefaultHttpContent(this.content.copy());
    }

    @Override
    public int refCnt() {
        return this.content.refCnt();
    }

    public DefaultHttpContent(ByteBuf byteBuf) {
        if (byteBuf == null) {
            throw new NullPointerException("content");
        }
        this.content = byteBuf;
    }

    @Override
    public HttpContent duplicate() {
        return new DefaultHttpContent(this.content.duplicate());
    }

    @Override
    public boolean release(int n) {
        return this.content.release(n);
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
    public HttpContent retain(int n) {
        this.content.retain(n);
        return this;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "(data: " + this.content() + ", decoderResult: " + this.getDecoderResult() + ')';
    }

    @Override
    public HttpContent retain() {
        this.content.retain();
        return this;
    }
}

