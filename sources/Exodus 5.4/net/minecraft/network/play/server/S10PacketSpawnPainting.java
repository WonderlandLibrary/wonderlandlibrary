/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class S10PacketSpawnPainting
implements Packet<INetHandlerPlayClient> {
    private EnumFacing facing;
    private BlockPos position;
    private String title;
    private int entityID;

    public String getTitle() {
        return this.title;
    }

    public S10PacketSpawnPainting() {
    }

    public int getEntityID() {
        return this.entityID;
    }

    public BlockPos getPosition() {
        return this.position;
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleSpawnPainting(this);
    }

    public S10PacketSpawnPainting(EntityPainting entityPainting) {
        this.entityID = entityPainting.getEntityId();
        this.position = entityPainting.getHangingPosition();
        this.facing = entityPainting.facingDirection;
        this.title = entityPainting.art.title;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.entityID);
        packetBuffer.writeString(this.title);
        packetBuffer.writeBlockPos(this.position);
        packetBuffer.writeByte(this.facing.getHorizontalIndex());
    }

    public EnumFacing getFacing() {
        return this.facing;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.entityID = packetBuffer.readVarIntFromBuffer();
        this.title = packetBuffer.readStringFromBuffer(EntityPainting.EnumArt.field_180001_A);
        this.position = packetBuffer.readBlockPos();
        this.facing = EnumFacing.getHorizontal(packetBuffer.readUnsignedByte());
    }
}

