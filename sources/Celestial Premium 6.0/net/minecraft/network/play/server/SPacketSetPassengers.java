/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSetPassengers
implements Packet<INetHandlerPlayClient> {
    private int entityId;
    private int[] passengerIds;

    public SPacketSetPassengers() {
    }

    public SPacketSetPassengers(Entity entityIn) {
        this.entityId = entityIn.getEntityId();
        List<Entity> list = entityIn.getPassengers();
        this.passengerIds = new int[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            this.passengerIds[i] = list.get(i).getEntityId();
        }
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        this.passengerIds = buf.readVarIntArray();
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeVarIntArray(this.passengerIds);
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleSetPassengers(this);
    }

    public int[] getPassengerIds() {
        return this.passengerIds;
    }

    public int getEntityId() {
        return this.entityId;
    }
}

