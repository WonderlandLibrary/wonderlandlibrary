// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import javax.annotation.Nullable;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import java.util.List;
import net.minecraft.network.datasync.EntityDataManager;
import java.util.UUID;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketSpawnMob implements Packet<INetHandlerPlayClient>
{
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
    
    public SPacketSpawnMob(final EntityLivingBase entityIn) {
        this.entityId = entityIn.getEntityId();
        this.uniqueId = entityIn.getUniqueID();
        this.type = EntityList.REGISTRY.getIDForObject(entityIn.getClass());
        this.x = entityIn.posX;
        this.y = entityIn.posY;
        this.z = entityIn.posZ;
        this.yaw = (byte)(entityIn.rotationYaw * 256.0f / 360.0f);
        this.pitch = (byte)(entityIn.rotationPitch * 256.0f / 360.0f);
        this.headPitch = (byte)(entityIn.rotationYawHead * 256.0f / 360.0f);
        final double d0 = 3.9;
        double d2 = entityIn.motionX;
        double d3 = entityIn.motionY;
        double d4 = entityIn.motionZ;
        if (d2 < -3.9) {
            d2 = -3.9;
        }
        if (d3 < -3.9) {
            d3 = -3.9;
        }
        if (d4 < -3.9) {
            d4 = -3.9;
        }
        if (d2 > 3.9) {
            d2 = 3.9;
        }
        if (d3 > 3.9) {
            d3 = 3.9;
        }
        if (d4 > 3.9) {
            d4 = 3.9;
        }
        this.velocityX = (int)(d2 * 8000.0);
        this.velocityY = (int)(d3 * 8000.0);
        this.velocityZ = (int)(d4 * 8000.0);
        this.dataManager = entityIn.getDataManager();
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarInt();
        this.uniqueId = buf.readUniqueId();
        this.type = buf.readVarInt();
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
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.entityId);
        buf.writeUniqueId(this.uniqueId);
        buf.writeVarInt(this.type);
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
    public void processPacket(final INetHandlerPlayClient handler) {
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
