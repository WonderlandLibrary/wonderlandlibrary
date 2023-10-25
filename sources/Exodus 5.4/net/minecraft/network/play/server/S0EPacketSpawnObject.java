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

public class S0EPacketSpawnObject
implements Packet<INetHandlerPlayClient> {
    private int type;
    private int x;
    private int yaw;
    private int y;
    private int z;
    private int pitch;
    private int entityId;
    private int field_149020_k;
    private int speedZ;
    private int speedY;
    private int speedX;

    public int getSpeedX() {
        return this.speedX;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.entityId = packetBuffer.readVarIntFromBuffer();
        this.type = packetBuffer.readByte();
        this.x = packetBuffer.readInt();
        this.y = packetBuffer.readInt();
        this.z = packetBuffer.readInt();
        this.pitch = packetBuffer.readByte();
        this.yaw = packetBuffer.readByte();
        this.field_149020_k = packetBuffer.readInt();
        if (this.field_149020_k > 0) {
            this.speedX = packetBuffer.readShort();
            this.speedY = packetBuffer.readShort();
            this.speedZ = packetBuffer.readShort();
        }
    }

    public void setX(int n) {
        this.x = n;
    }

    public void setY(int n) {
        this.y = n;
    }

    public int getEntityID() {
        return this.entityId;
    }

    public int getSpeedZ() {
        return this.speedZ;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.entityId);
        packetBuffer.writeByte(this.type);
        packetBuffer.writeInt(this.x);
        packetBuffer.writeInt(this.y);
        packetBuffer.writeInt(this.z);
        packetBuffer.writeByte(this.pitch);
        packetBuffer.writeByte(this.yaw);
        packetBuffer.writeInt(this.field_149020_k);
        if (this.field_149020_k > 0) {
            packetBuffer.writeShort(this.speedX);
            packetBuffer.writeShort(this.speedY);
            packetBuffer.writeShort(this.speedZ);
        }
    }

    public int getYaw() {
        return this.yaw;
    }

    public int getPitch() {
        return this.pitch;
    }

    public void setSpeedZ(int n) {
        this.speedZ = n;
    }

    public void setSpeedX(int n) {
        this.speedX = n;
    }

    public int getType() {
        return this.type;
    }

    public int getSpeedY() {
        return this.speedY;
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleSpawnObject(this);
    }

    public int getZ() {
        return this.z;
    }

    public void setZ(int n) {
        this.z = n;
    }

    public S0EPacketSpawnObject(Entity entity, int n, int n2) {
        this.entityId = entity.getEntityId();
        this.x = MathHelper.floor_double(entity.posX * 32.0);
        this.y = MathHelper.floor_double(entity.posY * 32.0);
        this.z = MathHelper.floor_double(entity.posZ * 32.0);
        this.pitch = MathHelper.floor_float(entity.rotationPitch * 256.0f / 360.0f);
        this.yaw = MathHelper.floor_float(entity.rotationYaw * 256.0f / 360.0f);
        this.type = n;
        this.field_149020_k = n2;
        if (n2 > 0) {
            double d = entity.motionX;
            double d2 = entity.motionY;
            double d3 = entity.motionZ;
            double d4 = 3.9;
            if (d < -d4) {
                d = -d4;
            }
            if (d2 < -d4) {
                d2 = -d4;
            }
            if (d3 < -d4) {
                d3 = -d4;
            }
            if (d > d4) {
                d = d4;
            }
            if (d2 > d4) {
                d2 = d4;
            }
            if (d3 > d4) {
                d3 = d4;
            }
            this.speedX = (int)(d * 8000.0);
            this.speedY = (int)(d2 * 8000.0);
            this.speedZ = (int)(d3 * 8000.0);
        }
    }

    public void func_149002_g(int n) {
        this.field_149020_k = n;
    }

    public int func_149009_m() {
        return this.field_149020_k;
    }

    public S0EPacketSpawnObject(Entity entity, int n) {
        this(entity, n, 0);
    }

    public int getX() {
        return this.x;
    }

    public void setSpeedY(int n) {
        this.speedY = n;
    }

    public S0EPacketSpawnObject() {
    }
}

