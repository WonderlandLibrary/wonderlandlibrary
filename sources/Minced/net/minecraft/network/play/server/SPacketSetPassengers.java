// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketSetPassengers implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private int[] passengerIds;
    
    public SPacketSetPassengers() {
    }
    
    public SPacketSetPassengers(final Entity entityIn) {
        this.entityId = entityIn.getEntityId();
        final List<Entity> list = entityIn.getPassengers();
        this.passengerIds = new int[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            this.passengerIds[i] = list.get(i).getEntityId();
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarInt();
        this.passengerIds = buf.readVarIntArray();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.entityId);
        buf.writeVarIntArray(this.passengerIds);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSetPassengers(this);
    }
    
    public int[] getPassengerIds() {
        return this.passengerIds;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
}
