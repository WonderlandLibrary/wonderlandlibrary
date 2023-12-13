// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import java.util.Iterator;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockLog;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import java.util.Random;

public class WorldGenBigTree extends WorldGenAbstractTree
{
    private Random field_175949_k;
    private World field_175946_l;
    private BlockPos field_175947_m;
    int heightLimit;
    int height;
    double heightAttenuation;
    double field_175944_d;
    double field_175945_e;
    double leafDensity;
    int field_175943_g;
    int field_175950_h;
    int leafDistanceLimit;
    List field_175948_j;
    private static final String __OBFID = "CL_00000400";
    
    public WorldGenBigTree(final boolean p_i2008_1_) {
        super(p_i2008_1_);
        this.field_175947_m = BlockPos.ORIGIN;
        this.heightAttenuation = 0.618;
        this.field_175944_d = 0.381;
        this.field_175945_e = 1.0;
        this.leafDensity = 1.0;
        this.field_175943_g = 1;
        this.field_175950_h = 12;
        this.leafDistanceLimit = 4;
    }
    
    void generateLeafNodeList() {
        this.height = (int)(this.heightLimit * this.heightAttenuation);
        if (this.height >= this.heightLimit) {
            this.height = this.heightLimit - 1;
        }
        int var1 = (int)(1.382 + Math.pow(this.leafDensity * this.heightLimit / 13.0, 2.0));
        if (var1 < 1) {
            var1 = 1;
        }
        final int var2 = this.field_175947_m.getY() + this.height;
        int var3 = this.heightLimit - this.leafDistanceLimit;
        (this.field_175948_j = Lists.newArrayList()).add(new FoliageCoordinates(this.field_175947_m.offsetUp(var3), var2));
        while (var3 >= 0) {
            final float var4 = this.layerSize(var3);
            if (var4 >= 0.0f) {
                for (int var5 = 0; var5 < var1; ++var5) {
                    final double var6 = this.field_175945_e * var4 * (this.field_175949_k.nextFloat() + 0.328);
                    final double var7 = this.field_175949_k.nextFloat() * 2.0f * 3.141592653589793;
                    final double var8 = var6 * Math.sin(var7) + 0.5;
                    final double var9 = var6 * Math.cos(var7) + 0.5;
                    final BlockPos var10 = this.field_175947_m.add(var8, var3 - 1, var9);
                    final BlockPos var11 = var10.offsetUp(this.leafDistanceLimit);
                    if (this.func_175936_a(var10, var11) == -1) {
                        final int var12 = this.field_175947_m.getX() - var10.getX();
                        final int var13 = this.field_175947_m.getZ() - var10.getZ();
                        final double var14 = var10.getY() - Math.sqrt(var12 * var12 + var13 * var13) * this.field_175944_d;
                        final int var15 = (var14 > var2) ? var2 : ((int)var14);
                        final BlockPos var16 = new BlockPos(this.field_175947_m.getX(), var15, this.field_175947_m.getZ());
                        if (this.func_175936_a(var16, var10) == -1) {
                            this.field_175948_j.add(new FoliageCoordinates(var10, var16.getY()));
                        }
                    }
                }
            }
            --var3;
        }
    }
    
    void func_180712_a(final BlockPos p_180712_1_, final float p_180712_2_, final Block p_180712_3_) {
        for (int var4 = (int)(p_180712_2_ + 0.618), var5 = -var4; var5 <= var4; ++var5) {
            for (int var6 = -var4; var6 <= var4; ++var6) {
                if (Math.pow(Math.abs(var5) + 0.5, 2.0) + Math.pow(Math.abs(var6) + 0.5, 2.0) <= p_180712_2_ * p_180712_2_) {
                    final BlockPos var7 = p_180712_1_.add(var5, 0, var6);
                    final Material var8 = this.field_175946_l.getBlockState(var7).getBlock().getMaterial();
                    if (var8 == Material.air || var8 == Material.leaves) {
                        this.func_175905_a(this.field_175946_l, var7, p_180712_3_, 0);
                    }
                }
            }
        }
    }
    
    float layerSize(final int p_76490_1_) {
        if (p_76490_1_ < this.heightLimit * 0.3f) {
            return -1.0f;
        }
        final float var2 = this.heightLimit / 2.0f;
        final float var3 = var2 - p_76490_1_;
        float var4 = MathHelper.sqrt_float(var2 * var2 - var3 * var3);
        if (var3 == 0.0f) {
            var4 = var2;
        }
        else if (Math.abs(var3) >= var2) {
            return 0.0f;
        }
        return var4 * 0.5f;
    }
    
    float leafSize(final int p_76495_1_) {
        return (p_76495_1_ >= 0 && p_76495_1_ < this.leafDistanceLimit) ? ((p_76495_1_ != 0 && p_76495_1_ != this.leafDistanceLimit - 1) ? 3.0f : 2.0f) : -1.0f;
    }
    
    void func_175940_a(final BlockPos p_175940_1_) {
        for (int var2 = 0; var2 < this.leafDistanceLimit; ++var2) {
            this.func_180712_a(p_175940_1_.offsetUp(var2), this.leafSize(var2), Blocks.leaves);
        }
    }
    
    void func_175937_a(final BlockPos p_175937_1_, final BlockPos p_175937_2_, final Block p_175937_3_) {
        final BlockPos var4 = p_175937_2_.add(-p_175937_1_.getX(), -p_175937_1_.getY(), -p_175937_1_.getZ());
        final int var5 = this.func_175935_b(var4);
        final float var6 = var4.getX() / var5;
        final float var7 = var4.getY() / var5;
        final float var8 = var4.getZ() / var5;
        for (int var9 = 0; var9 <= var5; ++var9) {
            final BlockPos var10 = p_175937_1_.add(0.5f + var9 * var6, 0.5f + var9 * var7, 0.5f + var9 * var8);
            final BlockLog.EnumAxis var11 = this.func_175938_b(p_175937_1_, var10);
            this.func_175903_a(this.field_175946_l, var10, p_175937_3_.getDefaultState().withProperty(BlockLog.AXIS_PROP, var11));
        }
    }
    
    private int func_175935_b(final BlockPos p_175935_1_) {
        final int var2 = MathHelper.abs_int(p_175935_1_.getX());
        final int var3 = MathHelper.abs_int(p_175935_1_.getY());
        final int var4 = MathHelper.abs_int(p_175935_1_.getZ());
        return (var4 > var2 && var4 > var3) ? var4 : ((var3 > var2) ? var3 : var2);
    }
    
    private BlockLog.EnumAxis func_175938_b(final BlockPos p_175938_1_, final BlockPos p_175938_2_) {
        BlockLog.EnumAxis var3 = BlockLog.EnumAxis.Y;
        final int var4 = Math.abs(p_175938_2_.getX() - p_175938_1_.getX());
        final int var5 = Math.abs(p_175938_2_.getZ() - p_175938_1_.getZ());
        final int var6 = Math.max(var4, var5);
        if (var6 > 0) {
            if (var4 == var6) {
                var3 = BlockLog.EnumAxis.X;
            }
            else if (var5 == var6) {
                var3 = BlockLog.EnumAxis.Z;
            }
        }
        return var3;
    }
    
    void func_175941_b() {
        for (final FoliageCoordinates var2 : this.field_175948_j) {
            this.func_175940_a(var2);
        }
    }
    
    boolean leafNodeNeedsBase(final int p_76493_1_) {
        return p_76493_1_ >= this.heightLimit * 0.2;
    }
    
    void func_175942_c() {
        final BlockPos var1 = this.field_175947_m;
        final BlockPos var2 = this.field_175947_m.offsetUp(this.height);
        final Block var3 = Blocks.log;
        this.func_175937_a(var1, var2, var3);
        if (this.field_175943_g == 2) {
            this.func_175937_a(var1.offsetEast(), var2.offsetEast(), var3);
            this.func_175937_a(var1.offsetEast().offsetSouth(), var2.offsetEast().offsetSouth(), var3);
            this.func_175937_a(var1.offsetSouth(), var2.offsetSouth(), var3);
        }
    }
    
    void func_175939_d() {
        for (final FoliageCoordinates var2 : this.field_175948_j) {
            final int var3 = var2.func_177999_q();
            final BlockPos var4 = new BlockPos(this.field_175947_m.getX(), var3, this.field_175947_m.getZ());
            if (this.leafNodeNeedsBase(var3 - this.field_175947_m.getY())) {
                this.func_175937_a(var4, var2, Blocks.log);
            }
        }
    }
    
    int func_175936_a(final BlockPos p_175936_1_, final BlockPos p_175936_2_) {
        final BlockPos var3 = p_175936_2_.add(-p_175936_1_.getX(), -p_175936_1_.getY(), -p_175936_1_.getZ());
        final int var4 = this.func_175935_b(var3);
        final float var5 = var3.getX() / var4;
        final float var6 = var3.getY() / var4;
        final float var7 = var3.getZ() / var4;
        if (var4 == 0) {
            return -1;
        }
        for (int var8 = 0; var8 <= var4; ++var8) {
            final BlockPos var9 = p_175936_1_.add(0.5f + var8 * var5, 0.5f + var8 * var6, 0.5f + var8 * var7);
            if (!this.func_150523_a(this.field_175946_l.getBlockState(var9).getBlock())) {
                return var8;
            }
        }
        return -1;
    }
    
    @Override
    public void func_175904_e() {
        this.leafDistanceLimit = 5;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, final BlockPos p_180709_3_) {
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
        final Block var1 = this.field_175946_l.getBlockState(this.field_175947_m.offsetDown()).getBlock();
        if (var1 != Blocks.dirt && var1 != Blocks.grass && var1 != Blocks.farmland) {
            return false;
        }
        final int var2 = this.func_175936_a(this.field_175947_m, this.field_175947_m.offsetUp(this.heightLimit - 1));
        if (var2 == -1) {
            return true;
        }
        if (var2 < 6) {
            return false;
        }
        this.heightLimit = var2;
        return true;
    }
    
    static class FoliageCoordinates extends BlockPos
    {
        private final int field_178000_b;
        private static final String __OBFID = "CL_00002001";
        
        public FoliageCoordinates(final BlockPos p_i45635_1_, final int p_i45635_2_) {
            super(p_i45635_1_.getX(), p_i45635_1_.getY(), p_i45635_1_.getZ());
            this.field_178000_b = p_i45635_2_;
        }
        
        public int func_177999_q() {
            return this.field_178000_b;
        }
    }
}
