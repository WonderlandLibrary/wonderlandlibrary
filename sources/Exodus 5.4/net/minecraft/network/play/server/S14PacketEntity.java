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

public class S14PacketEntity
implements Packet<INetHandlerPlayClient> {
    protected byte yaw;
    protected boolean field_149069_g;
    protected boolean onGround;
    protected byte posY;
    protected byte posZ;
    protected byte pitch;
    protected byte posX;
    protected int entityId;

    public byte func_149063_g() {
        return this.pitch;
    }

    public byte func_149062_c() {
        return this.posX;
    }

    public byte func_149064_e() {
        return this.posZ;
    }

    public S14PacketEntity(int n) {
        this.entityId = n;
    }

    public S14PacketEntity() {
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleEntityMovement(this);
    }

    public String toString() {
        return "Entity_" + super.toString();
    }

    public Entity getEntity(World world) {
        return world.getEntityByID(this.entityId);
    }

    public byte func_149066_f() {
        return this.yaw;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.entityId);
    }

    public boolean func_149060_h() {
        return this.field_149069_g;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.entityId = packetBuffer.readVarIntFromBuffer();
    }

    public byte func_149061_d() {
        return this.posY;
    }

    public boolean getOnGround() {
        return this.onGround;
    }

    public static class S15PacketEntityRelMove
    extends S14PacketEntity {
        public S15PacketEntityRelMove(int n, byte by, byte by2, byte by3, boolean bl) {
            super(n);
            this.posX = by;
            this.posY = by2;
            this.posZ = by3;
            this.onGround = bl;
        }

        public S15PacketEntityRelMove() {
        }

        @Override
        public void readPacketData(PacketBuffer packetBuffer) throws IOException {
            super.readPacketData(packetBuffer);
            this.posX = packetBuffer.readByte();
            this.posY = packetBuffer.readByte();
            this.posZ = packetBuffer.readByte();
            this.onGround = packetBuffer.readBoolean();
        }

        @Override
        public void writePacketData(PacketBuffer packetBuffer) throws IOException {
            super.writePacketData(packetBuffer);
            packetBuffer.writeByte(this.posX);
            packetBuffer.writeByte(this.posY);
            packetBuffer.writeByte(this.posZ);
            packetBuffer.writeBoolean(this.onGround);
        }
    }

    public static class S17PacketEntityLookMove
    extends S14PacketEntity {
        @Override
        public void readPacketData(PacketBuffer packetBuffer) throws IOException {
            super.readPacketData(packetBuffer);
            this.posX = packetBuffer.readByte();
            this.posY = packetBuffer.readByte();
            this.posZ = packetBuffer.readByte();
            this.yaw = packetBuffer.readByte();
            this.pitch = packetBuffer.readByte();
            this.onGround = packetBuffer.readBoolean();
        }

        @Override
        public void writePacketData(PacketBuffer packetBuffer) throws IOException {
            super.writePacketData(packetBuffer);
            packetBuffer.writeByte(this.posX);
            packetBuffer.writeByte(this.posY);
            packetBuffer.writeByte(this.posZ);
            packetBuffer.writeByte(this.yaw);
            packetBuffer.writeByte(this.pitch);
            packetBuffer.writeBoolean(this.onGround);
        }

        public S17PacketEntityLookMove(int n, byte by, byte by2, byte by3, byte by4, byte by5, boolean bl) {
            super(n);
            this.posX = by;
            this.posY = by2;
            this.posZ = by3;
            this.yaw = by4;
            this.pitch = by5;
            this.onGround = bl;
            this.field_149069_g = true;
        }

        public S17PacketEntityLookMove() {
            this.field_149069_g = true;
        }
    }

    public static class S16PacketEntityLook
    extends S14PacketEntity {
        @Override
        public void readPacketData(PacketBuffer packetBuffer) throws IOException {
            super.readPacketData(packetBuffer);
            this.yaw = packetBuffer.readByte();
            this.pitch = packetBuffer.readByte();
            this.onGround = packetBuffer.readBoolean();
        }

        public S16PacketEntityLook(int n, byte by, byte by2, boolean bl) {
            super(n);
            this.yaw = by;
            this.pitch = by2;
            this.field_149069_g = true;
            this.onGround = bl;
        }

        @Override
        public void writePacketData(PacketBuffer packetBuffer) throws IOException {
            super.writePacketData(packetBuffer);
            packetBuffer.writeByte(this.yaw);
            packetBuffer.writeByte(this.pitch);
            packetBuffer.writeBoolean(this.onGround);
        }

        public S16PacketEntityLook() {
            this.field_149069_g = true;
        }
    }
}

