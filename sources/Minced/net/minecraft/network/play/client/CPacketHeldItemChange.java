// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketHeldItemChange implements Packet<INetHandlerPlayServer>
{
    private int slotId;
    
    public CPacketHeldItemChange() {
    }
    
    public CPacketHeldItemChange(final int slotIdIn) {
        this.slotId = slotIdIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.slotId = buf.readShort();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeShort(this.slotId);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processHeldItemChange(this);
    }
    
    public int getSlotId() {
        return this.slotId;
    }
}
