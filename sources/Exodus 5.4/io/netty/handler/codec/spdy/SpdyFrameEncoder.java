/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.spdy.SpdySettingsFrame;
import io.netty.handler.codec.spdy.SpdyVersion;
import java.nio.ByteOrder;
import java.util.Set;

public class SpdyFrameEncoder {
    private final int version;

    public ByteBuf encodeGoAwayFrame(ByteBufAllocator byteBufAllocator, int n, int n2) {
        byte by = 0;
        int n3 = 8;
        ByteBuf byteBuf = byteBufAllocator.ioBuffer(8 + n3).order(ByteOrder.BIG_ENDIAN);
        this.writeControlFrameHeader(byteBuf, 7, by, n3);
        byteBuf.writeInt(n);
        byteBuf.writeInt(n2);
        return byteBuf;
    }

    public ByteBuf encodeSynReplyFrame(ByteBufAllocator byteBufAllocator, int n, boolean bl, ByteBuf byteBuf) {
        int n2 = byteBuf.readableBytes();
        byte by = bl ? (byte)1 : 0;
        int n3 = 4 + n2;
        ByteBuf byteBuf2 = byteBufAllocator.ioBuffer(8 + n3).order(ByteOrder.BIG_ENDIAN);
        this.writeControlFrameHeader(byteBuf2, 2, by, n3);
        byteBuf2.writeInt(n);
        byteBuf2.writeBytes(byteBuf, byteBuf.readerIndex(), n2);
        return byteBuf2;
    }

    public ByteBuf encodeDataFrame(ByteBufAllocator byteBufAllocator, int n, boolean bl, ByteBuf byteBuf) {
        int n2 = bl ? 1 : 0;
        int n3 = byteBuf.readableBytes();
        ByteBuf byteBuf2 = byteBufAllocator.ioBuffer(8 + n3).order(ByteOrder.BIG_ENDIAN);
        byteBuf2.writeInt(n & Integer.MAX_VALUE);
        byteBuf2.writeByte(n2);
        byteBuf2.writeMedium(n3);
        byteBuf2.writeBytes(byteBuf, byteBuf.readerIndex(), n3);
        return byteBuf2;
    }

    public ByteBuf encodeRstStreamFrame(ByteBufAllocator byteBufAllocator, int n, int n2) {
        byte by = 0;
        int n3 = 8;
        ByteBuf byteBuf = byteBufAllocator.ioBuffer(8 + n3).order(ByteOrder.BIG_ENDIAN);
        this.writeControlFrameHeader(byteBuf, 3, by, n3);
        byteBuf.writeInt(n);
        byteBuf.writeInt(n2);
        return byteBuf;
    }

    public ByteBuf encodeSynStreamFrame(ByteBufAllocator byteBufAllocator, int n, int n2, byte by, boolean bl, boolean bl2, ByteBuf byteBuf) {
        byte by2;
        int n3 = byteBuf.readableBytes();
        byte by3 = by2 = bl ? (byte)1 : 0;
        if (bl2) {
            by2 = (byte)(by2 | 2);
        }
        int n4 = 10 + n3;
        ByteBuf byteBuf2 = byteBufAllocator.ioBuffer(8 + n4).order(ByteOrder.BIG_ENDIAN);
        this.writeControlFrameHeader(byteBuf2, 1, by2, n4);
        byteBuf2.writeInt(n);
        byteBuf2.writeInt(n2);
        byteBuf2.writeShort((by & 0xFF) << 13);
        byteBuf2.writeBytes(byteBuf, byteBuf.readerIndex(), n3);
        return byteBuf2;
    }

    public ByteBuf encodePingFrame(ByteBufAllocator byteBufAllocator, int n) {
        byte by = 0;
        int n2 = 4;
        ByteBuf byteBuf = byteBufAllocator.ioBuffer(8 + n2).order(ByteOrder.BIG_ENDIAN);
        this.writeControlFrameHeader(byteBuf, 6, by, n2);
        byteBuf.writeInt(n);
        return byteBuf;
    }

    public SpdyFrameEncoder(SpdyVersion spdyVersion) {
        if (spdyVersion == null) {
            throw new NullPointerException("spdyVersion");
        }
        this.version = spdyVersion.getVersion();
    }

    public ByteBuf encodeWindowUpdateFrame(ByteBufAllocator byteBufAllocator, int n, int n2) {
        byte by = 0;
        int n3 = 8;
        ByteBuf byteBuf = byteBufAllocator.ioBuffer(8 + n3).order(ByteOrder.BIG_ENDIAN);
        this.writeControlFrameHeader(byteBuf, 9, by, n3);
        byteBuf.writeInt(n);
        byteBuf.writeInt(n2);
        return byteBuf;
    }

    public ByteBuf encodeSettingsFrame(ByteBufAllocator byteBufAllocator, SpdySettingsFrame spdySettingsFrame) {
        Set<Integer> set = spdySettingsFrame.ids();
        int n = set.size();
        byte by = spdySettingsFrame.clearPreviouslyPersistedSettings() ? (byte)1 : 0;
        int n2 = 4 + 8 * n;
        ByteBuf byteBuf = byteBufAllocator.ioBuffer(8 + n2).order(ByteOrder.BIG_ENDIAN);
        this.writeControlFrameHeader(byteBuf, 4, by, n2);
        byteBuf.writeInt(n);
        for (Integer n3 : set) {
            by = 0;
            if (spdySettingsFrame.isPersistValue(n3)) {
                by = (byte)(by | 1);
            }
            if (spdySettingsFrame.isPersisted(n3)) {
                by = (byte)(by | 2);
            }
            byteBuf.writeByte(by);
            byteBuf.writeMedium(n3);
            byteBuf.writeInt(spdySettingsFrame.getValue(n3));
        }
        return byteBuf;
    }

    public ByteBuf encodeHeadersFrame(ByteBufAllocator byteBufAllocator, int n, boolean bl, ByteBuf byteBuf) {
        int n2 = byteBuf.readableBytes();
        byte by = bl ? (byte)1 : 0;
        int n3 = 4 + n2;
        ByteBuf byteBuf2 = byteBufAllocator.ioBuffer(8 + n3).order(ByteOrder.BIG_ENDIAN);
        this.writeControlFrameHeader(byteBuf2, 8, by, n3);
        byteBuf2.writeInt(n);
        byteBuf2.writeBytes(byteBuf, byteBuf.readerIndex(), n2);
        return byteBuf2;
    }

    private void writeControlFrameHeader(ByteBuf byteBuf, int n, byte by, int n2) {
        byteBuf.writeShort(this.version | 0x8000);
        byteBuf.writeShort(n);
        byteBuf.writeByte(by);
        byteBuf.writeMedium(n2);
    }
}

