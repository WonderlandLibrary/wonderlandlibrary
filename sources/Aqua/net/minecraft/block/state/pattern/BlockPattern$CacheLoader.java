package net.minecraft.block.state.pattern;

import com.google.common.cache.CacheLoader;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

static class BlockPattern.CacheLoader
extends CacheLoader<BlockPos, BlockWorldState> {
    private final World world;
    private final boolean field_181626_b;

    public BlockPattern.CacheLoader(World worldIn, boolean p_i46460_2_) {
        this.world = worldIn;
        this.field_181626_b = p_i46460_2_;
    }

    public BlockWorldState load(BlockPos p_load_1_) throws Exception {
        return new BlockWorldState(this.world, p_load_1_, this.field_181626_b);
    }
}
