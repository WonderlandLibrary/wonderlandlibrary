/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@ChannelHandler.Sharable
public class LengthFieldPrepender
extends MessageToByteEncoder<ByteBuf> {
    private final int lengthAdjustment;
    private final int lengthFieldLength;
    private final boolean lengthIncludesLengthFieldLength;

    public LengthFieldPrepender(int n) {
        this(n, false);
    }

    public LengthFieldPrepender(int n, boolean bl) {
        this(n, 0, bl);
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, ByteBuf byteBuf2) throws Exception {
        int n = byteBuf.readableBytes() + this.lengthAdjustment;
        if (this.lengthIncludesLengthFieldLength) {
            n += this.lengthFieldLength;
        }
        if (n < 0) {
            throw new IllegalArgumentException("Adjusted frame length (" + n + ") is less than zero");
        }
        switch (this.lengthFieldLength) {
            case 1: {
                if (n >= 256) {
                    throw new IllegalArgumentException("length does not fit into a byte: " + n);
                }
                byteBuf2.writeByte((byte)n);
                break;
            }
            case 2: {
                if (n >= 65536) {
                    throw new IllegalArgumentException("length does not fit into a short integer: " + n);
                }
                byteBuf2.writeShort((short)n);
                break;
            }
            case 3: {
                if (n >= 0x1000000) {
                    throw new IllegalArgumentException("length does not fit into a medium integer: " + n);
                }
                byteBuf2.writeMedium(n);
                break;
            }
            case 4: {
                byteBuf2.writeInt(n);
                break;
            }
            case 8: {
                byteBuf2.writeLong(n);
                break;
            }
            default: {
                throw new Error("should not reach here");
            }
        }
        byteBuf2.writeBytes(byteBuf, byteBuf.readerIndex(), byteBuf.readableBytes());
    }

    public LengthFieldPrepender(int n, int n2, boolean bl) {
        if (n != 1 && n != 2 && n != 3 && n != 4 && n != 8) {
            throw new IllegalArgumentException("lengthFieldLength must be either 1, 2, 3, 4, or 8: " + n);
        }
        this.lengthFieldLength = n;
        this.lengthIncludesLengthFieldLength = bl;
        this.lengthAdjustment = n2;
    }

    public LengthFieldPrepender(int n, int n2) {
        this(n, n2, false);
    }
}

