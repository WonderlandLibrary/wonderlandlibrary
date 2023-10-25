/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class S23PacketBlockChange
implements Packet<INetHandlerPlayClient> {
    private BlockPos blockPosition;
    private IBlockState blockState;

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.blockPosition = packetBuffer.readBlockPos();
        this.blockState = Block.BLOCK_STATE_IDS.getByValue(packetBuffer.readVarIntFromBuffer());
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeBlockPos(this.blockPosition);
        packetBuffer.writeVarIntToBuffer(Block.BLOCK_STATE_IDS.get(this.blockState));
    }

    public IBlockState getBlockState() {
        return this.blockState;
    }

    public S23PacketBlockChange() {
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleBlockChange(this);
    }

    public S23PacketBlockChange(World world, BlockPos blockPos) {
        this.blockPosition = blockPos;
        this.blockState = world.getBlockState(blockPos);
    }

    public BlockPos getBlockPosition() {
        return this.blockPosition;
    }
}

