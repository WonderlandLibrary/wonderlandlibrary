/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

public class S0CPacketSpawnPlayer
implements Packet<INetHandlerPlayClient> {
    private List<DataWatcher.WatchableObject> field_148958_j;
    private int x;
    private DataWatcher watcher;
    private int y;
    private UUID playerId;
    private int z;
    private byte yaw;
    private int entityId;
    private int currentItem;
    private byte pitch;

    public int getCurrentItemID() {
        return this.currentItem;
    }

    public byte getYaw() {
        return this.yaw;
    }

    public byte getPitch() {
        return this.pitch;
    }

    public S0CPacketSpawnPlayer(EntityPlayer entityPlayer) {
        this.entityId = entityPlayer.getEntityId();
        this.playerId = entityPlayer.getGameProfile().getId();
        this.x = MathHelper.floor_double(entityPlayer.posX * 32.0);
        this.y = MathHelper.floor_double(entityPlayer.posY * 32.0);
        this.z = MathHelper.floor_double(entityPlayer.posZ * 32.0);
        this.yaw = (byte)(entityPlayer.rotationYaw * 256.0f / 360.0f);
        this.pitch = (byte)(entityPlayer.rotationPitch * 256.0f / 360.0f);
        ItemStack itemStack = entityPlayer.inventory.getCurrentItem();
        this.currentItem = itemStack == null ? 0 : Item.getIdFromItem(itemStack.getItem());
        this.watcher = entityPlayer.getDataWatcher();
    }

    public List<DataWatcher.WatchableObject> func_148944_c() {
        if (this.field_148958_j == null) {
            this.field_148958_j = this.watcher.getAllWatched();
        }
        return this.field_148958_j;
    }

    public UUID getPlayer() {
        return this.playerId;
    }

    public int getEntityID() {
        return this.entityId;
    }

    public int getY() {
        return this.y;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public S0CPacketSpawnPlayer() {
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.entityId);
        packetBuffer.writeUuid(this.playerId);
        packetBuffer.writeInt(this.x);
        packetBuffer.writeInt(this.y);
        packetBuffer.writeInt(this.z);
        packetBuffer.writeByte(this.yaw);
        packetBuffer.writeByte(this.pitch);
        packetBuffer.writeShort(this.currentItem);
        this.watcher.writeTo(packetBuffer);
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.entityId = packetBuffer.readVarIntFromBuffer();
        this.playerId = packetBuffer.readUuid();
        this.x = packetBuffer.readInt();
        this.y = packetBuffer.readInt();
        this.z = packetBuffer.readInt();
        this.yaw = packetBuffer.readByte();
        this.pitch = packetBuffer.readByte();
        this.currentItem = packetBuffer.readShort();
        this.field_148958_j = DataWatcher.readWatchedListFromPacketBuffer(packetBuffer);
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleSpawnPlayer(this);
    }
}

