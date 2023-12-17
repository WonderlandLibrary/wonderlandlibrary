// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketKeepAlive implements Packet<INetHandlerPlayServer>
{
    private long key;
    
    public CPacketKeepAlive() {
    }
    
    public CPacketKeepAlive(final long idIn) {
        this.key = idIn;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processKeepAlive(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.key = buf.readLong();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeLong(this.key);
    }
    
    public long getKey() {
        return this.key;
    }
}
