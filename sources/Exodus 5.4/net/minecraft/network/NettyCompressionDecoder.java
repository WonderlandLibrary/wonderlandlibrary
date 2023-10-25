/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import net.minecraft.network.PacketBuffer;

public class NettyCompressionDecoder
extends ByteToMessageDecoder {
    private final Inflater inflater;
    private int treshold;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception, DataFormatException {
        if (byteBuf.readableBytes() != 0) {
            PacketBuffer packetBuffer = new PacketBuffer(byteBuf);
            int n = packetBuffer.readVarIntFromBuffer();
            if (n == 0) {
                list.add(packetBuffer.readBytes(packetBuffer.readableBytes()));
            } else {
                if (n < this.treshold) {
                    throw new DecoderException("Badly compressed packet - size of " + n + " is below server threshold of " + this.treshold);
                }
                if (n > 0x200000) {
                    throw new DecoderException("Badly compressed packet - size of " + n + " is larger than protocol maximum of " + 0x200000);
                }
                byte[] byArray = new byte[packetBuffer.readableBytes()];
                packetBuffer.readBytes(byArray);
                this.inflater.setInput(byArray);
                byte[] byArray2 = new byte[n];
                this.inflater.inflate(byArray2);
                list.add(Unpooled.wrappedBuffer(byArray2));
                this.inflater.reset();
            }
        }
    }

    public void setCompressionTreshold(int n) {
        this.treshold = n;
    }

    public NettyCompressionDecoder(int n) {
        this.treshold = n;
        this.inflater = new Inflater();
    }
}

