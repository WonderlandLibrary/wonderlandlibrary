package vip.radium.utils;

import net.minecraft.block.*;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import org.lwjgl.input.Keyboard;
import vip.radium.event.impl.player.MoveEntityEvent;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.module.impl.combat.TargetStrafe;
import vip.radium.module.impl.movement.NoSlowdown;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MovementUtils {

    private static final List<Double> frictionValues = Arrays.asList(0.0, 0.0, 0.0);

    private static final double AIR_FRICTION = 0.98F;
    private static final double WATER_FRICTION = 0.89F;
    private static final double LAVA_FRICTION = 0.535F;

    private MovementUtils() {
    }

    public static final double BUNNY_SLOPE = 0.72;
    public static final double SPRINTING_MOD = 1.3F;
    public static final double SNEAK_MOD = 0.3F;
    public static final double ICE_MOD = 2.5F;
    public static final double VANILLA_JUMP_HEIGHT = 0.42F;
    public static final double WALK_SPEED = 0.221F;
    private static final double SWIM_MOD = 0.115F / WALK_SPEED;
    private static final double[] DEPTH_STRIDER_VALUES = {
        1.0F,
        0.1645F / SWIM_MOD / WALK_SPEED,
        0.1995F / SWIM_MOD / WALK_SPEED,
        1.0F / SWIM_MOD,
    };
    public static final double MAX_DIST = 1.85F;
    public static final double BUNNY_DIV_FRICTION = 160.0 - 1.0E-3;

    public static int getJumpBoostModifier() {
        PotionEffect effect = Wrapper.getPlayer().getActivePotionEffect(Potion.jump.id);
        if (effect != null)
            return effect.getAmplifier() + 1;
        return 0;
    }

    public static double getPosYForJumpTick(int tick) {
        switch (tick) {
            case 1:
                return 0.42F;
            case 2:
                return 0.7532F;
            case 3:
                return 1.00133597911214D;
            case 4:
                return 1.16610926093821D;
            case 5:
            case 6:
                return 1.24918707874468D;
            case 7:
                return 1.1707870772188D;
            case 8:
                return 1.0155550727022D;
            case 9:
                return 0.78502770378923D;
            case 10:
                return 0.48071087633169D;
            case 11:
                return 0.10408037809304D;
            default:
                return 0;
        }
    }

    public static int getSpeedModifier() {
        PotionEffect effect = Wrapper.getPlayer().getActivePotionEffect(Potion.moveSpeed.id);
        if (effect != null)
            return effect.getAmplifier() + 1;
        return 0;
    }


    private static boolean isMovingEnoughForSprint() {
        MovementInput movementInput = Wrapper.getPlayer().movementInput;
        return movementInput.moveForward > 0.8F || movementInput.moveForward < -0.8F ||
            movementInput.moveStrafe > 0.8F || movementInput.moveStrafe < -0.8F;
    }

    public static float getMovementDirection() {
        final EntityPlayerSP player = Wrapper.getPlayer();
        float forward = player.moveForward;
        float strafe = player.moveStrafing;

        float direction = 0.0f;
        if (forward < 0) {
            direction += 180;
            if (strafe > 0) {
                direction += 45;
            } else if (strafe < 0) {
                direction -= 45;
            }
        } else if (forward > 0) {
            if (strafe > 0) {
                direction -= 45;
            } else if (strafe < 0) {
                direction += 45;
            }
        } else {
            if (strafe > 0) {
                direction -= 90;
            } else if (strafe < 0) {
                direction += 90;
            }
        }

        direction += player.rotationYaw;

        return MathHelper.wrapAngleTo180_float(direction);
    }

    public static boolean isOnStairs() {
        return getBlockUnder(0.5) instanceof BlockStairs;
    }

    public static boolean isBlockAbove() {
        return Wrapper.getWorld()
            .checkBlockCollision(
                Wrapper.getPlayer().getEntityBoundingBox()
                    .addCoord(0.0D, 1.0D, 0.0D));
    }

    public static boolean isDistFromGround(double dist) {
        return Wrapper.getWorld()
                .checkBlockCollision(
                        Wrapper.getPlayer().getEntityBoundingBox()
                                .addCoord(0.0D, -dist, 0.0D));
    }

    public static double estimateDistFromGround(int maxIterations) {
        final int playerPosY = (int) Math.floor(Wrapper.getPlayer().posY);
        final int min = playerPosY - maxIterations;

        for (int i = playerPosY; i > min; i -= 2) {
            if (Wrapper.getWorld()
                    .checkBlockCollision(
                            Wrapper.getPlayer().getEntityBoundingBox()
                                    .addCoord(0.0D, -(playerPosY - i), 0.0D))) {
                return playerPosY - i;
            }
        }

        return maxIterations;
    }

    public static boolean fallDistDamage() {
        if (isBlockAbove() || !ServerUtils.isOnHypixel() || !HypixelGameUtils.hasGameStarted()) return false;
        final UpdatePositionEvent e = Wrapper.getPlayer().currentEvent;
        final double x = e.getPosX();
        final double y = e.getPosY();
        final double z = e.getPosZ();
        final double smallOffset = 0.0013F;
        final double offset = 0.0611F;
        final double packets = Math.ceil(getMinFallDist() / (offset - smallOffset));

        for (int i = 0; i < packets; i++) {
            Wrapper.sendPacketDirect(new C03PacketPlayer.C04PacketPlayerPosition(
                x, y + offset, z,
                false));
            Wrapper.sendPacketDirect(new C03PacketPlayer.C04PacketPlayerPosition(
                x, y + smallOffset, z,
                false));
        }
        return true;
    }

    public static boolean isInLiquid() {
        return Wrapper.getPlayer().isInWater() || Wrapper.getPlayer().isInLava();
    }

    public static boolean isOverVoid() {
        for (double posY = Wrapper.getPlayer().posY; posY > 0.0; posY--) {
            if (!(Wrapper.getWorld().getBlockState(
                new BlockPos(Wrapper.getPlayer().posX, posY, Wrapper.getPlayer().posZ)).getBlock() instanceof BlockAir))
                return false;
        }

        return true;
    }

    public static double getJumpHeight() {
        double baseJumpHeight = VANILLA_JUMP_HEIGHT;
        if (isInLiquid()) {
            return WALK_SPEED * SWIM_MOD + 0.02F;
        } else if (Wrapper.getPlayer().isPotionActive(Potion.jump)) {
            return baseJumpHeight + (Wrapper.getPlayer().getActivePotionEffect(Potion.jump).getAmplifier() + 1.0F) * 0.1F;
        }
        return baseJumpHeight;
    }

    public static double getMinFallDist() {
        final boolean isSg = HypixelGameUtils.getGameMode() == HypixelGameUtils.GameMode.BLITZ_SG;
        double baseFallDist = isSg ? 4.0D : 3.0D;
        if (Wrapper.getPlayer().isPotionActive(Potion.jump))
            baseFallDist += Wrapper.getPlayer().getActivePotionEffect(Potion.jump).getAmplifier() + 1.0F;
        return baseFallDist;
    }

    public static double calculateFriction(double moveSpeed, double lastDist, double baseMoveSpeedRef) {
        frictionValues.set(0, lastDist - (lastDist / BUNNY_DIV_FRICTION));
        frictionValues.set(1, lastDist - ((moveSpeed - lastDist) / 33.3D));
        double materialFriction =
            Wrapper.getPlayer().isInWater() ?
                WATER_FRICTION :
                Wrapper.getPlayer().isInLava() ?
                    LAVA_FRICTION :
                    AIR_FRICTION;
        frictionValues.set(2, lastDist - (baseMoveSpeedRef * (1.0D - materialFriction)));
        return Collections.min(frictionValues);
    }

    public static boolean isOnIce() {
        final Block blockUnder = getBlockUnder(1);
        return blockUnder instanceof BlockIce || blockUnder instanceof BlockPackedIce;
    }

    public static Block getBlockUnder(double offset) {
        EntityPlayerSP player = Wrapper.getPlayer();
        return Wrapper.getWorld().getBlockState(
            new BlockPos(
                player.posX,
                player.posY - offset,
                player.posZ)).getBlock();
    }

    public static double getBlockHeight() {
        return Wrapper.getPlayer().posY - (int) Wrapper.getPlayer().posY;
    }

    public static boolean canSprint(boolean omni) {
        final EntityPlayerSP player = Wrapper.getPlayer();
        return (omni ? isMovingEnoughForSprint() : player.movementInput.moveForward >= 0.8F) &&
            !player.isCollidedHorizontally &&
            (player.getFoodStats().getFoodLevel() > 6 ||
                player.capabilities.allowFlying) &&
            !player.isSneaking() &&
            (!player.isUsingItem() || NoSlowdown.isNoSlowdownEnabled()) &&
            !player.isPotionActive(Potion.moveSlowdown.id);
    }

    public static double getBaseMoveSpeed() {
        final EntityPlayerSP player = Wrapper.getPlayer();
        double base = player.isSneaking() ? WALK_SPEED * MovementUtils.SNEAK_MOD : canSprint(true) ? WALK_SPEED * SPRINTING_MOD : WALK_SPEED;

        PotionEffect moveSpeed = player.getActivePotionEffect(Potion.moveSpeed.id);
        PotionEffect moveSlowness = player.getActivePotionEffect(Potion.moveSlowdown.id);

        if (moveSpeed != null)
            base *= 1.0 + 0.2 * (moveSpeed.getAmplifier() + 1);

        if (moveSlowness != null)
            base *= 1.0 + 0.2 * (moveSlowness.getAmplifier() + 1);


        if (player.isInWater()) {
            base *= SWIM_MOD;
            final int depthStriderLevel = InventoryUtils.getDepthStriderLevel();
            if (depthStriderLevel > 0) {
                base *= DEPTH_STRIDER_VALUES[depthStriderLevel];
            }
        } else if (player.isInLava()) {
            base *= SWIM_MOD;
        }
        return base;
    }

    public static void setSpeed(MoveEntityEvent e, double speed) {
        final EntityPlayerSP player = Wrapper.getPlayer();
        final TargetStrafe targetStrafe = TargetStrafe.getInstance();
        if (targetStrafe.isEnabled() &&
            (!targetStrafe.holdSpaceProperty.getValue() || Keyboard.isKeyDown(Keyboard.KEY_SPACE))) {
            if (targetStrafe.shouldStrafe()) {
                if (targetStrafe.shouldAdaptSpeed())
                    speed = Math.min(speed, targetStrafe.getAdaptedSpeed());
                targetStrafe.setSpeed(e, speed);
                return;
            }
        }
        setSpeed(e, speed, player.moveForward, player.moveStrafing, player.rotationYaw);
    }

    public static void setSpeed(MoveEntityEvent e, double speed, float forward, float strafing, float yaw) {
        if (forward == 0.0F && strafing == 0.0F)
            return;

        boolean reversed = forward < 0.0f;
        float strafingYaw = 90.0f *
            (forward > 0.0f ? 0.5f : reversed ? -0.5f : 1.0f);

        if (reversed)
            yaw += 180.0f;
        if (strafing > 0.0f)
            yaw -= strafingYaw;
        else if (strafing < 0.0f)
            yaw += strafingYaw;

        double x = StrictMath.cos(StrictMath.toRadians(yaw + 90.0f));
        double z = StrictMath.cos(StrictMath.toRadians(yaw));

        e.setX(x * speed);
        e.setZ(z * speed);
    }

    public static boolean isOnGround() {
//        List<AxisAlignedBB> collidingList = Wrapper.getWorld().getCollidingBoundingBoxes(Wrapper.getPlayer(), Wrapper.getPlayer().getEntityBoundingBox().offset(0.0, -(0.01 - MIN_DIF), 0.0));
//        return collidingList.size() > 0;
        return Wrapper.getPlayer().onGround && Wrapper.getPlayer().isCollidedVertically;
    }

    public static boolean isMoving() {
        return Wrapper.getPlayer().movementInput.moveForward != 0.0F || Wrapper.getPlayer().movementInput.moveStrafe != 0.0F;
    }
}
