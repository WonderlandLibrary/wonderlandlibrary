package io.github.nevalackin.client.util.movement;

import io.github.nevalackin.client.event.player.MoveEvent;
import io.github.nevalackin.client.module.combat.rage.TargetStrafe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public final class MovementUtil {

    public static final double SPRINTING_MOD = 1.0 / 1.3F;
    public static final double SNEAK_MOD = 0.3F;
    public static final double ICE_MOD = 2.5F;
    public static final double WALK_SPEED = 0.221;
    private static final double SWIM_MOD = 0.115F / WALK_SPEED;
    private static final double[] DEPTH_STRIDER_VALUES = {
        1.0F,
        0.1645F / SWIM_MOD / WALK_SPEED,
        0.1995F / SWIM_MOD / WALK_SPEED,
        1.0F / SWIM_MOD,
    };
    public static final double MIN_DIST = 1.0E-3;

    private MovementUtil() {
    }

    public static double getMotion(final EntityPlayerSP player) {
        return Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);
    }

    public static void setMotion(final EntityPlayerSP player,
                                 final float forward,
                                 final float strafe,
                                 float yaw,
                                 final double speed) {
        if (forward == 0.0F && strafe == 0.0F) return;

        yaw = getMovementDirection(forward, strafe, yaw);

        final double movementDirectionRads = Math.toRadians(yaw);
        player.motionX = -Math.sin(movementDirectionRads) * speed;
        player.motionZ = Math.cos(movementDirectionRads) * speed;
    }

    public static void setMotion(final EntityPlayerSP player,
                                 final TargetStrafe targetStrafe,
                                 final double speed) {
        if (targetStrafe.shouldStrafe()) {
            targetStrafe.setMotion(speed);
            return;
        }

        setMotion(player, player.moveForward, player.moveStrafing, player.rotationYaw, speed);
    }

    public static void setMotion(final EntityPlayerSP player,
                                 final double[] motion,
                                 final double speed) {
        final float forward = player.moveForward;
        final float strafing = player.moveStrafing;

        float yaw = player.rotationYaw;

        if (forward == 0.0F && strafing == 0.0F) return;

        yaw = getMovementDirection(forward, strafing, yaw);

        final double movementDirectionRads = Math.toRadians(yaw);
        motion[0] = -Math.sin(movementDirectionRads) * speed;
        motion[2] = Math.cos(movementDirectionRads) * speed;
    }

    public static boolean isOverVoid(final Minecraft mc) {
        final AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox();
        final double height = bb.maxY - bb.minY;

        double offset = height;

        AxisAlignedBB bbPos;

        while (!mc.theWorld.checkBlockCollision((bbPos = bb.addCoord(0, -offset, 0)))) {
            if (bbPos.minY <= 0.0) return true;

            offset += height;
        }

        return false;
    }

    public static boolean canSprint(final EntityPlayerSP player, final boolean omni) {
        return (player.movementInput.moveForward >= 0.8F || (omni && isMoving(player))) &&
            (player.getFoodStats().getFoodLevel() > 6.0F || player.capabilities.allowFlying) &&
            !player.isPotionActive(Potion.blindness) &&
            !player.isCollidedHorizontally;
    }

    public static boolean canSprint(final EntityPlayerSP player) {
        return canSprint(player, true);
    }

    public static void setSpeed(final EntityPlayerSP player,
                                final MoveEvent event,
                                final TargetStrafe targetStrafe,
                                double speed) {
        if (targetStrafe.shouldStrafe()) {
            if (targetStrafe.shouldAdaptSpeed())
                speed = Math.min(speed, targetStrafe.getAdaptedSpeed());
            targetStrafe.setSpeed(event, speed);
            return;
        }

        setSpeed(event, speed, player.moveForward, player.moveStrafing, player.rotationYaw);
    }

    public static float getMovementDirection(final float forward,
                                             final float strafing,
                                             float yaw) {
        if (forward == 0.0F && strafing == 0.0F) return yaw;

        boolean reversed = forward < 0.0f;
        float strafingYaw = 90.0f *
            (forward > 0.0f ? 0.5f : reversed ? -0.5f : 1.0f);

        if (reversed)
            yaw += 180.0f;
        if (strafing > 0.0f)
            yaw -= strafingYaw;
        else if (strafing < 0.0f)
            yaw += strafingYaw;

        return yaw;
    }

    public static void setSpeed(final MoveEvent moveEvent,
                                final double speed,
                                float forward,
                                float strafing,
                                float yaw) {
        if (forward == 0.0F && strafing == 0.0F) return;

        yaw = getMovementDirection(forward, strafing, yaw);

        final double movementDirectionRads = Math.toRadians(yaw);
        final double x = -Math.sin(movementDirectionRads) * speed;
        final double z = Math.cos(movementDirectionRads) * speed;

        moveEvent.setX(x);
        moveEvent.setZ(z);
    }

    public static boolean isOnGround(final World world,
                                     final EntityPlayerSP player,
                                     final double offset) {
        return world.checkBlockCollision(player.getEntityBoundingBox().addCoord(0, -offset, 0));
    }

    public static double getBaseMoveSpeed(final EntityPlayerSP player) {
        double base = player.isSneaking() ? WALK_SPEED * SNEAK_MOD : canSprint(player) ? WALK_SPEED / SPRINTING_MOD : WALK_SPEED;

        final PotionEffect speed = player.getActivePotionEffect(Potion.moveSpeed);
        final int moveSpeedAmp = speed == null ? 0 : speed.getAmplifier() + 1;

        if (moveSpeedAmp > 0)
            base *= 1.0 + 0.2 * moveSpeedAmp;

        if (player.isInWater()) {
            base *= SWIM_MOD;
            final int depthStriderLevel = EnchantmentHelper.getDepthStriderModifier(player);
            if (depthStriderLevel > 0) {
                base *= DEPTH_STRIDER_VALUES[depthStriderLevel];
            }

            return base * SWIM_MOD;
        } else if (player.isInLava()) {
            return base * SWIM_MOD;
        } else {
            return base;
        }
    }

    public static boolean isMoving(final EntityPlayerSP player) {
        return player.moveForward != 0.0F || player.moveStrafing != 0.0F;
    }
}
