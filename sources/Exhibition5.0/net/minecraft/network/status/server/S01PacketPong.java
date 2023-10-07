// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.status.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.status.INetHandlerStatusClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.Packet;

public class S01PacketPong implements Packet
{
    private long clientTime;
    private static final String __OBFID = "CL_00001383";
    
    public S01PacketPong() {
    }
    
    public S01PacketPong(final long time) {
        this.clientTime = time;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.clientTime = data.readLong();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeLong(this.clientTime);
    }
    
    public void processPacket(final INetHandlerStatusClient handler) {
        handler.handlePong(this);
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerStatusClient)handler);
    }
}
