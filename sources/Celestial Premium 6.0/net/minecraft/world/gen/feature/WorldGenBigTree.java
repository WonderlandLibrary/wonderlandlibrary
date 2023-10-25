/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.feature;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenBigTree
extends WorldGenAbstractTree {
    private Random rand;
    private World world;
    private BlockPos basePos = BlockPos.ORIGIN;
    int heightLimit;
    int height;
    double heightAttenuation = 0.618;
    double branchSlope = 0.381;
    double scaleWidth = 1.0;
    double leafDensity = 1.0;
    int trunkSize = 1;
    int heightLimitLimit = 12;
    int leafDistanceLimit = 4;
    List<FoliageCoordinates> foliageCoords;

    public WorldGenBigTree(boolean notify) {
        super(notify);
    }

    void generateLeafNodeList() {
        int k;
        int i;
        this.height = (int)((double)this.heightLimit * this.heightAttenuation);
        if (this.height >= this.heightLimit) {
            this.height = this.heightLimit - 1;
        }
        if ((i = (int)(1.382 + Math.pow(this.leafDensity * (double)this.heightLimit / 13.0, 2.0))) < 1) {
            i = 1;
        }
        int j = this.basePos.getY() + this.height;
        this.foliageCoords = Lists.newArrayList();
        this.foliageCoords.add(new FoliageCoordinates(this.basePos.up(k), j));
        for (k = this.heightLimit - this.leafDistanceLimit; k >= 0; --k) {
            float f = this.layerSize(k);
            if (!(f >= 0.0f)) continue;
            for (int l = 0; l < i; ++l) {
                BlockPos blockpos1;
                double d3;
                double d1;
                double d0 = this.scaleWidth * (double)f * ((double)this.rand.nextFloat() + 0.328);
                double d2 = d0 * Math.sin(d1 = (double)(this.rand.nextFloat() * 2.0f) * Math.PI) + 0.5;
                BlockPos blockpos = this.basePos.add(d2, (double)(k - 1), d3 = d0 * Math.cos(d1) + 0.5);
                if (this.checkBlockLine(blockpos, blockpos1 = blockpos.up(this.leafDistanceLimit)) != -1) continue;
                int i1 = this.basePos.getX() - blockpos.getX();
                int j1 = this.basePos.getZ() - blockpos.getZ();
                double d4 = (double)blockpos.getY() - Math.sqrt(i1 * i1 + j1 * j1) * this.branchSlope;
                int k1 = d4 > (double)j ? j : (int)d4;
                BlockPos blockpos2 = new BlockPos(this.basePos.getX(), k1, this.basePos.getZ());
                if (this.checkBlockLine(blockpos2, blockpos) != -1) continue;
                this.foliageCoords.add(new FoliageCoordinates(blockpos, blockpos2.getY()));
            }
        }
    }

    void crosSection(BlockPos pos, float p_181631_2_, IBlockState p_181631_3_) {
        int i = (int)((double)p_181631_2_ + 0.618);
        for (int j = -i; j <= i; ++j) {
            for (int k = -i; k <= i; ++k) {
                BlockPos blockpos;
                Material material;
                if (!(Math.pow((double)Math.abs(j) + 0.5, 2.0) + Math.pow((double)Math.abs(k) + 0.5, 2.0) <= (double)(p_181631_2_ * p_181631_2_)) || (material = this.world.getBlockState(blockpos = pos.add(j, 0, k)).getMaterial()) != Material.AIR && material != Material.LEAVES) continue;
                this.setBlockAndNotifyAdequately(this.world, blockpos, p_181631_3_);
            }
        }
    }

    float layerSize(int y) {
        if ((float)y < (float)this.heightLimit * 0.3f) {
            return -1.0f;
        }
        float f = (float)this.heightLimit / 2.0f;
        float f1 = f - (float)y;
        float f2 = MathHelper.sqrt(f * f - f1 * f1);
        if (f1 == 0.0f) {
            f2 = f;
        } else if (Math.abs(f1) >= f) {
            return 0.0f;
        }
        return f2 * 0.5f;
    }

    float leafSize(int y) {
        if (y >= 0 && y < this.leafDistanceLimit) {
            return y != 0 && y != this.leafDistanceLimit - 1 ? 3.0f : 2.0f;
        }
        return -1.0f;
    }

    void generateLeafNode(BlockPos pos) {
        for (int i = 0; i < this.leafDistanceLimit; ++i) {
            this.crosSection(pos.up(i), this.leafSize(i), Blocks.LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, false));
        }
    }

    void limb(BlockPos p_175937_1_, BlockPos p_175937_2_, Block p_175937_3_) {
        BlockPos blockpos = p_175937_2_.add(-p_175937_1_.getX(), -p_175937_1_.getY(), -p_175937_1_.getZ());
        int i = this.getGreatestDistance(blockpos);
        float f = (float)blockpos.getX() / (float)i;
        float f1 = (float)blockpos.getY() / (float)i;
        float f2 = (float)blockpos.getZ() / (float)i;
        for (int j = 0; j <= i; ++j) {
            BlockPos blockpos1 = p_175937_1_.add(0.5f + (float)j * f, 0.5f + (float)j * f1, 0.5f + (float)j * f2);
            BlockLog.EnumAxis blocklog$enumaxis = this.getLogAxis(p_175937_1_, blockpos1);
            this.setBlockAndNotifyAdequately(this.world, blockpos1, p_175937_3_.getDefaultState().withProperty(BlockLog.LOG_AXIS, blocklog$enumaxis));
        }
    }

    private int getGreatestDistance(BlockPos posIn) {
        int i = MathHelper.abs(posIn.getX());
        int j = MathHelper.abs(posIn.getY());
        int k = MathHelper.abs(posIn.getZ());
        if (k > i && k > j) {
            return k;
        }
        return j > i ? j : i;
    }

    private BlockLog.EnumAxis getLogAxis(BlockPos p_175938_1_, BlockPos p_175938_2_) {
        int j;
        BlockLog.EnumAxis blocklog$enumaxis = BlockLog.EnumAxis.Y;
        int i = Math.abs(p_175938_2_.getX() - p_175938_1_.getX());
        int k = Math.max(i, j = Math.abs(p_175938_2_.getZ() - p_175938_1_.getZ()));
        if (k > 0) {
            if (i == k) {
                blocklog$enumaxis = BlockLog.EnumAxis.X;
            } else if (j == k) {
                blocklog$enumaxis = BlockLog.EnumAxis.Z;
            }
        }
        return blocklog$enumaxis;
    }

    void generateLeaves() {
        for (FoliageCoordinates worldgenbigtree$foliagecoordinates : this.foliageCoords) {
            this.generateLeafNode(worldgenbigtree$foliagecoordinates);
        }
    }

    boolean leafNodeNeedsBase(int p_76493_1_) {
        return (double)p_76493_1_ >= (double)this.heightLimit * 0.2;
    }

    void generateTrunk() {
        BlockPos blockpos = this.basePos;
        BlockPos blockpos1 = this.basePos.up(this.height);
        Block block = Blocks.LOG;
        this.limb(blockpos, blockpos1, block);
        if (this.trunkSize == 2) {
            this.limb(blockpos.east(), blockpos1.east(), block);
            this.limb(blockpos.east().south(), blockpos1.east().south(), block);
            this.limb(blockpos.south(), blockpos1.south(), block);
        }
    }

    void generateLeafNodeBases() {
        for (FoliageCoordinates worldgenbigtree$foliagecoordinates : this.foliageCoords) {
            int i = worldgenbigtree$foliagecoordinates.getBranchBase();
            BlockPos blockpos = new BlockPos(this.basePos.getX(), i, this.basePos.getZ());
            if (blockpos.equals(worldgenbigtree$foliagecoordinates) || !this.leafNodeNeedsBase(i - this.basePos.getY())) continue;
            this.limb(blockpos, worldgenbigtree$foliagecoordinates, Blocks.LOG);
        }
    }

    int checkBlockLine(BlockPos posOne, BlockPos posTwo) {
        BlockPos blockpos = posTwo.add(-posOne.getX(), -posOne.getY(), -posOne.getZ());
        int i = this.getGreatestDistance(blockpos);
        float f = (float)blockpos.getX() / (float)i;
        float f1 = (float)blockpos.getY() / (float)i;
        float f2 = (float)blockpos.getZ() / (float)i;
        if (i == 0) {
            return -1;
        }
        for (int j = 0; j <= i; ++j) {
            BlockPos blockpos1 = posOne.add(0.5f + (float)j * f, 0.5f + (float)j * f1, 0.5f + (float)j * f2);
            if (this.canGrowInto(this.world.getBlockState(blockpos1).getBlock())) continue;
            return j;
        }
        return -1;
    }

    @Override
    public void setDecorationDefaults() {
        this.leafDistanceLimit = 5;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        this.world = worldIn;
        this.basePos = position;
        this.rand = new Random(rand.nextLong());
        if (this.heightLimit == 0) {
            this.heightLimit = 5 + this.rand.nextInt(this.heightLimitLimit);
        }
        if (!this.validTreeLocation()) {
            return false;
        }
        this.generateLeafNodeList();
        this.generateLeaves();
        this.generateTrunk();
        this.generateLeafNodeBases();
        return true;
    }

    private boolean validTreeLocation() {
        Block block = this.world.getBlockState(this.basePos.down()).getBlock();
        if (block != Blocks.DIRT && block != Blocks.GRASS && block != Blocks.FARMLAND) {
            return false;
        }
        int i = this.checkBlockLine(this.basePos, this.basePos.up(this.heightLimit - 1));
        if (i == -1) {
            return true;
        }
        if (i < 6) {
            return false;
        }
        this.heightLimit = i;
        return true;
    }

    static class FoliageCoordinates
    extends BlockPos {
        private final int branchBase;

        public FoliageCoordinates(BlockPos pos, int p_i45635_2_) {
            super(pos.getX(), pos.getY(), pos.getZ());
            this.branchBase = p_i45635_2_;
        }

        public int getBranchBase() {
            return this.branchBase;
        }
    }
}

