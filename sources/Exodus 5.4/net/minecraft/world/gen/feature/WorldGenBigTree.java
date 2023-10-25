/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
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
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenBigTree
extends WorldGenAbstractTree {
    List<FoliageCoordinates> field_175948_j;
    double leafDensity = 1.0;
    int heightLimitLimit = 12;
    double heightAttenuation = 0.618;
    int leafDistanceLimit = 4;
    private Random rand;
    private World world;
    double scaleWidth = 1.0;
    double branchSlope = 0.381;
    int trunkSize = 1;
    int height;
    private BlockPos basePos = BlockPos.ORIGIN;
    int heightLimit;

    public WorldGenBigTree(boolean bl) {
        super(bl);
    }

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        this.world = world;
        this.basePos = blockPos;
        this.rand = new Random(random.nextLong());
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

    float layerSize(int n) {
        if ((float)n < (float)this.heightLimit * 0.3f) {
            return -1.0f;
        }
        float f = (float)this.heightLimit / 2.0f;
        float f2 = f - (float)n;
        float f3 = MathHelper.sqrt_float(f * f - f2 * f2);
        if (f2 == 0.0f) {
            f3 = f;
        } else if (Math.abs(f2) >= f) {
            return 0.0f;
        }
        return f3 * 0.5f;
    }

    private int getGreatestDistance(BlockPos blockPos) {
        int n = MathHelper.abs_int(blockPos.getX());
        int n2 = MathHelper.abs_int(blockPos.getY());
        int n3 = MathHelper.abs_int(blockPos.getZ());
        return n3 > n && n3 > n2 ? n3 : (n2 > n ? n2 : n);
    }

    private BlockLog.EnumAxis func_175938_b(BlockPos blockPos, BlockPos blockPos2) {
        int n;
        BlockLog.EnumAxis enumAxis = BlockLog.EnumAxis.Y;
        int n2 = Math.abs(blockPos2.getX() - blockPos.getX());
        int n3 = Math.max(n2, n = Math.abs(blockPos2.getZ() - blockPos.getZ()));
        if (n3 > 0) {
            if (n2 == n3) {
                enumAxis = BlockLog.EnumAxis.X;
            } else if (n == n3) {
                enumAxis = BlockLog.EnumAxis.Z;
            }
        }
        return enumAxis;
    }

    void generateLeaves() {
        for (FoliageCoordinates foliageCoordinates : this.field_175948_j) {
            this.generateLeafNode(foliageCoordinates);
        }
    }

    void func_175937_a(BlockPos blockPos, BlockPos blockPos2, Block block) {
        BlockPos blockPos3 = blockPos2.add(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ());
        int n = this.getGreatestDistance(blockPos3);
        float f = (float)blockPos3.getX() / (float)n;
        float f2 = (float)blockPos3.getY() / (float)n;
        float f3 = (float)blockPos3.getZ() / (float)n;
        int n2 = 0;
        while (n2 <= n) {
            BlockPos blockPos4 = blockPos.add(0.5f + (float)n2 * f, 0.5f + (float)n2 * f2, 0.5f + (float)n2 * f3);
            BlockLog.EnumAxis enumAxis = this.func_175938_b(blockPos, blockPos4);
            this.setBlockAndNotifyAdequately(this.world, blockPos4, block.getDefaultState().withProperty(BlockLog.LOG_AXIS, enumAxis));
            ++n2;
        }
    }

    float leafSize(int n) {
        return n >= 0 && n < this.leafDistanceLimit ? (n != 0 && n != this.leafDistanceLimit - 1 ? 3.0f : 2.0f) : -1.0f;
    }

    @Override
    public void func_175904_e() {
        this.leafDistanceLimit = 5;
    }

    void generateTrunk() {
        BlockPos blockPos = this.basePos;
        BlockPos blockPos2 = this.basePos.up(this.height);
        Block block = Blocks.log;
        this.func_175937_a(blockPos, blockPos2, block);
        if (this.trunkSize == 2) {
            this.func_175937_a(blockPos.east(), blockPos2.east(), block);
            this.func_175937_a(blockPos.east().south(), blockPos2.east().south(), block);
            this.func_175937_a(blockPos.south(), blockPos2.south(), block);
        }
    }

    int checkBlockLine(BlockPos blockPos, BlockPos blockPos2) {
        BlockPos blockPos3 = blockPos2.add(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ());
        int n = this.getGreatestDistance(blockPos3);
        float f = (float)blockPos3.getX() / (float)n;
        float f2 = (float)blockPos3.getY() / (float)n;
        float f3 = (float)blockPos3.getZ() / (float)n;
        if (n == 0) {
            return -1;
        }
        int n2 = 0;
        while (n2 <= n) {
            BlockPos blockPos4 = blockPos.add(0.5f + (float)n2 * f, 0.5f + (float)n2 * f2, 0.5f + (float)n2 * f3);
            if (!this.func_150523_a(this.world.getBlockState(blockPos4).getBlock())) {
                return n2;
            }
            ++n2;
        }
        return -1;
    }

    void func_181631_a(BlockPos blockPos, float f, IBlockState iBlockState) {
        int n = (int)((double)f + 0.618);
        int n2 = -n;
        while (n2 <= n) {
            int n3 = -n;
            while (n3 <= n) {
                BlockPos blockPos2;
                Material material;
                if (Math.pow((double)Math.abs(n2) + 0.5, 2.0) + Math.pow((double)Math.abs(n3) + 0.5, 2.0) <= (double)(f * f) && ((material = this.world.getBlockState(blockPos2 = blockPos.add(n2, 0, n3)).getBlock().getMaterial()) == Material.air || material == Material.leaves)) {
                    this.setBlockAndNotifyAdequately(this.world, blockPos2, iBlockState);
                }
                ++n3;
            }
            ++n2;
        }
    }

    boolean leafNodeNeedsBase(int n) {
        return (double)n >= (double)this.heightLimit * 0.2;
    }

    void generateLeafNodeBases() {
        for (FoliageCoordinates foliageCoordinates : this.field_175948_j) {
            int n = foliageCoordinates.func_177999_q();
            BlockPos blockPos = new BlockPos(this.basePos.getX(), n, this.basePos.getZ());
            if (blockPos.equals(foliageCoordinates) || !this.leafNodeNeedsBase(n - this.basePos.getY())) continue;
            this.func_175937_a(blockPos, foliageCoordinates, Blocks.log);
        }
    }

    void generateLeafNode(BlockPos blockPos) {
        int n = 0;
        while (n < this.leafDistanceLimit) {
            this.func_181631_a(blockPos.up(n), this.leafSize(n), Blocks.leaves.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, false));
            ++n;
        }
    }

    private boolean validTreeLocation() {
        Block block = this.world.getBlockState(this.basePos.down()).getBlock();
        if (block != Blocks.dirt && block != Blocks.grass && block != Blocks.farmland) {
            return false;
        }
        int n = this.checkBlockLine(this.basePos, this.basePos.up(this.heightLimit - 1));
        if (n == -1) {
            return true;
        }
        if (n < 6) {
            return false;
        }
        this.heightLimit = n;
        return true;
    }

    void generateLeafNodeList() {
        int n;
        this.height = (int)((double)this.heightLimit * this.heightAttenuation);
        if (this.height >= this.heightLimit) {
            this.height = this.heightLimit - 1;
        }
        if ((n = (int)(1.382 + Math.pow(this.leafDensity * (double)this.heightLimit / 13.0, 2.0))) < 1) {
            n = 1;
        }
        int n2 = this.basePos.getY() + this.height;
        int n3 = this.heightLimit - this.leafDistanceLimit;
        this.field_175948_j = Lists.newArrayList();
        this.field_175948_j.add(new FoliageCoordinates(this.basePos.up(n3), n2));
        while (n3 >= 0) {
            float f = this.layerSize(n3);
            if (f >= 0.0f) {
                int n4 = 0;
                while (n4 < n) {
                    BlockPos blockPos;
                    double d;
                    double d2;
                    double d3 = this.scaleWidth * (double)f * ((double)this.rand.nextFloat() + 0.328);
                    double d4 = d3 * Math.sin(d2 = (double)(this.rand.nextFloat() * 2.0f) * Math.PI) + 0.5;
                    BlockPos blockPos2 = this.basePos.add(d4, (double)(n3 - 1), d = d3 * Math.cos(d2) + 0.5);
                    if (this.checkBlockLine(blockPos2, blockPos = blockPos2.up(this.leafDistanceLimit)) == -1) {
                        int n5 = this.basePos.getX() - blockPos2.getX();
                        int n6 = this.basePos.getZ() - blockPos2.getZ();
                        double d5 = (double)blockPos2.getY() - Math.sqrt(n5 * n5 + n6 * n6) * this.branchSlope;
                        int n7 = d5 > (double)n2 ? n2 : (int)d5;
                        BlockPos blockPos3 = new BlockPos(this.basePos.getX(), n7, this.basePos.getZ());
                        if (this.checkBlockLine(blockPos3, blockPos2) == -1) {
                            this.field_175948_j.add(new FoliageCoordinates(blockPos2, blockPos3.getY()));
                        }
                    }
                    ++n4;
                }
            }
            --n3;
        }
    }

    static class FoliageCoordinates
    extends BlockPos {
        private final int field_178000_b;

        public FoliageCoordinates(BlockPos blockPos, int n) {
            super(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            this.field_178000_b = n;
        }

        public int func_177999_q() {
            return this.field_178000_b;
        }
    }
}

