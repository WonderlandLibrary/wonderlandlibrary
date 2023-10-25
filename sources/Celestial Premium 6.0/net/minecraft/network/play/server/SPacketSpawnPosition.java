/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketSpawnPosition
implements Packet<INetHandlerPlayClient> {
    private BlockPos spawnBlockPos;

    public SPacketSpawnPosition() {
    }

    public SPacketSpawnPosition(BlockPos posIn) {
        this.spawnBlockPos = posIn;
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.spawnBlockPos = buf.readBlockPos();
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeBlockPos(this.spawnBlockPos);
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleSpawnPosition(this);
    }

    public BlockPos getSpawnPos() {
        return this.spawnBlockPos;
    }
}

