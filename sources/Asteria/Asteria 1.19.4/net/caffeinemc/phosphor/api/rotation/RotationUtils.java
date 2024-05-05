package net.caffeinemc.phosphor.api.rotation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;

import static net.caffeinemc.phosphor.common.Phosphor.mc;

public class RotationUtils {
    public static class Rotation {
        private double yaw, pitch;
        private Runnable callback;

        public Rotation(double yaw, double pitch) {
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public Rotation(double yaw, double pitch, Runnable callback) {
            this.yaw = yaw;
            this.pitch = pitch;
            this.callback = callback;
        }

        public double yaw() {
            return yaw;
        }

        public double pitch() {
            return pitch;
        }

        public void sendPacket() {
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround((float) yaw(), (float) pitch, mc.player.isOnGround()));
        }

        public void runCallback() {
            if (callback != null)
                callback.run();
        }
    }

    public static Rotation getDirection(Entity entity, Vec3d vec) {
        double dx = vec.x - entity.getX(),
                dy = vec.y - entity.getY(),
                dz = vec.z - entity.getZ(),
                dist = MathHelper.sqrt((float) (dx * dx + dz * dz));

        return new Rotation(MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(dz, dx)) - 90.0), -MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(dy, dist))));
    }

    public static double getAngleToRotation(Rotation rotation) {
        double currentYaw = MathHelper.wrapDegrees(mc.player.getYaw());
        double currentPitch = MathHelper.wrapDegrees(mc.player.getPitch());

        double diffYaw = MathHelper.wrapDegrees(currentYaw - rotation.yaw());
        double diffPitch = MathHelper.wrapDegrees(currentPitch - rotation.pitch());

        return Math.sqrt(diffYaw * diffYaw + diffPitch * diffPitch);
    }

    public static Vec3d getPlayerLookVec(float yaw, float pitch) {
        float f = pitch * 0.017453292F;
        float g = -yaw * 0.017453292F;

        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);

        return new Vec3d((i * j), (-k), (h * j));
    }

    public static Vec3d getPlayerLookVec(PlayerEntity player) {
        return getPlayerLookVec(player.getYaw(), player.getPitch());
    }

    public static HitResult getHitResult(PlayerEntity entity, boolean ignoreInvisibles, float yaw, float pitch) {
        HitResult result = null;

        if (entity != null) {
            if (mc.world != null) {
                double d = mc.interactionManager.getReachDistance();

                Vec3d cameraPosVec = entity.getCameraPosVec(mc.getTickDelta());
                Vec3d rotationVec = getPlayerLookVec(yaw, pitch);
                Vec3d range = cameraPosVec.add(rotationVec.x * d, rotationVec.y * d, rotationVec.z * d);
                result = mc.world.raycast(new RaycastContext(cameraPosVec, range, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));

                boolean bl = false;
                double e = d;
                if (mc.interactionManager.hasExtendedReach()) {
                    e = 6.0;
                    d = e;
                } else {
                    if (d > 3.0) {
                        bl = true;
                    }

                    d = d;
                }

                e *= e;
                if (result != null) {
                    e = result.getPos().squaredDistanceTo(cameraPosVec);
                }

                Vec3d vec3d3 = cameraPosVec.add(rotationVec.x * d, rotationVec.y * d, rotationVec.z * d);
                float f = 1.0F;
                Box box = entity.getBoundingBox().stretch(rotationVec.multiply(d)).expand(1.0, 1.0, 1.0);
                EntityHitResult entityHitResult = ProjectileUtil.raycast(entity, cameraPosVec, vec3d3, box, (entityx) -> !entityx.isSpectator() && entityx.canHit() && (entityx.isInvisible() && !ignoreInvisibles), e);
                if (entityHitResult != null) {
                    Vec3d vec3d4 = entityHitResult.getPos();
                    double g = cameraPosVec.squaredDistanceTo(vec3d4);
                    if (bl && g > 9.0) {
                        result = BlockHitResult.createMissed(vec3d4, Direction.getFacing(rotationVec.x, rotationVec.y, rotationVec.z), BlockPos.ofFloored(vec3d4));
                    } else if (g < e || result == null) {
                        result = entityHitResult;
                    }
                }
            }
        }

        return result;
    }

    public static HitResult getHitResult(PlayerEntity player, boolean ignoreInvisibles) {
        return getHitResult(player, ignoreInvisibles, player.getYaw(), player.getPitch());
    }

    public static Rotation getSmoothRotation(Rotation from, Rotation to, double speed) {
        return new Rotation(
                MathHelper.lerpAngleDegrees((float) speed, (float) from.yaw(), (float) to.yaw()),
                MathHelper.lerpAngleDegrees((float) speed, (float) from.pitch(), (float) to.pitch())
        );
    }

    public static Rotation getDiff(Rotation rotation1, Rotation rotation2) {
        double yaw = Math.abs(Math.max(rotation1.yaw(), rotation2.yaw()) - Math.min(rotation1.yaw(), rotation2.yaw()));
        double pitch = Math.abs(Math.max(rotation1.pitch(), rotation2.pitch()) - Math.min(rotation1.pitch(), rotation2.pitch()));

        return new Rotation(yaw, pitch);
    }

    public static double getTotalDiff(Rotation rotation1, Rotation rotation2) {
        Rotation diff = getDiff(rotation1, rotation2);

        return diff.yaw() + diff.pitch();
    }
}
