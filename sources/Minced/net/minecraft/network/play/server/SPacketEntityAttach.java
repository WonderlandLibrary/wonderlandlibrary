// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketEntityAttach implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private int vehicleEntityId;
    
    public SPacketEntityAttach() {
    }
    
    public SPacketEntityAttach(final Entity entityIn, @Nullable final Entity vehicleIn) {
        this.entityId = entityIn.getEntityId();
        this.vehicleEntityId = ((vehicleIn != null) ? vehicleIn.getEntityId() : -1);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readInt();
        this.vehicleEntityId = buf.readInt();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeInt(this.entityId);
        buf.writeInt(this.vehicleEntityId);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityAttach(this);
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public int getVehicleEntityId() {
        return this.vehicleEntityId;
    }
}
