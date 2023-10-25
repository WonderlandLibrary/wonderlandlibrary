/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSpawnMob
implements Packet<INetHandlerPlayClient> {
    private int entityId;
    private UUID uniqueId;
    private int type;
    private double x;
    private double y;
    private double z;
    private int velocityX;
    private int velocityY;
    private int velocityZ;
    private byte yaw;
    private byte pitch;
    private byte headPitch;
    private EntityDataManager dataManager;
    private List<EntityDataManager.DataEntry<?>> dataManagerEntries;

    public SPacketSpawnMob() {
    }

    public SPacketSpawnMob(EntityLivingBase entityIn) {
        this.entityId = entityIn.getEntityId();
        this.uniqueId = entityIn.getUniqueID();
        this.type = EntityList.REGISTRY.getIDForObject(entityIn.getClass());
        this.x = entityIn.posX;
        this.y = entityIn.posY;
        this.z = entityIn.posZ;
        this.yaw = (byte)(entityIn.rotationYaw * 256.0f / 360.0f);
        this.pitch = (byte)(entityIn.rotationPitch * 256.0f / 360.0f);
        this.headPitch = (byte)(entityIn.rotationYawHead * 256.0f / 360.0f);
        double d0 = 3.9;
        double d1 = entityIn.motionX;
        double d2 = entityIn.motionY;
        double d3 = entityIn.motionZ;
        if (d1 < -3.9) {
            d1 = -3.9;
        }
        if (d2 < -3.9) {
            d2 = -3.9;
        }
        if (d3 < -3.9) {
            d3 = -3.9;
        }
        if (d1 > 3.9) {
            d1 = 3.9;
        }
        if (d2 > 3.9) {
            d2 = 3.9;
        }
        if (d3 > 3.9) {
            d3 = 3.9;
        }
        this.velocityX = (int)(d1 * 8000.0);
        this.velocityY = (int)(d2 * 8000.0);
        this.velocityZ = (int)(d3 * 8000.0);
        this.dataManager = entityIn.getDataManager();
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        this.uniqueId = buf.readUuid();
        this.type = buf.readVarIntFromBuffer();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.yaw = buf.readByte();
        this.pitch = buf.readByte();
        this.headPitch = buf.readByte();
        this.velocityX = buf.readShort();
        this.velocityY = buf.readShort();
        this.velocityZ = buf.readShort();
        this.dataManagerEntries = EntityDataManager.readEntries(buf);
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeUuid(this.uniqueId);
        buf.writeVarIntToBuffer(this.type);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeByte(this.yaw);
        buf.writeByte(this.pitch);
        buf.writeByte(this.headPitch);
        buf.writeShort(this.velocityX);
        buf.writeShort(this.velocityY);
        buf.writeShort(this.velocityZ);
        this.dataManager.writeEntries(buf);
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleSpawnMob(this);
    }

    @Nullable
    public List<EntityDataManager.DataEntry<?>> getDataManagerEntries() {
        return this.dataManagerEntries;
    }

    public int getEntityID() {
        return this.entityId;
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public int getEntityType() {
        return this.type;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public int getVelocityX() {
        return this.velocityX;
    }

    public int getVelocityY() {
        return this.velocityY;
    }

    public int getVelocityZ() {
        return this.velocityZ;
    }

    public byte getYaw() {
        return this.yaw;
    }

    public byte getPitch() {
        return this.pitch;
    }

    public byte getHeadPitch() {
        return this.headPitch;
    }
}

