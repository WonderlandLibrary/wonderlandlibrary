/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.spdy.SpdyCodecUtil;
import io.netty.handler.codec.spdy.SpdyFrameDecoderDelegate;
import io.netty.handler.codec.spdy.SpdyVersion;

public class SpdyFrameDecoder {
    private int streamId;
    private byte flags;
    private final SpdyFrameDecoderDelegate delegate;
    private final int spdyVersion;
    private int length;
    private int numSettings;
    private final int maxChunkSize;
    private State state;

    private static boolean isValidFrameHeader(int n, int n2, byte by, int n3) {
        switch (n2) {
            case 0: {
                return n != 0;
            }
            case 1: {
                return n3 >= 10;
            }
            case 2: {
                return n3 >= 4;
            }
            case 3: {
                return by == 0 && n3 == 8;
            }
            case 4: {
                return n3 >= 4;
            }
            case 6: {
                return n3 == 4;
            }
            case 7: {
                return n3 == 8;
            }
            case 8: {
                return n3 >= 4;
            }
            case 9: {
                return n3 == 8;
            }
        }
        return true;
    }

    public void decode(ByteBuf byteBuf) {
        block16: while (true) {
            switch (this.state) {
                case READ_COMMON_HEADER: {
                    int n;
                    int n2;
                    boolean bl;
                    if (byteBuf.readableBytes() < 8) {
                        return;
                    }
                    int n3 = byteBuf.readerIndex();
                    int n4 = n3 + 4;
                    int n5 = n3 + 5;
                    byteBuf.skipBytes(8);
                    boolean bl2 = bl = (byteBuf.getByte(n3) & 0x80) != 0;
                    if (bl) {
                        n2 = SpdyCodecUtil.getUnsignedShort(byteBuf, n3) & Short.MAX_VALUE;
                        n = SpdyCodecUtil.getUnsignedShort(byteBuf, n3 + 2);
                        this.streamId = 0;
                    } else {
                        n2 = this.spdyVersion;
                        n = 0;
                        this.streamId = SpdyCodecUtil.getUnsignedInt(byteBuf, n3);
                    }
                    this.flags = byteBuf.getByte(n4);
                    this.length = SpdyCodecUtil.getUnsignedMedium(byteBuf, n5);
                    if (n2 != this.spdyVersion) {
                        this.state = State.FRAME_ERROR;
                        this.delegate.readFrameError("Invalid SPDY Version");
                        continue block16;
                    }
                    if (!SpdyFrameDecoder.isValidFrameHeader(this.streamId, n, this.flags, this.length)) {
                        this.state = State.FRAME_ERROR;
                        this.delegate.readFrameError("Invalid Frame Error");
                        continue block16;
                    }
                    this.state = SpdyFrameDecoder.getNextState(n, this.length);
                    continue block16;
                }
                case READ_DATA_FRAME: {
                    if (this.length == 0) {
                        this.state = State.READ_COMMON_HEADER;
                        this.delegate.readDataFrame(this.streamId, SpdyFrameDecoder.hasFlag(this.flags, (byte)1), Unpooled.buffer(0));
                        continue block16;
                    }
                    int n = Math.min(this.maxChunkSize, this.length);
                    if (byteBuf.readableBytes() < n) {
                        return;
                    }
                    ByteBuf byteBuf2 = byteBuf.alloc().buffer(n);
                    byteBuf2.writeBytes(byteBuf, n);
                    this.length -= n;
                    if (this.length == 0) {
                        this.state = State.READ_COMMON_HEADER;
                    }
                    boolean bl = this.length == 0 && SpdyFrameDecoder.hasFlag(this.flags, (byte)1);
                    this.delegate.readDataFrame(this.streamId, bl, byteBuf2);
                    continue block16;
                }
                case READ_SYN_STREAM_FRAME: {
                    if (byteBuf.readableBytes() < 10) {
                        return;
                    }
                    int n = byteBuf.readerIndex();
                    this.streamId = SpdyCodecUtil.getUnsignedInt(byteBuf, n);
                    int n6 = SpdyCodecUtil.getUnsignedInt(byteBuf, n + 4);
                    byte by = (byte)(byteBuf.getByte(n + 8) >> 5 & 7);
                    boolean bl = SpdyFrameDecoder.hasFlag(this.flags, (byte)1);
                    boolean bl3 = SpdyFrameDecoder.hasFlag(this.flags, (byte)2);
                    byteBuf.skipBytes(10);
                    this.length -= 10;
                    if (this.streamId == 0) {
                        this.state = State.FRAME_ERROR;
                        this.delegate.readFrameError("Invalid SYN_STREAM Frame");
                        continue block16;
                    }
                    this.state = State.READ_HEADER_BLOCK;
                    this.delegate.readSynStreamFrame(this.streamId, n6, by, bl, bl3);
                    continue block16;
                }
                case READ_SYN_REPLY_FRAME: {
                    if (byteBuf.readableBytes() < 4) {
                        return;
                    }
                    this.streamId = SpdyCodecUtil.getUnsignedInt(byteBuf, byteBuf.readerIndex());
                    boolean bl = SpdyFrameDecoder.hasFlag(this.flags, (byte)1);
                    byteBuf.skipBytes(4);
                    this.length -= 4;
                    if (this.streamId == 0) {
                        this.state = State.FRAME_ERROR;
                        this.delegate.readFrameError("Invalid SYN_REPLY Frame");
                        continue block16;
                    }
                    this.state = State.READ_HEADER_BLOCK;
                    this.delegate.readSynReplyFrame(this.streamId, bl);
                    continue block16;
                }
                case READ_RST_STREAM_FRAME: {
                    if (byteBuf.readableBytes() < 8) {
                        return;
                    }
                    this.streamId = SpdyCodecUtil.getUnsignedInt(byteBuf, byteBuf.readerIndex());
                    int n8 = SpdyCodecUtil.getSignedInt(byteBuf, byteBuf.readerIndex() + 4);
                    byteBuf.skipBytes(8);
                    if (this.streamId == 0 || n8 == 0) {
                        this.state = State.FRAME_ERROR;
                        this.delegate.readFrameError("Invalid RST_STREAM Frame");
                        continue block16;
                    }
                    this.state = State.READ_COMMON_HEADER;
                    this.delegate.readRstStreamFrame(this.streamId, n8);
                    continue block16;
                }
                case READ_SETTINGS_FRAME: {
                    if (byteBuf.readableBytes() < 4) {
                        return;
                    }
                    boolean bl = SpdyFrameDecoder.hasFlag(this.flags, (byte)1);
                    this.numSettings = SpdyCodecUtil.getUnsignedInt(byteBuf, byteBuf.readerIndex());
                    byteBuf.skipBytes(4);
                    this.length -= 4;
                    if ((this.length & 7) != 0 || this.length >> 3 != this.numSettings) {
                        this.state = State.FRAME_ERROR;
                        this.delegate.readFrameError("Invalid SETTINGS Frame");
                        continue block16;
                    }
                    this.state = State.READ_SETTING;
                    this.delegate.readSettingsFrame(bl);
                    continue block16;
                }
                case READ_SETTING: {
                    if (this.numSettings == 0) {
                        this.state = State.READ_COMMON_HEADER;
                        this.delegate.readSettingsEnd();
                        continue block16;
                    }
                    if (byteBuf.readableBytes() < 8) {
                        return;
                    }
                    byte by = byteBuf.getByte(byteBuf.readerIndex());
                    int n = SpdyCodecUtil.getUnsignedMedium(byteBuf, byteBuf.readerIndex() + 1);
                    int n7 = SpdyCodecUtil.getSignedInt(byteBuf, byteBuf.readerIndex() + 4);
                    boolean bl = SpdyFrameDecoder.hasFlag(by, (byte)1);
                    boolean bl4 = SpdyFrameDecoder.hasFlag(by, (byte)2);
                    byteBuf.skipBytes(8);
                    --this.numSettings;
                    this.delegate.readSetting(n, n7, bl, bl4);
                    continue block16;
                }
                case READ_PING_FRAME: {
                    if (byteBuf.readableBytes() < 4) {
                        return;
                    }
                    int n = SpdyCodecUtil.getSignedInt(byteBuf, byteBuf.readerIndex());
                    byteBuf.skipBytes(4);
                    this.state = State.READ_COMMON_HEADER;
                    this.delegate.readPingFrame(n);
                    continue block16;
                }
                case READ_GOAWAY_FRAME: {
                    if (byteBuf.readableBytes() < 8) {
                        return;
                    }
                    int n = SpdyCodecUtil.getUnsignedInt(byteBuf, byteBuf.readerIndex());
                    int n8 = SpdyCodecUtil.getSignedInt(byteBuf, byteBuf.readerIndex() + 4);
                    byteBuf.skipBytes(8);
                    this.state = State.READ_COMMON_HEADER;
                    this.delegate.readGoAwayFrame(n, n8);
                    continue block16;
                }
                case READ_HEADERS_FRAME: {
                    if (byteBuf.readableBytes() < 4) {
                        return;
                    }
                    this.streamId = SpdyCodecUtil.getUnsignedInt(byteBuf, byteBuf.readerIndex());
                    boolean bl = SpdyFrameDecoder.hasFlag(this.flags, (byte)1);
                    byteBuf.skipBytes(4);
                    this.length -= 4;
                    if (this.streamId == 0) {
                        this.state = State.FRAME_ERROR;
                        this.delegate.readFrameError("Invalid HEADERS Frame");
                        continue block16;
                    }
                    this.state = State.READ_HEADER_BLOCK;
                    this.delegate.readHeadersFrame(this.streamId, bl);
                    continue block16;
                }
                case READ_WINDOW_UPDATE_FRAME: {
                    if (byteBuf.readableBytes() < 8) {
                        return;
                    }
                    this.streamId = SpdyCodecUtil.getUnsignedInt(byteBuf, byteBuf.readerIndex());
                    int n = SpdyCodecUtil.getUnsignedInt(byteBuf, byteBuf.readerIndex() + 4);
                    byteBuf.skipBytes(8);
                    if (n == 0) {
                        this.state = State.FRAME_ERROR;
                        this.delegate.readFrameError("Invalid WINDOW_UPDATE Frame");
                        continue block16;
                    }
                    this.state = State.READ_COMMON_HEADER;
                    this.delegate.readWindowUpdateFrame(this.streamId, n);
                    continue block16;
                }
                case READ_HEADER_BLOCK: {
                    if (this.length == 0) {
                        this.state = State.READ_COMMON_HEADER;
                        this.delegate.readHeaderBlockEnd();
                        continue block16;
                    }
                    if (!byteBuf.isReadable()) {
                        return;
                    }
                    int n = Math.min(byteBuf.readableBytes(), this.length);
                    ByteBuf byteBuf3 = byteBuf.alloc().buffer(n);
                    byteBuf3.writeBytes(byteBuf, n);
                    this.length -= n;
                    this.delegate.readHeaderBlock(byteBuf3);
                    continue block16;
                }
                case DISCARD_FRAME: {
                    int n = Math.min(byteBuf.readableBytes(), this.length);
                    byteBuf.skipBytes(n);
                    this.length -= n;
                    if (this.length == 0) {
                        this.state = State.READ_COMMON_HEADER;
                        continue block16;
                    }
                    return;
                }
                case FRAME_ERROR: {
                    byteBuf.skipBytes(byteBuf.readableBytes());
                    return;
                }
            }
            break;
        }
        throw new Error("Shouldn't reach here.");
    }

    private static State getNextState(int n, int n2) {
        switch (n) {
            case 0: {
                return State.READ_DATA_FRAME;
            }
            case 1: {
                return State.READ_SYN_STREAM_FRAME;
            }
            case 2: {
                return State.READ_SYN_REPLY_FRAME;
            }
            case 3: {
                return State.READ_RST_STREAM_FRAME;
            }
            case 4: {
                return State.READ_SETTINGS_FRAME;
            }
            case 6: {
                return State.READ_PING_FRAME;
            }
            case 7: {
                return State.READ_GOAWAY_FRAME;
            }
            case 8: {
                return State.READ_HEADERS_FRAME;
            }
            case 9: {
                return State.READ_WINDOW_UPDATE_FRAME;
            }
        }
        if (n2 != 0) {
            return State.DISCARD_FRAME;
        }
        return State.READ_COMMON_HEADER;
    }

    public SpdyFrameDecoder(SpdyVersion spdyVersion, SpdyFrameDecoderDelegate spdyFrameDecoderDelegate) {
        this(spdyVersion, spdyFrameDecoderDelegate, 8192);
    }

    private static boolean hasFlag(byte by, byte by2) {
        return (by & by2) != 0;
    }

    public SpdyFrameDecoder(SpdyVersion spdyVersion, SpdyFrameDecoderDelegate spdyFrameDecoderDelegate, int n) {
        if (spdyVersion == null) {
            throw new NullPointerException("spdyVersion");
        }
        if (spdyFrameDecoderDelegate == null) {
            throw new NullPointerException("delegate");
        }
        if (n <= 0) {
            throw new IllegalArgumentException("maxChunkSize must be a positive integer: " + n);
        }
        this.spdyVersion = spdyVersion.getVersion();
        this.delegate = spdyFrameDecoderDelegate;
        this.maxChunkSize = n;
        this.state = State.READ_COMMON_HEADER;
    }

    private static enum State {
        READ_COMMON_HEADER,
        READ_DATA_FRAME,
        READ_SYN_STREAM_FRAME,
        READ_SYN_REPLY_FRAME,
        READ_RST_STREAM_FRAME,
        READ_SETTINGS_FRAME,
        READ_SETTING,
        READ_PING_FRAME,
        READ_GOAWAY_FRAME,
        READ_HEADERS_FRAME,
        READ_WINDOW_UPDATE_FRAME,
        READ_HEADER_BLOCK,
        DISCARD_FRAME,
        FRAME_ERROR;

    }
}

