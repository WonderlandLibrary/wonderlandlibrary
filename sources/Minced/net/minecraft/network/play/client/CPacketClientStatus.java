// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketClientStatus implements Packet<INetHandlerPlayServer>
{
    private State status;
    
    public CPacketClientStatus() {
    }
    
    public CPacketClientStatus(final State p_i46886_1_) {
        this.status = p_i46886_1_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.status = buf.readEnumValue(State.class);
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.status);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processClientStatus(this);
    }
    
    public State getStatus() {
        return this.status;
    }
    
    public enum State
    {
        PERFORM_RESPAWN, 
        REQUEST_STATS;
    }
}
