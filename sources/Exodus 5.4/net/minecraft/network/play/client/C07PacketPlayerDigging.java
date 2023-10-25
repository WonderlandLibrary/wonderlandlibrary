/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class C07PacketPlayerDigging
implements Packet<INetHandlerPlayServer> {
    private Action status;
    private BlockPos position;
    private EnumFacing facing;

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeEnumValue(this.status);
        packetBuffer.writeBlockPos(this.position);
        packetBuffer.writeByte(this.facing.getIndex());
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.status = packetBuffer.readEnumValue(Action.class);
        this.position = packetBuffer.readBlockPos();
        this.facing = EnumFacing.getFront(packetBuffer.readUnsignedByte());
    }

    public C07PacketPlayerDigging() {
    }

    public Action getStatus() {
        return this.status;
    }

    @Override
    public void processPacket(INetHandlerPlayServer iNetHandlerPlayServer) {
        iNetHandlerPlayServer.processPlayerDigging(this);
    }

    public C07PacketPlayerDigging(Action action, BlockPos blockPos, EnumFacing enumFacing) {
        this.status = action;
        this.position = blockPos;
        this.facing = enumFacing;
    }

    public EnumFacing getFacing() {
        return this.facing;
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public static enum Action {
        START_DESTROY_BLOCK,
        ABORT_DESTROY_BLOCK,
        STOP_DESTROY_BLOCK,
        DROP_ALL_ITEMS,
        DROP_ITEM,
        RELEASE_USE_ITEM;

    }
}

