/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  io.netty.buffer.Unpooled
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.handler.codec.ByteToMessageCodec
 *  io.netty.handler.codec.CorruptedFrameException
 */
package com.github.steveice10.packetlib.tcp;

import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.tcp.io.ByteBufNetInput;
import com.github.steveice10.packetlib.tcp.io.ByteBufNetOutput;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.CorruptedFrameException;
import java.util.List;

public class TcpPacketSizer
extends ByteToMessageCodec<ByteBuf> {
    private Session session;

    public TcpPacketSizer(Session session) {
        this.session = session;
    }

    public void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
        int length = in.readableBytes();
        out.ensureWritable(this.session.getPacketProtocol().getPacketHeader().getLengthSize(length) + length);
        this.session.getPacketProtocol().getPacketHeader().writeLength(new ByteBufNetOutput(out), length);
        out.writeBytes(in);
    }

    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        int size = this.session.getPacketProtocol().getPacketHeader().getLengthSize();
        if (size > 0) {
            buf.markReaderIndex();
            byte[] lengthBytes = new byte[size];
            int index = 0;
            while (index < lengthBytes.length) {
                if (!buf.isReadable()) {
                    buf.resetReaderIndex();
                    return;
                }
                lengthBytes[index] = buf.readByte();
                if (this.session.getPacketProtocol().getPacketHeader().isLengthVariable() && lengthBytes[index] >= 0 || index == size - 1) {
                    int length = this.session.getPacketProtocol().getPacketHeader().readLength(new ByteBufNetInput(Unpooled.wrappedBuffer((byte[])lengthBytes)), buf.readableBytes());
                    if (buf.readableBytes() < length) {
                        buf.resetReaderIndex();
                        return;
                    }
                    out.add(buf.readBytes(length));
                    return;
                }
                ++index;
            }
            throw new CorruptedFrameException("Length is too long.");
        }
        out.add(buf.readBytes(buf.readableBytes()));
    }
}

