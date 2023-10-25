/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityAttach
implements Packet<INetHandlerPlayClient> {
    private int entityId;
    private int vehicleEntityId;

    public SPacketEntityAttach() {
    }

    public SPacketEntityAttach(Entity entityIn, @Nullable Entity vehicleIn) {
        this.entityId = entityIn.getEntityId();
        this.vehicleEntityId = vehicleIn != null ? vehicleIn.getEntityId() : -1;
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.entityId = buf.readInt();
        this.vehicleEntityId = buf.readInt();
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeInt(this.entityId);
        buf.writeInt(this.vehicleEntityId);
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleEntityAttach(this);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public int getVehicleEntityId() {
        return this.vehicleEntityId;
    }
}

