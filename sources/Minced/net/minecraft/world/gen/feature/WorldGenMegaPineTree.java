// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;

public class WorldGenMegaPineTree extends WorldGenHugeTrees
{
    private static final IBlockState TRUNK;
    private static final IBlockState LEAF;
    private static final IBlockState PODZOL;
    private final boolean useBaseHeight;
    
    public WorldGenMegaPineTree(final boolean notify, final boolean p_i45457_2_) {
        super(notify, 13, 15, WorldGenMegaPineTree.TRUNK, WorldGenMegaPineTree.LEAF);
        this.useBaseHeight = p_i45457_2_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        final int i = this.getHeight(rand);
        if (!this.ensureGrowable(worldIn, rand, position, i)) {
            return false;
        }
        this.createCrown(worldIn, position.getX(), position.getZ(), position.getY() + i, 0, rand);
        for (int j = 0; j < i; ++j) {
            IBlockState iblockstate = worldIn.getBlockState(position.up(j));
            if (iblockstate.getMaterial() == Material.AIR || iblockstate.getMaterial() == Material.LEAVES) {
                this.setBlockAndNotifyAdequately(worldIn, position.up(j), this.woodMetadata);
            }
            if (j < i - 1) {
                iblockstate = worldIn.getBlockState(position.add(1, j, 0));
                if (iblockstate.getMaterial() == Material.AIR || iblockstate.getMaterial() == Material.LEAVES) {
                    this.setBlockAndNotifyAdequately(worldIn, position.add(1, j, 0), this.woodMetadata);
                }
                iblockstate = worldIn.getBlockState(position.add(1, j, 1));
                if (iblockstate.getMaterial() == Material.AIR || iblockstate.getMaterial() == Material.LEAVES) {
                    this.setBlockAndNotifyAdequately(worldIn, position.add(1, j, 1), this.woodMetadata);
                }
                iblockstate = worldIn.getBlockState(position.add(0, j, 1));
                if (iblockstate.getMaterial() == Material.AIR || iblockstate.getMaterial() == Material.LEAVES) {
                    this.setBlockAndNotifyAdequately(worldIn, position.add(0, j, 1), this.woodMetadata);
                }
            }
        }
        return true;
    }
    
    private void createCrown(final World worldIn, final int x, final int z, final int y, final int p_150541_5_, final Random rand) {
        final int i = rand.nextInt(5) + (this.useBaseHeight ? this.baseHeight : 3);
        int j = 0;
        for (int k = y - i; k <= y; ++k) {
            final int l = y - k;
            final int i2 = p_150541_5_ + MathHelper.floor(l / (float)i * 3.5f);
            this.growLeavesLayerStrict(worldIn, new BlockPos(x, k, z), i2 + ((l > 0 && i2 == j && (k & 0x1) == 0x0) ? 1 : 0));
            j = i2;
        }
    }
    
    @Override
    public void generateSaplings(final World worldIn, final Random random, final BlockPos pos) {
        this.placePodzolCircle(worldIn, pos.west().north());
        this.placePodzolCircle(worldIn, pos.east(2).north());
        this.placePodzolCircle(worldIn, pos.west().south(2));
        this.placePodzolCircle(worldIn, pos.east(2).south(2));
        for (int i = 0; i < 5; ++i) {
            final int j = random.nextInt(64);
            final int k = j % 8;
            final int l = j / 8;
            if (k == 0 || k == 7 || l == 0 || l == 7) {
                this.placePodzolCircle(worldIn, pos.add(-3 + k, 0, -3 + l));
            }
        }
    }
    
    private void placePodzolCircle(final World worldIn, final BlockPos center) {
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                if (Math.abs(i) != 2 || Math.abs(j) != 2) {
                    this.placePodzolAt(worldIn, center.add(i, 0, j));
                }
            }
        }
    }
    
    private void placePodzolAt(final World worldIn, final BlockPos pos) {
        for (int i = 2; i >= -3; --i) {
            final BlockPos blockpos = pos.up(i);
            final IBlockState iblockstate = worldIn.getBlockState(blockpos);
            final Block block = iblockstate.getBlock();
            if (block == Blocks.GRASS || block == Blocks.DIRT) {
                this.setBlockAndNotifyAdequately(worldIn, blockpos, WorldGenMegaPineTree.PODZOL);
                break;
            }
            if (iblockstate.getMaterial() != Material.AIR && i < 0) {
                break;
            }
        }
    }
    
    static {
        TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
        LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty((IProperty<Comparable>)BlockLeaves.CHECK_DECAY, false);
        PODZOL = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
    }
}
