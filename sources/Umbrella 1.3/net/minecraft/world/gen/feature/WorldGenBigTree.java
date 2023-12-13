/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.world.gen.feature;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenBigTree
extends WorldGenAbstractTree {
    private Random field_175949_k;
    private World field_175946_l;
    private BlockPos field_175947_m = BlockPos.ORIGIN;
    int heightLimit;
    int height;
    double heightAttenuation = 0.618;
    double field_175944_d = 0.381;
    double field_175945_e = 1.0;
    double leafDensity = 1.0;
    int field_175943_g = 1;
    int field_175950_h = 12;
    int leafDistanceLimit = 4;
    List field_175948_j;
    private static final String __OBFID = "CL_00000400";

    public WorldGenBigTree(boolean p_i2008_1_) {
        super(p_i2008_1_);
    }

    void generateLeafNodeList() {
        int var3;
        int var1;
        this.height = (int)((double)this.heightLimit * this.heightAttenuation);
        if (this.height >= this.heightLimit) {
            this.height = this.heightLimit - 1;
        }
        if ((var1 = (int)(1.382 + Math.pow(this.leafDensity * (double)this.heightLimit / 13.0, 2.0))) < 1) {
            var1 = 1;
        }
        int var2 = this.field_175947_m.getY() + this.height;
        this.field_175948_j = Lists.newArrayList();
        this.field_175948_j.add(new FoliageCoordinates(this.field_175947_m.offsetUp(var3), var2));
        for (var3 = this.heightLimit - this.leafDistanceLimit; var3 >= 0; --var3) {
            float var4 = this.layerSize(var3);
            if (!(var4 >= 0.0f)) continue;
            for (int var5 = 0; var5 < var1; ++var5) {
                BlockPos var15;
                double var12;
                double var8;
                double var6 = this.field_175945_e * (double)var4 * ((double)this.field_175949_k.nextFloat() + 0.328);
                double var10 = var6 * Math.sin(var8 = (double)(this.field_175949_k.nextFloat() * 2.0f) * Math.PI) + 0.5;
                BlockPos var14 = this.field_175947_m.add(var10, (double)(var3 - 1), var12 = var6 * Math.cos(var8) + 0.5);
                if (this.func_175936_a(var14, var15 = var14.offsetUp(this.leafDistanceLimit)) != -1) continue;
                int var16 = this.field_175947_m.getX() - var14.getX();
                int var17 = this.field_175947_m.getZ() - var14.getZ();
                double var18 = (double)var14.getY() - Math.sqrt(var16 * var16 + var17 * var17) * this.field_175944_d;
                int var20 = var18 > (double)var2 ? var2 : (int)var18;
                BlockPos var21 = new BlockPos(this.field_175947_m.getX(), var20, this.field_175947_m.getZ());
                if (this.func_175936_a(var21, var14) != -1) continue;
                this.field_175948_j.add(new FoliageCoordinates(var14, var21.getY()));
            }
        }
    }

    void func_180712_a(BlockPos p_180712_1_, float p_180712_2_, Block p_180712_3_) {
        int var4 = (int)((double)p_180712_2_ + 0.618);
        for (int var5 = -var4; var5 <= var4; ++var5) {
            for (int var6 = -var4; var6 <= var4; ++var6) {
                BlockPos var7;
                Material var8;
                if (!(Math.pow((double)Math.abs(var5) + 0.5, 2.0) + Math.pow((double)Math.abs(var6) + 0.5, 2.0) <= (double)(p_180712_2_ * p_180712_2_)) || (var8 = this.field_175946_l.getBlockState(var7 = p_180712_1_.add(var5, 0, var6)).getBlock().getMaterial()) != Material.air && var8 != Material.leaves) continue;
                this.func_175905_a(this.field_175946_l, var7, p_180712_3_, 0);
            }
        }
    }

    float layerSize(int p_76490_1_) {
        if ((float)p_76490_1_ < (float)this.heightLimit * 0.3f) {
            return -1.0f;
        }
        float var2 = (float)this.heightLimit / 2.0f;
        float var3 = var2 - (float)p_76490_1_;
        float var4 = MathHelper.sqrt_float(var2 * var2 - var3 * var3);
        if (var3 == 0.0f) {
            var4 = var2;
        } else if (Math.abs(var3) >= var2) {
            return 0.0f;
        }
        return var4 * 0.5f;
    }

    float leafSize(int p_76495_1_) {
        return p_76495_1_ >= 0 && p_76495_1_ < this.leafDistanceLimit ? (p_76495_1_ != 0 && p_76495_1_ != this.leafDistanceLimit - 1 ? 3.0f : 2.0f) : -1.0f;
    }

    void func_175940_a(BlockPos p_175940_1_) {
        for (int var2 = 0; var2 < this.leafDistanceLimit; ++var2) {
            this.func_180712_a(p_175940_1_.offsetUp(var2), this.leafSize(var2), Blocks.leaves);
        }
    }

    void func_175937_a(BlockPos p_175937_1_, BlockPos p_175937_2_, Block p_175937_3_) {
        BlockPos var4 = p_175937_2_.add(-p_175937_1_.getX(), -p_175937_1_.getY(), -p_175937_1_.getZ());
        int var5 = this.func_175935_b(var4);
        float var6 = (float)var4.getX() / (float)var5;
        float var7 = (float)var4.getY() / (float)var5;
        float var8 = (float)var4.getZ() / (float)var5;
        for (int var9 = 0; var9 <= var5; ++var9) {
            BlockPos var10 = p_175937_1_.add(0.5f + (float)var9 * var6, 0.5f + (float)var9 * var7, 0.5f + (float)var9 * var8);
            BlockLog.EnumAxis var11 = this.func_175938_b(p_175937_1_, var10);
            this.func_175903_a(this.field_175946_l, var10, p_175937_3_.getDefaultState().withProperty(BlockLog.AXIS_PROP, (Comparable)((Object)var11)));
        }
    }

    private int func_175935_b(BlockPos p_175935_1_) {
        int var2 = MathHelper.abs_int(p_175935_1_.getX());
        int var3 = MathHelper.abs_int(p_175935_1_.getY());
        int var4 = MathHelper.abs_int(p_175935_1_.getZ());
        return var4 > var2 && var4 > var3 ? var4 : (var3 > var2 ? var3 : var2);
    }

    private BlockLog.EnumAxis func_175938_b(BlockPos p_175938_1_, BlockPos p_175938_2_) {
        int var5;
        BlockLog.EnumAxis var3 = BlockLog.EnumAxis.Y;
        int var4 = Math.abs(p_175938_2_.getX() - p_175938_1_.getX());
        int var6 = Math.max(var4, var5 = Math.abs(p_175938_2_.getZ() - p_175938_1_.getZ()));
        if (var6 > 0) {
            if (var4 == var6) {
                var3 = BlockLog.EnumAxis.X;
            } else if (var5 == var6) {
                var3 = BlockLog.EnumAxis.Z;
            }
        }
        return var3;
    }

    void func_175941_b() {
        for (FoliageCoordinates var2 : this.field_175948_j) {
            this.func_175940_a(var2);
        }
    }

    boolean leafNodeNeedsBase(int p_76493_1_) {
        return (double)p_76493_1_ >= (double)this.heightLimit * 0.2;
    }

    void func_175942_c() {
        BlockPos var1 = this.field_175947_m;
        BlockPos var2 = this.field_175947_m.offsetUp(this.height);
        Block var3 = Blocks.log;
        this.func_175937_a(var1, var2, var3);
        if (this.field_175943_g == 2) {
            this.func_175937_a(var1.offsetEast(), var2.offsetEast(), var3);
            this.func_175937_a(var1.offsetEast().offsetSouth(), var2.offsetEast().offsetSouth(), var3);
            this.func_175937_a(var1.offsetSouth(), var2.offsetSouth(), var3);
        }
    }

    void func_175939_d() {
        for (FoliageCoordinates var2 : this.field_175948_j) {
            int var3 = var2.func_177999_q();
            BlockPos var4 = new BlockPos(this.field_175947_m.getX(), var3, this.field_175947_m.getZ());
            if (!this.leafNodeNeedsBase(var3 - this.field_175947_m.getY())) continue;
            this.func_175937_a(var4, var2, Blocks.log);
        }
    }

    int func_175936_a(BlockPos p_175936_1_, BlockPos p_175936_2_) {
        BlockPos var3 = p_175936_2_.add(-p_175936_1_.getX(), -p_175936_1_.getY(), -p_175936_1_.getZ());
        int var4 = this.func_175935_b(var3);
        float var5 = (float)var3.getX() / (float)var4;
        float var6 = (float)var3.getY() / (float)var4;
        float var7 = (float)var3.getZ() / (float)var4;
        if (var4 == 0) {
            return -1;
        }
        for (int var8 = 0; var8 <= var4; ++var8) {
            BlockPos var9 = p_175936_1_.add(0.5f + (float)var8 * var5, 0.5f + (float)var8 * var6, 0.5f + (float)var8 * var7);
            if (this.func_150523_a(this.field_175946_l.getBlockState(var9).getBlock())) continue;
            return var8;
        }
        return -1;
    }

    @Override
    public void func_175904_e() {
        this.leafDistanceLimit = 5;
    }

    @Override
    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_) {
        this.field_175946_l = worldIn;
        this.field_175947_m = p_180709_3_;
        this.field_175949_k = new Random(p_180709_2_.nextLong());
        if (this.heightLimit == 0) {
            this.heightLimit = 5 + this.field_175949_k.nextInt(this.field_175950_h);
        }
        if (!this.validTreeLocation()) {
            return false;
        }
        this.generateLeafNodeList();
        this.func_175941_b();
        this.func_175942_c();
        this.func_175939_d();
        return true;
    }

    private boolean validTreeLocation() {
        Block var1 = this.field_175946_l.getBlockState(this.field_175947_m.offsetDown()).getBlock();
        if (var1 != Blocks.dirt && var1 != Blocks.grass && var1 != Blocks.farmland) {
            return false;
        }
        int var2 = this.func_175936_a(this.field_175947_m, this.field_175947_m.offsetUp(this.heightLimit - 1));
        if (var2 == -1) {
            return true;
        }
        if (var2 < 6) {
            return false;
        }
        this.heightLimit = var2;
        return true;
    }

    static class FoliageCoordinates
    extends BlockPos {
        private final int field_178000_b;
        private static final String __OBFID = "CL_00002001";

        public FoliageCoordinates(BlockPos p_i45635_1_, int p_i45635_2_) {
            super(p_i45635_1_.getX(), p_i45635_1_.getY(), p_i45635_1_.getZ());
            this.field_178000_b = p_i45635_2_;
        }

        public int func_177999_q() {
            return this.field_178000_b;
        }
    }
}

