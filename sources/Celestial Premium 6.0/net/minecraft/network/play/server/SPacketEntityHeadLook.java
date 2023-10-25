/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class SPacketEntityHeadLook
implements Packet<INetHandlerPlayClient> {
    private int entityId;
    private byte yaw;

    public SPacketEntityHeadLook() {
    }

    public SPacketEntityHeadLook(Entity entityIn, byte yawIn) {
        this.entityId = entityIn.getEntityId();
        this.yaw = yawIn;
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        this.yaw = buf.readByte();
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeByte(this.yaw);
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleEntityHeadLook(this);
    }

    public Entity getEntity(World worldIn) {
        return worldIn.getEntityByID(this.entityId);
    }

    public byte getYaw() {
        return this.yaw;
    }
}

