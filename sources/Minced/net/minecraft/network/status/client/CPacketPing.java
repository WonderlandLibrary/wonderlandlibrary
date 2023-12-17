// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.status.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;
import net.minecraft.network.Packet;

public class CPacketPing implements Packet<INetHandlerStatusServer>
{
    private long clientTime;
    
    public CPacketPing() {
    }
    
    public CPacketPing(final long clientTimeIn) {
        this.clientTime = clientTimeIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.clientTime = buf.readLong();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeLong(this.clientTime);
    }
    
    @Override
    public void processPacket(final INetHandlerStatusServer handler) {
        handler.processPing(this);
    }
    
    public long getClientTime() {
        return this.clientTime;
    }
}
