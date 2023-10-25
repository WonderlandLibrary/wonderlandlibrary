/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S1BPacketEntityAttach
implements Packet<INetHandlerPlayClient> {
    private int entityId;
    private int vehicleEntityId;
    private int leash;

    public int getLeash() {
        return this.leash;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public S1BPacketEntityAttach() {
    }

    public int getVehicleEntityId() {
        return this.vehicleEntityId;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeInt(this.entityId);
        packetBuffer.writeInt(this.vehicleEntityId);
        packetBuffer.writeByte(this.leash);
    }

    public S1BPacketEntityAttach(int n, Entity entity, Entity entity2) {
        this.leash = n;
        this.entityId = entity.getEntityId();
        this.vehicleEntityId = entity2 != null ? entity2.getEntityId() : -1;
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleEntityAttach(this);
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.entityId = packetBuffer.readInt();
        this.vehicleEntityId = packetBuffer.readInt();
        this.leash = packetBuffer.readUnsignedByte();
    }
}

