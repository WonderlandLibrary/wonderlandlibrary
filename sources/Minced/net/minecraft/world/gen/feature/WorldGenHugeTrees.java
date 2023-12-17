// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Random;
import net.minecraft.block.state.IBlockState;

public abstract class WorldGenHugeTrees extends WorldGenAbstractTree
{
    protected final int baseHeight;
    protected final IBlockState woodMetadata;
    protected final IBlockState leavesMetadata;
    protected int extraRandomHeight;
    
    public WorldGenHugeTrees(final boolean notify, final int baseHeightIn, final int extraRandomHeightIn, final IBlockState woodMetadataIn, final IBlockState leavesMetadataIn) {
        super(notify);
        this.baseHeight = baseHeightIn;
        this.extraRandomHeight = extraRandomHeightIn;
        this.woodMetadata = woodMetadataIn;
        this.leavesMetadata = leavesMetadataIn;
    }
    
    protected int getHeight(final Random rand) {
        int i = rand.nextInt(3) + this.baseHeight;
        if (this.extraRandomHeight > 1) {
            i += rand.nextInt(this.extraRandomHeight);
        }
        return i;
    }
    
    private boolean isSpaceAt(final World worldIn, final BlockPos leavesPos, final int height) {
        boolean flag = true;
        if (leavesPos.getY() >= 1 && leavesPos.getY() + height + 1 <= 256) {
            for (int i = 0; i <= 1 + height; ++i) {
                int j = 2;
                if (i == 0) {
                    j = 1;
                }
                else if (i >= 1 + height - 2) {
                    j = 2;
                }
                for (int k = -j; k <= j && flag; ++k) {
                    for (int l = -j; l <= j && flag; ++l) {
                        if (leavesPos.getY() + i < 0 || leavesPos.getY() + i >= 256 || !this.canGrowInto(worldIn.getBlockState(leavesPos.add(k, i, l)).getBlock())) {
                            flag = false;
                        }
                    }
                }
            }
            return flag;
        }
        return false;
    }
    
    private boolean ensureDirtsUnderneath(final BlockPos pos, final World worldIn) {
        final BlockPos blockpos = pos.down();
        final Block block = worldIn.getBlockState(blockpos).getBlock();
        if ((block == Blocks.GRASS || block == Blocks.DIRT) && pos.getY() >= 2) {
            this.setDirtAt(worldIn, blockpos);
            this.setDirtAt(worldIn, blockpos.east());
            this.setDirtAt(worldIn, blockpos.south());
            this.setDirtAt(worldIn, blockpos.south().east());
            return true;
        }
        return false;
    }
    
    protected boolean ensureGrowable(final World worldIn, final Random rand, final BlockPos treePos, final int height) {
        return this.isSpaceAt(worldIn, treePos, height) && this.ensureDirtsUnderneath(treePos, worldIn);
    }
    
    protected void growLeavesLayerStrict(final World worldIn, final BlockPos layerCenter, final int width) {
        final int i = width * width;
        for (int j = -width; j <= width + 1; ++j) {
            for (int k = -width; k <= width + 1; ++k) {
                final int l = j - 1;
                final int i2 = k - 1;
                if (j * j + k * k <= i || l * l + i2 * i2 <= i || j * j + i2 * i2 <= i || l * l + k * k <= i) {
                    final BlockPos blockpos = layerCenter.add(j, 0, k);
                    final Material material = worldIn.getBlockState(blockpos).getMaterial();
                    if (material == Material.AIR || material == Material.LEAVES) {
                        this.setBlockAndNotifyAdequately(worldIn, blockpos, this.leavesMetadata);
                    }
                }
            }
        }
    }
    
    protected void growLeavesLayer(final World worldIn, final BlockPos layerCenter, final int width) {
        final int i = width * width;
        for (int j = -width; j <= width; ++j) {
            for (int k = -width; k <= width; ++k) {
                if (j * j + k * k <= i) {
                    final BlockPos blockpos = layerCenter.add(j, 0, k);
                    final Material material = worldIn.getBlockState(blockpos).getMaterial();
                    if (material == Material.AIR || material == Material.LEAVES) {
                        this.setBlockAndNotifyAdequately(worldIn, blockpos, this.leavesMetadata);
                    }
                }
            }
        }
    }
}
