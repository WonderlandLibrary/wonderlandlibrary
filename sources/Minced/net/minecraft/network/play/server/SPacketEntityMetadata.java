// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import java.util.List;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketEntityMetadata implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private List<EntityDataManager.DataEntry<?>> dataManagerEntries;
    
    public SPacketEntityMetadata() {
    }
    
    public SPacketEntityMetadata(final int entityIdIn, final EntityDataManager dataManagerIn, final boolean sendAll) {
        this.entityId = entityIdIn;
        if (sendAll) {
            this.dataManagerEntries = dataManagerIn.getAll();
            dataManagerIn.setClean();
        }
        else {
            this.dataManagerEntries = dataManagerIn.getDirty();
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarInt();
        this.dataManagerEntries = EntityDataManager.readEntries(buf);
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.entityId);
        EntityDataManager.writeEntries(this.dataManagerEntries, buf);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityMetadata(this);
    }
    
    public List<EntityDataManager.DataEntry<?>> getDataManagerEntries() {
        return this.dataManagerEntries;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
}
