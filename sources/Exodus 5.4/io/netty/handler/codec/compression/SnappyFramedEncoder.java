/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.compression.CompressionException;
import io.netty.handler.codec.compression.Snappy;

public class SnappyFramedEncoder
extends MessageToByteEncoder<ByteBuf> {
    private static final int MIN_COMPRESSIBLE_LENGTH = 18;
    private static final byte[] STREAM_START = new byte[]{-1, 6, 0, 0, 115, 78, 97, 80, 112, 89};
    private boolean started;
    private final Snappy snappy = new Snappy();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, ByteBuf byteBuf2) throws Exception {
        block6: {
            int n;
            if (!byteBuf.isReadable()) {
                return;
            }
            if (!this.started) {
                this.started = true;
                byteBuf2.writeBytes(STREAM_START);
            }
            if ((n = byteBuf.readableBytes()) > 18) {
                ByteBuf byteBuf3;
                int n2;
                while (true) {
                    n2 = byteBuf2.writerIndex() + 1;
                    if (n < 18) {
                        byteBuf3 = byteBuf.readSlice(n);
                        SnappyFramedEncoder.writeUnencodedChunk(byteBuf3, byteBuf2, n);
                        break block6;
                    }
                    byteBuf2.writeInt(0);
                    if (n <= Short.MAX_VALUE) break;
                    byteBuf3 = byteBuf.readSlice(Short.MAX_VALUE);
                    SnappyFramedEncoder.calculateAndWriteChecksum(byteBuf3, byteBuf2);
                    this.snappy.encode(byteBuf3, byteBuf2, Short.MAX_VALUE);
                    SnappyFramedEncoder.setChunkLength(byteBuf2, n2);
                    n -= Short.MAX_VALUE;
                }
                byteBuf3 = byteBuf.readSlice(n);
                SnappyFramedEncoder.calculateAndWriteChecksum(byteBuf3, byteBuf2);
                this.snappy.encode(byteBuf3, byteBuf2, n);
                SnappyFramedEncoder.setChunkLength(byteBuf2, n2);
            } else {
                SnappyFramedEncoder.writeUnencodedChunk(byteBuf, byteBuf2, n);
            }
        }
    }

    private static void writeChunkLength(ByteBuf byteBuf, int n) {
        byteBuf.writeMedium(ByteBufUtil.swapMedium(n));
    }

    private static void writeUnencodedChunk(ByteBuf byteBuf, ByteBuf byteBuf2, int n) {
        byteBuf2.writeByte(1);
        SnappyFramedEncoder.writeChunkLength(byteBuf2, n + 4);
        SnappyFramedEncoder.calculateAndWriteChecksum(byteBuf, byteBuf2);
        byteBuf2.writeBytes(byteBuf, n);
    }

    private static void calculateAndWriteChecksum(ByteBuf byteBuf, ByteBuf byteBuf2) {
        byteBuf2.writeInt(ByteBufUtil.swapInt(Snappy.calculateChecksum(byteBuf)));
    }

    private static void setChunkLength(ByteBuf byteBuf, int n) {
        int n2 = byteBuf.writerIndex() - n - 3;
        if (n2 >>> 24 != 0) {
            throw new CompressionException("compressed data too large: " + n2);
        }
        byteBuf.setMedium(n, ByteBufUtil.swapMedium(n2));
    }
}

