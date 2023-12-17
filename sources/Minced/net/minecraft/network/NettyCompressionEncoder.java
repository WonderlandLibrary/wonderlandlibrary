// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network;

import io.netty.channel.ChannelHandlerContext;
import java.util.zip.Deflater;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyCompressionEncoder extends MessageToByteEncoder<ByteBuf>
{
    private final byte[] buffer;
    private final Deflater deflater;
    private int threshold;
    
    public NettyCompressionEncoder(final int thresholdIn) {
        this.buffer = new byte[8192];
        this.threshold = thresholdIn;
        this.deflater = new Deflater();
    }
    
    protected void encode(final ChannelHandlerContext p_encode_1_, final ByteBuf p_encode_2_, final ByteBuf p_encode_3_) throws Exception {
        final int i = p_encode_2_.readableBytes();
        final PacketBuffer packetbuffer = new PacketBuffer(p_encode_3_);
        if (i < this.threshold) {
            packetbuffer.writeVarInt(0);
            packetbuffer.writeBytes(p_encode_2_);
        }
        else {
            final byte[] abyte = new byte[i];
            p_encode_2_.readBytes(abyte);
            packetbuffer.writeVarInt(abyte.length);
            this.deflater.setInput(abyte, 0, i);
            this.deflater.finish();
            while (!this.deflater.finished()) {
                final int j = this.deflater.deflate(this.buffer);
                packetbuffer.writeBytes(this.buffer, 0, j);
            }
            this.deflater.reset();
        }
    }
    
    public void setCompressionThreshold(final int thresholdIn) {
        this.threshold = thresholdIn;
    }
}
