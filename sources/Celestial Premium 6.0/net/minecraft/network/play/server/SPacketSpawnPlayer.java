/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSpawnPlayer
implements Packet<INetHandlerPlayClient> {
    private int entityId;
    private UUID uniqueId;
    private double x;
    private double y;
    private double z;
    private byte yaw;
    private byte pitch;
    private EntityDataManager watcher;
    private List<EntityDataManager.DataEntry<?>> dataManagerEntries;

    public SPacketSpawnPlayer() {
    }

    public SPacketSpawnPlayer(EntityPlayer player) {
        this.entityId = player.getEntityId();
        this.uniqueId = player.getGameProfile().getId();
        this.x = player.posX;
        this.y = player.posY;
        this.z = player.posZ;
        this.yaw = (byte)(player.rotationYaw * 256.0f / 360.0f);
        this.pitch = (byte)(player.rotationPitch * 256.0f / 360.0f);
        this.watcher = player.getDataManager();
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        this.uniqueId = buf.readUuid();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.yaw = buf.readByte();
        this.pitch = buf.readByte();
        this.dataManagerEntries = EntityDataManager.readEntries(buf);
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeUuid(this.uniqueId);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeByte(this.yaw);
        buf.writeByte(this.pitch);
        this.watcher.writeEntries(buf);
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleSpawnPlayer(this);
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

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public byte getYaw() {
        return this.yaw;
    }

    public byte getPitch() {
        return this.pitch;
    }
}

