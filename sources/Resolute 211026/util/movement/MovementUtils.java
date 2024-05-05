// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.movement;

import java.util.ArrayList;
import net.minecraft.potion.PotionEffect;
import vip.Resolute.util.player.RotationUtils;
import vip.Resolute.modules.impl.combat.KillAura;
import org.lwjgl.input.Keyboard;
import vip.Resolute.modules.impl.combat.TargetStrafe;
import java.util.Collection;
import java.util.Collections;
import vip.Resolute.events.impl.EventMove;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovementInput;
import java.util.Comparator;
import java.util.Arrays;
import vip.Resolute.events.impl.EventMotion;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.BlockHopper;
import net.minecraft.world.World;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.stats.StatList;
import net.minecraft.potion.Potion;
import java.util.List;
import net.minecraft.client.Minecraft;

public class MovementUtils
{
    protected static Minecraft mc;
    public static final double BUNNY_SLOPE = 0.66;
    public static final double WATCHDOG_BUNNY_SLOPE = 0.6336;
    public static final double SPRINTING_MOD = 1.3;
    public static final double SNEAK_MOD = 0.3;
    public static final double ICE_MOD = 2.5;
    public static final double VANILLA_JUMP_HEIGHT = 0.41999998688697815;
    public static final double WALK_SPEED = 0.221;
    private static final List<Double> frictionValues;
    private static final double MIN_DIF = 0.01;
    public static final double MAX_DIST = 2.14;
    public static final double BUNNY_DIV_FRICTION = 159.99;
    private static final double SWIM_MOD = 0.5203619909502263;
    private static final double[] DEPTH_STRIDER_VALUES;
    private static final double AIR_FRICTION = 0.98;
    private static final double WATER_FRICTION = 0.89;
    private static final double LAVA_FRICTION = 0.535;
    
    public static double getBlockHeight() {
        return MovementUtils.mc.thePlayer.posY - (int)MovementUtils.mc.thePlayer.posY;
    }
    
    public static double getJumpHeight() {
        final double baseJumpHeight = 0.41999998688697815;
        if (isInLiquid()) {
            return 0.13500000163912773;
        }
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.jump)) {
            return 0.41999998688697815 + (MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1.0f) * 0.1f;
        }
        return 0.41999998688697815;
    }
    
    public static void fakeJump() {
        MovementUtils.mc.thePlayer.isAirBorne = true;
        MovementUtils.mc.thePlayer.triggerAchievement(StatList.jumpStat);
    }
    
    public static float getSensitivityMultiplier() {
        final float f = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6f + 0.2f;
        return f * f * f * 8.0f * 0.15f;
    }
    
    public static double getMotion(final EntityPlayerSP player) {
        return Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);
    }
    
    public static boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minY); y < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                    final Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Minecraft.getMinecraft().theWorld, new BlockPos(x, y, z), Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)));
                        if (block instanceof BlockHopper) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }
                        if (boundingBox != null && Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public static double[] yawPos(final double value) {
        final double yaw = Math.toRadians(MovementUtils.mc.thePlayer.rotationYaw);
        return new double[] { -Math.sin(yaw) * value, Math.cos(yaw) * value };
    }
    
    public static float applyCustomFriction(final float speed, final float friction) {
        final float value = speed / 100.0f * friction;
        return value;
    }
    
    public static boolean fallDistDamage() {
        if (!isOnGround() || isBlockAbove()) {
            return false;
        }
        final EntityPlayerSP player = MovementUtils.mc.thePlayer;
        final double randomOffset = Math.random() * 3.000000142492354E-4;
        final double jumpHeight = 0.0525 - randomOffset;
        for (int packets = (int)(getMinFallDist() / (jumpHeight - randomOffset) + 1.0), i = 0; i < packets; ++i) {
            MovementUtils.mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(player.posX, player.posY + jumpHeight, player.posZ, false));
            MovementUtils.mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(player.posX, player.posY + randomOffset, player.posZ, false));
        }
        MovementUtils.mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer(true));
        return true;
    }
    
    public static float getMinFallDist() {
        float minDist = 3.0f;
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.jump)) {
            minDist += MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1.0f;
        }
        return minDist;
    }
    
    public static double getBaseSpeedHypixel() {
        final double baseSpeed = 0.2873;
        return baseSpeed;
    }
    
    public static void bypassOffSet(final EventMotion event) {
        if (isMoving()) {
            final List<Double> BypassOffset = Arrays.asList(0.125, 0.25, 0.375, 0.625, 0.75, 0.015625, 0.5, 0.0625, 0.875, 0.1875);
            final double d3 = event.getY() % 1.0;
            BypassOffset.sort(Comparator.comparingDouble(PreY -> Math.abs(PreY - d3)));
            double acc = event.getY() - d3 + BypassOffset.get(0);
            if (Math.abs(BypassOffset.get(0) - d3) < 0.005) {
                event.setY(acc);
                event.setOnGround(true);
            }
            else {
                final List<Double> BypassOffset2 = Arrays.asList(0.715, 0.945, 0.09, 0.155, 0.14, 0.045, 0.63, 0.31);
                final double d3_ = event.getY() % 1.0;
                BypassOffset2.sort(Comparator.comparingDouble(PreY -> Math.abs(PreY - d3_)));
                acc = event.getY() - d3_ + BypassOffset2.get(0);
                if (Math.abs(BypassOffset2.get(0) - d3_) < 0.005) {
                    event.setY(acc);
                }
            }
        }
    }
    
    public static int getSpeedEffect() {
        return MovementUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed) ? (MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) : 0;
    }
    
    public static double getBaseSpeedHypixelAppliedLow() {
        double baseSpeed = 0.2873;
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 0.3);
        }
        return baseSpeed;
    }
    
    public static double getBaseSpeedHypixelApplied() {
        double baseSpeed = 0.2873;
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1.0);
        }
        return baseSpeed;
    }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2875;
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }
    
    public static double getBaseMoveSpeed(final double basespeed) {
        double baseSpeed = basespeed;
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }
    
    public static boolean canSprint(final boolean omni) {
        if (omni) {
            if (!isMovingEnoughForSprint()) {
                return false;
            }
        }
        else {
            final MovementInput movementInput = MovementUtils.mc.thePlayer.movementInput;
            if (MovementInput.moveForward < 0.8f) {
                return false;
            }
        }
        return !MovementUtils.mc.thePlayer.isCollidedHorizontally && (MovementUtils.mc.thePlayer.getFoodStats().getFoodLevel() > 6 || MovementUtils.mc.thePlayer.capabilities.allowFlying) && !MovementUtils.mc.thePlayer.isSneaking();
    }
    
    private static boolean isMovingEnoughForSprint() {
        final MovementInput movementInput = MovementUtils.mc.thePlayer.movementInput;
        return MovementInput.moveForward > 0.8f || MovementInput.moveForward < -0.8f || MovementInput.moveStrafe > 0.8f || MovementInput.moveStrafe < -0.8f;
    }
    
    public static double getSpeed(final EntityPlayer player) {
        return Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);
    }
    
    public static boolean isInLiquid(final Entity e) {
        for (int x = MathHelper.floor_double(e.boundingBox.minY); x < MathHelper.floor_double(e.boundingBox.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(e.boundingBox.minZ); z < MathHelper.floor_double(e.boundingBox.maxZ) + 1; ++z) {
                final BlockPos pos = new BlockPos(x, (int)e.boundingBox.minY, z);
                final Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
                if (block != null && !(block instanceof BlockAir)) {
                    return block instanceof BlockLiquid;
                }
            }
        }
        return false;
    }
    
    public static double getFriction(final double moveSpeed) {
        final double xDist = MovementUtils.mc.thePlayer.posX - MovementUtils.mc.thePlayer.prevPosX;
        final double zDist = MovementUtils.mc.thePlayer.posZ - MovementUtils.mc.thePlayer.prevPosZ;
        final double lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        return calculateFriction(moveSpeed, lastDist, getBaseMoveSpeed());
    }
    
    public static boolean isDistFromGround(final double dist) {
        return Minecraft.getMinecraft().theWorld.checkBlockCollision(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().addCoord(0.0, -dist, 0.0));
    }
    
    public static Block getBlockUnder() {
        final EntityPlayerSP player = MovementUtils.mc.thePlayer;
        return MovementUtils.mc.theWorld.getBlockState(new BlockPos(player.posX, StrictMath.floor(player.getEntityBoundingBox().minY) - 1.0, player.posZ)).getBlock();
    }
    
    public static boolean isBlockAbove() {
        for (double height = 0.0; height <= 1.0; height += 0.5) {
            final List<AxisAlignedBB> collidingList = MovementUtils.mc.theWorld.getCollidingBoundingBoxes(MovementUtils.mc.thePlayer, MovementUtils.mc.thePlayer.getEntityBoundingBox().offset(0.0, height, 0.0));
            if (!collidingList.isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isOverVoid() {
        for (double posY = MovementUtils.mc.thePlayer.posY; posY > 0.0; --posY) {
            if (!(MovementUtils.mc.theWorld.getBlockState(new BlockPos(MovementUtils.mc.thePlayer.posX, posY, MovementUtils.mc.thePlayer.posZ)).getBlock() instanceof BlockAir)) {
                return false;
            }
        }
        return true;
    }
    
    public static void actualSetSpeed(final double moveSpeed) {
        final float rotationYaw = MovementUtils.mc.thePlayer.rotationYaw;
        final MovementInput movementInput = MovementUtils.mc.thePlayer.movementInput;
        final double strafe = MovementInput.moveStrafe;
        final MovementInput movementInput2 = MovementUtils.mc.thePlayer.movementInput;
        setSpeed(moveSpeed, rotationYaw, strafe, MovementInput.moveForward);
    }
    
    public static float getBaseSpeed() {
        return 0.3f;
    }
    
    public static double getBaseSpeedVerus() {
        double baseSpeed = 0.24;
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amp = MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
            baseSpeed *= 1.0 + 0.05 * amp;
        }
        return baseSpeed;
    }
    
    public static boolean isBlockUnderneath(final BlockPos pos) {
        for (int k = 0; k < pos.getY() + 1; ++k) {
            if (MovementUtils.mc.theWorld.getBlockState(new BlockPos(pos.getX(), k, pos.getZ())).getBlock().getMaterial() != Material.air) {
                return true;
            }
        }
        return false;
    }
    
    public static void move(final float speed) {
        if (!isMoving()) {
            return;
        }
        final double yaw = getDirection();
        final EntityPlayerSP thePlayer = MovementUtils.mc.thePlayer;
        thePlayer.motionX += -Math.sin(yaw) * speed;
        final EntityPlayerSP thePlayer2 = MovementUtils.mc.thePlayer;
        thePlayer2.motionZ += Math.cos(yaw) * speed;
    }
    
    public static void limitSpeed(final float speed) {
        final double yaw = getDirection();
        final double maxXSpeed = -Math.sin(yaw) * speed;
        final double maxZSpeed = Math.cos(yaw) * speed;
        if (MovementUtils.mc.thePlayer.motionX > maxZSpeed) {
            MovementUtils.mc.thePlayer.motionX = maxXSpeed;
        }
        if (MovementUtils.mc.thePlayer.motionZ > maxZSpeed) {
            MovementUtils.mc.thePlayer.motionZ = maxZSpeed;
        }
    }
    
    public static void setMotionWithValues(final EventMove em, final double speed, float yaw, double forward, double strafe) {
        if (forward == 0.0 && strafe == 0.0) {
            if (em != null) {
                em.setX(0.0);
                em.setZ(0.0);
            }
            else {
                MovementUtils.mc.thePlayer.motionX = 0.0;
                MovementUtils.mc.thePlayer.motionZ = 0.0;
            }
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
            if (em != null) {
                em.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
                em.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
            }
            else {
                MovementUtils.mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
                MovementUtils.mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
            }
        }
    }
    
    public static void setSpeed(final EventMotion e, final double speed, final float forward, final float strafing, float yaw) {
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
        e.setX(x * speed);
        e.setZ(z * speed);
    }
    
    public static void setSpeed(final EventMove moveEvent, final double moveSpeed) {
        final float rotationYaw = MovementUtils.mc.thePlayer.rotationYaw;
        final MovementInput movementInput = MovementUtils.mc.thePlayer.movementInput;
        final double pseudoStrafe = MovementInput.moveStrafe;
        final MovementInput movementInput2 = MovementUtils.mc.thePlayer.movementInput;
        setSpeed(moveEvent, moveSpeed, rotationYaw, pseudoStrafe, MovementInput.moveForward);
    }
    
    public static float getMovementDirection() {
        final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        final float forward = player.moveForward;
        final float strafe = player.moveStrafing;
        float direction = 0.0f;
        if (forward < 0.0f) {
            direction += 180.0f;
            if (strafe > 0.0f) {
                direction += 45.0f;
            }
            else if (strafe < 0.0f) {
                direction -= 45.0f;
            }
        }
        else if (forward > 0.0f) {
            if (strafe > 0.0f) {
                direction -= 45.0f;
            }
            else if (strafe < 0.0f) {
                direction += 45.0f;
            }
        }
        else if (strafe > 0.0f) {
            direction -= 90.0f;
        }
        else if (strafe < 0.0f) {
            direction += 90.0f;
        }
        direction += player.rotationYaw;
        return MathHelper.wrapAngleTo180_float(direction);
    }
    
    public static boolean isOnGround() {
        return MovementUtils.mc.thePlayer.onGround && MovementUtils.mc.thePlayer.isCollidedVertically;
    }
    
    public static boolean isMovingOnGround() {
        return isMoving() && MovementUtils.mc.thePlayer.onGround;
    }
    
    public static double getJumpHeight(final double baseJumpHeight) {
        if (isInLiquid()) {
            return 0.13499999955296516;
        }
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.jump)) {
            return baseJumpHeight + (MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1.0f) * 0.1f;
        }
        return baseJumpHeight;
    }
    
    public static double fallPacket() {
        double i;
        for (i = MovementUtils.mc.thePlayer.posY; i > getGroundLevel(); i -= 8.0) {
            if (i < getGroundLevel()) {
                i = getGroundLevel();
            }
            MovementUtils.mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(MovementUtils.mc.thePlayer.posX, i, MovementUtils.mc.thePlayer.posZ, true));
        }
        return i;
    }
    
    public static void ascendPacket() {
        for (double i = getGroundLevel(); i < MovementUtils.mc.thePlayer.posY; i += 8.0) {
            if (i > MovementUtils.mc.thePlayer.posY) {
                i = MovementUtils.mc.thePlayer.posY;
            }
            MovementUtils.mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(MovementUtils.mc.thePlayer.posX, i, MovementUtils.mc.thePlayer.posZ, true));
        }
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
    
    public static boolean isColliding(final AxisAlignedBB box) {
        return MovementUtils.mc.theWorld.checkBlockCollision(box);
    }
    
    public static boolean isInLiquid() {
        return MovementUtils.mc.thePlayer.isInWater() || MovementUtils.mc.thePlayer.isInLava();
    }
    
    public static void sendPosition(final double x, final double y, final double z, final boolean ground, final boolean moving) {
        if (!moving) {
            MovementUtils.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MovementUtils.mc.thePlayer.posX, MovementUtils.mc.thePlayer.posY + y, MovementUtils.mc.thePlayer.posZ, ground));
        }
        else {
            MovementUtils.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MovementUtils.mc.thePlayer.posX + x, MovementUtils.mc.thePlayer.posY + y, MovementUtils.mc.thePlayer.posZ + z, ground));
        }
    }
    
    public static double calculateFriction(final double moveSpeed, final double lastDist, final double baseMoveSpeedRef) {
        MovementUtils.frictionValues.clear();
        MovementUtils.frictionValues.add(lastDist - lastDist / 159.99);
        MovementUtils.frictionValues.add(lastDist - (moveSpeed - lastDist) / 33.3);
        final double materialFriction = MovementUtils.mc.thePlayer.isInWater() ? 0.89 : (MovementUtils.mc.thePlayer.isInLava() ? 0.535 : 0.98);
        MovementUtils.frictionValues.add(lastDist - baseMoveSpeedRef * (1.0 - materialFriction));
        return Collections.min((Collection<? extends Double>)MovementUtils.frictionValues);
    }
    
    public static void setStrafeSpeed(final EventMove e, final double speed) {
        final EntityPlayerSP player = MovementUtils.mc.thePlayer;
        if (TargetStrafe.enabled && (!TargetStrafe.onSpace.isEnabled() || Keyboard.isKeyDown(57)) && KillAura.target != null) {
            final float dist = MovementUtils.mc.thePlayer.getDistanceToEntity(KillAura.target);
            final double radius = TargetStrafe.range.getValue();
            if (TargetStrafe.behind.isEnabled()) {
                TargetStrafe.setSpeed(e, speed);
            }
            else {
                setTargetStrafeSpeed(e, speed, 1.0f, (dist <= radius + 1.0) ? ((float)TargetStrafe.direction) : 0.0f, RotationUtils.getYawToEntity(KillAura.target, false));
            }
            return;
        }
        setTargetStrafeSpeed(e, speed, player.moveForward, player.moveStrafing, player.rotationYaw);
    }
    
    public static void setTargetStrafeSpeed(final EventMove e, final double speed, float forward, float strafing, float yaw) {
        if (forward == 0.0 && strafing == 0.0) {
            MovementUtils.mc.thePlayer.motionX = 0.0;
            MovementUtils.mc.thePlayer.motionZ = 0.0;
        }
        else {
            if (forward != 0.0) {
                if (strafing > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafing < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafing = 0.0f;
                if (forward > 0.0) {
                    forward = 1.0f;
                }
                else if (forward < 0.0) {
                    forward = -1.0f;
                }
            }
            e.setX(MovementUtils.mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafing * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            e.setZ(MovementUtils.mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafing * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }
    
    public static void setSpeed(final EventMove moveEvent, final double moveSpeed, final float pseudoYaw, final double pseudoStrafe, final double pseudoForward) {
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
        final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        moveEvent.x = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        moveEvent.z = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }
    
    public static boolean isMoving() {
        final MovementInput movementInput = MovementUtils.mc.thePlayer.movementInput;
        if (MovementInput.moveForward == 0.0f) {
            final MovementInput movementInput2 = MovementUtils.mc.thePlayer.movementInput;
            if (MovementInput.moveStrafe == 0.0f) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isOnGround(final double height) {
        return !MovementUtils.mc.theWorld.getCollidingBoundingBoxes(MovementUtils.mc.thePlayer, MovementUtils.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }
    
    public static int getJumpBoostModifier() {
        final PotionEffect effect = MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.jump.id);
        if (effect != null) {
            return effect.getAmplifier() + 1;
        }
        return 0;
    }
    
    public static double getJumpBoostModifier(double baseJumpHeight, final boolean potionJumpHeight) {
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.jump) && potionJumpHeight) {
            final int amplifier = MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
            baseJumpHeight += (amplifier + 1) * 0.1f;
        }
        return baseJumpHeight;
    }
    
    public static double getJumpBoostModifier(double baseJumpHeight) {
        if (MovementUtils.mc.thePlayer.isPotionActive(Potion.jump)) {
            final int amplifier = MovementUtils.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
            baseJumpHeight += (amplifier + 1) * 0.1f;
        }
        return baseJumpHeight;
    }
    
    public static void setSpeed(final double moveSpeed, float yaw, double strafe, double forward) {
        final double fforward = forward;
        final double sstrafe = strafe;
        final float yyaw = yaw;
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
        MovementUtils.mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        MovementUtils.mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }
    
    public static float getSpeed() {
        return (float)Math.sqrt(MovementUtils.mc.thePlayer.motionX * MovementUtils.mc.thePlayer.motionX + MovementUtils.mc.thePlayer.motionZ * MovementUtils.mc.thePlayer.motionZ);
    }
    
    public static void setSpeed(final double moveSpeed) {
        final float rotationYaw = MovementUtils.mc.thePlayer.rotationYaw;
        final MovementInput movementInput = MovementUtils.mc.thePlayer.movementInput;
        final double strafe = MovementInput.moveStrafe;
        final MovementInput movementInput2 = MovementUtils.mc.thePlayer.movementInput;
        setSpeed(moveSpeed, rotationYaw, strafe, MovementInput.moveForward);
    }
    
    public double getTickDist() {
        final double xDist = MovementUtils.mc.thePlayer.posX - MovementUtils.mc.thePlayer.lastTickPosX;
        final double zDist = MovementUtils.mc.thePlayer.posZ - MovementUtils.mc.thePlayer.lastTickPosZ;
        return Math.sqrt(Math.pow(xDist, 2.0) + Math.pow(zDist, 2.0));
    }
    
    public static void strafe() {
        strafe(getSpeed());
    }
    
    public static void strafe(final float speed) {
        if (!isMoving()) {
            return;
        }
        final double yaw = getDirection();
        MovementUtils.mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        MovementUtils.mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }
    
    public static double getDirection() {
        final EntityPlayerSP thePlayer = MovementUtils.mc.thePlayer;
        Float rotationYaw = thePlayer.rotationYaw;
        if (thePlayer.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        Float forward = 1.0f;
        if (thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (thePlayer.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (thePlayer.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }
    
    public static void setMotion(final EventMove event, final double speed) {
        final MovementInput movementInput = MovementUtils.mc.thePlayer.movementInput;
        double forward = MovementInput.moveForward;
        final MovementInput movementInput2 = MovementUtils.mc.thePlayer.movementInput;
        double strafe = MovementInput.moveStrafe;
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
            event.setX(MovementUtils.mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setZ(MovementUtils.mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }
    
    public static void setMotion(final double speed) {
        final MovementInput movementInput = MovementUtils.mc.thePlayer.movementInput;
        double forward = MovementInput.moveForward;
        final MovementInput movementInput2 = MovementUtils.mc.thePlayer.movementInput;
        double strafe = MovementInput.moveStrafe;
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
    
    public static boolean canStep(final double height) {
        return MovementUtils.mc.thePlayer.isCollidedHorizontally && isOnGround(0.001) && ((!MovementUtils.mc.theWorld.getCollidingBoundingBoxes(MovementUtils.mc.thePlayer, MovementUtils.mc.thePlayer.getEntityBoundingBox().expand(0.1, 0.0, 0.0).offset(0.0, height - 0.1, 0.0)).isEmpty() && MovementUtils.mc.theWorld.getCollidingBoundingBoxes(MovementUtils.mc.thePlayer, MovementUtils.mc.thePlayer.getEntityBoundingBox().expand(0.1, 0.0, 0.0).offset(0.0, height + 0.1, 0.0)).isEmpty()) || (!MovementUtils.mc.theWorld.getCollidingBoundingBoxes(MovementUtils.mc.thePlayer, MovementUtils.mc.thePlayer.getEntityBoundingBox().expand(-0.1, 0.0, 0.0).offset(0.0, height - 0.1, 0.0)).isEmpty() && MovementUtils.mc.theWorld.getCollidingBoundingBoxes(MovementUtils.mc.thePlayer, MovementUtils.mc.thePlayer.getEntityBoundingBox().expand(-0.1, 0.0, 0.0).offset(0.0, height + 0.1, 0.0)).isEmpty()) || (!MovementUtils.mc.theWorld.getCollidingBoundingBoxes(MovementUtils.mc.thePlayer, MovementUtils.mc.thePlayer.getEntityBoundingBox().expand(0.0, 0.0, 0.1).offset(0.0, height - 0.1, 0.0)).isEmpty() && MovementUtils.mc.theWorld.getCollidingBoundingBoxes(MovementUtils.mc.thePlayer, MovementUtils.mc.thePlayer.getEntityBoundingBox().expand(0.0, 0.0, 0.1).offset(0.0, height + 0.1, 0.0)).isEmpty()) || (!MovementUtils.mc.theWorld.getCollidingBoundingBoxes(MovementUtils.mc.thePlayer, MovementUtils.mc.thePlayer.getEntityBoundingBox().expand(0.0, 0.0, -0.1).offset(0.0, height - 0.1, 0.0)).isEmpty() && MovementUtils.mc.theWorld.getCollidingBoundingBoxes(MovementUtils.mc.thePlayer, MovementUtils.mc.thePlayer.getEntityBoundingBox().expand(0.0, 0.0, -0.1).offset(0.0, height + 0.1, 0.0)).isEmpty()));
    }
    
    static {
        MovementUtils.mc = Minecraft.getMinecraft();
        frictionValues = new ArrayList<Double>();
        DEPTH_STRIDER_VALUES = new double[] { 1.0, 1.4304347826086956, 1.734782608695652, 1.9217391304347824 };
    }
}
