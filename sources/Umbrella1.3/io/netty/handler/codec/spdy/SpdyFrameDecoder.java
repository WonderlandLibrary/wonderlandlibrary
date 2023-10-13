/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.spdy.DefaultSpdyDataFrame;
import io.netty.handler.codec.spdy.DefaultSpdyGoAwayFrame;
import io.netty.handler.codec.spdy.DefaultSpdyHeadersFrame;
import io.netty.handler.codec.spdy.DefaultSpdyPingFrame;
import io.netty.handler.codec.spdy.DefaultSpdyRstStreamFrame;
import io.netty.handler.codec.spdy.DefaultSpdySettingsFrame;
import io.netty.handler.codec.spdy.DefaultSpdySynReplyFrame;
import io.netty.handler.codec.spdy.DefaultSpdySynStreamFrame;
import io.netty.handler.codec.spdy.DefaultSpdyWindowUpdateFrame;
import io.netty.handler.codec.spdy.SpdyCodecUtil;
import io.netty.handler.codec.spdy.SpdyHeaderBlockDecoder;
import io.netty.handler.codec.spdy.SpdyHeadersFrame;
import io.netty.handler.codec.spdy.SpdyProtocolException;
import io.netty.handler.codec.spdy.SpdySettingsFrame;
import io.netty.handler.codec.spdy.SpdyVersion;
import java.util.List;

public class SpdyFrameDecoder
extends ByteToMessageDecoder {
    private static final SpdyProtocolException INVALID_FRAME = new SpdyProtocolException("Received invalid frame");
    private final int spdyVersion;
    private final int maxChunkSize;
    private final SpdyHeaderBlockDecoder headerBlockDecoder;
    private State state;
    private SpdySettingsFrame spdySettingsFrame;
    private SpdyHeadersFrame spdyHeadersFrame;
    private byte flags;
    private int length;
    private int version;
    private int type;
    private int streamId;

    public SpdyFrameDecoder(SpdyVersion version) {
        this(version, 8192, 16384);
    }

    public SpdyFrameDecoder(SpdyVersion version, int maxChunkSize, int maxHeaderSize) {
        this(version, maxChunkSize, SpdyHeaderBlockDecoder.newInstance(version, maxHeaderSize));
    }

    protected SpdyFrameDecoder(SpdyVersion version, int maxChunkSize, SpdyHeaderBlockDecoder headerBlockDecoder) {
        if (version == null) {
            throw new NullPointerException("version");
        }
        if (maxChunkSize <= 0) {
            throw new IllegalArgumentException("maxChunkSize must be a positive integer: " + maxChunkSize);
        }
        this.spdyVersion = version.getVersion();
        this.maxChunkSize = maxChunkSize;
        this.headerBlockDecoder = headerBlockDecoder;
        this.state = State.READ_COMMON_HEADER;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            this.decode(ctx, in, out);
        }
        finally {
            this.headerBlockDecoder.end();
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        switch (this.state) {
            case READ_COMMON_HEADER: {
                this.state = this.readCommonHeader(buffer);
                if (this.state == State.FRAME_ERROR) {
                    if (this.version != this.spdyVersion) {
                        SpdyFrameDecoder.fireProtocolException(ctx, "Unsupported version: " + this.version);
                    } else {
                        SpdyFrameDecoder.fireInvalidFrameException(ctx);
                    }
                }
                if (this.length == 0) {
                    if (this.state == State.READ_DATA_FRAME) {
                        DefaultSpdyDataFrame spdyDataFrame = new DefaultSpdyDataFrame(this.streamId);
                        spdyDataFrame.setLast((this.flags & 1) != 0);
                        this.state = State.READ_COMMON_HEADER;
                        out.add(spdyDataFrame);
                        return;
                    }
                    this.state = State.READ_COMMON_HEADER;
                }
                return;
            }
            case READ_CONTROL_FRAME: {
                try {
                    Object frame = this.readControlFrame(buffer);
                    if (frame != null) {
                        this.state = State.READ_COMMON_HEADER;
                        out.add(frame);
                    }
                    return;
                }
                catch (IllegalArgumentException e) {
                    this.state = State.FRAME_ERROR;
                    SpdyFrameDecoder.fireInvalidFrameException(ctx);
                    return;
                }
            }
            case READ_SETTINGS_FRAME: {
                if (this.spdySettingsFrame == null) {
                    if (buffer.readableBytes() < 4) {
                        return;
                    }
                    int numEntries = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
                    buffer.skipBytes(4);
                    this.length -= 4;
                    if ((this.length & 7) != 0 || this.length >> 3 != numEntries) {
                        this.state = State.FRAME_ERROR;
                        SpdyFrameDecoder.fireInvalidFrameException(ctx);
                        return;
                    }
                    this.spdySettingsFrame = new DefaultSpdySettingsFrame();
                    boolean clear = (this.flags & 1) != 0;
                    this.spdySettingsFrame.setClearPreviouslyPersistedSettings(clear);
                }
                int readableEntries = Math.min(buffer.readableBytes() >> 3, this.length >> 3);
                for (int i = 0; i < readableEntries; ++i) {
                    byte ID_flags = buffer.getByte(buffer.readerIndex());
                    int ID = SpdyCodecUtil.getUnsignedMedium(buffer, buffer.readerIndex() + 1);
                    int value = SpdyCodecUtil.getSignedInt(buffer, buffer.readerIndex() + 4);
                    buffer.skipBytes(8);
                    if (this.spdySettingsFrame.isSet(ID)) continue;
                    boolean persistVal = (ID_flags & 1) != 0;
                    boolean persisted = (ID_flags & 2) != 0;
                    this.spdySettingsFrame.setValue(ID, value, persistVal, persisted);
                }
                this.length -= 8 * readableEntries;
                if (this.length == 0) {
                    this.state = State.READ_COMMON_HEADER;
                    SpdySettingsFrame frame = this.spdySettingsFrame;
                    this.spdySettingsFrame = null;
                    out.add(frame);
                    return;
                }
                return;
            }
            case READ_HEADER_BLOCK_FRAME: {
                try {
                    this.spdyHeadersFrame = this.readHeaderBlockFrame(buffer);
                    if (this.spdyHeadersFrame != null) {
                        if (this.length == 0) {
                            this.state = State.READ_COMMON_HEADER;
                            SpdyHeadersFrame frame = this.spdyHeadersFrame;
                            this.spdyHeadersFrame = null;
                            out.add(frame);
                            return;
                        }
                        this.state = State.READ_HEADER_BLOCK;
                    }
                    return;
                }
                catch (IllegalArgumentException e) {
                    this.state = State.FRAME_ERROR;
                    SpdyFrameDecoder.fireInvalidFrameException(ctx);
                    return;
                }
            }
            case READ_HEADER_BLOCK: {
                int compressedBytes = Math.min(buffer.readableBytes(), this.length);
                ByteBuf compressed = buffer.slice(buffer.readerIndex(), compressedBytes);
                try {
                    this.headerBlockDecoder.decode(compressed, this.spdyHeadersFrame);
                }
                catch (Exception e) {
                    this.state = State.FRAME_ERROR;
                    this.spdyHeadersFrame = null;
                    ctx.fireExceptionCaught(e);
                    return;
                }
                int readBytes = compressedBytes - compressed.readableBytes();
                buffer.skipBytes(readBytes);
                this.length -= readBytes;
                if (this.spdyHeadersFrame != null && (this.spdyHeadersFrame.isInvalid() || this.spdyHeadersFrame.isTruncated())) {
                    SpdyHeadersFrame frame = this.spdyHeadersFrame;
                    this.spdyHeadersFrame = null;
                    if (this.length == 0) {
                        this.headerBlockDecoder.reset();
                        this.state = State.READ_COMMON_HEADER;
                    }
                    out.add(frame);
                    return;
                }
                if (this.length == 0) {
                    SpdyHeadersFrame frame = this.spdyHeadersFrame;
                    this.spdyHeadersFrame = null;
                    this.headerBlockDecoder.reset();
                    this.state = State.READ_COMMON_HEADER;
                    if (frame != null) {
                        out.add(frame);
                    }
                }
                return;
            }
            case READ_DATA_FRAME: {
                if (this.streamId == 0) {
                    this.state = State.FRAME_ERROR;
                    SpdyFrameDecoder.fireProtocolException(ctx, "Received invalid data frame");
                    return;
                }
                int dataLength = Math.min(this.maxChunkSize, this.length);
                if (buffer.readableBytes() < dataLength) {
                    return;
                }
                ByteBuf data = ctx.alloc().buffer(dataLength);
                data.writeBytes(buffer, dataLength);
                DefaultSpdyDataFrame spdyDataFrame = new DefaultSpdyDataFrame(this.streamId, data);
                this.length -= dataLength;
                if (this.length == 0) {
                    spdyDataFrame.setLast((this.flags & 1) != 0);
                    this.state = State.READ_COMMON_HEADER;
                }
                out.add(spdyDataFrame);
                return;
            }
            case DISCARD_FRAME: {
                int numBytes = Math.min(buffer.readableBytes(), this.length);
                buffer.skipBytes(numBytes);
                this.length -= numBytes;
                if (this.length == 0) {
                    this.state = State.READ_COMMON_HEADER;
                }
                return;
            }
            case FRAME_ERROR: {
                buffer.skipBytes(buffer.readableBytes());
                return;
            }
        }
        throw new Error("Shouldn't reach here.");
    }

    private State readCommonHeader(ByteBuf buffer) {
        State nextState;
        if (buffer.readableBytes() < 8) {
            return State.READ_COMMON_HEADER;
        }
        int frameOffset = buffer.readerIndex();
        int flagsOffset = frameOffset + 4;
        int lengthOffset = frameOffset + 5;
        buffer.skipBytes(8);
        boolean control = (buffer.getByte(frameOffset) & 0x80) != 0;
        this.flags = buffer.getByte(flagsOffset);
        this.length = SpdyCodecUtil.getUnsignedMedium(buffer, lengthOffset);
        if (control) {
            this.version = SpdyCodecUtil.getUnsignedShort(buffer, frameOffset) & 0x7FFF;
            int typeOffset = frameOffset + 2;
            this.type = SpdyCodecUtil.getUnsignedShort(buffer, typeOffset);
            this.streamId = 0;
        } else {
            this.version = this.spdyVersion;
            this.type = 0;
            this.streamId = SpdyCodecUtil.getUnsignedInt(buffer, frameOffset);
        }
        if (this.version != this.spdyVersion || !this.isValidFrameHeader()) {
            return State.FRAME_ERROR;
        }
        if (this.willGenerateFrame()) {
            switch (this.type) {
                case 0: {
                    nextState = State.READ_DATA_FRAME;
                    break;
                }
                case 1: 
                case 2: 
                case 8: {
                    nextState = State.READ_HEADER_BLOCK_FRAME;
                    break;
                }
                case 4: {
                    nextState = State.READ_SETTINGS_FRAME;
                    break;
                }
                default: {
                    nextState = State.READ_CONTROL_FRAME;
                    break;
                }
            }
        } else {
            nextState = this.length != 0 ? State.DISCARD_FRAME : State.READ_COMMON_HEADER;
        }
        return nextState;
    }

    private Object readControlFrame(ByteBuf buffer) {
        switch (this.type) {
            case 3: {
                if (buffer.readableBytes() < 8) {
                    return null;
                }
                int streamId = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
                int statusCode = SpdyCodecUtil.getSignedInt(buffer, buffer.readerIndex() + 4);
                buffer.skipBytes(8);
                return new DefaultSpdyRstStreamFrame(streamId, statusCode);
            }
            case 6: {
                if (buffer.readableBytes() < 4) {
                    return null;
                }
                int ID = SpdyCodecUtil.getSignedInt(buffer, buffer.readerIndex());
                buffer.skipBytes(4);
                return new DefaultSpdyPingFrame(ID);
            }
            case 7: {
                if (buffer.readableBytes() < 8) {
                    return null;
                }
                int lastGoodStreamId = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
                int statusCode = SpdyCodecUtil.getSignedInt(buffer, buffer.readerIndex() + 4);
                buffer.skipBytes(8);
                return new DefaultSpdyGoAwayFrame(lastGoodStreamId, statusCode);
            }
            case 9: {
                if (buffer.readableBytes() < 8) {
                    return null;
                }
                int streamId = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
                int deltaWindowSize = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex() + 4);
                buffer.skipBytes(8);
                return new DefaultSpdyWindowUpdateFrame(streamId, deltaWindowSize);
            }
        }
        throw new Error("Shouldn't reach here.");
    }

    private SpdyHeadersFrame readHeaderBlockFrame(ByteBuf buffer) {
        switch (this.type) {
            case 1: {
                if (buffer.readableBytes() < 10) {
                    return null;
                }
                int offset = buffer.readerIndex();
                int streamId = SpdyCodecUtil.getUnsignedInt(buffer, offset);
                int associatedToStreamId = SpdyCodecUtil.getUnsignedInt(buffer, offset + 4);
                byte priority = (byte)(buffer.getByte(offset + 8) >> 5 & 7);
                buffer.skipBytes(10);
                this.length -= 10;
                DefaultSpdySynStreamFrame spdySynStreamFrame = new DefaultSpdySynStreamFrame(streamId, associatedToStreamId, priority);
                spdySynStreamFrame.setLast((this.flags & 1) != 0);
                spdySynStreamFrame.setUnidirectional((this.flags & 2) != 0);
                return spdySynStreamFrame;
            }
            case 2: {
                if (buffer.readableBytes() < 4) {
                    return null;
                }
                int streamId = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
                buffer.skipBytes(4);
                this.length -= 4;
                DefaultSpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamId);
                spdySynReplyFrame.setLast((this.flags & 1) != 0);
                return spdySynReplyFrame;
            }
            case 8: {
                if (buffer.readableBytes() < 4) {
                    return null;
                }
                int streamId = SpdyCodecUtil.getUnsignedInt(buffer, buffer.readerIndex());
                buffer.skipBytes(4);
                this.length -= 4;
                DefaultSpdyHeadersFrame spdyHeadersFrame = new DefaultSpdyHeadersFrame(streamId);
                spdyHeadersFrame.setLast((this.flags & 1) != 0);
                return spdyHeadersFrame;
            }
        }
        throw new Error("Shouldn't reach here.");
    }

    private boolean isValidFrameHeader() {
        switch (this.type) {
            case 0: {
                return this.streamId != 0;
            }
            case 1: {
                return this.length >= 10;
            }
            case 2: {
                return this.length >= 4;
            }
            case 3: {
                return this.flags == 0 && this.length == 8;
            }
            case 4: {
                return this.length >= 4;
            }
            case 6: {
                return this.length == 4;
            }
            case 7: {
                return this.length == 8;
            }
            case 8: {
                return this.length >= 4;
            }
            case 9: {
                return this.length == 8;
            }
        }
        return true;
    }

    private boolean willGenerateFrame() {
        switch (this.type) {
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 6: 
            case 7: 
            case 8: 
            case 9: {
                return true;
            }
        }
        return false;
    }

    private static void fireInvalidFrameException(ChannelHandlerContext ctx) {
        ctx.fireExceptionCaught(INVALID_FRAME);
    }

    private static void fireProtocolException(ChannelHandlerContext ctx, String message) {
        ctx.fireExceptionCaught(new SpdyProtocolException(message));
    }

    private static enum State {
        READ_COMMON_HEADER,
        READ_CONTROL_FRAME,
        READ_SETTINGS_FRAME,
        READ_HEADER_BLOCK_FRAME,
        READ_HEADER_BLOCK,
        READ_DATA_FRAME,
        DISCARD_FRAME,
        FRAME_ERROR;

    }
}

