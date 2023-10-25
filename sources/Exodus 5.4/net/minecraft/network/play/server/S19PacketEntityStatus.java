/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class S19PacketEntityStatus
implements Packet<INetHandlerPlayClient> {
    private int entityId;
    private byte logicOpcode;

    public S19PacketEntityStatus(Entity entity, byte by) {
        this.entityId = entity.getEntityId();
        this.logicOpcode = by;
    }

    public Entity getEntity(World world) {
        return world.getEntityByID(this.entityId);
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.entityId = packetBuffer.readInt();
        this.logicOpcode = packetBuffer.readByte();
    }

    public S19PacketEntityStatus() {
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleEntityStatus(this);
    }

    public byte getOpCode() {
        return this.logicOpcode;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeInt(this.entityId);
        packetBuffer.writeByte(this.logicOpcode);
    }
}

