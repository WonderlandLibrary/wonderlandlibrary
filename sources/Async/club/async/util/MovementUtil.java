package club.async.util;

import club.async.Async;
import club.async.event.impl.EventMovePlayer;
import club.async.interfaces.MinecraftInterface;
import club.async.module.impl.combat.KillAura;
import club.async.module.impl.combat.TargetStrafe;
import club.async.module.impl.movement.NoSlowDown;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;

public final class MovementUtil implements MinecraftInterface {

    public static boolean isMoving() {
        return mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F;
    }

    public static void setSpeed(EventMovePlayer e, double speed) {
        if(Async.INSTANCE.getModuleManager().isToggled(KillAura.class) && Async.INSTANCE.getModuleManager().isToggled(TargetStrafe.class)) {
            if(Async.INSTANCE.getModuleManager().moduleBy(TargetStrafe.class).canStrafe()) {
                Async.INSTANCE.getModuleManager().moduleBy(TargetStrafe.class).strafe(e, speed);
                return;
            }
        }
        setSpeed(e, speed, mc.thePlayer.moveForward, mc.thePlayer.moveStrafing, mc.thePlayer.rotationYaw);
    }

    public static void setSpeed(EventMovePlayer e, double speed, float forward, float strafing, float yaw) {
        if (forward == 0.0F && strafing == 0.0F) return;
        boolean reversed = forward < 0.0f;
        float strafingYaw = 90.0f * (forward > 0.0f ? 0.5f : reversed ? -0.5f : 1.0f);
        if (reversed) yaw += 180.0f;
        if (strafing > 0.0f) yaw -= strafingYaw;
        else if (strafing < 0.0f) yaw += strafingYaw;
        double x = Math.cos(StrictMath.toRadians(yaw + 90.0f));
        double z = Math.cos(StrictMath.toRadians(yaw));
        e.x(x * speed);
        e.z(z * speed);
    }
    public static void verusDMG(){

        if (mc.thePlayer.onGround && mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0, 4, 0).expand(0, 0, 0)).isEmpty()) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + 4, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
        }
        mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;


    }
    public static boolean fallDistDamage() {
        if (!isOnGround() || isBlockAbove()) {
            return false;
        }
        final EntityPlayerSP player = mc.thePlayer;
        final double randomOffset = Math.random() * 3.000000142492354E-4;
        final double jumpHeight = 0.0525 - randomOffset;
        for (int packets = (int)(getMinFallDist() / (jumpHeight - randomOffset) + 1.0), i = 0; i < packets; ++i) {
            mc.getNetHandler().getNetworkManager().sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(player.posX, player.posY + jumpHeight, player.posZ, false));
            mc.getNetHandler().getNetworkManager().sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(player.posX, player.posY + randomOffset, player.posZ, false));
        }
        mc.getNetHandler().getNetworkManager().sendPacketSilent (new C03PacketPlayer(true));
        return true;
    }
    public static boolean isBlockAbove() {
        for (double height = 0.0; height <= 1.0; height += 0.5) {
            final List <AxisAlignedBB> collidingList = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, height, 0.0));
            if (!collidingList.isEmpty()) {
                return true;
            }
        }
        return false;
    }
    public static void jump() {
        if (mc.thePlayer.onGround && isMoving())
            mc.thePlayer.jump();
    }
    public static boolean isOnGround() {
        return mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically;
    }

    public static float getMinFallDist() {
        float minDist = 3.0f;
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            minDist += mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1.0f;
        }
        return minDist;
    }
    public static double getBaseMoveSpeed() {
        double base = mc.thePlayer.isSneaking() ? 0.06630000288486482 : (canSprint() ? 0.2872999905467033 : 0.22100000083446503);
        final PotionEffect moveSpeed = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed);
        final PotionEffect moveSlowness = mc.thePlayer.getActivePotionEffect(Potion.moveSlowdown);
        if (moveSpeed != null) {
            base *= 1.0 + 0.2 * (moveSpeed.getAmplifier() + 1);
        }
        if (moveSlowness != null) {
            base *= 1.0 + 0.2 * (moveSlowness.getAmplifier() + 1);
        }
        if (mc.thePlayer.isInWater()) {
            base *= 0.5203619984250619;
        }
        if (mc.thePlayer.isInLava()) {
            base *= 0.5203619984250619;
        }
        return base;
    }

    public static boolean canSprint() {
        return mc.thePlayer.movementInput.moveForward >= 0.8 && !mc.thePlayer.isCollidedHorizontally && (mc.thePlayer.getFoodStats().getFoodLevel() > 6) && !mc.thePlayer.isSneaking()
                && (!mc.thePlayer.isUsingItem() || Async.INSTANCE.getModuleManager().isToggled(NoSlowDown.class));
    }

}
