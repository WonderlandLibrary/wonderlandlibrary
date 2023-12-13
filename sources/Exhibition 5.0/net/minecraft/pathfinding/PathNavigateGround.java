// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.pathfinding;

import java.util.Iterator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.pathfinder.NodeProcessor;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.pathfinder.WalkNodeProcessor;

public class PathNavigateGround extends PathNavigate
{
    protected WalkNodeProcessor field_179695_a;
    private boolean field_179694_f;
    private static final String __OBFID = "CL_00002246";
    
    public PathNavigateGround(final EntityLiving p_i45875_1_, final World worldIn) {
        super(p_i45875_1_, worldIn);
    }
    
    @Override
    protected PathFinder func_179679_a() {
        (this.field_179695_a = new WalkNodeProcessor()).func_176175_a(true);
        return new PathFinder(this.field_179695_a);
    }
    
    @Override
    protected boolean canNavigate() {
        return this.theEntity.onGround || (this.func_179684_h() && this.isInLiquid()) || (this.theEntity.isRiding() && this.theEntity instanceof EntityZombie && this.theEntity.ridingEntity instanceof EntityChicken);
    }
    
    @Override
    protected Vec3 getEntityPosition() {
        return new Vec3(this.theEntity.posX, this.func_179687_p(), this.theEntity.posZ);
    }
    
    private int func_179687_p() {
        if (this.theEntity.isInWater() && this.func_179684_h()) {
            int var1 = (int)this.theEntity.getEntityBoundingBox().minY;
            Block var2 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.theEntity.posX), var1, MathHelper.floor_double(this.theEntity.posZ))).getBlock();
            int var3 = 0;
            while (var2 == Blocks.flowing_water || var2 == Blocks.water) {
                ++var1;
                var2 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.theEntity.posX), var1, MathHelper.floor_double(this.theEntity.posZ))).getBlock();
                if (++var3 > 16) {
                    return (int)this.theEntity.getEntityBoundingBox().minY;
                }
            }
            return var1;
        }
        return (int)(this.theEntity.getEntityBoundingBox().minY + 0.5);
    }
    
    @Override
    protected void removeSunnyPath() {
        super.removeSunnyPath();
        if (this.field_179694_f) {
            if (this.worldObj.isAgainstSky(new BlockPos(MathHelper.floor_double(this.theEntity.posX), (int)(this.theEntity.getEntityBoundingBox().minY + 0.5), MathHelper.floor_double(this.theEntity.posZ)))) {
                return;
            }
            for (int var1 = 0; var1 < this.currentPath.getCurrentPathLength(); ++var1) {
                final PathPoint var2 = this.currentPath.getPathPointFromIndex(var1);
                if (this.worldObj.isAgainstSky(new BlockPos(var2.xCoord, var2.yCoord, var2.zCoord))) {
                    this.currentPath.setCurrentPathLength(var1 - 1);
                    return;
                }
            }
        }
    }
    
    @Override
    protected boolean isDirectPathBetweenPoints(final Vec3 p_75493_1_, final Vec3 p_75493_2_, int p_75493_3_, final int p_75493_4_, int p_75493_5_) {
        int var6 = MathHelper.floor_double(p_75493_1_.xCoord);
        int var7 = MathHelper.floor_double(p_75493_1_.zCoord);
        double var8 = p_75493_2_.xCoord - p_75493_1_.xCoord;
        double var9 = p_75493_2_.zCoord - p_75493_1_.zCoord;
        final double var10 = var8 * var8 + var9 * var9;
        if (var10 < 1.0E-8) {
            return false;
        }
        final double var11 = 1.0 / Math.sqrt(var10);
        var8 *= var11;
        var9 *= var11;
        p_75493_3_ += 2;
        p_75493_5_ += 2;
        if (!this.func_179683_a(var6, (int)p_75493_1_.yCoord, var7, p_75493_3_, p_75493_4_, p_75493_5_, p_75493_1_, var8, var9)) {
            return false;
        }
        p_75493_3_ -= 2;
        p_75493_5_ -= 2;
        final double var12 = 1.0 / Math.abs(var8);
        final double var13 = 1.0 / Math.abs(var9);
        double var14 = var6 * 1 - p_75493_1_.xCoord;
        double var15 = var7 * 1 - p_75493_1_.zCoord;
        if (var8 >= 0.0) {
            ++var14;
        }
        if (var9 >= 0.0) {
            ++var15;
        }
        var14 /= var8;
        var15 /= var9;
        final int var16 = (var8 < 0.0) ? -1 : 1;
        final int var17 = (var9 < 0.0) ? -1 : 1;
        final int var18 = MathHelper.floor_double(p_75493_2_.xCoord);
        final int var19 = MathHelper.floor_double(p_75493_2_.zCoord);
        int var20 = var18 - var6;
        int var21 = var19 - var7;
        while (var20 * var16 > 0 || var21 * var17 > 0) {
            if (var14 < var15) {
                var14 += var12;
                var6 += var16;
                var20 = var18 - var6;
            }
            else {
                var15 += var13;
                var7 += var17;
                var21 = var19 - var7;
            }
            if (!this.func_179683_a(var6, (int)p_75493_1_.yCoord, var7, p_75493_3_, p_75493_4_, p_75493_5_, p_75493_1_, var8, var9)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean func_179683_a(final int p_179683_1_, final int p_179683_2_, final int p_179683_3_, final int p_179683_4_, final int p_179683_5_, final int p_179683_6_, final Vec3 p_179683_7_, final double p_179683_8_, final double p_179683_10_) {
        final int var12 = p_179683_1_ - p_179683_4_ / 2;
        final int var13 = p_179683_3_ - p_179683_6_ / 2;
        if (!this.func_179692_b(var12, p_179683_2_, var13, p_179683_4_, p_179683_5_, p_179683_6_, p_179683_7_, p_179683_8_, p_179683_10_)) {
            return false;
        }
        for (int var14 = var12; var14 < var12 + p_179683_4_; ++var14) {
            for (int var15 = var13; var15 < var13 + p_179683_6_; ++var15) {
                final double var16 = var14 + 0.5 - p_179683_7_.xCoord;
                final double var17 = var15 + 0.5 - p_179683_7_.zCoord;
                if (var16 * p_179683_8_ + var17 * p_179683_10_ >= 0.0) {
                    final Block var18 = this.worldObj.getBlockState(new BlockPos(var14, p_179683_2_ - 1, var15)).getBlock();
                    final Material var19 = var18.getMaterial();
                    if (var19 == Material.air) {
                        return false;
                    }
                    if (var19 == Material.water && !this.theEntity.isInWater()) {
                        return false;
                    }
                    if (var19 == Material.lava) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private boolean func_179692_b(final int p_179692_1_, final int p_179692_2_, final int p_179692_3_, final int p_179692_4_, final int p_179692_5_, final int p_179692_6_, final Vec3 p_179692_7_, final double p_179692_8_, final double p_179692_10_) {
        for (final BlockPos var13 : BlockPos.getAllInBox(new BlockPos(p_179692_1_, p_179692_2_, p_179692_3_), new BlockPos(p_179692_1_ + p_179692_4_ - 1, p_179692_2_ + p_179692_5_ - 1, p_179692_3_ + p_179692_6_ - 1))) {
            final double var14 = var13.getX() + 0.5 - p_179692_7_.xCoord;
            final double var15 = var13.getZ() + 0.5 - p_179692_7_.zCoord;
            if (var14 * p_179692_8_ + var15 * p_179692_10_ >= 0.0) {
                final Block var16 = this.worldObj.getBlockState(var13).getBlock();
                if (!var16.isPassable(this.worldObj, var13)) {
                    return false;
                }
                continue;
            }
        }
        return true;
    }
    
    public void func_179690_a(final boolean p_179690_1_) {
        this.field_179695_a.func_176176_c(p_179690_1_);
    }
    
    public boolean func_179689_e() {
        return this.field_179695_a.func_176173_e();
    }
    
    public void func_179688_b(final boolean p_179688_1_) {
        this.field_179695_a.func_176172_b(p_179688_1_);
    }
    
    public void func_179691_c(final boolean p_179691_1_) {
        this.field_179695_a.func_176175_a(p_179691_1_);
    }
    
    public boolean func_179686_g() {
        return this.field_179695_a.func_176179_b();
    }
    
    public void func_179693_d(final boolean p_179693_1_) {
        this.field_179695_a.func_176178_d(p_179693_1_);
    }
    
    public boolean func_179684_h() {
        return this.field_179695_a.func_176174_d();
    }
    
    public void func_179685_e(final boolean p_179685_1_) {
        this.field_179694_f = p_179685_1_;
    }
}
