package cn.hanabi.modules.modules.movement.Speed;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.events.EventJump;
import cn.hanabi.events.EventLoop;
import cn.hanabi.events.EventMove;
import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventPostMotion;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.events.EventPullback;
import cn.hanabi.events.EventStep;
import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.combat.KillAura;
import cn.hanabi.modules.modules.combat.TargetStrafe;
import cn.hanabi.utils.MathUtils;
import cn.hanabi.utils.MoveUtils;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;

@ObfuscationClass
public class Speed_Hypixel {
   public static Value Friction = (new Value("Speed", "Friction Mode", 0)).LoadValue(new String[]{"Normal", "Fix"});
   public static Value boostMode = (new Value("Speed", "Boost Mode", 0)).LoadValue(new String[]{"Normal", "Boost"});
   public static Value motion = (new Value("Speed", "Motion", 0)).LoadValue(new String[]{"Normal", "New", "LowHop"});
   public static Value damageBoost = new Value("Speed", "DMG Boost Value", 0.39, 0.01, 1.0, 0.01);
   public static Value speedchange = new Value("Speed", "Bhop Speed", 1.0, 0.8, 1.0, 0.01);
   public static Value glideValue = new Value("Speed", "Glide Stage", 0.1, 0.0, 0.4, 0.01);
   public static TimeHelper ticks = new TimeHelper();
   public Value boost = new Value("Speed", "DMG Boost", true);
   public Value tp = new Value("Speed", "Tp-Motion", true);
   public Value groundspoof = new Value("Speed", "GroundSpoof", true);
   Minecraft mc = Minecraft.getMinecraft();
   double y;
   private double speed;
   private int stage;
   float yaw;
   int failTimes;
   private double lastDist;

   public static double defaultSpeed() {
      double baseSpeed = 0.2887;
      if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
         int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
         baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
      }

      return baseSpeed;
   }

   public static void setMotion(EventMove em, double speed) {
      Minecraft mc = Minecraft.getMinecraft();
      double forward = (double)mc.thePlayer.movementInput.moveForward;
      double strafe = (double)mc.thePlayer.movementInput.moveStrafe;
      float yaw = mc.thePlayer.rotationYaw;
      if (forward == 0.0 && strafe == 0.0) {
         em.setX(0.0);
         em.setZ(0.0);
      } else {
         if (forward != 0.0) {
            if (strafe > 0.0) {
               yaw += (float)(forward > 0.0 ? -45 : 45);
            } else if (strafe < 0.0) {
               yaw += (float)(forward > 0.0 ? 45 : -45);
            }

            strafe = 0.0;
            if (forward > 0.0) {
               forward = 1.0;
            } else if (forward < 0.0) {
               forward = -1.0;
            }
         }

         em.setX(forward * speed * Math.cos(Math.toRadians((double)(yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((double)(yaw + 90.0F))));
         em.setZ(forward * speed * Math.sin(Math.toRadians((double)(yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((double)(yaw + 90.0F))));
      }

   }

   public static double getRandomInRange(double min, double max) {
      Random random = new Random();
      double range = max - min;
      double scaled = random.nextDouble() * range;
      if (scaled > max) {
         scaled = max;
      }

      double shifted = scaled + min;
      if (shifted > max) {
         shifted = max;
      }

      return shifted;
   }

   public static boolean isOnIce() {
      Block blockUnder = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(Minecraft.getMinecraft().thePlayer.posX, StrictMath.floor(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minY) - 1.0, Minecraft.getMinecraft().thePlayer.posZ)).getBlock();
      return blockUnder instanceof BlockIce || blockUnder instanceof BlockPackedIce;
   }

   public void onStep(EventStep event) {
   }

   public void onLoop(EventLoop event) {
   }

   public void onPre(EventPreMotion e) {
      double xDist = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
      double zDist = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
      this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
      if (motion.isCurrentMode("OnGround")) {
         EntityPlayerSP var10000 = this.mc.thePlayer;
         var10000.posY -= this.mc.thePlayer.posY - this.mc.thePlayer.lastTickPosY;
         var10000 = this.mc.thePlayer;
         var10000.lastTickPosY -= this.mc.thePlayer.posY - this.mc.thePlayer.lastTickPosY;
      }

      if (this.failTimes > 0) {
         --this.failTimes;
      }

      float yaw = (float)(Math.toDegrees(Math.atan2(zDist, xDist)) - 90.0);
      if (Math.abs(this.yaw - yaw) >= 45.0F) {
         this.failTimes = 5;
      }

      this.yaw = yaw;
      if (MoveUtils.isMoving() && !e.isOnGround() && this.mc.thePlayer.motionY > 0.06 && (Boolean)this.groundspoof.getValue()) {
         e.setOnGround(true);
      }

   }

   public void onPost(EventPostMotion e) {
   }

   public void onJump(EventJump e) {
      if (motion.isCurrentMode("OnGround")) {
         e.setCancelled(true);
      }

   }

   public void onPacket(EventPacket e) {
   }

   public void onMove(EventMove event) {
      double rounded = MathUtils.round(this.mc.thePlayer.posY - (double)((int)this.mc.thePlayer.posY), 3.0);
      KillAura killAura = (KillAura)ModManager.getModule(KillAura.class);
      TargetStrafe targetStrafe = (TargetStrafe)ModManager.getModule(TargetStrafe.class);
      if (motion.isCurrentMode("LowHop") || motion.isCurrentMode("OnGround")) {
         if (rounded == MathUtils.round(0.4, 3.0)) {
            event.y = this.mc.thePlayer.motionY = 0.31;
         } else if (rounded == MathUtils.round(0.71, 3.0)) {
            event.y = this.mc.thePlayer.motionY = 0.04;
         } else if (rounded == MathUtils.round(0.75, 3.0)) {
            event.y = this.mc.thePlayer.motionY = -0.2;
         } else if (rounded == MathUtils.round(0.55, 3.0)) {
            event.y = this.mc.thePlayer.motionY = -0.14;
         } else if (rounded == MathUtils.round(0.41, 3.0)) {
            event.y = this.mc.thePlayer.motionY = -0.2;
         }
      }

      if (this.stage > 0) {
         if (this.stage == 1 && this.mc.thePlayer.onGround && MoveUtils.isMoving()) {
            ++this.stage;
         }

         if (this.stage == 2 && this.mc.thePlayer.onGround && MoveUtils.isMoving()) {
            if ((Boolean)this.tp.getValue()) {
               this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.001 * Math.random(), this.mc.thePlayer.posZ);
            }

            this.y = ThreadLocalRandom.current().nextDouble(0.39999998688698, 0.4000199999);
            event.setY(this.mc.thePlayer.motionY = this.y + (double)PlayerUtil.getJumpEffect() * 0.1);
         } else if (this.stage >= 3) {
            if (motion.isCurrentMode("New") && this.stage == 6) {
               this.mc.thePlayer.motionY = -(this.y - 0.310999999999998);
            }

            if (this.mc.thePlayer.isCollidedVertically) {
               this.speed = this.getBaseSpeed();
               this.lastDist = 0.0;
               this.stage = 0;
            }
         }
      } else {
         this.stage = 0;
      }

      if (event.y < 0.0) {
         event.y *= 1.0 - (Double)glideValue.getValue();
      }

      if (boostMode.isCurrentMode("Boost")) {
         this.onHypixelSpeed();
      } else {
         this.getHypixelBest();
      }

      ++this.stage;
      double add = this.mc.thePlayer.isBurning() ? 0.0 : (this.mc.thePlayer.hurtResistantTime < 8 ? (double)this.mc.thePlayer.hurtResistantTime * (Double)damageBoost.getValue() * 0.008 : (double)this.mc.thePlayer.hurtResistantTime * (Double)damageBoost.getValue() * 0.01);
      this.speed *= 1.0 + ((Boolean)this.boost.getValue() ? add : 0.0);
      if (this.failTimes > 0 && this.failTimes % 2 == 0) {
         this.speed *= 0.9;
      }

      if (MoveUtils.isMoving()) {
         if (targetStrafe.isStrafing(event, KillAura.target, this.speed)) {
            setMotion(event, this.speed);
         }
      } else {
         setMotion(event, 0.0);
         this.stage = 0;
      }

   }

   public boolean isMoving2() {
      return this.mc.thePlayer.moveForward != 0.0F || this.mc.thePlayer.moveStrafing != 0.0F;
   }

   private boolean canSpeed() {
      Block blockBelow = this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ)).getBlock();
      return this.mc.thePlayer.onGround && !this.mc.gameSettings.keyBindJump.isPressed() && blockBelow != Blocks.stone_stairs && blockBelow != Blocks.oak_stairs && blockBelow != Blocks.sandstone_stairs && blockBelow != Blocks.nether_brick_stairs && blockBelow != Blocks.spruce_stairs && blockBelow != Blocks.stone_brick_stairs && blockBelow != Blocks.birch_stairs && blockBelow != Blocks.jungle_stairs && blockBelow != Blocks.acacia_stairs && blockBelow != Blocks.brick_stairs && blockBelow != Blocks.dark_oak_stairs && blockBelow != Blocks.quartz_stairs && blockBelow != Blocks.red_sandstone_stairs && this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 2.0, this.mc.thePlayer.posZ)).getBlock() == Blocks.air;
   }

   public boolean isOnGround(double height) {
      return !this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
   }

   public void onHypixelSpeed() {
      this.speed = this.getBaseSpeed();
      if (this.stage < 1) {
         ++this.stage;
         this.lastDist = 0.0;
      }

      int amplifier = -1;
      if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
         amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
      }

      if (this.stage == 2 && (this.mc.thePlayer.moveForward != 0.0F || this.mc.thePlayer.moveStrafing != 0.0F) && this.mc.thePlayer.isCollidedVertically && this.mc.thePlayer.onGround) {
         this.speed *= 1.8125 + (0.041 + ThreadLocalRandom.current().nextDouble(0.005) / 10.0) * (double)(amplifier + 1);
         this.speed *= (Double)speedchange.getValue();
      } else if (this.stage == 3) {
         float diff = (float)(0.72 * (this.lastDist - this.getBaseSpeed() * (isOnIce() ? 1.1 : 1.0)));
         this.speed = this.lastDist - (double)diff;
      } else {
         if ((this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically) && this.stage > 0) {
            this.stage = this.mc.thePlayer.moveForward == 0.0F && this.mc.thePlayer.moveStrafing == 0.0F ? 0 : 1;
         }

         if (Friction.isCurrentMode("Normal")) {
            this.speed = this.lastDist - this.lastDist / (isOnIce() ? 91.0 : 99.00000014353486);
         } else if (Friction.isCurrentMode("Fix")) {
            this.speed = MoveUtils.calculateFriction(this.speed, this.lastDist, this.getBaseSpeed());
         }
      }

      this.speed = Math.max(this.speed, this.getBaseSpeed());
   }

   public void onPullback(EventPullback event) {
   }

   public void onEnable() {
      this.lastDist = 0.0;
      this.speed = defaultSpeed();
      this.stage = 2;
   }

   public void onDisable() {
      this.mc.thePlayer.stepHeight = 0.5F;
   }

   private double getBaseSpeed() {
      EntityPlayerSP player = this.mc.thePlayer;
      double base = 0.2895;
      PotionEffect moveSpeed = player.getActivePotionEffect(Potion.moveSpeed);
      PotionEffect moveSlowness = player.getActivePotionEffect(Potion.moveSlowdown);
      if (moveSpeed != null) {
         base *= 1.0 + 0.19 * (double)(moveSpeed.getAmplifier() + 1);
      }

      if (moveSlowness != null) {
         base *= 1.0 - 0.13 * (double)(moveSlowness.getAmplifier() + 1);
      }

      if (player.isInWater()) {
         base *= 0.5203619984250619;
         int depthStriderLevel = EnchantmentHelper.getDepthStriderModifier(this.mc.thePlayer);
         if (depthStriderLevel > 0) {
            double[] DEPTH_STRIDER_VALUES = new double[]{1.0, 1.4304347400741908, 1.7347825295420374, 1.9217391028296074};
            base *= DEPTH_STRIDER_VALUES[depthStriderLevel];
         }
      } else if (player.isInLava()) {
         base *= 0.5203619984250619;
      }

      return base;
   }

   private void getHypixelBest() {
      EntityPlayer player = this.mc.thePlayer;
      if (player != null) {
         switch (this.stage) {
            case 1:
               break;
            case 2:
               if (player.onGround && MoveUtils.isMoving()) {
                  EntityPlayerSP var10000 = this.mc.thePlayer;
                  var10000.motionY += 0.005;
                  this.speed *= 1.9;
               }

               this.speed *= (Double)speedchange.getValue();
               break;
            case 3:
               this.speed += (new Random()).nextDouble() / 4800.0;
               double difference = 0.66 * (this.lastDist - this.getBaseSpeed());
               this.speed = this.lastDist - difference;
               PlayerUtil.debugChat(this.speed);
               break;
            default:
               if ((this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically) && this.stage > 0) {
                  this.stage = this.mc.thePlayer.moveForward == 0.0F && this.mc.thePlayer.moveStrafing == 0.0F ? 0 : 1;
               }

               if (Friction.isCurrentMode("Normal")) {
                  this.speed = this.lastDist - this.lastDist / 159.0;
               } else if (Friction.isCurrentMode("Fix")) {
                  this.speed = MoveUtils.calculateFriction(this.speed, this.lastDist, this.getBaseSpeed());
               }
         }

         this.speed = Math.max(this.speed - 0.02 * this.lastDist, this.getBaseSpeed());
      }
   }
}
