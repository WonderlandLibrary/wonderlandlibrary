/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.CharsetUtil;

public class ContinuationWebSocketFrame
extends WebSocketFrame {
    private String aggregatedText;

    public ContinuationWebSocketFrame() {
        super(Unpooled.buffer(0));
    }

    public ContinuationWebSocketFrame(ByteBuf binaryData) {
        super(binaryData);
    }

    public ContinuationWebSocketFrame(boolean finalFragment, int rsv, ByteBuf binaryData) {
        super(finalFragment, rsv, binaryData);
    }

    public ContinuationWebSocketFrame(boolean finalFragment, int rsv, ByteBuf binaryData, String aggregatedText) {
        super(finalFragment, rsv, binaryData);
        this.aggregatedText = aggregatedText;
    }

    public ContinuationWebSocketFrame(boolean finalFragment, int rsv, String text) {
        this(finalFragment, rsv, ContinuationWebSocketFrame.fromText(text), null);
    }

    public String text() {
        return this.content().toString(CharsetUtil.UTF_8);
    }

    private static ByteBuf fromText(String text) {
        if (text == null || text.isEmpty()) {
            return Unpooled.EMPTY_BUFFER;
        }
        return Unpooled.copiedBuffer(text, CharsetUtil.UTF_8);
    }

    public String aggregatedText() {
        return this.aggregatedText;
    }

    @Override
    public ContinuationWebSocketFrame copy() {
        return new ContinuationWebSocketFrame(this.isFinalFragment(), this.rsv(), this.content().copy(), this.aggregatedText());
    }

    @Override
    public ContinuationWebSocketFrame duplicate() {
        return new ContinuationWebSocketFrame(this.isFinalFragment(), this.rsv(), this.content().duplicate(), this.aggregatedText());
    }

    @Override
    public ContinuationWebSocketFrame retain() {
        super.retain();
        return this;
    }

    @Override
    public ContinuationWebSocketFrame retain(int increment) {
        super.retain(increment);
        return this;
    }
}

