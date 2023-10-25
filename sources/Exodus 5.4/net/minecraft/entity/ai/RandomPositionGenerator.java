/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import java.util.Random;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RandomPositionGenerator {
    private static Vec3 staticVector = new Vec3(0.0, 0.0, 0.0);

    public static Vec3 findRandomTargetBlockTowards(EntityCreature entityCreature, int n, int n2, Vec3 vec3) {
        staticVector = vec3.subtract(entityCreature.posX, entityCreature.posY, entityCreature.posZ);
        return RandomPositionGenerator.findRandomTargetBlock(entityCreature, n, n2, staticVector);
    }

    private static Vec3 findRandomTargetBlock(EntityCreature entityCreature, int n, int n2, Vec3 vec3) {
        double d;
        double d2;
        Random random = entityCreature.getRNG();
        boolean bl = false;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        float f = -99999.0f;
        boolean bl2 = entityCreature.hasHome() ? (d2 = entityCreature.getHomePosition().distanceSq(MathHelper.floor_double(entityCreature.posX), MathHelper.floor_double(entityCreature.posY), MathHelper.floor_double(entityCreature.posZ)) + 4.0) < (d = (double)(entityCreature.getMaximumHomeDistance() + (float)n)) * d : false;
        int n6 = 0;
        while (n6 < 10) {
            int n7 = random.nextInt(2 * n + 1) - n;
            int n8 = random.nextInt(2 * n2 + 1) - n2;
            int n9 = random.nextInt(2 * n + 1) - n;
            if (vec3 == null || (double)n7 * vec3.xCoord + (double)n9 * vec3.zCoord >= 0.0) {
                float f2;
                BlockPos blockPos;
                if (entityCreature.hasHome() && n > 1) {
                    blockPos = entityCreature.getHomePosition();
                    n7 = entityCreature.posX > (double)blockPos.getX() ? (n7 -= random.nextInt(n / 2)) : (n7 += random.nextInt(n / 2));
                    n9 = entityCreature.posZ > (double)blockPos.getZ() ? (n9 -= random.nextInt(n / 2)) : (n9 += random.nextInt(n / 2));
                }
                blockPos = new BlockPos(n7 += MathHelper.floor_double(entityCreature.posX), n8 += MathHelper.floor_double(entityCreature.posY), n9 += MathHelper.floor_double(entityCreature.posZ));
                if ((!bl2 || entityCreature.isWithinHomeDistanceFromPosition(blockPos)) && (f2 = entityCreature.getBlockPathWeight(blockPos)) > f) {
                    f = f2;
                    n3 = n7;
                    n4 = n8;
                    n5 = n9;
                    bl = true;
                }
            }
            ++n6;
        }
        if (bl) {
            return new Vec3(n3, n4, n5);
        }
        return null;
    }

    public static Vec3 findRandomTargetBlockAwayFrom(EntityCreature entityCreature, int n, int n2, Vec3 vec3) {
        staticVector = new Vec3(entityCreature.posX, entityCreature.posY, entityCreature.posZ).subtract(vec3);
        return RandomPositionGenerator.findRandomTargetBlock(entityCreature, n, n2, staticVector);
    }

    public static Vec3 findRandomTarget(EntityCreature entityCreature, int n, int n2) {
        return RandomPositionGenerator.findRandomTargetBlock(entityCreature, n, n2, null);
    }
}

