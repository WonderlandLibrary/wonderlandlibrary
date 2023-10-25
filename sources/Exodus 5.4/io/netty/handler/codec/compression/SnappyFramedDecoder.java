/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.compression.DecompressionException;
import io.netty.handler.codec.compression.Snappy;
import java.util.Arrays;
import java.util.List;

public class SnappyFramedDecoder
extends ByteToMessageDecoder {
    private static final byte[] SNAPPY = new byte[]{115, 78, 97, 80, 112, 89};
    private final boolean validateChecksums;
    private static final int MAX_UNCOMPRESSED_DATA_SIZE = 65540;
    private final Snappy snappy = new Snappy();
    private boolean started;
    private boolean corrupted;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (this.corrupted) {
            byteBuf.skipBytes(byteBuf.readableBytes());
            return;
        }
        try {
            int n = byteBuf.readerIndex();
            int n2 = byteBuf.readableBytes();
            if (n2 < 4) {
                return;
            }
            short s = byteBuf.getUnsignedByte(n);
            ChunkType chunkType = SnappyFramedDecoder.mapChunkType((byte)s);
            int n3 = ByteBufUtil.swapMedium(byteBuf.getUnsignedMedium(n + 1));
            switch (chunkType) {
                case STREAM_IDENTIFIER: {
                    if (n3 != SNAPPY.length) {
                        throw new DecompressionException("Unexpected length of stream identifier: " + n3);
                    }
                    if (n2 < 4 + SNAPPY.length) break;
                    byte[] byArray = new byte[n3];
                    byteBuf.skipBytes(4).readBytes(byArray);
                    if (!Arrays.equals(byArray, SNAPPY)) {
                        throw new DecompressionException("Unexpected stream identifier contents. Mismatched snappy protocol version?");
                    }
                    this.started = true;
                    break;
                }
                case RESERVED_SKIPPABLE: {
                    if (!this.started) {
                        throw new DecompressionException("Received RESERVED_SKIPPABLE tag before STREAM_IDENTIFIER");
                    }
                    if (n2 < 4 + n3) {
                        return;
                    }
                    byteBuf.skipBytes(4 + n3);
                    break;
                }
                case RESERVED_UNSKIPPABLE: {
                    throw new DecompressionException("Found reserved unskippable chunk type: 0x" + Integer.toHexString(s));
                }
                case UNCOMPRESSED_DATA: {
                    if (!this.started) {
                        throw new DecompressionException("Received UNCOMPRESSED_DATA tag before STREAM_IDENTIFIER");
                    }
                    if (n3 > 65540) {
                        throw new DecompressionException("Received UNCOMPRESSED_DATA larger than 65540 bytes");
                    }
                    if (n2 < 4 + n3) {
                        return;
                    }
                    byteBuf.skipBytes(4);
                    if (this.validateChecksums) {
                        int n4 = ByteBufUtil.swapInt(byteBuf.readInt());
                        Snappy.validateChecksum(n4, byteBuf, byteBuf.readerIndex(), n3 - 4);
                    } else {
                        byteBuf.skipBytes(4);
                    }
                    list.add(byteBuf.readSlice(n3 - 4).retain());
                    break;
                }
                case COMPRESSED_DATA: {
                    if (!this.started) {
                        throw new DecompressionException("Received COMPRESSED_DATA tag before STREAM_IDENTIFIER");
                    }
                    if (n2 < 4 + n3) {
                        return;
                    }
                    byteBuf.skipBytes(4);
                    int n5 = ByteBufUtil.swapInt(byteBuf.readInt());
                    ByteBuf byteBuf2 = channelHandlerContext.alloc().buffer(0);
                    if (this.validateChecksums) {
                        int n6 = byteBuf.writerIndex();
                        byteBuf.writerIndex(byteBuf.readerIndex() + n3 - 4);
                        this.snappy.decode(byteBuf, byteBuf2);
                        byteBuf.writerIndex(n6);
                        Snappy.validateChecksum(n5, byteBuf2, 0, byteBuf2.writerIndex());
                    } else {
                        this.snappy.decode(byteBuf.readSlice(n3 - 4), byteBuf2);
                    }
                    list.add(byteBuf2);
                    this.snappy.reset();
                }
            }
        }
        catch (Exception exception) {
            this.corrupted = true;
            throw exception;
        }
    }

    private static ChunkType mapChunkType(byte by) {
        if (by == 0) {
            return ChunkType.COMPRESSED_DATA;
        }
        if (by == 1) {
            return ChunkType.UNCOMPRESSED_DATA;
        }
        if (by == -1) {
            return ChunkType.STREAM_IDENTIFIER;
        }
        if ((by & 0x80) == 128) {
            return ChunkType.RESERVED_SKIPPABLE;
        }
        return ChunkType.RESERVED_UNSKIPPABLE;
    }

    public SnappyFramedDecoder(boolean bl) {
        this.validateChecksums = bl;
    }

    public SnappyFramedDecoder() {
        this(false);
    }

    private static enum ChunkType {
        STREAM_IDENTIFIER,
        COMPRESSED_DATA,
        UNCOMPRESSED_DATA,
        RESERVED_UNSKIPPABLE,
        RESERVED_SKIPPABLE;

    }
}

