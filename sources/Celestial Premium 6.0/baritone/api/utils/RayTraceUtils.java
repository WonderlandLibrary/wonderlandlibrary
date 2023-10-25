/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.utils;

import baritone.api.utils.IPlayerContext;
import baritone.api.utils.Rotation;
import baritone.api.utils.RotationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public final class RayTraceUtils {
    private RayTraceUtils() {
    }

    public static RayTraceResult rayTraceTowards(Entity entity, Rotation rotation, double blockReachDistance) {
        return RayTraceUtils.rayTraceTowards(entity, rotation, blockReachDistance, false);
    }

    public static RayTraceResult rayTraceTowards(Entity entity, Rotation rotation, double blockReachDistance, boolean wouldSneak) {
        Vec3d start = wouldSneak ? RayTraceUtils.inferSneakingEyePosition(entity) : entity.getPositionEyes(1.0f);
        Vec3d direction = RotationUtils.calcVec3dFromRotation(rotation);
        Vec3d end = start.add(direction.x * blockReachDistance, direction.y * blockReachDistance, direction.z * blockReachDistance);
        return entity.world.rayTraceBlocks(start, end, false, false, true);
    }

    public static Vec3d inferSneakingEyePosition(Entity entity) {
        return new Vec3d(entity.posX, entity.posY + IPlayerContext.eyeHeight(true), entity.posZ);
    }
}

