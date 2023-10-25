/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.EmptyArrays;

public class CloseWebSocketFrame
extends WebSocketFrame {
    @Override
    public CloseWebSocketFrame retain() {
        super.retain();
        return this;
    }

    @Override
    public CloseWebSocketFrame retain(int n) {
        super.retain(n);
        return this;
    }

    public int statusCode() {
        ByteBuf byteBuf = this.content();
        if (byteBuf == null || byteBuf.capacity() == 0) {
            return -1;
        }
        byteBuf.readerIndex(0);
        short s = byteBuf.readShort();
        byteBuf.readerIndex(0);
        return s;
    }

    @Override
    public CloseWebSocketFrame copy() {
        return new CloseWebSocketFrame(this.isFinalFragment(), this.rsv(), this.content().copy());
    }

    public CloseWebSocketFrame(boolean bl, int n) {
        this(bl, n, Unpooled.buffer(0));
    }

    @Override
    public CloseWebSocketFrame duplicate() {
        return new CloseWebSocketFrame(this.isFinalFragment(), this.rsv(), this.content().duplicate());
    }

    public CloseWebSocketFrame() {
        super(Unpooled.buffer(0));
    }

    public CloseWebSocketFrame(int n, String string) {
        this(true, 0, n, string);
    }

    private static ByteBuf newBinaryData(int n, String string) {
        byte[] byArray = EmptyArrays.EMPTY_BYTES;
        if (string != null) {
            byArray = string.getBytes(CharsetUtil.UTF_8);
        }
        ByteBuf byteBuf = Unpooled.buffer(2 + byArray.length);
        byteBuf.writeShort(n);
        if (byArray.length > 0) {
            byteBuf.writeBytes(byArray);
        }
        byteBuf.readerIndex(0);
        return byteBuf;
    }

    public CloseWebSocketFrame(boolean bl, int n, ByteBuf byteBuf) {
        super(bl, n, byteBuf);
    }

    public String reasonText() {
        ByteBuf byteBuf = this.content();
        if (byteBuf == null || byteBuf.capacity() <= 2) {
            return "";
        }
        byteBuf.readerIndex(2);
        String string = byteBuf.toString(CharsetUtil.UTF_8);
        byteBuf.readerIndex(0);
        return string;
    }

    public CloseWebSocketFrame(boolean bl, int n, int n2, String string) {
        super(bl, n, CloseWebSocketFrame.newBinaryData(n2, string));
    }
}

