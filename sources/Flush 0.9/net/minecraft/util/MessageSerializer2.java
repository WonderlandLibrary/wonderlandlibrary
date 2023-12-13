package net.minecraft.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.minecraft.network.PacketBuffer;

public class MessageSerializer2 extends MessageToByteEncoder<ByteBuf>
{
    protected void encode(ChannelHandlerContext context, ByteBuf byteBuf, ByteBuf byteBuf1) throws Exception
    {
        int i = byteBuf.readableBytes();
        int j = PacketBuffer.getVarIntSize(i);

        if (j > 3)
            throw new IllegalArgumentException("unable to fit " + i + " into " + 3);
        else
        {
            PacketBuffer packetBuffer = new PacketBuffer(byteBuf1);
            packetBuffer.ensureWritable(j + i);
            packetBuffer.writeVarIntToBuffer(i);
            packetBuffer.writeBytes(byteBuf, byteBuf.readerIndex(), i);
        }
    }
}
