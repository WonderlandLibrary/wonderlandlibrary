/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.utils;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.utils.IPlayerContext;
import baritone.api.utils.RayTraceUtils;
import baritone.api.utils.Rotation;
import baritone.api.utils.VecUtils;
import java.util.Optional;
import net.minecraft.block.BlockFire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public final class RotationUtils {
    public static final double DEG_TO_RAD = Math.PI / 180;
    public static final double RAD_TO_DEG = 57.29577951308232;
    private static final Vec3d[] BLOCK_SIDE_MULTIPLIERS = new Vec3d[]{new Vec3d(0.5, 0.0, 0.5), new Vec3d(0.5, 1.0, 0.5), new Vec3d(0.5, 0.5, 0.0), new Vec3d(0.5, 0.5, 1.0), new Vec3d(0.0, 0.5, 0.5), new Vec3d(1.0, 0.5, 0.5)};

    private RotationUtils() {
    }

    public static Rotation calcRotationFromCoords(BlockPos orig, BlockPos dest) {
        return RotationUtils.calcRotationFromVec3d(new Vec3d(orig), new Vec3d(dest));
    }

    public static Rotation wrapAnglesToRelative(Rotation current, Rotation target) {
        if (current.yawIsReallyClose(target)) {
            return new Rotation(current.getYaw(), target.getPitch());
        }
        return target.subtract(current).normalize().add(current);
    }

    public static Rotation calcRotationFromVec3d(Vec3d orig, Vec3d dest, Rotation current) {
        return RotationUtils.wrapAnglesToRelative(current, RotationUtils.calcRotationFromVec3d(orig, dest));
    }

    private static Rotation calcRotationFromVec3d(Vec3d orig, Vec3d dest) {
        double[] delta = new double[]{orig.x - dest.x, orig.y - dest.y, orig.z - dest.z};
        double yaw = MathHelper.atan2(delta[0], -delta[2]);
        double dist = Math.sqrt(delta[0] * delta[0] + delta[2] * delta[2]);
        double pitch = MathHelper.atan2(delta[1], dist);
        return new Rotation((float)(yaw * 57.29577951308232), (float)(pitch * 57.29577951308232));
    }

    public static Vec3d calcVec3dFromRotation(Rotation rotation) {
        float f = MathHelper.cos(-rotation.getYaw() * ((float)Math.PI / 180) - (float)Math.PI);
        float f1 = MathHelper.sin(-rotation.getYaw() * ((float)Math.PI / 180) - (float)Math.PI);
        float f2 = -MathHelper.cos(-rotation.getPitch() * ((float)Math.PI / 180));
        float f3 = MathHelper.sin(-rotation.getPitch() * ((float)Math.PI / 180));
        return new Vec3d(f1 * f2, f3, f * f2);
    }

    public static Optional<Rotation> reachable(IPlayerContext ctx, BlockPos pos) {
        return RotationUtils.reachable(ctx.player(), pos, ctx.playerController().getBlockReachDistance());
    }

    public static Optional<Rotation> reachable(IPlayerContext ctx, BlockPos pos, boolean wouldSneak) {
        return RotationUtils.reachable(ctx.player(), pos, ctx.playerController().getBlockReachDistance(), wouldSneak);
    }

    public static Optional<Rotation> reachable(EntityPlayerSP entity, BlockPos pos, double blockReachDistance) {
        return RotationUtils.reachable(entity, pos, blockReachDistance, false);
    }

    public static Optional<Rotation> reachable(EntityPlayerSP entity, BlockPos pos, double blockReachDistance, boolean wouldSneak) {
        Optional<Rotation> possibleRotation;
        IBaritone baritone = BaritoneAPI.getProvider().getBaritoneForPlayer(entity);
        if (baritone.getPlayerContext().isLookingAt(pos)) {
            Rotation hypothetical = new Rotation(entity.rotationYaw, entity.rotationPitch + 1.0E-4f);
            if (wouldSneak) {
                RayTraceResult result = RayTraceUtils.rayTraceTowards(entity, hypothetical, blockReachDistance, true);
                if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK && result.getBlockPos().equals(pos)) {
                    return Optional.of(hypothetical);
                }
            } else {
                return Optional.of(hypothetical);
            }
        }
        if ((possibleRotation = RotationUtils.reachableCenter(entity, pos, blockReachDistance, wouldSneak)).isPresent()) {
            return possibleRotation;
        }
        IBlockState state = entity.world.getBlockState(pos);
        AxisAlignedBB aabb = state.getBoundingBox(entity.world, pos);
        for (Vec3d sideOffset : BLOCK_SIDE_MULTIPLIERS) {
            double xDiff = aabb.minX * sideOffset.x + aabb.maxX * (1.0 - sideOffset.x);
            double yDiff = aabb.minY * sideOffset.y + aabb.maxY * (1.0 - sideOffset.y);
            double zDiff = aabb.minZ * sideOffset.z + aabb.maxZ * (1.0 - sideOffset.z);
            possibleRotation = RotationUtils.reachableOffset(entity, pos, new Vec3d(pos).add(xDiff, yDiff, zDiff), blockReachDistance, wouldSneak);
            if (!possibleRotation.isPresent()) continue;
            return possibleRotation;
        }
        return Optional.empty();
    }

    public static Optional<Rotation> reachableOffset(Entity entity, BlockPos pos, Vec3d offsetPos, double blockReachDistance, boolean wouldSneak) {
        Vec3d eyes = wouldSneak ? RayTraceUtils.inferSneakingEyePosition(entity) : entity.getPositionEyes(1.0f);
        Rotation rotation = RotationUtils.calcRotationFromVec3d(eyes, offsetPos, new Rotation(entity.rotationYaw, entity.rotationPitch));
        RayTraceResult result = RayTraceUtils.rayTraceTowards(entity, rotation, blockReachDistance, wouldSneak);
        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            if (result.getBlockPos().equals(pos)) {
                return Optional.of(rotation);
            }
            if (entity.world.getBlockState(pos).getBlock() instanceof BlockFire && result.getBlockPos().equals(pos.down())) {
                return Optional.of(rotation);
            }
        }
        return Optional.empty();
    }

    public static Optional<Rotation> reachableCenter(Entity entity, BlockPos pos, double blockReachDistance, boolean wouldSneak) {
        return RotationUtils.reachableOffset(entity, pos, VecUtils.calculateBlockCenter(entity.world, pos), blockReachDistance, wouldSneak);
    }
}

