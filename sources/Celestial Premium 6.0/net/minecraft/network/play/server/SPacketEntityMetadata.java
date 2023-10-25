/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityMetadata
implements Packet<INetHandlerPlayClient> {
    private int entityId;
    private List<EntityDataManager.DataEntry<?>> dataManagerEntries;

    public SPacketEntityMetadata() {
    }

    public SPacketEntityMetadata(int entityIdIn, EntityDataManager dataManagerIn, boolean sendAll) {
        this.entityId = entityIdIn;
        if (sendAll) {
            this.dataManagerEntries = dataManagerIn.getAll();
            dataManagerIn.setClean();
        } else {
            this.dataManagerEntries = dataManagerIn.getDirty();
        }
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        this.dataManagerEntries = EntityDataManager.readEntries(buf);
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        EntityDataManager.writeEntries(this.dataManagerEntries, buf);
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleEntityMetadata(this);
    }

    public List<EntityDataManager.DataEntry<?>> getDataManagerEntries() {
        return this.dataManagerEntries;
    }

    public int getEntityId() {
        return this.entityId;
    }
}

