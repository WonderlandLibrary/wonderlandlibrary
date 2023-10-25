/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

public class S18PacketEntityTeleport
implements Packet<INetHandlerPlayClient> {
    private int entityId;
    private byte pitch;
    private boolean onGround;
    private byte yaw;
    private int posX;
    private int posY;
    private int posZ;

    public boolean getOnGround() {
        return this.onGround;
    }

    public S18PacketEntityTeleport(Entity entity) {
        this.entityId = entity.getEntityId();
        this.posX = MathHelper.floor_double(entity.posX * 32.0);
        this.posY = MathHelper.floor_double(entity.posY * 32.0);
        this.posZ = MathHelper.floor_double(entity.posZ * 32.0);
        this.yaw = (byte)(entity.rotationYaw * 256.0f / 360.0f);
        this.pitch = (byte)(entity.rotationPitch * 256.0f / 360.0f);
        this.onGround = entity.onGround;
    }

    public S18PacketEntityTeleport(int n, int n2, int n3, int n4, byte by, byte by2, boolean bl) {
        this.entityId = n;
        this.posX = n2;
        this.posY = n3;
        this.posZ = n4;
        this.yaw = by;
        this.pitch = by2;
        this.onGround = bl;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.entityId = packetBuffer.readVarIntFromBuffer();
        this.posX = packetBuffer.readInt();
        this.posY = packetBuffer.readInt();
        this.posZ = packetBuffer.readInt();
        this.yaw = packetBuffer.readByte();
        this.pitch = packetBuffer.readByte();
        this.onGround = packetBuffer.readBoolean();
    }

    public int getEntityId() {
        return this.entityId;
    }

    public int getY() {
        return this.posY;
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleEntityTeleport(this);
    }

    public byte getYaw() {
        return this.yaw;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.entityId);
        packetBuffer.writeInt(this.posX);
        packetBuffer.writeInt(this.posY);
        packetBuffer.writeInt(this.posZ);
        packetBuffer.writeByte(this.yaw);
        packetBuffer.writeByte(this.pitch);
        packetBuffer.writeBoolean(this.onGround);
    }

    public byte getPitch() {
        return this.pitch;
    }

    public S18PacketEntityTeleport() {
    }

    public int getX() {
        return this.posX;
    }

    public int getZ() {
        return this.posZ;
    }
}

