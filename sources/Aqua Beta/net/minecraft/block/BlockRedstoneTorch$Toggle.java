package net.minecraft.block;

import net.minecraft.util.BlockPos;

static class BlockRedstoneTorch.Toggle {
    BlockPos pos;
    long time;

    public BlockRedstoneTorch.Toggle(BlockPos pos, long time) {
        this.pos = pos;
        this.time = time;
    }
}
