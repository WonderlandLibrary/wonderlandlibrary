// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.block.material.Material;
import java.util.Random;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.Vec3d;

public class RandomPositionGenerator
{
    private static Vec3d staticVector;
    
    @Nullable
    public static Vec3d findRandomTarget(final EntityCreature entitycreatureIn, final int xz, final int y) {
        return findRandomTargetBlock(entitycreatureIn, xz, y, null);
    }
    
    @Nullable
    public static Vec3d getLandPos(final EntityCreature p_191377_0_, final int p_191377_1_, final int p_191377_2_) {
        return generateRandomPos(p_191377_0_, p_191377_1_, p_191377_2_, null, false);
    }
    
    @Nullable
    public static Vec3d findRandomTargetBlockTowards(final EntityCreature entitycreatureIn, final int xz, final int y, final Vec3d targetVec3) {
        RandomPositionGenerator.staticVector = targetVec3.subtract(entitycreatureIn.posX, entitycreatureIn.posY, entitycreatureIn.posZ);
        return findRandomTargetBlock(entitycreatureIn, xz, y, RandomPositionGenerator.staticVector);
    }
    
    @Nullable
    public static Vec3d findRandomTargetBlockAwayFrom(final EntityCreature entitycreatureIn, final int xz, final int y, final Vec3d targetVec3) {
        RandomPositionGenerator.staticVector = new Vec3d(entitycreatureIn.posX, entitycreatureIn.posY, entitycreatureIn.posZ).subtract(targetVec3);
        return findRandomTargetBlock(entitycreatureIn, xz, y, RandomPositionGenerator.staticVector);
    }
    
    @Nullable
    private static Vec3d findRandomTargetBlock(final EntityCreature entitycreatureIn, final int xz, final int y, @Nullable final Vec3d targetVec3) {
        return generateRandomPos(entitycreatureIn, xz, y, targetVec3, true);
    }
    
    @Nullable
    private static Vec3d generateRandomPos(final EntityCreature p_191379_0_, final int p_191379_1_, final int p_191379_2_, @Nullable final Vec3d p_191379_3_, final boolean p_191379_4_) {
        final PathNavigate pathnavigate = p_191379_0_.getNavigator();
        final Random random = p_191379_0_.getRNG();
        boolean flag;
        if (p_191379_0_.hasHome()) {
            final double d0 = p_191379_0_.getHomePosition().distanceSq(MathHelper.floor(p_191379_0_.posX), MathHelper.floor(p_191379_0_.posY), MathHelper.floor(p_191379_0_.posZ)) + 4.0;
            final double d2 = p_191379_0_.getMaximumHomeDistance() + p_191379_1_;
            flag = (d0 < d2 * d2);
        }
        else {
            flag = false;
        }
        boolean flag2 = false;
        float f = -99999.0f;
        int k1 = 0;
        int i = 0;
        int j = 0;
        for (int l = 0; l < 10; ++l) {
            int m = random.nextInt(2 * p_191379_1_ + 1) - p_191379_1_;
            final int i2 = random.nextInt(2 * p_191379_2_ + 1) - p_191379_2_;
            int j2 = random.nextInt(2 * p_191379_1_ + 1) - p_191379_1_;
            if (p_191379_3_ == null || m * p_191379_3_.x + j2 * p_191379_3_.z >= 0.0) {
                if (p_191379_0_.hasHome() && p_191379_1_ > 1) {
                    final BlockPos blockpos = p_191379_0_.getHomePosition();
                    if (p_191379_0_.posX > blockpos.getX()) {
                        m -= random.nextInt(p_191379_1_ / 2);
                    }
                    else {
                        m += random.nextInt(p_191379_1_ / 2);
                    }
                    if (p_191379_0_.posZ > blockpos.getZ()) {
                        j2 -= random.nextInt(p_191379_1_ / 2);
                    }
                    else {
                        j2 += random.nextInt(p_191379_1_ / 2);
                    }
                }
                BlockPos blockpos2 = new BlockPos(m + p_191379_0_.posX, i2 + p_191379_0_.posY, j2 + p_191379_0_.posZ);
                if ((!flag || p_191379_0_.isWithinHomeDistanceFromPosition(blockpos2)) && pathnavigate.canEntityStandOnPos(blockpos2)) {
                    if (!p_191379_4_) {
                        blockpos2 = moveAboveSolid(blockpos2, p_191379_0_);
                        if (isWaterDestination(blockpos2, p_191379_0_)) {
                            continue;
                        }
                    }
                    final float f2 = p_191379_0_.getBlockPathWeight(blockpos2);
                    if (f2 > f) {
                        f = f2;
                        k1 = m;
                        i = i2;
                        j = j2;
                        flag2 = true;
                    }
                }
            }
        }
        if (flag2) {
            return new Vec3d(k1 + p_191379_0_.posX, i + p_191379_0_.posY, j + p_191379_0_.posZ);
        }
        return null;
    }
    
    private static BlockPos moveAboveSolid(final BlockPos p_191378_0_, final EntityCreature p_191378_1_) {
        if (!p_191378_1_.world.getBlockState(p_191378_0_).getMaterial().isSolid()) {
            return p_191378_0_;
        }
        BlockPos blockpos;
        for (blockpos = p_191378_0_.up(); blockpos.getY() < p_191378_1_.world.getHeight() && p_191378_1_.world.getBlockState(blockpos).getMaterial().isSolid(); blockpos = blockpos.up()) {}
        return blockpos;
    }
    
    private static boolean isWaterDestination(final BlockPos p_191380_0_, final EntityCreature p_191380_1_) {
        return p_191380_1_.world.getBlockState(p_191380_0_).getMaterial() == Material.WATER;
    }
    
    static {
        RandomPositionGenerator.staticVector = Vec3d.ZERO;
    }
}
