/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import java.util.List;
import net.minecraft.network.PacketBuffer;

public class MessageDeserializer2
extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byteBuf.markReaderIndex();
        byte[] byArray = new byte[3];
        int n = 0;
        while (n < byArray.length) {
            if (!byteBuf.isReadable()) {
                byteBuf.resetReaderIndex();
                return;
            }
            byArray[n] = byteBuf.readByte();
            if (byArray[n] >= 0) {
                PacketBuffer packetBuffer = new PacketBuffer(Unpooled.wrappedBuffer(byArray));
                int n2 = packetBuffer.readVarIntFromBuffer();
                if (byteBuf.readableBytes() >= n2) {
                    list.add(byteBuf.readBytes(n2));
                    packetBuffer.release();
                    return;
                }
                byteBuf.resetReaderIndex();
                packetBuffer.release();
                return;
            }
            ++n;
        }
        throw new CorruptedFrameException("length wider than 21-bit");
    }
}

