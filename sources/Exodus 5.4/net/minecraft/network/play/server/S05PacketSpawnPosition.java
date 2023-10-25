/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;

public class S05PacketSpawnPosition
implements Packet<INetHandlerPlayClient> {
    private BlockPos spawnBlockPos;

    public BlockPos getSpawnPos() {
        return this.spawnBlockPos;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeBlockPos(this.spawnBlockPos);
    }

    public S05PacketSpawnPosition() {
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleSpawnPosition(this);
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.spawnBlockPos = packetBuffer.readBlockPos();
    }

    public S05PacketSpawnPosition(BlockPos blockPos) {
        this.spawnBlockPos = blockPos;
    }
}

