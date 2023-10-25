/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.util.internal.StringUtil;

public abstract class WebSocketFrame
extends DefaultByteBufHolder {
    private final int rsv;
    private final boolean finalFragment;

    protected WebSocketFrame(boolean bl, int n, ByteBuf byteBuf) {
        super(byteBuf);
        this.finalFragment = bl;
        this.rsv = n;
    }

    @Override
    public WebSocketFrame retain() {
        super.retain();
        return this;
    }

    public int rsv() {
        return this.rsv;
    }

    public boolean isFinalFragment() {
        return this.finalFragment;
    }

    protected WebSocketFrame(ByteBuf byteBuf) {
        this(true, 0, byteBuf);
    }

    @Override
    public WebSocketFrame retain(int n) {
        super.retain(n);
        return this;
    }

    @Override
    public abstract WebSocketFrame copy();

    @Override
    public abstract WebSocketFrame duplicate();

    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + "(data: " + this.content().toString() + ')';
    }
}

