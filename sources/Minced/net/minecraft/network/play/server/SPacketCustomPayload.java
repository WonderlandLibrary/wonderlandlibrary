// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketCustomPayload implements Packet<INetHandlerPlayClient>
{
    private String channel;
    private PacketBuffer data;
    
    public SPacketCustomPayload() {
    }
    
    public SPacketCustomPayload(final String channelIn, final PacketBuffer bufIn) {
        this.channel = channelIn;
        this.data = bufIn;
        if (bufIn.writerIndex() > 1048576) {
            throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.channel = buf.readString(20);
        final int i = buf.readableBytes();
        if (i >= 0 && i <= 1048576) {
            this.data = new PacketBuffer(buf.readBytes(i));
            return;
        }
        throw new IOException("Payload may not be larger than 1048576 bytes");
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeString(this.channel);
        buf.writeBytes(this.data);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleCustomPayload(this);
    }
    
    public String getChannelName() {
        return this.channel;
    }
    
    public PacketBuffer getBufferData() {
        return this.data;
    }
}
