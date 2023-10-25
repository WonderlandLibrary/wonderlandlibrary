/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class PongWebSocketFrame
extends WebSocketFrame {
    @Override
    public PongWebSocketFrame retain() {
        super.retain();
        return this;
    }

    @Override
    public PongWebSocketFrame copy() {
        return new PongWebSocketFrame(this.isFinalFragment(), this.rsv(), this.content().copy());
    }

    @Override
    public PongWebSocketFrame duplicate() {
        return new PongWebSocketFrame(this.isFinalFragment(), this.rsv(), this.content().duplicate());
    }

    public PongWebSocketFrame(boolean bl, int n, ByteBuf byteBuf) {
        super(bl, n, byteBuf);
    }

    @Override
    public PongWebSocketFrame retain(int n) {
        super.retain(n);
        return this;
    }

    public PongWebSocketFrame() {
        super(Unpooled.buffer(0));
    }

    public PongWebSocketFrame(ByteBuf byteBuf) {
        super(byteBuf);
    }
}

