// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.BlockDirt;
import net.minecraft.util.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.BlockPlanks;

public class WorldGenMegaPineTree extends WorldGenHugeTrees
{
    private boolean field_150542_e;
    private static final String __OBFID = "CL_00000421";
    
    public WorldGenMegaPineTree(final boolean p_i45457_1_, final boolean p_i45457_2_) {
        super(p_i45457_1_, 13, 15, BlockPlanks.EnumType.SPRUCE.func_176839_a(), BlockPlanks.EnumType.SPRUCE.func_176839_a());
        this.field_150542_e = p_i45457_2_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, final BlockPos p_180709_3_) {
        final int var4 = this.func_150533_a(p_180709_2_);
        if (!this.func_175929_a(worldIn, p_180709_2_, p_180709_3_, var4)) {
            return false;
        }
        this.func_150541_c(worldIn, p_180709_3_.getX(), p_180709_3_.getZ(), p_180709_3_.getY() + var4, 0, p_180709_2_);
        for (int var5 = 0; var5 < var4; ++var5) {
            Block var6 = worldIn.getBlockState(p_180709_3_.offsetUp(var5)).getBlock();
            if (var6.getMaterial() == Material.air || var6.getMaterial() == Material.leaves) {
                this.func_175905_a(worldIn, p_180709_3_.offsetUp(var5), Blocks.log, this.woodMetadata);
            }
            if (var5 < var4 - 1) {
                var6 = worldIn.getBlockState(p_180709_3_.add(1, var5, 0)).getBlock();
                if (var6.getMaterial() == Material.air || var6.getMaterial() == Material.leaves) {
                    this.func_175905_a(worldIn, p_180709_3_.add(1, var5, 0), Blocks.log, this.woodMetadata);
                }
                var6 = worldIn.getBlockState(p_180709_3_.add(1, var5, 1)).getBlock();
                if (var6.getMaterial() == Material.air || var6.getMaterial() == Material.leaves) {
                    this.func_175905_a(worldIn, p_180709_3_.add(1, var5, 1), Blocks.log, this.woodMetadata);
                }
                var6 = worldIn.getBlockState(p_180709_3_.add(0, var5, 1)).getBlock();
                if (var6.getMaterial() == Material.air || var6.getMaterial() == Material.leaves) {
                    this.func_175905_a(worldIn, p_180709_3_.add(0, var5, 1), Blocks.log, this.woodMetadata);
                }
            }
        }
        return true;
    }
    
    private void func_150541_c(final World worldIn, final int p_150541_2_, final int p_150541_3_, final int p_150541_4_, final int p_150541_5_, final Random p_150541_6_) {
        final int var7 = p_150541_6_.nextInt(5) + (this.field_150542_e ? this.baseHeight : 3);
        int var8 = 0;
        for (int var9 = p_150541_4_ - var7; var9 <= p_150541_4_; ++var9) {
            final int var10 = p_150541_4_ - var9;
            final int var11 = p_150541_5_ + MathHelper.floor_float(var10 / var7 * 3.5f);
            this.func_175925_a(worldIn, new BlockPos(p_150541_2_, var9, p_150541_3_), var11 + ((var10 > 0 && var11 == var8 && (var9 & 0x1) == 0x0) ? 1 : 0));
            var8 = var11;
        }
    }
    
    @Override
    public void func_180711_a(final World worldIn, final Random p_180711_2_, final BlockPos p_180711_3_) {
        this.func_175933_b(worldIn, p_180711_3_.offsetWest().offsetNorth());
        this.func_175933_b(worldIn, p_180711_3_.offsetEast(2).offsetNorth());
        this.func_175933_b(worldIn, p_180711_3_.offsetWest().offsetSouth(2));
        this.func_175933_b(worldIn, p_180711_3_.offsetEast(2).offsetSouth(2));
        for (int var4 = 0; var4 < 5; ++var4) {
            final int var5 = p_180711_2_.nextInt(64);
            final int var6 = var5 % 8;
            final int var7 = var5 / 8;
            if (var6 == 0 || var6 == 7 || var7 == 0 || var7 == 7) {
                this.func_175933_b(worldIn, p_180711_3_.add(-3 + var6, 0, -3 + var7));
            }
        }
    }
    
    private void func_175933_b(final World worldIn, final BlockPos p_175933_2_) {
        for (int var3 = -2; var3 <= 2; ++var3) {
            for (int var4 = -2; var4 <= 2; ++var4) {
                if (Math.abs(var3) != 2 || Math.abs(var4) != 2) {
                    this.func_175934_c(worldIn, p_175933_2_.add(var3, 0, var4));
                }
            }
        }
    }
    
    private void func_175934_c(final World worldIn, final BlockPos p_175934_2_) {
        for (int var3 = 2; var3 >= -3; --var3) {
            final BlockPos var4 = p_175934_2_.offsetUp(var3);
            final Block var5 = worldIn.getBlockState(var4).getBlock();
            if (var5 == Blocks.grass || var5 == Blocks.dirt) {
                this.func_175905_a(worldIn, var4, Blocks.dirt, BlockDirt.DirtType.PODZOL.getMetadata());
                break;
            }
            if (var5.getMaterial() != Material.air && var3 < 0) {
                break;
            }
        }
    }
}
