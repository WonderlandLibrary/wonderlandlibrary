/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
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
    List<FoliageCoordinates> field_175948_j;

    public WorldGenBigTree(boolean p_i2008_1_) {
        super(p_i2008_1_);
    }

    void generateLeafNodeList() {
        int i;
        this.height = (int)((double)this.heightLimit * this.heightAttenuation);
        if (this.height >= this.heightLimit) {
            this.height = this.heightLimit - 1;
        }
        if ((i = (int)(1.382 + Math.pow(this.leafDensity * (double)this.heightLimit / 13.0, 2.0))) < 1) {
            i = 1;
        }
        int j = this.basePos.getY() + this.height;
        int k = this.heightLimit - this.leafDistanceLimit;
        this.field_175948_j = Lists.newArrayList();
        this.field_175948_j.add(new FoliageCoordinates(this.basePos.up(k), j));
        while (k >= 0) {
            float f = this.layerSize(k);
            if (f >= 0.0f) {
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
                    this.field_175948_j.add(new FoliageCoordinates(blockpos, blockpos2.getY()));
                }
            }
            --k;
        }
    }

    void func_181631_a(BlockPos p_181631_1_, float p_181631_2_, IBlockState p_181631_3_) {
        int i = (int)((double)p_181631_2_ + 0.618);
        int j = -i;
        while (j <= i) {
            for (int k = -i; k <= i; ++k) {
                BlockPos blockpos;
                Material material;
                if (!(Math.pow((double)Math.abs(j) + 0.5, 2.0) + Math.pow((double)Math.abs(k) + 0.5, 2.0) <= (double)(p_181631_2_ * p_181631_2_)) || (material = this.world.getBlockState(blockpos = p_181631_1_.add(j, 0, k)).getBlock().getMaterial()) != Material.air && material != Material.leaves) continue;
                this.setBlockAndNotifyAdequately(this.world, blockpos, p_181631_3_);
            }
            ++j;
        }
    }

    float layerSize(int p_76490_1_) {
        if ((float)p_76490_1_ < (float)this.heightLimit * 0.3f) {
            return -1.0f;
        }
        float f = (float)this.heightLimit / 2.0f;
        float f1 = f - (float)p_76490_1_;
        float f2 = MathHelper.sqrt_float(f * f - f1 * f1);
        if (f1 == 0.0f) {
            f2 = f;
            return f2 * 0.5f;
        }
        if (!(Math.abs(f1) >= f)) return f2 * 0.5f;
        return 0.0f;
    }

    float leafSize(int p_76495_1_) {
        if (p_76495_1_ < 0) return -1.0f;
        if (p_76495_1_ >= this.leafDistanceLimit) return -1.0f;
        if (p_76495_1_ == 0) return 2.0f;
        if (p_76495_1_ == this.leafDistanceLimit - 1) return 2.0f;
        return 3.0f;
    }

    void generateLeafNode(BlockPos pos) {
        int i = 0;
        while (i < this.leafDistanceLimit) {
            this.func_181631_a(pos.up(i), this.leafSize(i), Blocks.leaves.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, false));
            ++i;
        }
    }

    void func_175937_a(BlockPos p_175937_1_, BlockPos p_175937_2_, Block p_175937_3_) {
        BlockPos blockpos = p_175937_2_.add(-p_175937_1_.getX(), -p_175937_1_.getY(), -p_175937_1_.getZ());
        int i = this.getGreatestDistance(blockpos);
        float f = (float)blockpos.getX() / (float)i;
        float f1 = (float)blockpos.getY() / (float)i;
        float f2 = (float)blockpos.getZ() / (float)i;
        int j = 0;
        while (j <= i) {
            BlockPos blockpos1 = p_175937_1_.add(0.5f + (float)j * f, 0.5f + (float)j * f1, 0.5f + (float)j * f2);
            BlockLog.EnumAxis blocklog$enumaxis = this.func_175938_b(p_175937_1_, blockpos1);
            this.setBlockAndNotifyAdequately(this.world, blockpos1, p_175937_3_.getDefaultState().withProperty(BlockLog.LOG_AXIS, blocklog$enumaxis));
            ++j;
        }
    }

    private int getGreatestDistance(BlockPos posIn) {
        int n;
        int i = MathHelper.abs_int(posIn.getX());
        int j = MathHelper.abs_int(posIn.getY());
        int k = MathHelper.abs_int(posIn.getZ());
        if (k > i && k > j) {
            n = k;
            return n;
        }
        if (j > i) {
            n = j;
            return n;
        }
        n = i;
        return n;
    }

    private BlockLog.EnumAxis func_175938_b(BlockPos p_175938_1_, BlockPos p_175938_2_) {
        int j;
        BlockLog.EnumAxis blocklog$enumaxis = BlockLog.EnumAxis.Y;
        int i = Math.abs(p_175938_2_.getX() - p_175938_1_.getX());
        int k = Math.max(i, j = Math.abs(p_175938_2_.getZ() - p_175938_1_.getZ()));
        if (k <= 0) return blocklog$enumaxis;
        if (i == k) {
            return BlockLog.EnumAxis.X;
        }
        if (j != k) return blocklog$enumaxis;
        return BlockLog.EnumAxis.Z;
    }

    void generateLeaves() {
        Iterator<FoliageCoordinates> iterator = this.field_175948_j.iterator();
        while (iterator.hasNext()) {
            FoliageCoordinates worldgenbigtree$foliagecoordinates = iterator.next();
            this.generateLeafNode(worldgenbigtree$foliagecoordinates);
        }
    }

    boolean leafNodeNeedsBase(int p_76493_1_) {
        if (!((double)p_76493_1_ >= (double)this.heightLimit * 0.2)) return false;
        return true;
    }

    void generateTrunk() {
        BlockPos blockpos = this.basePos;
        BlockPos blockpos1 = this.basePos.up(this.height);
        Block block = Blocks.log;
        this.func_175937_a(blockpos, blockpos1, block);
        if (this.trunkSize != 2) return;
        this.func_175937_a(blockpos.east(), blockpos1.east(), block);
        this.func_175937_a(blockpos.east().south(), blockpos1.east().south(), block);
        this.func_175937_a(blockpos.south(), blockpos1.south(), block);
    }

    void generateLeafNodeBases() {
        Iterator<FoliageCoordinates> iterator = this.field_175948_j.iterator();
        while (iterator.hasNext()) {
            FoliageCoordinates worldgenbigtree$foliagecoordinates = iterator.next();
            int i = worldgenbigtree$foliagecoordinates.func_177999_q();
            BlockPos blockpos = new BlockPos(this.basePos.getX(), i, this.basePos.getZ());
            if (blockpos.equals(worldgenbigtree$foliagecoordinates) || !this.leafNodeNeedsBase(i - this.basePos.getY())) continue;
            this.func_175937_a(blockpos, worldgenbigtree$foliagecoordinates, Blocks.log);
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
        int j = 0;
        while (j <= i) {
            BlockPos blockpos1 = posOne.add(0.5f + (float)j * f, 0.5f + (float)j * f1, 0.5f + (float)j * f2);
            if (!this.func_150523_a(this.world.getBlockState(blockpos1).getBlock())) {
                return j;
            }
            ++j;
        }
        return -1;
    }

    @Override
    public void func_175904_e() {
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
        if (block != Blocks.dirt && block != Blocks.grass && block != Blocks.farmland) {
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
        private final int field_178000_b;

        public FoliageCoordinates(BlockPos p_i45635_1_, int p_i45635_2_) {
            super(p_i45635_1_.getX(), p_i45635_1_.getY(), p_i45635_1_.getZ());
            this.field_178000_b = p_i45635_2_;
        }

        public int func_177999_q() {
            return this.field_178000_b;
        }
    }
}

