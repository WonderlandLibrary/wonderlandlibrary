// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.Packet;

public class S3FPacketCustomPayload implements Packet
{
    private String channel;
    private PacketBuffer data;
    private static final String __OBFID = "CL_00001297";
    
    public S3FPacketCustomPayload() {
    }
    
    public S3FPacketCustomPayload(final String channelName, final PacketBuffer dataIn) {
        this.channel = channelName;
        this.data = dataIn;
        if (dataIn.writerIndex() > 1048576) {
            throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.channel = data.readStringFromBuffer(20);
        final int var2 = data.readableBytes();
        if (var2 >= 0 && var2 <= 1048576) {
            this.data = new PacketBuffer(data.readBytes(var2));
            return;
        }
        throw new IOException("Payload may not be larger than 1048576 bytes");
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeString(this.channel);
        data.writeBytes(this.data);
    }
    
    public void process(final INetHandlerPlayClient p_180734_1_) {
        p_180734_1_.handleCustomPayload(this);
    }
    
    public String getChannelName() {
        return this.channel;
    }
    
    public PacketBuffer getBufferData() {
        return this.data;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.process((INetHandlerPlayClient)handler);
    }
}
