/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.Utf8Validator;
import io.netty.handler.codec.http.websocketx.WebSocketFrameDecoder;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.List;

public class WebSocket08FrameDecoder
extends ReplayingDecoder<State>
implements WebSocketFrameDecoder {
    private boolean frameFinalFlag;
    private boolean receivedClosingHandshake;
    private int frameRsv;
    private final boolean maskedPayload;
    private final boolean allowExtensions;
    private ByteBuf payloadBuffer;
    private Utf8Validator utf8Validator;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocket08FrameDecoder.class);
    private int framePayloadBytesRead;
    private int frameOpcode;
    private byte[] maskingKey;
    private static final byte OPCODE_PING = 9;
    private static final byte OPCODE_BINARY = 2;
    private final long maxFramePayloadLength;
    private static final byte OPCODE_TEXT = 1;
    private long framePayloadLength;
    private int fragmentedFramesCount;
    private ByteBuf framePayload;
    private static final byte OPCODE_CLOSE = 8;
    private static final byte OPCODE_PONG = 10;
    private static final byte OPCODE_CONT = 0;

    private void checkUTF8String(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        try {
            if (this.utf8Validator == null) {
                this.utf8Validator = new Utf8Validator();
            }
            this.utf8Validator.check(byteBuf);
        }
        catch (CorruptedFrameException corruptedFrameException) {
            this.protocolViolation(channelHandlerContext, corruptedFrameException);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (this.receivedClosingHandshake) {
            byteBuf.skipBytes(this.actualReadableBytes());
            return;
        }
        try {
            switch ((State)((Object)this.state())) {
                case FRAME_START: {
                    this.framePayloadBytesRead = 0;
                    this.framePayloadLength = -1L;
                    this.framePayload = null;
                    this.payloadBuffer = null;
                    byte by = byteBuf.readByte();
                    this.frameFinalFlag = (by & 0x80) != 0;
                    this.frameRsv = (by & 0x70) >> 4;
                    this.frameOpcode = by & 0xF;
                    if (logger.isDebugEnabled()) {
                        logger.debug("Decoding WebSocket Frame opCode={}", (Object)this.frameOpcode);
                    }
                    boolean bl = ((by = byteBuf.readByte()) & 0x80) != 0;
                    int n = by & 0x7F;
                    if (this.frameRsv != 0 && !this.allowExtensions) {
                        this.protocolViolation(channelHandlerContext, "RSV != 0 and no extension negotiated, RSV:" + this.frameRsv);
                        return;
                    }
                    if (this.maskedPayload && !bl) {
                        this.protocolViolation(channelHandlerContext, "unmasked client to server frame");
                        return;
                    }
                    if (this.frameOpcode > 7) {
                        if (!this.frameFinalFlag) {
                            this.protocolViolation(channelHandlerContext, "fragmented control frame");
                            return;
                        }
                        if (n > 125) {
                            this.protocolViolation(channelHandlerContext, "control frame with payload length > 125 octets");
                            return;
                        }
                        if (this.frameOpcode != 8 && this.frameOpcode != 9 && this.frameOpcode != 10) {
                            this.protocolViolation(channelHandlerContext, "control frame using reserved opcode " + this.frameOpcode);
                            return;
                        }
                        if (this.frameOpcode == 8 && n == 1) {
                            this.protocolViolation(channelHandlerContext, "received close control frame with payload len 1");
                            return;
                        }
                    } else {
                        if (this.frameOpcode != 0 && this.frameOpcode != 1 && this.frameOpcode != 2) {
                            this.protocolViolation(channelHandlerContext, "data frame using reserved opcode " + this.frameOpcode);
                            return;
                        }
                        if (this.fragmentedFramesCount == 0 && this.frameOpcode == 0) {
                            this.protocolViolation(channelHandlerContext, "received continuation data frame outside fragmented message");
                            return;
                        }
                        if (this.fragmentedFramesCount != 0 && this.frameOpcode != 0 && this.frameOpcode != 9) {
                            this.protocolViolation(channelHandlerContext, "received non-continuation data frame while inside fragmented message");
                            return;
                        }
                    }
                    if (n == 126) {
                        this.framePayloadLength = byteBuf.readUnsignedShort();
                        if (this.framePayloadLength < 126L) {
                            this.protocolViolation(channelHandlerContext, "invalid data frame length (not using minimal length encoding)");
                            return;
                        }
                    } else if (n == 127) {
                        this.framePayloadLength = byteBuf.readLong();
                        if (this.framePayloadLength < 65536L) {
                            this.protocolViolation(channelHandlerContext, "invalid data frame length (not using minimal length encoding)");
                            return;
                        }
                    } else {
                        this.framePayloadLength = n;
                    }
                    if (this.framePayloadLength > this.maxFramePayloadLength) {
                        this.protocolViolation(channelHandlerContext, "Max frame length of " + this.maxFramePayloadLength + " has been exceeded.");
                        return;
                    }
                    if (logger.isDebugEnabled()) {
                        logger.debug("Decoding WebSocket Frame length={}", (Object)this.framePayloadLength);
                    }
                    this.checkpoint(State.MASKING_KEY);
                }
                case MASKING_KEY: {
                    if (this.maskedPayload) {
                        if (this.maskingKey == null) {
                            this.maskingKey = new byte[4];
                        }
                        byteBuf.readBytes(this.maskingKey);
                    }
                    this.checkpoint(State.PAYLOAD);
                }
                case PAYLOAD: {
                    int n = this.actualReadableBytes();
                    long l = this.framePayloadBytesRead + n;
                    if (l == this.framePayloadLength) {
                        this.payloadBuffer = channelHandlerContext.alloc().buffer(n);
                        this.payloadBuffer.writeBytes(byteBuf, n);
                    } else {
                        if (l < this.framePayloadLength) {
                            if (this.framePayload == null) {
                                this.framePayload = channelHandlerContext.alloc().buffer(WebSocket08FrameDecoder.toFrameLength(this.framePayloadLength));
                            }
                            this.framePayload.writeBytes(byteBuf, n);
                            this.framePayloadBytesRead += n;
                            return;
                        }
                        if (l > this.framePayloadLength) {
                            if (this.framePayload == null) {
                                this.framePayload = channelHandlerContext.alloc().buffer(WebSocket08FrameDecoder.toFrameLength(this.framePayloadLength));
                            }
                            this.framePayload.writeBytes(byteBuf, WebSocket08FrameDecoder.toFrameLength(this.framePayloadLength - (long)this.framePayloadBytesRead));
                        }
                    }
                    this.checkpoint(State.FRAME_START);
                    if (this.framePayload == null) {
                        this.framePayload = this.payloadBuffer;
                        this.payloadBuffer = null;
                    } else if (this.payloadBuffer != null) {
                        this.framePayload.writeBytes(this.payloadBuffer);
                        this.payloadBuffer.release();
                        this.payloadBuffer = null;
                    }
                    if (this.maskedPayload) {
                        this.unmask(this.framePayload);
                    }
                    if (this.frameOpcode == 9) {
                        list.add(new PingWebSocketFrame(this.frameFinalFlag, this.frameRsv, this.framePayload));
                        this.framePayload = null;
                        return;
                    }
                    if (this.frameOpcode == 10) {
                        list.add(new PongWebSocketFrame(this.frameFinalFlag, this.frameRsv, this.framePayload));
                        this.framePayload = null;
                        return;
                    }
                    if (this.frameOpcode == 8) {
                        this.checkCloseFrameBody(channelHandlerContext, this.framePayload);
                        this.receivedClosingHandshake = true;
                        list.add(new CloseWebSocketFrame(this.frameFinalFlag, this.frameRsv, this.framePayload));
                        this.framePayload = null;
                        return;
                    }
                    if (this.frameFinalFlag) {
                        if (this.frameOpcode != 9) {
                            this.fragmentedFramesCount = 0;
                            if (this.frameOpcode == 1 || this.utf8Validator != null && this.utf8Validator.isChecking()) {
                                this.checkUTF8String(channelHandlerContext, this.framePayload);
                                this.utf8Validator.finish();
                            }
                        }
                    } else {
                        if (this.fragmentedFramesCount == 0) {
                            if (this.frameOpcode == 1) {
                                this.checkUTF8String(channelHandlerContext, this.framePayload);
                            }
                        } else if (this.utf8Validator != null && this.utf8Validator.isChecking()) {
                            this.checkUTF8String(channelHandlerContext, this.framePayload);
                        }
                        ++this.fragmentedFramesCount;
                    }
                    if (this.frameOpcode == 1) {
                        list.add(new TextWebSocketFrame(this.frameFinalFlag, this.frameRsv, this.framePayload));
                        this.framePayload = null;
                        return;
                    }
                    if (this.frameOpcode == 2) {
                        list.add(new BinaryWebSocketFrame(this.frameFinalFlag, this.frameRsv, this.framePayload));
                        this.framePayload = null;
                        return;
                    }
                    if (this.frameOpcode == 0) {
                        list.add(new ContinuationWebSocketFrame(this.frameFinalFlag, this.frameRsv, this.framePayload));
                        this.framePayload = null;
                        return;
                    }
                    throw new UnsupportedOperationException("Cannot decode web socket frame with opcode: " + this.frameOpcode);
                }
                case CORRUPT: {
                    byteBuf.readByte();
                    return;
                }
            }
            throw new Error("Shouldn't reach here.");
        }
        catch (Exception exception) {
            if (this.payloadBuffer != null) {
                if (this.payloadBuffer.refCnt() > 0) {
                    this.payloadBuffer.release();
                }
                this.payloadBuffer = null;
            }
            if (this.framePayload != null) {
                if (this.framePayload.refCnt() > 0) {
                    this.framePayload.release();
                }
                this.framePayload = null;
            }
            throw exception;
        }
    }

    public WebSocket08FrameDecoder(boolean bl, boolean bl2, int n) {
        super(State.FRAME_START);
        this.maskedPayload = bl;
        this.allowExtensions = bl2;
        this.maxFramePayloadLength = n;
    }

    private void protocolViolation(ChannelHandlerContext channelHandlerContext, CorruptedFrameException corruptedFrameException) {
        this.checkpoint(State.CORRUPT);
        if (channelHandlerContext.channel().isActive()) {
            channelHandlerContext.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
        throw corruptedFrameException;
    }

    protected void checkCloseFrameBody(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        if (byteBuf == null || !byteBuf.isReadable()) {
            return;
        }
        if (byteBuf.readableBytes() == 1) {
            this.protocolViolation(channelHandlerContext, "Invalid close frame body");
        }
        int n = byteBuf.readerIndex();
        byteBuf.readerIndex(0);
        short s = byteBuf.readShort();
        if (s >= 0 && s <= 999 || s >= 1004 && s <= 1006 || s >= 1012 && s <= 2999) {
            this.protocolViolation(channelHandlerContext, "Invalid close frame getStatus code: " + s);
        }
        if (byteBuf.isReadable()) {
            try {
                new Utf8Validator().check(byteBuf);
            }
            catch (CorruptedFrameException corruptedFrameException) {
                this.protocolViolation(channelHandlerContext, corruptedFrameException);
            }
        }
        byteBuf.readerIndex(n);
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.channelInactive(channelHandlerContext);
        if (this.framePayload != null) {
            this.framePayload.release();
        }
        if (this.payloadBuffer != null) {
            this.payloadBuffer.release();
        }
    }

    private static int toFrameLength(long l) {
        if (l > Integer.MAX_VALUE) {
            throw new TooLongFrameException("Length:" + l);
        }
        return (int)l;
    }

    private void unmask(ByteBuf byteBuf) {
        for (int i = byteBuf.readerIndex(); i < byteBuf.writerIndex(); ++i) {
            byteBuf.setByte(i, byteBuf.getByte(i) ^ this.maskingKey[i % 4]);
        }
    }

    private void protocolViolation(ChannelHandlerContext channelHandlerContext, String string) {
        this.protocolViolation(channelHandlerContext, new CorruptedFrameException(string));
    }

    static enum State {
        FRAME_START,
        MASKING_KEY,
        PAYLOAD,
        CORRUPT;

    }
}

