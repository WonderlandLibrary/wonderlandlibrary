/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.minecraft.network.PacketBuffer;

public class MessageSerializer2
extends MessageToByteEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, ByteBuf byteBuf2) throws Exception {
        int n = byteBuf.readableBytes();
        int n2 = PacketBuffer.getVarIntSize(n);
        if (n2 > 3) {
            throw new IllegalArgumentException("unable to fit " + n + " into " + 3);
        }
        PacketBuffer packetBuffer = new PacketBuffer(byteBuf2);
        packetBuffer.ensureWritable(n2 + n);
        packetBuffer.writeVarIntToBuffer(n);
        packetBuffer.writeBytes(byteBuf, byteBuf.readerIndex(), n);
    }
}

