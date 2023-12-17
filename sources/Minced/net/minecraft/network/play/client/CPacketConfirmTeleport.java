// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketConfirmTeleport implements Packet<INetHandlerPlayServer>
{
    private int telportId;
    
    public CPacketConfirmTeleport() {
    }
    
    public CPacketConfirmTeleport(final int teleportIdIn) {
        this.telportId = teleportIdIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.telportId = buf.readVarInt();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.telportId);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processConfirmTeleport(this);
    }
    
    public int getTeleportId() {
        return this.telportId;
    }
}
