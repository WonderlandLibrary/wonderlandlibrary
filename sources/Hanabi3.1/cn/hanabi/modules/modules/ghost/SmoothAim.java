package cn.hanabi.modules.modules.ghost;

import cn.hanabi.events.EventPreMotion;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.modules.world.AntiBot;
import cn.hanabi.modules.modules.world.Teams;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.utils.rotation.RotationUtil;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class SmoothAim extends Mod {
   public static Value range = new Value("AimAssist", "Range", 4.5, 0.0, 10.0);
   public static Value attackPlayers = new Value("AimAssist", "Players", true);
   public static Value attackAnimals = new Value("AimAssist", "Animals", false);
   public static Value attackMobs = new Value("AimAssist", "Mobs", false);
   public static Value throughblock = new Value("AimAssist", "ThroughBlock", true);
   public static Value invisible = new Value("AimAssist", "Invisibles", false);
   public static Value weapon = new Value("AimAssist", "OnlyWeapon", false);
   public static Value rotation2Value = new Value("AimAssist", "Yaw Offset", 200.0, 0.0, 400.0, 10.0);
   public static Value rotationValue = new Value("AimAssist", "Pitch Offset", 200.0, 0.0, 400.0, 10.0);
   public static Value randoms2 = new Value("AimAssist", "Pitch Random", true);
   public static ArrayList targets = new ArrayList();
   public static EntityLivingBase target = null;
   public Value speed = new Value("AimAssist", "Speed", 0.1, 0.0, 1.0, 0.01);
   public Value horizontal = new Value("AimAssist", "Horizontal", 4.0, 0.0, 10.0, 0.1);
   public Value vertical = new Value("AimAssist", "Vertical", 2.0, 0.0, 10.0, 0.1);
   public Value switchsize = new Value("AimAssist", "Max Targets", 1.0, 1.0, 5.0, 1.0);
   public Value switchDelay = new Value("AimAssist", "Switch Delay", 50.0, 0.0, 2000.0, 10.0);
   public Value clickAim = new Value("AimAssist", "Click Aim", false);
   public Value strafe = new Value("AimAssist", "Strafe Increase", false);
   public int index;
   float[] lastRotations;
   private final TimeHelper switchTimer = new TimeHelper();

   public SmoothAim() {
      super("AimAssist", Category.GHOST);
   }

   public static float[] getNeededRotations(Vec3 vec) {
      Vec3 playerVector = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
      double y = vec.yCoord - playerVector.yCoord;
      double x = vec.xCoord - playerVector.xCoord;
      double z = vec.zCoord - playerVector.zCoord;
      double dff = Math.sqrt(x * x + z * z);
      float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0F;
      float pitch = (float)(-Math.toDegrees(Math.atan2(y, dff)));
      return new float[]{MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch)};
   }

   public static Vec3 getCenter(AxisAlignedBB bb) {
      double value = Math.random();
      return new Vec3(bb.minX + (bb.maxX - bb.minX) * ((Double)rotation2Value.getValue() / 400.0), bb.minY + (bb.maxY - bb.minY) * ((Boolean)randoms2.getValue() ? value : (Double)rotationValue.getValue() / 400.0), bb.minZ + (bb.maxZ - bb.minZ) * ((Double)rotation2Value.getValue() / 400.0));
   }

   private static boolean isValidEntity(Entity entity) {
      if (entity instanceof EntityLivingBase) {
         if (entity.isDead || ((EntityLivingBase)entity).getHealth() <= 0.0F) {
            return false;
         }

         if ((double)mc.thePlayer.getDistanceToEntity(entity) < (Double)range.getValueState() && entity != mc.thePlayer && !mc.thePlayer.isDead && !(entity instanceof EntityArmorStand) && !(entity instanceof EntitySnowman)) {
            if (entity instanceof EntityPlayer && (Boolean)attackPlayers.getValueState()) {
               if (!mc.thePlayer.canEntityBeSeen(entity) && !(Boolean)throughblock.getValueState()) {
                  return false;
               }

               if (entity.isInvisible() && !(Boolean)invisible.getValueState()) {
                  return false;
               }

               return !AntiBot.isBot(entity) && !Teams.isOnSameTeam(entity);
            }

            if (entity instanceof EntityMob && (Boolean)attackMobs.getValueState()) {
               return !AntiBot.isBot(entity);
            }

            if ((entity instanceof EntityAnimal || entity instanceof EntityVillager) && (Boolean)attackAnimals.getValueState()) {
               return !AntiBot.isBot(entity);
            }
         }
      }

      return false;
   }

   @EventTarget
   public void onPre(EventPreMotion event) {
      if (mc.theWorld != null) {
         if (mc.thePlayer != null) {
            if (mc.thePlayer.isEntityAlive()) {
               if (!(Boolean)this.clickAim.getValueState() || mc.gameSettings.keyBindAttack.isKeyDown()) {
                  if (this.holdWeapon()) {
                     if (!targets.isEmpty() && this.index >= targets.size()) {
                        this.index = 0;
                     }

                     Iterator var2 = targets.iterator();

                     while(var2.hasNext()) {
                        EntityLivingBase ent = (EntityLivingBase)var2.next();
                        if (!isValidEntity(ent)) {
                           targets.remove(ent);
                        }
                     }

                     this.getTarget(event);
                     if (targets.size() == 0) {
                        target = null;
                     } else {
                        target = (EntityLivingBase)targets.get(this.index);
                        if ((double)mc.thePlayer.getDistanceToEntity(target) > (Double)range.getValueState()) {
                           target = (EntityLivingBase)targets.get(0);
                        }
                     }

                     if (target != null) {
                        if (target.hurtTime == 10 && this.switchTimer.isDelayComplete((Double)this.switchDelay.getValueState() * 1000.0) && targets.size() > 1) {
                           this.switchTimer.reset();
                           ++this.index;
                        }

                        this.lastRotations = getNeededRotations(getCenter(((EntityLivingBase)targets.get(this.index)).getEntityBoundingBox()));
                        if (this.lastRotations[1] > 90.0F) {
                           this.lastRotations[1] = 90.0F;
                        } else if (this.lastRotations[1] < -90.0F) {
                           this.lastRotations[1] = -90.0F;
                        }

                        if (target != null) {
                           double horizontalSpeed = (Double)this.horizontal.getValue() * 3.0 + ((Double)this.horizontal.getValue() > 0.0 ? rand.nextDouble() : 0.0);
                           double verticalSpeed = (Double)this.vertical.getValue() * 3.0 + ((Double)this.vertical.getValue() > 0.0 ? rand.nextDouble() : 0.0);
                           if ((Boolean)this.strafe.getValue() && mc.thePlayer.moveStrafing != 0.0F) {
                              horizontalSpeed *= 1.25;
                           }

                           if (target != null) {
                              horizontalSpeed *= (Double)this.speed.getValue();
                              verticalSpeed *= (Double)this.speed.getValue();
                           }

                           this.faceTarget(target, 0.0F, (float)verticalSpeed);
                           this.faceTarget(target, (float)horizontalSpeed, 0.0F);
                        }
                     } else {
                        targets.clear();
                        this.lastRotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
                     }

                  }
               }
            }
         }
      }
   }

   private void getTarget(EventPreMotion event) {
      int maxSize = ((Double)this.switchsize.getValueState()).intValue();
      Iterator var3 = mc.theWorld.loadedEntityList.iterator();

      while(var3.hasNext()) {
         Entity o3 = (Entity)var3.next();
         EntityLivingBase curEnt;
         if (o3 instanceof EntityLivingBase && isValidEntity(curEnt = (EntityLivingBase)o3) && !targets.contains(curEnt)) {
            targets.add(curEnt);
         }

         if (targets.size() >= maxSize) {
            break;
         }
      }

      targets.sort((o1, o2) -> {
         float[] rot1 = RotationUtil.getRotationToEntity(o1);
         float[] rot2 = RotationUtil.getRotationToEntity(o2);
         return (int)(mc.thePlayer.rotationYaw - rot1[0] - (mc.thePlayer.rotationYaw - rot2[0]));
      });
   }

   public boolean holdWeapon() {
      if (!(Boolean)weapon.getValue()) {
         return true;
      } else {
         Minecraft mc = Minecraft.getMinecraft();
         if (mc.thePlayer.getCurrentEquippedItem() == null) {
            return false;
         } else {
            return mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword || mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemTool;
         }
      }
   }

   protected float getRotation(float currentRotation, float targetRotation, float maxIncrement) {
      float deltaAngle = MathHelper.wrapAngleTo180_float(targetRotation - currentRotation);
      if (deltaAngle > maxIncrement) {
         deltaAngle = maxIncrement;
      }

      if (deltaAngle < -maxIncrement) {
         deltaAngle = -maxIncrement;
      }

      return currentRotation + deltaAngle / 2.0F;
   }

   private void faceTarget(Entity target, float yawspeed, float pitchspeed) {
      EntityPlayerSP player = mc.thePlayer;
      float yaw = this.lastRotations[0];
      float pitch = this.lastRotations[1];
      player.rotationYaw = this.getRotation(player.rotationYaw, yaw, yawspeed);
      player.rotationPitch = this.getRotation(player.rotationPitch, pitch, pitchspeed);
   }
}
