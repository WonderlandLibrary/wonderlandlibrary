// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockBreakable extends Block
{
    private final boolean ignoreSimilarity;
    
    protected BlockBreakable(final Material materialIn, final boolean ignoreSimilarityIn) {
        this(materialIn, ignoreSimilarityIn, materialIn.getMaterialMapColor());
    }
    
    protected BlockBreakable(final Material materialIn, final boolean ignoreSimilarityIn, final MapColor mapColorIn) {
        super(materialIn, mapColorIn);
        this.ignoreSimilarity = ignoreSimilarityIn;
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        final IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        final Block block = iblockstate.getBlock();
        if (this == Blocks.GLASS || this == Blocks.STAINED_GLASS) {
            if (blockState != iblockstate) {
                return true;
            }
            if (block == this) {
                return false;
            }
        }
        return (this.ignoreSimilarity || block != this) && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
}
