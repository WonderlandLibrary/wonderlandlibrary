/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrameDecoder;
import java.util.List;

public class WebSocket00FrameDecoder
extends ReplayingDecoder<Void>
implements WebSocketFrameDecoder {
    private final long maxFrameSize;
    private boolean receivedClosingHandshake;
    static final int DEFAULT_MAX_FRAME_SIZE = 16384;

    public WebSocket00FrameDecoder(int n) {
        this.maxFrameSize = n;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (this.receivedClosingHandshake) {
            byteBuf.skipBytes(this.actualReadableBytes());
            return;
        }
        byte by = byteBuf.readByte();
        WebSocketFrame webSocketFrame = (by & 0x80) == 128 ? this.decodeBinaryFrame(channelHandlerContext, by, byteBuf) : this.decodeTextFrame(channelHandlerContext, byteBuf);
        if (webSocketFrame != null) {
            list.add(webSocketFrame);
        }
    }

    private WebSocketFrame decodeTextFrame(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        int n;
        int n2 = byteBuf.readerIndex();
        int n3 = byteBuf.indexOf(n2, n2 + (n = this.actualReadableBytes()), (byte)-1);
        if (n3 == -1) {
            if ((long)n > this.maxFrameSize) {
                throw new TooLongFrameException();
            }
            return null;
        }
        int n4 = n3 - n2;
        if ((long)n4 > this.maxFrameSize) {
            throw new TooLongFrameException();
        }
        ByteBuf byteBuf2 = channelHandlerContext.alloc().buffer(n4);
        byteBuf.readBytes(byteBuf2);
        byteBuf.skipBytes(1);
        int n5 = byteBuf2.indexOf(byteBuf2.readerIndex(), byteBuf2.writerIndex(), (byte)-1);
        if (n5 >= 0) {
            throw new IllegalArgumentException("a text frame should not contain 0xFF.");
        }
        return new TextWebSocketFrame(byteBuf2);
    }

    private WebSocketFrame decodeBinaryFrame(ChannelHandlerContext channelHandlerContext, byte by, ByteBuf byteBuf) {
        byte by2;
        long l = 0L;
        int n = 0;
        do {
            by2 = byteBuf.readByte();
            l <<= 7;
            if ((l |= (long)(by2 & 0x7F)) > this.maxFrameSize) {
                throw new TooLongFrameException();
            }
            if (++n <= 8) continue;
            throw new TooLongFrameException();
        } while ((by2 & 0x80) == 128);
        if (by == -1 && l == 0L) {
            this.receivedClosingHandshake = true;
            return new CloseWebSocketFrame();
        }
        ByteBuf byteBuf2 = channelHandlerContext.alloc().buffer((int)l);
        byteBuf.readBytes(byteBuf2);
        return new BinaryWebSocketFrame(byteBuf2);
    }

    public WebSocket00FrameDecoder() {
        this(16384);
    }
}

