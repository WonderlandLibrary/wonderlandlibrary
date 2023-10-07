// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayServer;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.Packet;

public class C17PacketCustomPayload implements Packet
{
    private String channel;
    private PacketBuffer data;
    private static final String __OBFID = "CL_00001356";
    
    public C17PacketCustomPayload() {
    }
    
    public C17PacketCustomPayload(final String p_i45945_1_, final PacketBuffer p_i45945_2_) {
        this.channel = p_i45945_1_;
        this.data = p_i45945_2_;
        if (p_i45945_2_.writerIndex() > 32767) {
            throw new IllegalArgumentException("Payload may not be larger than 32767 bytes");
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.channel = data.readStringFromBuffer(20);
        final int var2 = data.readableBytes();
        if (var2 >= 0 && var2 <= 32767) {
            this.data = new PacketBuffer(data.readBytes(var2));
            return;
        }
        throw new IOException("Payload may not be larger than 32767 bytes");
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeString(this.channel);
        data.writeBytes(this.data);
    }
    
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processVanilla250Packet(this);
    }
    
    public String getChannelName() {
        return this.channel;
    }
    
    public PacketBuffer getBufferData() {
        return this.data;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}
