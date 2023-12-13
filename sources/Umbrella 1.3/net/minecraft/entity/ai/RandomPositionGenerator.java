/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import java.util.Random;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RandomPositionGenerator {
    private static Vec3 staticVector = new Vec3(0.0, 0.0, 0.0);
    private static final String __OBFID = "CL_00001629";

    public static Vec3 findRandomTarget(EntityCreature p_75463_0_, int p_75463_1_, int p_75463_2_) {
        return RandomPositionGenerator.findRandomTargetBlock(p_75463_0_, p_75463_1_, p_75463_2_, null);
    }

    public static Vec3 findRandomTargetBlockTowards(EntityCreature p_75464_0_, int p_75464_1_, int p_75464_2_, Vec3 p_75464_3_) {
        staticVector = p_75464_3_.subtract(p_75464_0_.posX, p_75464_0_.posY, p_75464_0_.posZ);
        return RandomPositionGenerator.findRandomTargetBlock(p_75464_0_, p_75464_1_, p_75464_2_, staticVector);
    }

    public static Vec3 findRandomTargetBlockAwayFrom(EntityCreature p_75461_0_, int p_75461_1_, int p_75461_2_, Vec3 p_75461_3_) {
        staticVector = new Vec3(p_75461_0_.posX, p_75461_0_.posY, p_75461_0_.posZ).subtract(p_75461_3_);
        return RandomPositionGenerator.findRandomTargetBlock(p_75461_0_, p_75461_1_, p_75461_2_, staticVector);
    }

    private static Vec3 findRandomTargetBlock(EntityCreature p_75462_0_, int p_75462_1_, int p_75462_2_, Vec3 p_75462_3_) {
        double var13;
        double var11;
        Random var4 = p_75462_0_.getRNG();
        boolean var5 = false;
        int var6 = 0;
        int var7 = 0;
        int var8 = 0;
        float var9 = -99999.0f;
        boolean var10 = p_75462_0_.hasHome() ? (var11 = p_75462_0_.func_180486_cf().distanceSq(MathHelper.floor_double(p_75462_0_.posX), MathHelper.floor_double(p_75462_0_.posY), MathHelper.floor_double(p_75462_0_.posZ)) + 4.0) < (var13 = (double)(p_75462_0_.getMaximumHomeDistance() + (float)p_75462_1_)) * var13 : false;
        for (int var17 = 0; var17 < 10; ++var17) {
            float var16;
            BlockPos var15;
            int var12 = var4.nextInt(2 * p_75462_1_ + 1) - p_75462_1_;
            int var18 = var4.nextInt(2 * p_75462_2_ + 1) - p_75462_2_;
            int var14 = var4.nextInt(2 * p_75462_1_ + 1) - p_75462_1_;
            if (p_75462_3_ != null && !((double)var12 * p_75462_3_.xCoord + (double)var14 * p_75462_3_.zCoord >= 0.0)) continue;
            if (p_75462_0_.hasHome() && p_75462_1_ > 1) {
                var15 = p_75462_0_.func_180486_cf();
                var12 = p_75462_0_.posX > (double)var15.getX() ? (var12 -= var4.nextInt(p_75462_1_ / 2)) : (var12 += var4.nextInt(p_75462_1_ / 2));
                var14 = p_75462_0_.posZ > (double)var15.getZ() ? (var14 -= var4.nextInt(p_75462_1_ / 2)) : (var14 += var4.nextInt(p_75462_1_ / 2));
            }
            var15 = new BlockPos(var12 += MathHelper.floor_double(p_75462_0_.posX), var18 += MathHelper.floor_double(p_75462_0_.posY), var14 += MathHelper.floor_double(p_75462_0_.posZ));
            if (var10 && !p_75462_0_.func_180485_d(var15) || !((var16 = p_75462_0_.func_180484_a(var15)) > var9)) continue;
            var9 = var16;
            var6 = var12;
            var7 = var18;
            var8 = var14;
            var5 = true;
        }
        if (var5) {
            return new Vec3(var6, var7, var8);
        }
        return null;
    }
}

