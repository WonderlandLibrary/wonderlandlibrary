package xyz.cucumber.base.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.ext.EventHit;
import xyz.cucumber.base.events.ext.EventMoveFlying;
import xyz.cucumber.base.utils.math.RotationUtils;
import xyz.cucumber.base.utils.math.Vector3d;

public class MovementUtils
{
    public static Minecraft mc = Minecraft.getMinecraft();

    public static double WALK_SPEED = 0.221;
    public static double BUNNY_SLOPE = 0.66;
    public static double MOD_SPRINTING = 1.3F;
    public static double MOD_SNEAK = 0.3F;
    public static double MOD_ICE = 2.5F;
    public static double MOD_WEB = 0.105 / WALK_SPEED;
    public static double JUMP_HEIGHT = 0.42F;
    public static double BUNNY_FRICTION = 159.9F;
    public static double Y_ON_GROUND_MIN = 0.00001;
    public static double Y_ON_GROUND_MAX = 0.0626;

    public static double AIR_FRICTION = 0.9800000190734863D;
    public static double WATER_FRICTION = 0.800000011920929D;
    public static double LAVA_FRICTION = 0.5D;
    public static double MOD_SWIM = 0.115F / WALK_SPEED;
    public static double[] MOD_DEPTH_STRIDER = {
            1.0F,
            0.1645F / MOD_SWIM / WALK_SPEED,
            0.1995F / MOD_SWIM / WALK_SPEED,
            1.0F / MOD_SWIM,
    };

    public static double UNLOADED_CHUNK_MOTION = -0.09800000190735147;
    public static double HEAD_HITTER_MOTION = -0.0784000015258789;
    
    public static List<Vector3d> findPath(final double tpX, final double tpY, final double tpZ, final double offset) {
        final List<Vector3d> positions = new ArrayList<>();
        final double steps = Math.ceil(getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY, tpZ) / offset);

        final double dX = tpX - mc.thePlayer.posX;
        final double dY = tpY - mc.thePlayer.posY;
        final double dZ = tpZ - mc.thePlayer.posZ;

        for (double d = 1D; d <= steps; ++d) {
            positions.add(new Vector3d(mc.thePlayer.posX + (dX * d) / steps, mc.thePlayer.posY + (dY * d) / steps, mc.thePlayer.posZ + (dZ * d) / steps));
        }

        return positions;
    }
    
    public static float[] incrementMoveDirection(float forward, float strafe) {
        if(forward != 0 || strafe != 0) {
            float value = forward != 0 ? Math.abs(forward) : Math.abs(strafe);

            if(forward > 0) {
                if(strafe > 0) {
                    strafe = 0;
                } else if(strafe == 0) {
                    strafe = -value;
                } else if(strafe < 0) {
                    forward = 0;
                }
            } else if(forward == 0) {
                if(strafe > 0) {
                    forward = value;
                } else {
                    forward = -value;
                }
            } else {
                if(strafe < 0) {
                    strafe = 0;
                } else if(strafe == 0) {
                    strafe = value;
                } else if(strafe > 0) {
                    forward = 0;
                }
            }
        }

        return new float[] {forward, strafe};
    }

    private static double getDistance(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        final double xDiff = x1 - x2;
        final double yDiff = y1 - y2;
        final double zDiff = z1 - z2;
        return MathHelper.sqrt_double(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }
    
    public static double getPredictedPlayerDistance(double x, double y, double z, int predict)
    {
    	double posX = mc.thePlayer.posX + ((mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * (predict));
		double posY = mc.thePlayer.posY;
		double posZ = mc.thePlayer.posZ + ((mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * (predict));
        double d0 = posX - x;
        double d1 = posY - y;
        double d2 = posZ - z;
        return (double)MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
    }
    
    public static void useDiagonalSpeed() {
        KeyBinding[] gameSettings = new KeyBinding[]{mc.gameSettings.keyBindForward, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft};

        int[] down = {0};

        Arrays.stream(gameSettings).forEach(keyBinding -> {
            down[0] = down[0] + (keyBinding.isKeyDown() ? 1 : 0);
        });

        boolean active = down[0] == 1;

        if (!active) return;

        double groundIncrease = (0.1299999676734952 - 0.12739998266255503) + 1E-7 - 1E-8;
        double airIncrease = (0.025999999334873708 - 0.025479999685988748) - 1E-8;
        double increase = mc.thePlayer.onGround ? groundIncrease : airIncrease;

        moveFlying(increase, RotationUtils.customRots ? RotationUtils.serverYaw : mc.thePlayer.rotationYaw);
    }
    
    public static void moveFlying(double increase, float playerYaw) {
        if (!isMoving()) return;
        double yaw = getDirection(playerYaw);
        mc.thePlayer.motionX += -MathHelper.sin((float) yaw) * increase;
        mc.thePlayer.motionZ += MathHelper.cos((float) yaw) * increase;
    }
    
    
    public static boolean isGoingDiagonally()
    {
        return Math.abs(mc.thePlayer.motionX) > 0.04 && Math.abs(mc.thePlayer.motionZ) > 0.04;
    }
    public static double getAllowedHorizontalDistance() {
        double horizontalDistance;
        boolean useBaseModifiers = false;

        if (mc.thePlayer.isInWeb) {
            horizontalDistance = MOD_WEB * WALK_SPEED;
        } else if (mc.thePlayer.isInLava() || mc.thePlayer.isInWater()) {
            horizontalDistance = MOD_SWIM * WALK_SPEED;

            int depthStriderLevel = depthStriderLevel();
            if (depthStriderLevel > 0) {
                horizontalDistance *= MOD_DEPTH_STRIDER[depthStriderLevel];
                useBaseModifiers = true;
            }

        } else if (mc.thePlayer.isSneaking()) {
            horizontalDistance = MOD_SNEAK * WALK_SPEED;
        } else {
            horizontalDistance = WALK_SPEED;
            useBaseModifiers = true;
        }

        if (useBaseModifiers) {
            if (canSprint(false)) {
                horizontalDistance *= MOD_SPRINTING;
            }

            if (mc.thePlayer.isPotionActive(Potion.moveSlowdown)) {
                horizontalDistance = 0.29;
            }
        }

        Block below = mc.theWorld.getBlockState(new BlockPos(0, -1, 0)).getBlock();
        if (below == Blocks.ice || below == Blocks.packed_ice) {
            horizontalDistance *= 1.2;
        }

        return horizontalDistance;
    }
    
    public static int depthStriderLevel() {
        return EnchantmentHelper.getDepthStriderModifier(mc.thePlayer);
    }
    
    public double jumpMotion() {
        return jumpBoostMotion(JUMP_HEIGHT);
    }
    
    public static double jumpBoostMotion(double motionY) {
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            return motionY + (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
        }

        return motionY;
    }
    
    public static boolean canSprint(boolean legit) {
        return (legit ? mc.thePlayer.moveForward >= 0.8F
                && !mc.thePlayer.isCollidedHorizontally
                && (mc.thePlayer.getFoodStats().getFoodLevel() > 6 || mc.thePlayer.capabilities.allowFlying)
                && !mc.thePlayer.isPotionActive(Potion.blindness)
                && !mc.thePlayer.isUsingItem()
                && !mc.thePlayer.isSneaking()
                : enoughMovementForSprinting());
    }
    
    public static boolean enoughMovementForSprinting() {
        return Math.abs(mc.thePlayer.moveForward) >= 0.8F || Math.abs(mc.thePlayer.moveStrafing) >= 0.8F;
    }

    public static double getPredictedMotionY(double motionY)
    {
        return (motionY - 0.08) * 0.98F;
    }

    public static void forward(double length)
    {
        double angleA = Math.toRadians(normalizeAngle((mc.thePlayer.rotationYawHead - 90.0F)));
        mc.thePlayer.setPosition(mc.thePlayer.posX - Math.cos(angleA) * length, mc.thePlayer.posY,
                                 mc.thePlayer.posZ - Math.sin(angleA) * length);
    }
    public static double normalizeAngle(double angle)
    {
        return (angle + 360.0D) % 360.0D;
    }
    
    public static double roundToOnGround(double posY) {
        return posY - (posY % 0.015625);
    }
    
    public static double distanceToGround(Vec3 vec3) {
        double playerY = vec3.yCoord;
        return playerY - getBlockBellow(vec3).getY() - 1;
    }
    public static double distanceToGround() {
    	Vec3 vec3 = mc.thePlayer.getPositionVector();
        double playerY = vec3.yCoord;
        return playerY - getBlockBellow(vec3).getY() - 1;
    }
      public static BlockPos getBlockBellow(Vec3 playerPos) {
        for (; playerPos.yCoord > 0; playerPos = playerPos.addVector(0, -1, 0)) {
          BlockPos blockPos = new BlockPos(playerPos);
          if (!(isAir(blockPos))) return blockPos;
        }
        return BlockPos.ORIGIN;
      }
      
      public static boolean isAir(BlockPos blockPos) {
    	    return mc.theWorld.getBlockState(blockPos).getBlock() == Blocks.air;
    	  }

    public static double getDirection(float yaw)
    {
        float rotationYaw = yaw;

        if (mc.thePlayer.moveForward < 0.0F)
        {
            rotationYaw += 180.0F;
        }

        float forward = 1.0F;

        if (mc.thePlayer.moveForward < 0.0F)
        {
            forward = -0.5F;
        }
        else if (mc.thePlayer.moveForward > 0.0F)
        {
            forward = 0.5F;
        }

        if (mc.thePlayer.moveStrafing > 0.0F)
        {
            rotationYaw -= 90.0F * forward;
        }

        if (mc.thePlayer.moveStrafing < 0.0F)
        {
            rotationYaw += 90.0F * forward;
        }

        return Math.toRadians(rotationYaw);
    }
    
    public static double getDirectionKeybinds(float yaw)
    {
        float rotationYaw = yaw;

        if (!Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()) && Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()))
        {
            rotationYaw += 180.0F;
        }

        float forward = 1.0F;

        if (!Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()) && Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()))
        {
            forward = -0.5F;
        }
        else if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()) && !Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()))
        {
            forward = 0.5F;
        }

        if (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()) && !Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()))
        {
            rotationYaw -= 90.0F * forward;
        }

        if (!Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()) && Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()))
        {
            rotationYaw += 90.0F * forward;
        }

        return Math.toRadians(rotationYaw);
    }
    
    public static double getDirection(float yaw, float moveForward, float moveStrafing)
    {
        float rotationYaw = yaw;

        if (moveForward < 0.0F)
        {
            rotationYaw += 180.0F;
        }

        float forward = 1.0F;

        if (moveForward < 0.0F)
        {
            forward = -0.5F;
        }
        else if (moveForward > 0.0F)
        {
            forward = 0.5F;
        }

        if (moveStrafing > 0.0F)
        {
            rotationYaw -= 90.0F * forward;
        }

        if (moveStrafing < 0.0F)
        {
            rotationYaw += 90.0F * forward;
        }

        return Math.toRadians(rotationYaw);
    }

    public static double getBaseMoveSpeed()
    {
        double baseSpeed = 0.2873D;

        if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
        {
            baseSpeed *= 1.0D + 0.2D * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }

        return baseSpeed;
    }

    public static double getSpeed()
    {
        return Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ);
    }

    public static boolean isMoving() {
        return Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown() ||
                 Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown() ||
                 Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown()
                || Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown();
    }

    public static void strafe(float speed)
    {
        double yaw = getDirection(mc.thePlayer.rotationYaw);
        mc.thePlayer.motionX = -Math.sin((float) yaw) * speed;
        mc.thePlayer.motionZ = Math.cos((float) yaw) * speed;
    }

    public static void strafe(float speed, float yaw)
    {
        mc.thePlayer.motionX = -Math.sin((float) yaw) * speed;
        mc.thePlayer.motionZ = Math.cos((float) yaw) * speed;
    }
    
    public static void strafe(float speed, float yaw, float moveForward, float moveStrafing)
    {
    	yaw = (float) getDirection((float) yaw, moveForward, moveStrafing);
        mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }
    
    public static void strafe()
    {
        double yaw = getDirection(mc.thePlayer.rotationYaw);
        mc.thePlayer.motionX = -Math.sin((float) yaw) * getSpeed();
        mc.thePlayer.motionZ = Math.cos((float) yaw) * getSpeed();
    }
    
    public static void silentMoveFix(EventMoveFlying event)
    {

        int dif = (int)((MathHelper.wrapAngleTo180_float((float)(mc.thePlayer.rotationYaw - RotationUtils.serverYaw - 23.5f - 135)) + 180) / 45);
        float yaw = RotationUtils.serverYaw;
        float strafe =event.getStrafe();//event.getStrafe()
        float forward = event.getForward();//event.getForward()
        float friction = event.getFriction();//event.getFriction()
        float calcForward = 0f;
        float calcStrafe = 0f;

        switch (dif)
        {
            case 0:
            {
                calcForward = forward;
                calcStrafe = strafe;
            }
            break;

            case 1:
            {
                calcForward += forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe += strafe;
            }
            break;

            case 2:
            {
                calcForward = strafe;
                calcStrafe = -forward;
            }
            break;

            case 3:
            {
                calcForward -= forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe -= strafe;
            }
            break;

            case 4:
            {
                calcForward = -forward;
                calcStrafe = -strafe;
            }
            break;

            case 5:
            {
                calcForward -= forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe -= strafe;
            }
            break;

            case 6:
            {
                calcForward = -strafe;
                calcStrafe = forward;
            }
            break;

            case 7:
            {
                calcForward += forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe += strafe;
            }
            break;
        }

        if (calcForward > 1f || calcForward < 0.9f && calcForward > 0.3f || calcForward < -1f || calcForward > -0.9f && calcForward < -0.3f)
        {
            calcForward *= 0.5f;
        }

        if (calcStrafe > 1f || calcStrafe < 0.9f && calcStrafe > 0.3f || calcStrafe < -1f || calcStrafe > -0.9f && calcStrafe < -0.3f)
        {
            calcStrafe *= 0.5f;
        }

        float d = calcStrafe * calcStrafe + calcForward * calcForward;

        if (d >= 1.0E-4f)
        {
            d = MathHelper.sqrt_float(d);

            if (d < 1.0f)
            {
                d = 1.0f;
            }

            d = friction / d;
            calcStrafe = calcStrafe * d;
            calcForward = calcForward * d;
            float yawSin = MathHelper.sin((float)(yaw * Math.PI / 180f));
            float yawCos = MathHelper.cos((float)(yaw * Math.PI / 180f));
            mc.thePlayer.motionX += calcStrafe * yawCos - calcForward * yawSin;
            mc.thePlayer.motionZ += calcForward * yawCos + calcStrafe * yawSin;
        }
    }
    public static boolean isOnGround(double height)
    {
        if (!mc.theWorld.getCollidingBoundingBoxes((Entity)mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty())
        {
            return true;
        }

        return false;
    }
    
    public static double[] getPredictedPos(float forward, float strafe, double motionX, double motionY, double motionZ, double posX, double posY, double posZ, final boolean isJumping) {
        strafe *= 0.98f;
        forward *= 0.98f;
        float f4 = 0.91f;
        final boolean isSprinting = mc.thePlayer.isSprinting();
        if (isJumping && mc.thePlayer.onGround && mc.thePlayer.jumpTicks == 0) {
            motionY = 0.42;
            if (mc.thePlayer.isPotionActive(Potion.jump)) {
                motionY += (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f;
            }
            if (isSprinting) {
                final float f5 = mc.thePlayer.rotationYaw * 0.017453292f;
                motionX -= MathHelper.sin(f5) * 0.2f;
                motionZ += MathHelper.cos(f5) * 0.2f;
            }
        }
        if (mc.thePlayer.onGround) {
            f4 = mc.thePlayer.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(posX), MathHelper.floor_double(posY) - 1, MathHelper.floor_double(posZ))).getBlock().slipperiness * 0.91f;
        }
        final float f6 = 0.16277136f / (f4 * f4 * f4);
        float friction;
        if (mc.thePlayer.onGround) {
            friction = mc.thePlayer.getAIMoveSpeed() * f6;
        }
        else {
            friction = mc.thePlayer.jumpMovementFactor;
        }
        float f7 = strafe * strafe + forward * forward;
        if (f7 >= 1.0E-4f) {
            f7 = MathHelper.sqrt_float(f7);
            if (f7 < 1.0f) {
                f7 = 1.0f;
            }
            f7 = friction / f7;
            strafe *= f7;
            forward *= f7;
            final float f8 = MathHelper.sin(mc.thePlayer.rotationYaw * 3.1415927f / 180.0f);
            final float f9 = MathHelper.cos(mc.thePlayer.rotationYaw * 3.1415927f / 180.0f);
            motionX += strafe * f9 - forward * f8;
            motionZ += forward * f9 + strafe * f8;
        }
        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        f4 = 0.91f;
        if (mc.thePlayer.onGround) {
            f4 = mc.thePlayer.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(posX), MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(posZ))).getBlock().slipperiness * 0.91f;
        }
        if (mc.thePlayer.worldObj.isRemote && (!mc.thePlayer.worldObj.isBlockLoaded(new BlockPos((int)posX, 0, (int)posZ)) || !mc.thePlayer.worldObj.getChunkFromBlockCoords(new BlockPos((int)posX, 0, (int)posZ)).isLoaded())) {
            if (posY > 0.0) {
                motionY = -0.1;
            }
            else {
                motionY = 0.0;
            }
        }
        else {
            motionY -= 0.08;
        }
        motionY *= 0.9800000190734863;
        motionX *= f4;
        motionZ *= f4;
        return new double[] { posX, posY, posZ, motionX, motionY, motionZ };
    }
    
    public static Vec3 getPredictedPos(final boolean isHitting, final Entity targetEntity, float forward, float strafe) {
        strafe *= 0.98f;
        forward *= 0.98f;
        float f4 = 0.91f;
        double motionX = mc.thePlayer.motionX;
        double motionZ = mc.thePlayer.motionZ;
        double motionY = mc.thePlayer.motionY;
        boolean isSprinting = mc.thePlayer.isSprinting();
        if (isHitting) {
            final float f5 = (float)mc.thePlayer.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
            float f6 = 0.0f;
            if (targetEntity instanceof EntityLivingBase) {
                f6 = EnchantmentHelper.getModifierForCreature(mc.thePlayer.getHeldItem(), ((EntityLivingBase)targetEntity).getCreatureAttribute());
            }
            else {
                f6 = EnchantmentHelper.getModifierForCreature(mc.thePlayer.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
            }
            if (f5 > 0.0f || f6 > 0.0f) {
                int i = EnchantmentHelper.getKnockbackModifier(mc.thePlayer);
                if (mc.thePlayer.isSprinting()) {
                    ++i;
                }
                final boolean flag2 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(mc.thePlayer), f5);
                if (flag2) {
                    if (i > 0) {
                        EventHit event = new EventHit(false);
                        Client.INSTANCE.getEventBus().call(event);
                        motionX *= 0.6;
                        motionZ *= 0.6;
                        isSprinting = event.isSprint();
                    }
                }
            }
        }
        if (mc.thePlayer.isJumping && mc.thePlayer.onGround && mc.thePlayer.jumpTicks == 0) {
            motionY = 0.42;
            if (mc.thePlayer.isPotionActive(Potion.jump)) {
                motionY += (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f;
            }
            if (isSprinting) {
                final float f5 = mc.thePlayer.rotationYaw * 0.017453292f;
                motionX -= MathHelper.sin(f5) * 0.2f;
                motionZ += MathHelper.cos(f5) * 0.2f;
            }
        }
        if (mc.thePlayer.onGround) {
            f4 = mc.thePlayer.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(mc.thePlayer.posX), MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(mc.thePlayer.posZ))).getBlock().slipperiness * 0.91f;
        }
        final float f7 = 0.16277136f / (f4 * f4 * f4);
        float friction;
        if (mc.thePlayer.onGround) {
            friction = mc.thePlayer.getAIMoveSpeed() * f7;
        }
        else {
            friction = mc.thePlayer.jumpMovementFactor;
        }
        float f8 = strafe * strafe + forward * forward;
        if (f8 >= 1.0E-4f) {
            f8 = MathHelper.sqrt_float(f8);
            if (f8 < 1.0f) {
                f8 = 1.0f;
            }
            f8 = friction / f8;
            strafe *= f8;
            forward *= f8;
            final float f9 = MathHelper.sin(mc.thePlayer.rotationYaw * 3.1415927f / 180.0f);
            final float f10 = MathHelper.cos(mc.thePlayer.rotationYaw * 3.1415927f / 180.0f);
            motionX += strafe * f10 - forward * f9;
            motionZ += forward * f10 + strafe * f9;
        }
        f4 = 0.91f;
        if (mc.thePlayer.onGround) {
            f4 = mc.thePlayer.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(mc.thePlayer.posX), MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(mc.thePlayer.posZ))).getBlock().slipperiness * 0.91f;
        }
        motionY *= 0.9800000190734863;
        motionX *= f4;
        motionZ *= f4;
        return new Vec3(motionX, motionY, motionZ);
    }
    
    public static double[] getMotion(final double speed, final float strafe, final float forward, final float yaw) {
        final float friction = (float)speed;
        final float f1 = MathHelper.sin((float) Math.toRadians(yaw));
        final float f2 = MathHelper.cos((float) Math.toRadians(yaw));
        final double motionX = strafe * friction * f2 - forward * friction * f1;
        final double motionZ = forward * friction * f2 + strafe * friction * f1;
        return new double[] { motionX, motionZ };
    }
    
    public static float[] silentStrafe(final float strafe, final float forward, final float yaw, final boolean advanced) {
    	int dif = (int)((MathHelper.wrapAngleTo180_float((float)(mc.thePlayer.rotationYaw - RotationUtils.serverYaw - 23.5f - 135)) + 180) / 45);
        float calcForward = forward;
        float calcStrafe = strafe;

        switch (dif)
        {
            case 0:
            {
                calcForward = forward;
                calcStrafe = strafe;
            }
            break;

            case 1:
            {
                calcForward += forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe += strafe;
            }
            break;

            case 2:
            {
                calcForward = strafe;
                calcStrafe = -forward;
            }
            break;

            case 3:
            {
                calcForward -= forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe -= strafe;
            }
            break;

            case 4:
            {
                calcForward = -forward;
                calcStrafe = -strafe;
            }
            break;

            case 5:
            {
                calcForward -= forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe -= strafe;
            }
            break;

            case 6:
            {
                calcForward = -strafe;
                calcStrafe = forward;
            }
            break;

            case 7:
            {
                calcForward += forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe += strafe;
            }
            break;
        }

        if (calcForward > 1f || calcForward < 0.9f && calcForward > 0.3f || calcForward < -1f || calcForward > -0.9f && calcForward < -0.3f)
        {
            calcForward *= 0.5f;
        }

        if (calcStrafe > 1f || calcStrafe < 0.9f && calcStrafe > 0.3f || calcStrafe < -1f || calcStrafe > -0.9f && calcStrafe < -0.3f)
        {
            calcStrafe *= 0.5f;
        }
        
        return new float[] {calcStrafe, calcForward};
    }
}
