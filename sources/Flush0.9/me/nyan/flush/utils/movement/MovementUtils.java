package me.nyan.flush.utils.movement;

import me.nyan.flush.event.impl.EventMove;
import me.nyan.flush.utils.other.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;

public class MovementUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static float getDirection(EntityLivingBase e) {
        return getDirectionCustom(e.rotationYaw, e.moveForward, e.moveStrafing);
    }

    public static float getDirectionCustom(float yaw, float forward, float strafe) {
        if (forward < 0) {
            yaw += 180;
        }

        float v = strafe < 0 ? -1 : strafe > 0 ? 1 : 0;
        if (forward > 0) {
            yaw -= 45 * v;
        } else if (forward < 0) {
            yaw += 45 * v;
        } else {
            yaw -= 90 * v;
        }
        return MathUtils.toRadians(yaw);
    }

    public static float getDirection() {
        return getDirection(mc.thePlayer);
    }

    public static void setSpeed(Entity e, double speed, float yaw, float forward, float strafe) {
        e.motionX = -MathHelper.sin(getDirectionCustom(yaw, forward, strafe)) * speed;
        e.motionZ = MathHelper.cos(getDirectionCustom(yaw, forward, strafe)) * speed;
    }

    public static void setSpeed(double speed, float yaw, float forward, float strafe) {
        setSpeed(mc.thePlayer, speed, yaw, forward, strafe);
    }

    public static void setSpeed(Entity e, double speed) {
        e.motionX = -MathHelper.sin(getDirection()) * speed;
        e.motionZ = MathHelper.cos(getDirection()) * speed;
    }

    public static void setSpeed(double speed) {
        setSpeed(mc.thePlayer, speed);
    }

    public static void setSpeed(EventMove event, double speed, float yaw, float forward, float strafe) {
        event.setX(-MathHelper.sin(getDirectionCustom(yaw, forward, strafe)) * speed);
        event.setZ(MathHelper.cos(getDirectionCustom(yaw, forward, strafe)) * speed);
    }

    public static void setSpeed(EventMove event, double speed) {
        event.setX(-MathHelper.sin(getDirection()) * speed);
        event.setZ(MathHelper.cos(getDirection()) * speed);
    }

    public static void stopMotion(EntityLivingBase e) {
        e.motionX = 0;
        e.motionZ = 0;
    }

    public static void stopMotion() {
        stopMotion(mc.thePlayer);
    }

    public static void stopMovement(EntityLivingBase e) {
        e.motionX = 0;
        e.motionY = 0;
        e.motionZ = 0;
        e.jumpMovementFactor = 0;
    }

    public static void stopMovement() {
        stopMovement(mc.thePlayer);
    }

    public static double getSpeed() {
        return getSpeed(mc.thePlayer);
    }

    public static double getSpeed(EntityLivingBase e) {
        return Math.sqrt(square(e.motionX) + square(e.motionZ));
    }

    public static double getBPS() {
        return getBPS(mc.thePlayer);
    }

    public static double getBPS(EntityLivingBase e) {
        return e.ticksExisted <= 1 ? 0 :
                Math.sqrt(square(e.posX - e.lastTickPosX) + square(e.posZ - e.lastTickPosZ))
                        * 20 * (e == mc.thePlayer ? mc.timer.timerSpeed : 1);
    }

    public static double square(double d) {
        return d * d;
    }

    public static boolean isMoving() {
        return mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0;
    }

    public static void vClip(double offset) {
        mc.thePlayer.setPosition(
                mc.thePlayer.posX,
                mc.thePlayer.posY + offset,
                mc.thePlayer.posZ
        );
    }

    public static void hClip(double offset) {
        float direction = getDirection();
        mc.thePlayer.setPosition(
                mc.thePlayer.posX - MathHelper.sin(direction) * offset,
                mc.thePlayer.posY,
                mc.thePlayer.posZ + MathHelper.cos(direction) * offset
        );
    }

    public static void packethClip(double offset, boolean ground) {
        float direction = getDirection();
        mc.thePlayer.sendQueue.addToSendQueue(
                new C03PacketPlayer.C04PacketPlayerPosition(
                        mc.thePlayer.posX - MathHelper.sin(direction) * offset,
                        mc.thePlayer.posY,
                        mc.thePlayer.posZ + MathHelper.cos(direction) * offset,
                        ground
                )
        );
    }

    public static void packetvClip(double offset, boolean ground) {
        mc.thePlayer.sendQueue.addToSendQueue(
                new C03PacketPlayer.C04PacketPlayerPosition(
                        mc.thePlayer.posX,
                        mc.thePlayer.posY + offset,
                        mc.thePlayer.posZ,
                        ground
                )
        );
    }

    public static void packethClip(double offset) {
        packethClip(offset, false);
    }

    public static void packetvClip(double offset) {
        packetvClip(offset, false);
    }

    public static double getBaseMoveSpeed() {
        double baseMoveSpeed = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseMoveSpeed *= 1 + 0.2 * mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
        }
        return baseMoveSpeed;
    }

    public static boolean isOnGround(double height) {
        return !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
                mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty();
    }

    public static KeyBinding[] getMoveKeys() {
        return new KeyBinding[]{
                mc.gameSettings.keyBindForward,
                mc.gameSettings.keyBindBack,
                mc.gameSettings.keyBindLeft,
                mc.gameSettings.keyBindRight,
                mc.gameSettings.keyBindJump,
                mc.gameSettings.keyBindSprint
        };
    }

    public static double getLastDistance() {
        double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        return Math.sqrt(xDist * xDist + zDist * zDist);
    }
}