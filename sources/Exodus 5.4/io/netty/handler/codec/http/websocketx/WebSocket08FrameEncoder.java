/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrameEncoder;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteBuffer;
import java.util.List;

public class WebSocket08FrameEncoder
extends MessageToMessageEncoder<WebSocketFrame>
implements WebSocketFrameEncoder {
    private static final byte OPCODE_CONT = 0;
    private static final byte OPCODE_PONG = 10;
    private static final byte OPCODE_PING = 9;
    private static final byte OPCODE_BINARY = 2;
    private final boolean maskPayload;
    private static final byte OPCODE_CLOSE = 8;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocket08FrameEncoder.class);
    private static final byte OPCODE_TEXT = 1;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame, List<Object> list) throws Exception {
        block27: {
            int n;
            int n2;
            int n3;
            int n4;
            ByteBuf byteBuf = webSocketFrame.content();
            if (webSocketFrame instanceof TextWebSocketFrame) {
                n4 = 1;
            } else if (webSocketFrame instanceof PingWebSocketFrame) {
                n4 = 9;
            } else if (webSocketFrame instanceof PongWebSocketFrame) {
                n4 = 10;
            } else if (webSocketFrame instanceof CloseWebSocketFrame) {
                n4 = 8;
            } else if (webSocketFrame instanceof BinaryWebSocketFrame) {
                n4 = 2;
            } else if (webSocketFrame instanceof ContinuationWebSocketFrame) {
                n4 = 0;
            } else {
                throw new UnsupportedOperationException("Cannot encode frame of type: " + webSocketFrame.getClass().getName());
            }
            int n5 = byteBuf.readableBytes();
            if (logger.isDebugEnabled()) {
                logger.debug("Encoding WebSocket Frame opCode=" + n4 + " length=" + n5);
            }
            int n6 = 0;
            if (webSocketFrame.isFinalFragment()) {
                n6 |= 0x80;
            }
            n6 |= webSocketFrame.rsv() % 8 << 4;
            n6 |= n4 % 128;
            if (n4 == 9 && n5 > 125) {
                throw new TooLongFrameException("invalid payload for PING (payload length must be <= 125, was " + n5);
            }
            boolean bl = true;
            ByteBuf byteBuf2 = null;
            int n7 = n3 = this.maskPayload ? 4 : 0;
            if (n5 <= 125) {
                n2 = 2 + n3;
                if (this.maskPayload) {
                    n2 += n5;
                }
                byteBuf2 = channelHandlerContext.alloc().buffer(n2);
                byteBuf2.writeByte(n6);
                n = this.maskPayload ? (byte)(0x80 | (byte)n5) : (byte)n5;
                byteBuf2.writeByte(n);
            } else if (n5 <= 65535) {
                n2 = 4 + n3;
                if (this.maskPayload) {
                    n2 += n5;
                }
                byteBuf2 = channelHandlerContext.alloc().buffer(n2);
                byteBuf2.writeByte(n6);
                byteBuf2.writeByte(this.maskPayload ? 254 : 126);
                byteBuf2.writeByte(n5 >>> 8 & 0xFF);
                byteBuf2.writeByte(n5 & 0xFF);
            } else {
                n2 = 10 + n3;
                if (this.maskPayload) {
                    n2 += n5;
                }
                byteBuf2 = channelHandlerContext.alloc().buffer(n2);
                byteBuf2.writeByte(n6);
                byteBuf2.writeByte(this.maskPayload ? 255 : 127);
                byteBuf2.writeLong(n5);
            }
            if (this.maskPayload) {
                n2 = (int)(Math.random() * 2.147483647E9);
                byte[] byArray = ByteBuffer.allocate(4).putInt(n2).array();
                byteBuf2.writeBytes(byArray);
                n = 0;
                for (int i = byteBuf.readerIndex(); i < byteBuf.writerIndex(); ++i) {
                    byte by = byteBuf.getByte(i);
                    byteBuf2.writeByte(by ^ byArray[n++ % 4]);
                }
                list.add(byteBuf2);
            } else if (byteBuf2.writableBytes() >= byteBuf.readableBytes()) {
                byteBuf2.writeBytes(byteBuf);
                list.add(byteBuf2);
            } else {
                list.add(byteBuf2);
                list.add(byteBuf.retain());
            }
            bl = false;
            if (!bl || byteBuf2 == null) break block27;
            byteBuf2.release();
        }
    }

    public WebSocket08FrameEncoder(boolean bl) {
        this.maskPayload = bl;
    }
}

