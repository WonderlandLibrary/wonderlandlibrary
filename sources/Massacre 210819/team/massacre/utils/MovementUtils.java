package team.massacre.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import org.lwjgl.input.Keyboard;
import team.massacre.Massacre;
import team.massacre.impl.event.EventMove;
import team.massacre.impl.module.combat.KillAura2;
import team.massacre.impl.module.combat.TargetStrafe;
import team.massacre.utils.baseHelper.ACType;

public final class MovementUtils {
   public static final Minecraft mc = Minecraft.getMinecraft();
   public static final double BUNNY_SLOPE = 0.72D;
   public static final double SPRINTING_MOD = 1.2999999523162842D;
   public static final double SNEAK_MOD = 0.30000001192092896D;
   public static final double ICE_MOD = 2.5D;
   public static final double VANILLA_JUMP_HEIGHT = 0.41999998688697815D;
   public static final double WALK_SPEED = 0.22100000083446503D;
   private static final List<Double> frictionValues = Arrays.asList(0.0D, 0.0D, 0.0D);
   private static final double AIR_FRICTION = 0.9800000190734863D;
   private static final double WATER_FRICTION = 0.8899999856948853D;
   private static final double LAVA_FRICTION = 0.5350000262260437D;
   private static final double SWIM_MOD = 0.5203619984250619D;
   private static final double[] DEPTH_STRIDER_VALUES = new double[]{1.0D, 1.4304347400741908D, 1.7347825295420374D, 1.9217391028296074D};
   private static final double MIN_DIST = 0.001D;
   public static final double MAX_DIST = 2.149D;
   public static final double BUNNY_DIV_FRICTION = 159.999D;

   private MovementUtils() {
   }

   public static double getBaseSpeed(ACType value) {
      double baseSpeed = value == ACType.MINEPLEX ? 0.4225D : (value == ACType.VERUS ? 0.24D : 0.2873D);
      if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
         int amp = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
         baseSpeed *= 1.0D + (value == ACType.VERUS ? 0.05D : 0.2D) * (double)amp;
      }

      return baseSpeed;
   }

   public static double getBaseSpeedAAC() {
      double baseSpeed = 0.24D;
      if (mc.thePlayer != null && mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
         int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
         baseSpeed *= 1.0D + 0.0525D * (double)(amplifier + 1);
      }

      return baseSpeed;
   }

   public static float getSensitivityMultiplier() {
      float f = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
      return f * f * f * 8.0F * 0.15F;
   }

   public static boolean isOnGround(double height) {
      return !Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty();
   }

   public static int getJumpBoostModifier() {
      return EntityUtils.getPotionModifier(PlayerUtils.getLocalPlayer(), Potion.jump);
   }

   public static double getMotion(EntityPlayerSP player) {
      return Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);
   }

   private static boolean isMovingEnoughForSprint() {
      float minInput = 0.8F;
      MovementInput movementInput = PlayerUtils.getLocalPlayer().movementInput;
      return movementInput.moveForward > 0.8F || movementInput.moveForward < -0.8F || movementInput.moveStrafe > 0.8F || movementInput.moveStrafe < -0.8F;
   }

   public static float getMovementDirection() {
      EntityPlayerSP player = PlayerUtils.getLocalPlayer();
      float forward = player.moveForward;
      float strafe = player.moveStrafing;
      float direction = 0.0F;
      if (forward < 0.0F) {
         direction += 180.0F;
         if (strafe > 0.0F) {
            direction += 45.0F;
         } else if (strafe < 0.0F) {
            direction -= 45.0F;
         }
      } else if (forward > 0.0F) {
         if (strafe > 0.0F) {
            direction -= 45.0F;
         } else if (strafe < 0.0F) {
            direction += 45.0F;
         }
      } else if (strafe > 0.0F) {
         direction -= 90.0F;
      } else if (strafe < 0.0F) {
         direction += 90.0F;
      }

      direction += player.rotationYaw;
      return MathHelper.wrapAngleTo180_float(direction);
   }

   public static boolean isOnStairs() {
      return EntityUtils.getBlockUnder(PlayerUtils.getLocalPlayer(), 0.5D) instanceof BlockStairs;
   }

   public static boolean checkNoBlockAbove() {
      return !WorldUtils.getWorld().checkBlockCollision(PlayerUtils.getLocalPlayer().getEntityBoundingBox().addCoord(0.0D, 1.0D, 0.0D));
   }

   public static boolean isDistFromGround(double dist) {
      return WorldUtils.getWorld().checkBlockCollision(PlayerUtils.getLocalPlayer().getEntityBoundingBox().addCoord(0.0D, -dist, 0.0D));
   }

   public static boolean isInLiquid() {
      return PlayerUtils.getLocalPlayer().isInWater() || PlayerUtils.getLocalPlayer().isInLava();
   }

   public static boolean isOverVoid() {
      for(double posY = PlayerUtils.getLocalPlayer().posY; posY > 0.0D; --posY) {
         if (!(WorldUtils.getWorld().getBlockState(new BlockPos(PlayerUtils.getLocalPlayer().posX, posY, PlayerUtils.getLocalPlayer().posZ)).getBlock() instanceof BlockAir)) {
            return false;
         }
      }

      return true;
   }

   public static double getJumpHeight() {
      return getJumpHeight(0.41999998688697815D);
   }

   public static double getJumpHeight(double baseJumpHeight) {
      return baseJumpHeight + (double)((float)getJumpBoostModifier() * 0.1F);
   }

   public static double getMinFallDist() {
      double baseFallDist = 3.0D;
      baseFallDist += (double)getJumpBoostModifier();
      return baseFallDist;
   }

   public static double calculateFriction(double moveSpeed, double lastDist, double baseMoveSpeed) {
      frictionValues.set(0, lastDist - lastDist / 159.999D);
      frictionValues.set(1, lastDist - (moveSpeed - lastDist) / 33.3D);
      double materialFriction = PlayerUtils.getLocalPlayer().isInWater() ? 0.8899999856948853D : (PlayerUtils.getLocalPlayer().isInLava() ? 0.5350000262260437D : 0.9800000190734863D);
      frictionValues.set(2, lastDist - baseMoveSpeed * (1.0D - materialFriction));
      return (Double)Collections.min(frictionValues);
   }

   public static boolean isOnIce() {
      Block blockUnder = EntityUtils.getBlockUnder(PlayerUtils.getLocalPlayer(), 1.0D);
      return blockUnder instanceof BlockIce || blockUnder instanceof BlockPackedIce;
   }

   public static double getBlockHeight() {
      return PlayerUtils.getLocalPlayer().posY - (double)((int)PlayerUtils.getLocalPlayer().posY);
   }

   public static boolean canSprint(boolean omni) {
      boolean var10000;
      label34: {
         EntityPlayerSP player = PlayerUtils.getLocalPlayer();
         if (omni) {
            if (!isMovingEnoughForSprint()) {
               break label34;
            }
         } else if (!(player.movementInput.moveForward >= 0.8F)) {
            break label34;
         }

         if (!player.isCollidedHorizontally && (player.getFoodStats().getFoodLevel() > 6 || player.capabilities.allowFlying) && !player.isSneaking() && (!player.isUsingItem() || Massacre.INSTANCE.getModuleManager().getModule("No Slow Down").getState()) && !player.isPotionActive(Potion.moveSlowdown.id)) {
            var10000 = true;
            return var10000;
         }
      }

      var10000 = false;
      return var10000;
   }

   public static double getBaseMoveSpeed() {
      EntityPlayerSP player = PlayerUtils.getLocalPlayer();
      double base = player.isSneaking() ? 0.06630000288486482D : (canSprint(true) ? 0.2872999905467033D : 0.22100000083446503D);
      int moveSpeedAmp = EntityUtils.getPotionModifier(player, Potion.moveSpeed);
      if (moveSpeedAmp > 0) {
         base *= 1.0D + 0.2D * (double)moveSpeedAmp;
      }

      if (player.isInWater()) {
         base *= 0.5203619984250619D;
         int depthStriderLevel = getDepthStriderLevel();
         if (depthStriderLevel > 0) {
            base *= DEPTH_STRIDER_VALUES[depthStriderLevel];
         }
      } else if (player.isInLava()) {
         base *= 0.5203619984250619D;
      }

      return base;
   }

   public static void setSpeed(double speed) {
      EntityPlayerSP player = mc.thePlayer;
      if (player.isMoving()) {
         TargetStrafe targetStrafe = (TargetStrafe)Massacre.INSTANCE.getModuleManager().getModule(TargetStrafe.class);
         KillAura2 killAura = (KillAura2)Massacre.INSTANCE.getModuleManager().getModule(KillAura2.class);
         if (targetStrafe.getState() && (!(Boolean)targetStrafe.holdspace.getValue() || Keyboard.isKeyDown(57))) {
            EntityLivingBase target = killAura.target;
            if (target != null) {
               float dist = mc.thePlayer.getDistanceToEntity(target);
               double radius = (Double)targetStrafe.radius.getValue();
               setSpeed(speed, (double)dist <= radius + 1.0E-4D ? 0.0F : 1.0F, (double)dist <= radius + 1.0D ? (float)targetStrafe.direction : 0.0F, RotationUtil.getYawToEntity(target));
               return;
            }
         }

         setSpeed(speed, player.moveForward, player.moveStrafing, player.rotationYaw);
      }

   }

   public static void setSpeed(double speed, float forward, float strafing, float yaw) {
      boolean reversed = forward < 0.0F;
      float strafingYaw = 90.0F * (forward > 0.0F ? 0.5F : (reversed ? -0.5F : 1.0F));
      if (reversed) {
         yaw += 180.0F;
      }

      if (strafing > 0.0F) {
         yaw -= strafingYaw;
      } else if (strafing < 0.0F) {
         yaw += strafingYaw;
      }

      double x = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
      double z = Math.cos(Math.toRadians((double)yaw));
      mc.thePlayer.motionX = x * speed;
      mc.thePlayer.motionZ = z * speed;
   }

   public static void setSpeed(EventMove e, double speed) {
      EntityPlayerSP player = mc.thePlayer;
      if (player.isMoving()) {
         TargetStrafe targetStrafe = (TargetStrafe)Massacre.INSTANCE.getModuleManager().getModule(TargetStrafe.class);
         KillAura2 killAura = (KillAura2)Massacre.INSTANCE.getModuleManager().getModule(KillAura2.class);
         if (targetStrafe.getState() && (!(Boolean)targetStrafe.holdspace.getValue() || Keyboard.isKeyDown(57))) {
            EntityLivingBase target = killAura.target;
            if (target != null) {
               float dist = mc.thePlayer.getDistanceToEntity(target);
               double radius = (Double)targetStrafe.radius.getValue();
               setSpeed(e, speed, (double)dist <= radius + 1.0E-4D ? 0.0F : 1.0F, (double)dist <= radius + 1.0D ? (float)targetStrafe.direction : 0.0F, RotationUtil.getYawToEntity(target));
               return;
            }
         }

         setSpeed(e, speed, player.moveForward, player.moveStrafing, player.rotationYaw);
      }

   }

   public static void setSpeed(EventMove e, double speed, float forward, float strafing, float yaw) {
      boolean reversed = forward < 0.0F;
      float strafingYaw = 90.0F * (forward > 0.0F ? 0.5F : (reversed ? -0.5F : 1.0F));
      if (reversed) {
         yaw += 180.0F;
      }

      if (strafing > 0.0F) {
         yaw -= strafingYaw;
      } else if (strafing < 0.0F) {
         yaw += strafingYaw;
      }

      double x = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
      double z = Math.cos(Math.toRadians((double)yaw));
      e.setX(x * speed);
      e.setZ(z * speed);
   }

   public static boolean isOnGround() {
      return EntityUtils.isOnGround(PlayerUtils.getLocalPlayer());
   }

   public static boolean isMoving() {
      return PlayerUtils.getLocalPlayer().movementInput.moveForward != 0.0F || PlayerUtils.getLocalPlayer().movementInput.moveStrafe != 0.0F;
   }

   public static int getDepthStriderLevel() {
      return EnchantmentHelper.getDepthStriderModifier(PlayerUtils.getLocalPlayer());
   }
}
