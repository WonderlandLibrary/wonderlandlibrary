/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;

public class S24PacketBlockAction
implements Packet<INetHandlerPlayClient> {
    private int instrument;
    private BlockPos blockPosition;
    private int pitch;
    private Block block;

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleBlockAction(this);
    }

    public int getData1() {
        return this.instrument;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.blockPosition = packetBuffer.readBlockPos();
        this.instrument = packetBuffer.readUnsignedByte();
        this.pitch = packetBuffer.readUnsignedByte();
        this.block = Block.getBlockById(packetBuffer.readVarIntFromBuffer() & 0xFFF);
    }

    public S24PacketBlockAction() {
    }

    public int getData2() {
        return this.pitch;
    }

    public S24PacketBlockAction(BlockPos blockPos, Block block, int n, int n2) {
        this.blockPosition = blockPos;
        this.instrument = n;
        this.pitch = n2;
        this.block = block;
    }

    public BlockPos getBlockPosition() {
        return this.blockPosition;
    }

    public Block getBlockType() {
        return this.block;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeBlockPos(this.blockPosition);
        packetBuffer.writeByte(this.instrument);
        packetBuffer.writeByte(this.pitch);
        packetBuffer.writeVarIntToBuffer(Block.getIdFromBlock(this.block) & 0xFFF);
    }
}

