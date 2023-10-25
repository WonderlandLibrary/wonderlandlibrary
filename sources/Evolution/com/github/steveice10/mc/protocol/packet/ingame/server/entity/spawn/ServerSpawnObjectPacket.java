/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.FallingBlockData;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.HangingDirection;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.MinecartType;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectData;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectType;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ProjectileData;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.SplashPotionData;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import java.io.IOException;
import java.util.UUID;

public class ServerSpawnObjectPacket
extends MinecraftPacket {
    private int entityId;
    private UUID uuid;
    private ObjectType type;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private ObjectData data;
    private double motX;
    private double motY;
    private double motZ;

    private ServerSpawnObjectPacket() {
    }

    public ServerSpawnObjectPacket(int entityId, UUID uuid, ObjectType type, double x, double y, double z, float yaw, float pitch) {
        this(entityId, uuid, type, null, x, y, z, yaw, pitch, 0.0, 0.0, 0.0);
    }

    public ServerSpawnObjectPacket(int entityId, UUID uuid, ObjectType type, ObjectData data, double x, double y, double z, float yaw, float pitch) {
        this(entityId, uuid, type, data, x, y, z, yaw, pitch, 0.0, 0.0, 0.0);
    }

    public ServerSpawnObjectPacket(int entityId, UUID uuid, ObjectType type, double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ) {
        this(entityId, uuid, type, new ObjectData(){}, x, y, z, yaw, pitch, motX, motY, motZ);
    }

    public ServerSpawnObjectPacket(int entityId, UUID uuid, ObjectType type, ObjectData data, double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ) {
        this.entityId = entityId;
        this.uuid = uuid;
        this.type = type;
        this.data = data;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.motX = motX;
        this.motY = motY;
        this.motZ = motZ;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public ObjectType getType() {
        return this.type;
    }

    public ObjectData getData() {
        return this.data;
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

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public double getMotionX() {
        return this.motX;
    }

    public double getMotionY() {
        return this.motY;
    }

    public double getMotionZ() {
        return this.motZ;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.uuid = in.readUUID();
        this.type = MagicValues.key(ObjectType.class, in.readByte());
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.pitch = (float)(in.readByte() * 360) / 256.0f;
        this.yaw = (float)(in.readByte() * 360) / 256.0f;
        int data = in.readInt();
        if (data > 0) {
            this.data = this.type == ObjectType.MINECART ? (ObjectData)MagicValues.key(MinecartType.class, data) : (this.type == ObjectType.ITEM_FRAME ? (ObjectData)MagicValues.key(HangingDirection.class, data) : (this.type == ObjectType.FALLING_BLOCK ? new FallingBlockData(data & 0xFFFF, data >> 16) : (this.type == ObjectType.POTION ? new SplashPotionData(data) : (this.type == ObjectType.SPECTRAL_ARROW || this.type == ObjectType.TIPPED_ARROW || this.type == ObjectType.GHAST_FIREBALL || this.type == ObjectType.BLAZE_FIREBALL || this.type == ObjectType.DRAGON_FIREBALL || this.type == ObjectType.WITHER_HEAD_PROJECTILE || this.type == ObjectType.FISH_HOOK ? new ProjectileData(data) : new ObjectData(){}))));
        }
        this.motX = (double)in.readShort() / 8000.0;
        this.motY = (double)in.readShort() / 8000.0;
        this.motZ = (double)in.readShort() / 8000.0;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeUUID(this.uuid);
        out.writeByte(MagicValues.value(Integer.class, (Object)this.type));
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeByte((byte)(this.pitch * 256.0f / 360.0f));
        out.writeByte((byte)(this.yaw * 256.0f / 360.0f));
        int data = 0;
        if (this.data != null) {
            data = this.data instanceof MinecartType ? MagicValues.value(Integer.class, (Enum)((Object)this.data)) : (this.data instanceof HangingDirection ? MagicValues.value(Integer.class, (Enum)((Object)this.data)) : (this.data instanceof FallingBlockData ? ((FallingBlockData)this.data).getId() | ((FallingBlockData)this.data).getMetadata() << 16 : (this.data instanceof SplashPotionData ? ((SplashPotionData)this.data).getPotionData() : (this.data instanceof ProjectileData ? ((ProjectileData)this.data).getOwnerId() : 1))));
        }
        out.writeInt(data);
        out.writeShort((int)(this.motX * 8000.0));
        out.writeShort((int)(this.motY * 8000.0));
        out.writeShort((int)(this.motZ * 8000.0));
    }
}

