/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;

public class S25PacketBlockBreakAnim
implements Packet<INetHandlerPlayClient> {
    private int breakerId;
    private int progress;
    private BlockPos position;

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleBlockBreakAnim(this);
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.breakerId);
        packetBuffer.writeBlockPos(this.position);
        packetBuffer.writeByte(this.progress);
    }

    public S25PacketBlockBreakAnim(int n, BlockPos blockPos, int n2) {
        this.breakerId = n;
        this.position = blockPos;
        this.progress = n2;
    }

    public int getProgress() {
        return this.progress;
    }

    public int getBreakerId() {
        return this.breakerId;
    }

    public S25PacketBlockBreakAnim() {
    }

    public BlockPos getPosition() {
        return this.position;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.breakerId = packetBuffer.readVarIntFromBuffer();
        this.position = packetBuffer.readBlockPos();
        this.progress = packetBuffer.readUnsignedByte();
    }
}

