package net.minecraft.network.play.server;

import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.chunk.Chunk;

/*
 * Exception performing whole class analysis ignored.
 */
public class S22PacketMultiBlockChange.BlockUpdateData {
    private final short chunkPosCrammed;
    private final IBlockState blockState;

    public S22PacketMultiBlockChange.BlockUpdateData(short p_i45984_2_, IBlockState state) {
        this.chunkPosCrammed = p_i45984_2_;
        this.blockState = state;
    }

    public S22PacketMultiBlockChange.BlockUpdateData(short p_i45985_2_, Chunk chunkIn) {
        this.chunkPosCrammed = p_i45985_2_;
        this.blockState = chunkIn.getBlockState(this.getPos());
    }

    public BlockPos getPos() {
        return new BlockPos((Vec3i)S22PacketMultiBlockChange.access$000((S22PacketMultiBlockChange)S22PacketMultiBlockChange.this).getBlock(this.chunkPosCrammed >> 12 & 0xF, this.chunkPosCrammed & 0xFF, this.chunkPosCrammed >> 8 & 0xF));
    }

    public short func_180089_b() {
        return this.chunkPosCrammed;
    }

    public IBlockState getBlockState() {
        return this.blockState;
    }
}
