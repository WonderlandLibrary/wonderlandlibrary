// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;

public abstract class WorldGenAbstractTree extends WorldGenerator
{
    public WorldGenAbstractTree(final boolean notify) {
        super(notify);
    }
    
    protected boolean canGrowInto(final Block blockType) {
        final Material material = blockType.getDefaultState().getMaterial();
        return material == Material.AIR || material == Material.LEAVES || blockType == Blocks.GRASS || blockType == Blocks.DIRT || blockType == Blocks.LOG || blockType == Blocks.LOG2 || blockType == Blocks.SAPLING || blockType == Blocks.VINE;
    }
    
    public void generateSaplings(final World worldIn, final Random random, final BlockPos pos) {
    }
    
    protected void setDirtAt(final World worldIn, final BlockPos pos) {
        if (worldIn.getBlockState(pos).getBlock() != Blocks.DIRT) {
            this.setBlockAndNotifyAdequately(worldIn, pos, Blocks.DIRT.getDefaultState());
        }
    }
}
