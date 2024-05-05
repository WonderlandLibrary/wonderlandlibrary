package net.minecraft.world.gen.feature;

import net.minecraft.util.BlockPos;

static class WorldGenBigTree.FoliageCoordinates
extends BlockPos {
    private final int field_178000_b;

    public WorldGenBigTree.FoliageCoordinates(BlockPos p_i45635_1_, int p_i45635_2_) {
        super(p_i45635_1_.getX(), p_i45635_1_.getY(), p_i45635_1_.getZ());
        this.field_178000_b = p_i45635_2_;
    }

    public int func_177999_q() {
        return this.field_178000_b;
    }
}
