package org.alphacentauri.modules;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.MoveUtils;
import org.alphacentauri.management.util.RotationUtils;
import org.alphacentauri.modules.ModuleKillAura;

public class ModuleBowAimBot extends Module implements EventListener {
   private Property silent = new Property(this, "Silent", Boolean.valueOf(true));
   private Property predict = new Property(this, "Predict", Boolean.valueOf(true));
   private Property predictionAccuracy = new Property(this, "PredictionAccuracy", Integer.valueOf(1));
   private boolean isSelectingTarget;
   private EntityLivingBase target;

   public ModuleBowAimBot() {
      super("BowAimBot", "Easy sniping!", new String[]{"bowaim", "bowaimbot"}, Module.Category.Combat, 6097215);
   }

   private int getFlightTicks(float pitch1, float yaw1, double dist) {
      EntityPlayerSP player = AC.getMC().getPlayer();
      double arrowPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)AC.getMC().timer.renderPartialTicks - (double)(MathHelper.cos((float)Math.toRadians((double)yaw1)) * 0.16F);
      double arrowPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)AC.getMC().timer.renderPartialTicks + (double)player.getEyeHeight() - 0.1D;
      double arrowPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)AC.getMC().timer.renderPartialTicks - (double)(MathHelper.sin((float)Math.toRadians((double)yaw1)) * 0.16F);
      double startX = arrowPosX;
      double stattZ = arrowPosZ;
      float yaw = (float)Math.toRadians((double)yaw1);
      float pitch = (float)Math.toRadians((double)pitch1);
      float arrowMotionX = -MathHelper.sin(yaw) * MathHelper.cos(pitch);
      float arrowMotionY = -MathHelper.sin(pitch);
      float arrowMotionZ = MathHelper.cos(yaw) * MathHelper.cos(pitch);
      double arrowMotion = Math.sqrt((double)(arrowMotionX * arrowMotionX + arrowMotionY * arrowMotionY + arrowMotionZ * arrowMotionZ));
      arrowMotionX = (float)((double)arrowMotionX / arrowMotion);
      arrowMotionY = (float)((double)arrowMotionY / arrowMotion);
      arrowMotionZ = (float)((double)arrowMotionZ / arrowMotion);
      float bowPower = (float)(72000 - player.getItemInUseCount()) / 20.0F;
      bowPower = (bowPower * bowPower + bowPower * 2.0F) / 3.0F;
      if(bowPower > 1.0F) {
         bowPower = 1.0F;
      }

      if(bowPower <= 0.1F) {
         bowPower = 1.0F;
      }

      bowPower = bowPower * 3.0F;
      arrowMotionX = arrowMotionX * bowPower;
      arrowMotionY = arrowMotionY * bowPower;
      arrowMotionZ = arrowMotionZ * bowPower;

      for(int i = 0; i < 1000; ++i) {
         arrowPosX += (double)arrowMotionX;
         arrowPosY += (double)arrowMotionY;
         arrowPosZ += (double)arrowMotionZ;
         double dX = arrowPosX - startX;
         double dZ = arrowPosZ - stattZ;
         if(dX * dX + dZ * dZ >= dist) {
            return i;
         }

         arrowMotionX = (float)((double)arrowMotionX * 0.99D);
         arrowMotionY = (float)((double)arrowMotionY * 0.99D);
         arrowMotionZ = (float)((double)arrowMotionZ * 0.99D);
         arrowMotionY = (float)((double)arrowMotionY - 0.05D);
      }

      return -1;
   }

   private void selectTarget() {
      if(!this.isSelectingTarget) {
         AC.getThreadPool().execute(() -> {
            this.isSelectingTarget = true;

            try {
               ArrayList<EntityLivingBase> copy = new ArrayList();
               copy.addAll(ModuleKillAura.getInstance().targets);
               ArrayList<EntityLivingBase> hitable = (ArrayList)copy.stream().filter((entityLivingBase) -> {
                  return AC.getMC().getPlayer().canEntityBeSeen(entityLivingBase);
               }).sorted((o1, o2) -> {
                  return (int)(ModuleKillAura.getInstance().getDistFromMouseSq(o1) - ModuleKillAura.getInstance().getDistFromMouseSq(o2));
               }).collect(Collectors.toCollection(ArrayList::<init>));
               if(hitable.size() > 0) {
                  this.target = (EntityLivingBase)hitable.get(0);
               } else {
                  this.target = null;
               }
            } catch (Exception var3) {
               ;
            }

            this.isSelectingTarget = false;
         });
      }
   }

   private float theta(double v, double g, double x, double y) {
      double yv = 2.0D * y * v * v;
      double gx = g * x * x;
      double g2 = g * (gx + yv);
      double insqrt = v * v * v * v - g2;
      double sqrt = Math.sqrt(insqrt);
      double numerator = v * v + sqrt;
      double numerator2 = v * v - sqrt;
      double atan1 = Math.atan2(numerator, g * x);
      double atan2 = Math.atan2(numerator2, g * x);
      return (float)Math.min(atan1, atan2);
   }

   private double getLaunchAngle(EntityLivingBase targetEntity, Vec3 pos, double v, double g) {
      EntityPlayerSP player = AC.getMC().getPlayer();
      double yDif = pos.yCoord + (double)(targetEntity.getEyeHeight() / 2.0F) - (player.posY + (double)player.getEyeHeight());
      double xDif = pos.xCoord - player.posX;
      double zDif = pos.zCoord - player.posZ;
      double xCoord = Math.sqrt(xDif * xDif + zDif * zDif);
      return (double)this.theta(v, g, xCoord, yDif);
   }

   private float getLaunchAngle(EntityLivingBase targetEntity, double v, double g) {
      EntityPlayerSP player = AC.getMC().getPlayer();
      double yDif = targetEntity.posY + (double)(targetEntity.getEyeHeight() / 2.0F) - (player.posY + (double)player.getEyeHeight());
      double xDif = targetEntity.posX - player.posX;
      double zDif = targetEntity.posZ - player.posZ;
      double xCoord = Math.sqrt(xDif * xDif + zDif * zDif);
      return this.theta(v, g, xCoord, yDif);
   }

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         ModuleKillAura.getInstance().updatePossibleTargets();
         this.selectTarget();
         EntityPlayerSP player = AC.getMC().getPlayer();
         if(player.getItemInUseDuration() == 0) {
            return;
         }

         if(player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemBow)) {
            return;
         }

         int use = player.getItemInUseDuration();
         float progress = (float)use / 20.0F;
         progress = (progress * progress + progress * 2.0F) / 3.0F;
         progress = MathHelper.clamp_float(progress, 0.0F, 1.0F);
         if(this.target == null) {
            return;
         }

         double v = (double)(progress * 3.0F);
         double g = 0.05000000074505806D;
         float pitch = (float)(-Math.toDegrees((double)this.getLaunchAngle(this.target, v, g)));
         if(Double.isNaN((double)pitch)) {
            return;
         }

         Vec3 pos;
         if(!((Boolean)this.predict.value).booleanValue()) {
            pos = this.target.getPositionVector();
         } else {
            pos = this.target.getPositionVector();

            for(int i = 0; i < Math.min(1, ((Integer)this.predictionAccuracy.value).intValue()); ++i) {
               double dist = player.getDistanceSq(pos.xCoord, pos.yCoord, pos.zCoord);
               double difX = pos.xCoord - player.posX;
               double difZ = pos.zCoord - player.posZ;
               float yaw = (float)(Math.atan2(difZ, difX) * 180.0D / 3.141592653589793D) - 90.0F;
               pitch = (float)(-Math.toDegrees(this.getLaunchAngle(this.target, pos, v, g)));
               if(Double.isNaN((double)pitch)) {
                  return;
               }

               int ticks = this.getFlightTicks(pitch, yaw, dist);
               double[] moveToLoc = MoveUtils.getMoveToLoc(this.target, ticks);
               pos = new Vec3(moveToLoc[0], moveToLoc[1], moveToLoc[2]);
            }
         }

         double difX = pos.xCoord - player.posX;
         double difZ = pos.zCoord - player.posZ;
         float yaw = (float)(Math.atan2(difZ, difX) * 180.0D / 3.141592653589793D) - 90.0F;
         pitch = (float)(-Math.toDegrees(this.getLaunchAngle(this.target, pos, v, g)));
         if(!((Boolean)this.silent.value).booleanValue()) {
            player.rotationYaw = yaw;
            player.rotationPitch = pitch;
         } else if(!RotationUtils.isSet() || !(RotationUtils.getSetBy() instanceof ModuleKillAura)) {
            RotationUtils.set(this, yaw, pitch);
         }
      }

   }
}
