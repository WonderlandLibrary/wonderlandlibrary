package com.initial.utils.movement;

import net.minecraft.client.*;
import net.minecraft.client.entity.*;
import net.minecraft.stats.*;
import net.minecraft.entity.player.*;
import com.initial.events.impl.*;
import net.minecraft.entity.*;
import net.minecraft.potion.*;
import com.initial.modules.*;
import com.initial.modules.impl.combat.*;
import org.lwjgl.input.*;
import net.minecraft.block.*;
import net.minecraft.util.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import com.initial.utils.network.*;
import java.util.*;

public class MovementUtils
{
    public static Minecraft mc;
    
    public static void multMotionBy(final double motion) {
        final EntityPlayerSP thePlayer = MovementUtils.mc.thePlayer;
        thePlayer.motionZ *= motion;
        final EntityPlayerSP thePlayer2 = MovementUtils.mc.thePlayer;
        thePlayer2.motionX *= motion;
    }
    
    public static boolean isOnGround() {
        return MovementUtils.mc.thePlayer.onGround && MovementUtils.mc.thePlayer.isCollidedVertically;
    }
    
    public static void setX(final double x) {
        setPos(x, MovementUtils.mc.thePlayer.posY, MovementUtils.mc.thePlayer.posZ);
    }
    
    public static void setZ(final double z) {
        setPos(MovementUtils.mc.thePlayer.posX, 0.0, MovementUtils.mc.thePlayer.posZ);
    }
    
    public static void setY(final double y) {
        MovementUtils.mc.thePlayer.setPosition(MovementUtils.mc.thePlayer.posX, y, MovementUtils.mc.thePlayer.posZ);
    }
    
    public static double getBlocksPerSecond() {
        if (MovementUtils.mc.thePlayer == null || MovementUtils.mc.thePlayer.ticksExisted < 1) {
            return 0.0;
        }
        return MovementUtils.mc.thePlayer.getDistance(MovementUtils.mc.thePlayer.lastTickPosX, MovementUtils.mc.thePlayer.lastTickPosY, MovementUtils.mc.thePlayer.lastTickPosZ) * (MovementUtils.mc.timer.ticksPerSecond * MovementUtils.mc.timer.timerSpeed);
    }
    
    public static void actualSetSpeed(final double moveSpeed) {
        setSpeed(moveSpeed, MovementUtils.mc.thePlayer.rotationYaw, MovementUtils.mc.thePlayer.movementInput.moveStrafe, MovementUtils.mc.thePlayer.movementInput.moveForward);
    }
    
    public static void jumpAndMotion(final EventMove e, final double y) {
        e.setY(MovementUtils.mc.thePlayer.motionY = y);
        MovementUtils.mc.thePlayer.triggerAchievement(StatList.jumpStat);
        if (MovementUtils.mc.thePlayer.isSprinting()) {
            final float f = MovementUtils.mc.thePlayer.rotationYaw * 0.017453292f;
            e.setX(e.getX() - MathHelper.sin(f) * 0.2f);
            e.setZ(e.getZ() + MathHelper.cos(f) * 0.2f);
        }
        MovementUtils.mc.thePlayer.isAirBorne = true;
    }
    
    public static final void doStrafe(final double speed) {
        if (!MovementUtils.mc.thePlayer.isMovingOnGround()) {
            return;
        }
        final double yaw = getYaw(true);
        MovementUtils.mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        MovementUtils.mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }
    
    public static final double getYaw(final boolean strafing) {
        float rotationYaw = strafing ? MovementUtils.mc.thePlayer.rotationYawHead : MovementUtils.mc.thePlayer.rotationYaw;
        float forward = 1.0f;
        final double moveForward = MovementUtils.mc.thePlayer.movementInput.moveForward;
        final double moveStrafing = MovementUtils.mc.thePlayer.movementInput.moveStrafe;
        final float yaw = MovementUtils.mc.thePlayer.rotationYaw;
        if (moveForward < 0.0) {
            rotationYaw += 180.0f;
        }
        if (moveForward < 0.0) {
            forward = -0.5f;
        }
        else if (moveForward > 0.0) {
            forward = 0.5f;
        }
        if (moveStrafing > 0.0) {
            rotationYaw -= 90.0f * forward;
        }
        else if (moveStrafing < 0.0) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }
    
    public static void forward(final double length) {
        final double yaw = Math.toRadians(MovementUtils.mc.thePlayer.rotationYaw);
        MovementUtils.mc.thePlayer.setPosition(MovementUtils.mc.thePlayer.posX + -Math.sin(yaw) * length, MovementUtils.mc.thePlayer.posY, MovementUtils.mc.thePlayer.posZ + Math.cos(yaw) * length);
    }
    
    public static boolean isOnGround(final double height, final EntityPlayer player) {
        return !MovementUtils.mc.theWorld.getCollidingBoundingBoxes(player, player.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }
    
    public static void setNigga(final double moveSpeed, final float pseudoYaw, final double pseudoStrafe, final double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += ((forward > 0.0) ? -45 : 45);
            }
            else if (strafe < 0.0) {
                yaw += ((forward > 0.0) ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            }
            else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        }
        else if (strafe < 0.0) {
            strafe = -1.0;
        }
        final double offsetX = Math.cos(Math.toRadians(yaw + 90.0f));
        final double offsetZ = Math.sin(Math.toRadians(yaw + 90.0f));
        MovementUtils.mc.thePlayer.motionX = forward * moveSpeed * offsetX + strafe * moveSpeed * offsetZ;
        MovementUtils.mc.thePlayer.motionZ = forward * moveSpeed * offsetZ - strafe * moveSpeed * offsetX;
    }
    
    public static void setSpeed(final MoveEvent moveEvent, final double moveSpeed) {
        double forward = MovementUtils.mc.thePlayer.movementInput.moveForward;
        double strafe = MovementUtils.mc.thePlayer.movementInput.moveStrafe;
        float yaw = MovementUtils.mc.thePlayer.rotationYaw;
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += ((forward > 0.0) ? -45 : 45);
            }
            else if (strafe < 0.0) {
                yaw += ((forward > 0.0) ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            }
            else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        }
        else if (strafe < 0.0) {
            strafe = -1.0;
        }
        final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        moveEvent.actualSetSpeedX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
        moveEvent.actualSetSpeedZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
    }
    
    public static void setSpeed(final EventMotion moveEvent, final double moveSpeed, final float pseudoYaw, final double pseudoStrafe, final double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if (pseudoForward != 0.0) {
            if (pseudoStrafe > 0.0) {
                yaw = pseudoYaw + ((pseudoForward > 0.0) ? -45 : 45);
            }
            else if (pseudoStrafe < 0.0) {
                yaw = pseudoYaw + ((pseudoForward > 0.0) ? 45 : -45);
            }
            strafe = 0.0;
            if (pseudoForward > 0.0) {
                forward = 1.0;
            }
            else if (pseudoForward < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        }
        else if (strafe < 0.0) {
            strafe = -1.0;
        }
        final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        moveEvent.setX(MovementUtils.mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz);
        moveEvent.setZ(MovementUtils.mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx);
    }
    
    public static void setSpeed(final EventMotionUpdate eventMotionUpdate, final double moveSpeed, final float pseudoYaw, final double pseudoStrafe, final double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if (pseudoForward != 0.0) {
            if (pseudoStrafe > 0.0) {
                yaw = pseudoYaw + ((pseudoForward > 0.0) ? -45 : 45);
            }
            else if (pseudoStrafe < 0.0) {
                yaw = pseudoYaw + ((pseudoForward > 0.0) ? 45 : -45);
            }
            strafe = 0.0;
            if (pseudoForward > 0.0) {
                forward = 1.0;
            }
            else if (pseudoForward < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        }
        else if (strafe < 0.0) {
            strafe = -1.0;
        }
        final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        MovementUtils.mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        MovementUtils.mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }
    
    public static void setSpeed(final EventMotion moveEvent, final double moveSpeed) {
        setSpeed(moveEvent, moveSpeed, MovementUtils.mc.thePlayer.rotationYaw, MovementUtils.mc.thePlayer.movementInput.moveStrafe, MovementUtils.mc.thePlayer.movementInput.moveForward);
    }
    
    public static void strafe(final double speed) {
        final float a = MovementUtils.mc.thePlayer.rotationYaw * 0.017453292f;
        final float l = MovementUtils.mc.thePlayer.rotationYaw * 0.017453292f - 4.712389f;
        final float r = MovementUtils.mc.thePlayer.rotationYaw * 0.017453292f + 4.712389f;
        final float rf = MovementUtils.mc.thePlayer.rotationYaw * 0.017453292f + 0.5969026f;
        final float lf = MovementUtils.mc.thePlayer.rotationYaw * 0.017453292f - 0.5969026f;
        final float lb = MovementUtils.mc.thePlayer.rotationYaw * 0.017453292f - 2.3876104f;
        final float rb = MovementUtils.mc.thePlayer.rotationYaw * 0.017453292f + 2.3876104f;
        if (MovementUtils.mc.gameSettings.keyBindForward.pressed) {
            if (MovementUtils.mc.gameSettings.keyBindLeft.pressed && !MovementUtils.mc.gameSettings.keyBindRight.pressed) {
                final EntityPlayerSP thePlayer = MovementUtils.mc.thePlayer;
                thePlayer.motionX -= MathHelper.sin(lf) * speed;
                final EntityPlayerSP thePlayer2 = MovementUtils.mc.thePlayer;
                thePlayer2.motionZ += MathHelper.cos(lf) * speed;
            }
            else if (MovementUtils.mc.gameSettings.keyBindRight.pressed && !MovementUtils.mc.gameSettings.keyBindLeft.pressed) {
                final EntityPlayerSP thePlayer3 = MovementUtils.mc.thePlayer;
                thePlayer3.motionX -= MathHelper.sin(rf) * speed;
                final EntityPlayerSP thePlayer4 = MovementUtils.mc.thePlayer;
                thePlayer4.motionZ += MathHelper.cos(rf) * speed;
            }
            else {
                final EntityPlayerSP thePlayer5 = MovementUtils.mc.thePlayer;
                thePlayer5.motionX -= MathHelper.sin(a) * speed;
                final EntityPlayerSP thePlayer6 = MovementUtils.mc.thePlayer;
                thePlayer6.motionZ += MathHelper.cos(a) * speed;
            }
        }
        else if (MovementUtils.mc.gameSettings.keyBindBack.pressed) {
            if (MovementUtils.mc.gameSettings.keyBindLeft.pressed && !MovementUtils.mc.gameSettings.keyBindRight.pressed) {
                final EntityPlayerSP thePlayer7 = MovementUtils.mc.thePlayer;
                thePlayer7.motionX -= MathHelper.sin(lb) * speed;
                final EntityPlayerSP thePlayer8 = MovementUtils.mc.thePlayer;
                thePlayer8.motionZ += MathHelper.cos(lb) * speed;
            }
            else if (MovementUtils.mc.gameSettings.keyBindRight.pressed && !MovementUtils.mc.gameSettings.keyBindLeft.pressed) {
                final EntityPlayerSP thePlayer9 = MovementUtils.mc.thePlayer;
                thePlayer9.motionX -= MathHelper.sin(rb) * speed;
                final EntityPlayerSP thePlayer10 = MovementUtils.mc.thePlayer;
                thePlayer10.motionZ += MathHelper.cos(rb) * speed;
            }
            else {
                final EntityPlayerSP thePlayer11 = MovementUtils.mc.thePlayer;
                thePlayer11.motionX += MathHelper.sin(a) * speed;
                final EntityPlayerSP thePlayer12 = MovementUtils.mc.thePlayer;
                thePlayer12.motionZ -= MathHelper.cos(a) * speed;
            }
        }
        else if (MovementUtils.mc.gameSettings.keyBindLeft.pressed && !MovementUtils.mc.gameSettings.keyBindRight.pressed && !MovementUtils.mc.gameSettings.keyBindForward.pressed && !MovementUtils.mc.gameSettings.keyBindBack.pressed) {
            final EntityPlayerSP thePlayer13 = MovementUtils.mc.thePlayer;
            thePlayer13.motionX += MathHelper.sin(l) * speed;
            final EntityPlayerSP thePlayer14 = MovementUtils.mc.thePlayer;
            thePlayer14.motionZ -= MathHelper.cos(l) * speed;
        }
        else if (MovementUtils.mc.gameSettings.keyBindRight.pressed && !MovementUtils.mc.gameSettings.keyBindLeft.pressed && !MovementUtils.mc.gameSettings.keyBindForward.pressed && !MovementUtils.mc.gameSettings.keyBindBack.pressed) {
            final EntityPlayerSP thePlayer15 = MovementUtils.mc.thePlayer;
            thePlayer15.motionX += MathHelper.sin(r) * speed;
            final EntityPlayerSP thePlayer16 = MovementUtils.mc.thePlayer;
            thePlayer16.motionZ -= MathHelper.cos(r) * speed;
        }
    }
    
    public static void setMotion(final double speed) {
        double forward = MovementUtils.mc.thePlayer.movementInput.moveForward;
        double strafe = MovementUtils.mc.thePlayer.movementInput.moveStrafe;
        float yaw = MovementUtils.mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            MovementUtils.mc.thePlayer.motionX = 0.0;
            MovementUtils.mc.thePlayer.motionZ = 0.0;
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            MovementUtils.mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            MovementUtils.mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }
    
    public static boolean isOnGround(final double height) {
        return !MovementUtils.mc.theWorld.getCollidingBoundingBoxes(MovementUtils.mc.thePlayer, MovementUtils.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }
    
    public static double getDirection() {
        float rotationYaw = MovementUtils.mc.thePlayer.rotationYaw;
        if (MovementUtils.mc.thePlayer.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (MovementUtils.mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (MovementUtils.mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (MovementUtils.mc.thePlayer.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (MovementUtils.mc.thePlayer.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }
    
    public static boolean isMoving() {
        return MovementUtils.mc.thePlayer != null && (MovementUtils.mc.thePlayer.movementInput.moveForward != 0.0f || MovementUtils.mc.thePlayer.movementInput.moveStrafe != 0.0f);
    }
    
    public static void strafe(final float speed) {
        if (!isMoving()) {
            return;
        }
        final double yaw = getDirection();
        MovementUtils.mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        MovementUtils.mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }
    
    public static void strafe() {
        strafe(getSpeed());
    }
    
    public static final double getSpeed() {
        return (float)Math.sqrt(MovementUtils.mc.thePlayer.motionX * MovementUtils.mc.thePlayer.motionX + MovementUtils.mc.thePlayer.motionZ * MovementUtils.mc.thePlayer.motionZ);
    }
    
    public static float getSpeed(final EntityLivingBase e) {
        return (float)Math.sqrt((e.posX - e.prevPosX) * (e.posX - e.prevPosX) + (e.posZ - e.prevPosZ) * (e.posZ - e.prevPosZ));
    }
    
    public static void setPos(final double x, final double y, final double z) {
        MovementUtils.mc.thePlayer.setPosition(x, y, z);
    }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2875;
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }
    
    public static double getBaseSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    public static void setSpeed(final EventMove e, final float speed) {
        if (!isMoving()) {
            return;
        }
        e.setX(-Math.sin(getDirection()) * speed);
        e.setZ(Math.cos(getDirection()) * speed);
    }
    
    public static void setSpeed(final double speed, final float forward, final float strafing, float yaw) {
        final boolean reversed = forward < 0.0f;
        final float strafingYaw = 90.0f * ((forward > 0.0f) ? 0.5f : (reversed ? -0.5f : 1.0f));
        if (reversed) {
            yaw += 180.0f;
        }
        if (strafing > 0.0f) {
            yaw -= strafingYaw;
        }
        else if (strafing < 0.0f) {
            yaw += strafingYaw;
        }
        final double x = Math.cos(Math.toRadians(yaw + 90.0f));
        final double z = Math.cos(Math.toRadians(yaw));
        MovementUtils.mc.thePlayer.motionX = x * speed;
        MovementUtils.mc.thePlayer.motionZ = z * speed;
    }
    
    public static void setSpeed1(final double speed) {
        final EntityPlayerSP player = MovementUtils.mc.thePlayer;
        if (isMoving()) {
            final TargetStrafe targetStrafe = ModuleManager.getModule(TargetStrafe.class);
            final KillAura killAura = ModuleManager.getModule(KillAura.class);
            if (targetStrafe.isEnabled() && (!targetStrafe.holdspace.isEnabled() || Keyboard.isKeyDown(57))) {
                final Entity target = KillAura.target;
                if (killAura.isEnabled() && target != null) {
                    final float dist = MovementUtils.mc.thePlayer.getDistanceToEntity(target);
                    final double radius = targetStrafe.radius.getValue();
                    setSpeed(speed, (dist <= radius + 1.0E-4) ? 0.0f : 1.0f, (dist <= radius + 1.0) ? ((float)targetStrafe.direction) : 0.0f, RotationUtils.getYawToEntity(target));
                    return;
                }
            }
            setSpeed(speed, player.moveForward, player.moveStrafing, player.rotationYaw);
        }
    }
    
    public static boolean getOnRealGround(final EntityLivingBase entity, final double y) {
        return !MovementUtils.mc.theWorld.getCollidingBoundingBoxes(MovementUtils.mc.thePlayer, entity.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty();
    }
    
    public static boolean isOverVoid() {
        for (int i = (int)(MovementUtils.mc.thePlayer.posY - 1.0); i > 0; --i) {
            final BlockPos pos = new BlockPos(MovementUtils.mc.thePlayer.posX, i, MovementUtils.mc.thePlayer.posZ);
            if (!(MovementUtils.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isColliding(final AxisAlignedBB box) {
        return MovementUtils.mc.theWorld.checkBlockCollision(box);
    }
    
    public static double getGroundLevel() {
        for (int i = (int)Math.round(MovementUtils.mc.thePlayer.posY); i > 0; --i) {
            final AxisAlignedBB box = MovementUtils.mc.thePlayer.boundingBox.addCoord(0.0, 0.0, 0.0);
            box.minY = i - 1;
            box.maxY = i;
            if (isColliding(box) && box.minY <= MovementUtils.mc.thePlayer.posY) {
                return i;
            }
        }
        return 0.0;
    }
    
    public static double fallPacket() {
        double i;
        for (i = MovementUtils.mc.thePlayer.posY; i > getGroundLevel(); i -= 8.0) {
            if (i < getGroundLevel()) {
                i = getGroundLevel();
            }
            MovementUtils.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MovementUtils.mc.thePlayer.posX, i, MovementUtils.mc.thePlayer.posZ, true));
        }
        return i;
    }
    
    public static void ascendPacket() {
        for (double i = getGroundLevel(); i < MovementUtils.mc.thePlayer.posY; i += 8.0) {
            if (i > MovementUtils.mc.thePlayer.posY) {
                i = MovementUtils.mc.thePlayer.posY;
            }
            MovementUtils.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MovementUtils.mc.thePlayer.posX, i, MovementUtils.mc.thePlayer.posZ, true));
        }
    }
    
    public static void damageVerus() {
        PacketUtil.sendPacketSilent(new C0BPacketEntityAction(MovementUtils.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
        int val1 = 0;
        for (int i = 0; i < 6; ++i) {
            val1 += (int)0.5;
            MovementUtils.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MovementUtils.mc.thePlayer.posX, MovementUtils.mc.thePlayer.posY + val1, MovementUtils.mc.thePlayer.posZ, true));
        }
        final double val2 = MovementUtils.mc.thePlayer.posY + val1;
        final ArrayList<Float> vals = new ArrayList<Float>();
        vals.add(0.0784f);
        vals.add(0.0784f);
        vals.add(0.23052737f);
        vals.add(0.30431682f);
        vals.add(0.37663049f);
        vals.add(0.4474979f);
        vals.add(0.5169479f);
        vals.add(0.585009f);
        vals.add(0.65170884f);
        vals.add(0.15372962f);
        MovementUtils.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MovementUtils.mc.thePlayer.posX, val2, MovementUtils.mc.thePlayer.posZ, false));
        MovementUtils.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        PacketUtil.sendPacketSilent(new C0BPacketEntityAction(MovementUtils.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
        MovementUtils.mc.thePlayer.jump();
    }
    
    static {
        MovementUtils.mc = Minecraft.getMinecraft();
    }
}
