/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.entity.DataWatcher;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S1CPacketEntityMetadata
implements Packet<INetHandlerPlayClient> {
    private int entityId;
    private List<DataWatcher.WatchableObject> field_149378_b;

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleEntityMetadata(this);
    }

    public List<DataWatcher.WatchableObject> func_149376_c() {
        return this.field_149378_b;
    }

    public S1CPacketEntityMetadata() {
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.entityId = packetBuffer.readVarIntFromBuffer();
        this.field_149378_b = DataWatcher.readWatchedListFromPacketBuffer(packetBuffer);
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.entityId);
        DataWatcher.writeWatchedListToPacketBuffer(this.field_149378_b, packetBuffer);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public S1CPacketEntityMetadata(int n, DataWatcher dataWatcher, boolean bl) {
        this.entityId = n;
        this.field_149378_b = bl ? dataWatcher.getAllWatched() : dataWatcher.getChanged();
    }
}

