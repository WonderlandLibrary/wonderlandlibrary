/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.CharsetUtil;

public class TextWebSocketFrame
extends WebSocketFrame {
    public TextWebSocketFrame(ByteBuf byteBuf) {
        super(byteBuf);
    }

    private static ByteBuf fromText(String string) {
        if (string == null || string.isEmpty()) {
            return Unpooled.EMPTY_BUFFER;
        }
        return Unpooled.copiedBuffer(string, CharsetUtil.UTF_8);
    }

    @Override
    public TextWebSocketFrame retain() {
        super.retain();
        return this;
    }

    public String text() {
        return this.content().toString(CharsetUtil.UTF_8);
    }

    @Override
    public TextWebSocketFrame copy() {
        return new TextWebSocketFrame(this.isFinalFragment(), this.rsv(), this.content().copy());
    }

    @Override
    public TextWebSocketFrame retain(int n) {
        super.retain(n);
        return this;
    }

    public TextWebSocketFrame(boolean bl, int n, String string) {
        super(bl, n, TextWebSocketFrame.fromText(string));
    }

    public TextWebSocketFrame() {
        super(Unpooled.buffer(0));
    }

    public TextWebSocketFrame(boolean bl, int n, ByteBuf byteBuf) {
        super(bl, n, byteBuf);
    }

    @Override
    public TextWebSocketFrame duplicate() {
        return new TextWebSocketFrame(this.isFinalFragment(), this.rsv(), this.content().duplicate());
    }

    public TextWebSocketFrame(String string) {
        super(TextWebSocketFrame.fromText(string));
    }
}

