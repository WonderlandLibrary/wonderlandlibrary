/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public abstract class WorldGenHugeTrees
extends WorldGenAbstractTree {
    protected final int baseHeight;
    protected final IBlockState woodMetadata;
    protected final IBlockState leavesMetadata;
    protected int extraRandomHeight;

    public WorldGenHugeTrees(boolean notify, int baseHeightIn, int extraRandomHeightIn, IBlockState woodMetadataIn, IBlockState leavesMetadataIn) {
        super(notify);
        this.baseHeight = baseHeightIn;
        this.extraRandomHeight = extraRandomHeightIn;
        this.woodMetadata = woodMetadataIn;
        this.leavesMetadata = leavesMetadataIn;
    }

    protected int getHeight(Random rand) {
        int i = rand.nextInt(3) + this.baseHeight;
        if (this.extraRandomHeight > 1) {
            i += rand.nextInt(this.extraRandomHeight);
        }
        return i;
    }

    private boolean isSpaceAt(World worldIn, BlockPos leavesPos, int height) {
        boolean flag = true;
        if (leavesPos.getY() >= 1 && leavesPos.getY() + height + 1 <= 256) {
            for (int i = 0; i <= 1 + height; ++i) {
                int j = 2;
                if (i == 0) {
                    j = 1;
                } else if (i >= 1 + height - 2) {
                    j = 2;
                }
                for (int k = -j; k <= j && flag; ++k) {
                    for (int l = -j; l <= j && flag; ++l) {
                        if (leavesPos.getY() + i >= 0 && leavesPos.getY() + i < 256 && this.canGrowInto(worldIn.getBlockState(leavesPos.add(k, i, l)).getBlock())) continue;
                        flag = false;
                    }
                }
            }
            return flag;
        }
        return false;
    }

    private boolean ensureDirtsUnderneath(BlockPos pos, World worldIn) {
        BlockPos blockpos = pos.down();
        Block block = worldIn.getBlockState(blockpos).getBlock();
        if ((block == Blocks.GRASS || block == Blocks.DIRT) && pos.getY() >= 2) {
            this.setDirtAt(worldIn, blockpos);
            this.setDirtAt(worldIn, blockpos.east());
            this.setDirtAt(worldIn, blockpos.south());
            this.setDirtAt(worldIn, blockpos.south().east());
            return true;
        }
        return false;
    }

    protected boolean ensureGrowable(World worldIn, Random rand, BlockPos treePos, int p_175929_4_) {
        return this.isSpaceAt(worldIn, treePos, p_175929_4_) && this.ensureDirtsUnderneath(treePos, worldIn);
    }

    protected void growLeavesLayerStrict(World worldIn, BlockPos layerCenter, int width) {
        int i = width * width;
        for (int j = -width; j <= width + 1; ++j) {
            for (int k = -width; k <= width + 1; ++k) {
                BlockPos blockpos;
                Material material;
                int l = j - 1;
                int i1 = k - 1;
                if (j * j + k * k > i && l * l + i1 * i1 > i && j * j + i1 * i1 > i && l * l + k * k > i || (material = worldIn.getBlockState(blockpos = layerCenter.add(j, 0, k)).getMaterial()) != Material.AIR && material != Material.LEAVES) continue;
                this.setBlockAndNotifyAdequately(worldIn, blockpos, this.leavesMetadata);
            }
        }
    }

    protected void growLeavesLayer(World worldIn, BlockPos layerCenter, int width) {
        int i = width * width;
        for (int j = -width; j <= width; ++j) {
            for (int k = -width; k <= width; ++k) {
                BlockPos blockpos;
                Material material;
                if (j * j + k * k > i || (material = worldIn.getBlockState(blockpos = layerCenter.add(j, 0, k)).getMaterial()) != Material.AIR && material != Material.LEAVES) continue;
                this.setBlockAndNotifyAdequately(worldIn, blockpos, this.leavesMetadata);
            }
        }
    }
}

