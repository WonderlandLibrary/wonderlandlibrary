/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.handler.codec.spdy.SpdyDataFrame;
import io.netty.handler.codec.spdy.SpdyFrame;
import io.netty.handler.codec.spdy.SpdyGoAwayFrame;
import io.netty.handler.codec.spdy.SpdyHeaderBlockEncoder;
import io.netty.handler.codec.spdy.SpdyHeadersFrame;
import io.netty.handler.codec.spdy.SpdyPingFrame;
import io.netty.handler.codec.spdy.SpdyRstStreamFrame;
import io.netty.handler.codec.spdy.SpdySettingsFrame;
import io.netty.handler.codec.spdy.SpdySynReplyFrame;
import io.netty.handler.codec.spdy.SpdySynStreamFrame;
import io.netty.handler.codec.spdy.SpdyVersion;
import io.netty.handler.codec.spdy.SpdyWindowUpdateFrame;
import java.util.Set;

public class SpdyFrameEncoder
extends MessageToByteEncoder<SpdyFrame> {
    private final int version;
    private final SpdyHeaderBlockEncoder headerBlockEncoder;

    public SpdyFrameEncoder(SpdyVersion version) {
        this(version, 6, 15, 8);
    }

    public SpdyFrameEncoder(SpdyVersion version, int compressionLevel, int windowBits, int memLevel) {
        this(version, SpdyHeaderBlockEncoder.newInstance(version, compressionLevel, windowBits, memLevel));
    }

    protected SpdyFrameEncoder(SpdyVersion version, SpdyHeaderBlockEncoder headerBlockEncoder) {
        if (version == null) {
            throw new NullPointerException("version");
        }
        this.version = version.getVersion();
        this.headerBlockEncoder = headerBlockEncoder;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().closeFuture().addListener(new ChannelFutureListener(){

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                SpdyFrameEncoder.this.headerBlockEncoder.end();
            }
        });
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, SpdyFrame msg, ByteBuf out) throws Exception {
        if (msg instanceof SpdyDataFrame) {
            SpdyDataFrame spdyDataFrame = (SpdyDataFrame)msg;
            ByteBuf data = spdyDataFrame.content();
            int flags = spdyDataFrame.isLast() ? 1 : 0;
            out.ensureWritable(8 + data.readableBytes());
            out.writeInt(spdyDataFrame.getStreamId() & Integer.MAX_VALUE);
            out.writeByte(flags);
            out.writeMedium(data.readableBytes());
            out.writeBytes(data, data.readerIndex(), data.readableBytes());
        } else if (msg instanceof SpdySynStreamFrame) {
            SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame)msg;
            ByteBuf data = this.headerBlockEncoder.encode(spdySynStreamFrame);
            try {
                int flags;
                int n = flags = spdySynStreamFrame.isLast() ? 1 : 0;
                if (spdySynStreamFrame.isUnidirectional()) {
                    flags = (byte)(flags | 2);
                }
                int headerBlockLength = data.readableBytes();
                int length = 10 + headerBlockLength;
                out.ensureWritable(8 + length);
                out.writeShort(this.version | 0x8000);
                out.writeShort(1);
                out.writeByte(flags);
                out.writeMedium(length);
                out.writeInt(spdySynStreamFrame.getStreamId());
                out.writeInt(spdySynStreamFrame.getAssociatedToStreamId());
                out.writeShort((spdySynStreamFrame.getPriority() & 0xFF) << 13);
                out.writeBytes(data, data.readerIndex(), headerBlockLength);
            }
            finally {
                data.release();
            }
        } else if (msg instanceof SpdySynReplyFrame) {
            SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame)msg;
            ByteBuf data = this.headerBlockEncoder.encode(spdySynReplyFrame);
            try {
                int flags = spdySynReplyFrame.isLast() ? 1 : 0;
                int headerBlockLength = data.readableBytes();
                int length = 4 + headerBlockLength;
                out.ensureWritable(8 + length);
                out.writeShort(this.version | 0x8000);
                out.writeShort(2);
                out.writeByte(flags);
                out.writeMedium(length);
                out.writeInt(spdySynReplyFrame.getStreamId());
                out.writeBytes(data, data.readerIndex(), headerBlockLength);
            }
            finally {
                data.release();
            }
        } else if (msg instanceof SpdyRstStreamFrame) {
            SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame)msg;
            out.ensureWritable(16);
            out.writeShort(this.version | 0x8000);
            out.writeShort(3);
            out.writeInt(8);
            out.writeInt(spdyRstStreamFrame.getStreamId());
            out.writeInt(spdyRstStreamFrame.getStatus().getCode());
        } else if (msg instanceof SpdySettingsFrame) {
            SpdySettingsFrame spdySettingsFrame = (SpdySettingsFrame)msg;
            int flags = spdySettingsFrame.clearPreviouslyPersistedSettings() ? 1 : 0;
            Set<Integer> IDs = spdySettingsFrame.getIds();
            int numEntries = IDs.size();
            int length = 4 + numEntries * 8;
            out.ensureWritable(8 + length);
            out.writeShort(this.version | 0x8000);
            out.writeShort(4);
            out.writeByte(flags);
            out.writeMedium(length);
            out.writeInt(numEntries);
            for (Integer id : IDs) {
                byte ID_flags = 0;
                if (spdySettingsFrame.isPersistValue(id)) {
                    ID_flags = (byte)(ID_flags | true ? 1 : 0);
                }
                if (spdySettingsFrame.isPersisted(id)) {
                    ID_flags = (byte)(ID_flags | 2);
                }
                out.writeByte(ID_flags);
                out.writeMedium(id);
                out.writeInt(spdySettingsFrame.getValue(id));
            }
        } else if (msg instanceof SpdyPingFrame) {
            SpdyPingFrame spdyPingFrame = (SpdyPingFrame)msg;
            out.ensureWritable(12);
            out.writeShort(this.version | 0x8000);
            out.writeShort(6);
            out.writeInt(4);
            out.writeInt(spdyPingFrame.getId());
        } else if (msg instanceof SpdyGoAwayFrame) {
            SpdyGoAwayFrame spdyGoAwayFrame = (SpdyGoAwayFrame)msg;
            out.ensureWritable(16);
            out.writeShort(this.version | 0x8000);
            out.writeShort(7);
            out.writeInt(8);
            out.writeInt(spdyGoAwayFrame.getLastGoodStreamId());
            out.writeInt(spdyGoAwayFrame.getStatus().getCode());
        } else if (msg instanceof SpdyHeadersFrame) {
            SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame)msg;
            ByteBuf data = this.headerBlockEncoder.encode(spdyHeadersFrame);
            try {
                int flags = spdyHeadersFrame.isLast() ? 1 : 0;
                int headerBlockLength = data.readableBytes();
                int length = 4 + headerBlockLength;
                out.ensureWritable(8 + length);
                out.writeShort(this.version | 0x8000);
                out.writeShort(8);
                out.writeByte(flags);
                out.writeMedium(length);
                out.writeInt(spdyHeadersFrame.getStreamId());
                out.writeBytes(data, data.readerIndex(), headerBlockLength);
            }
            finally {
                data.release();
            }
        } else if (msg instanceof SpdyWindowUpdateFrame) {
            SpdyWindowUpdateFrame spdyWindowUpdateFrame = (SpdyWindowUpdateFrame)msg;
            out.ensureWritable(16);
            out.writeShort(this.version | 0x8000);
            out.writeShort(9);
            out.writeInt(8);
            out.writeInt(spdyWindowUpdateFrame.getStreamId());
            out.writeInt(spdyWindowUpdateFrame.getDeltaWindowSize());
        } else {
            throw new UnsupportedMessageTypeException(msg, new Class[0]);
        }
    }
}

