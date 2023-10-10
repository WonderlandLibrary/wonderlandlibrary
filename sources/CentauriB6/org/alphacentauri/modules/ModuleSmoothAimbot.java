package org.alphacentauri.modules;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventIsBot;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventRender3D;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.util.LatencyUtils;
import org.alphacentauri.management.util.MoveUtils;
import org.alphacentauri.management.util.Timer;
import org.alphacentauri.modules.ModuleTeam;

public class ModuleSmoothAimbot extends Module implements EventListener {
   private EntityLivingBase target;
   private Timer timer = new Timer();

   public ModuleSmoothAimbot() {
      super("SmoothAimbot", "Look .legit", new String[]{"smoothaimbot", "smoothaim"}, Module.Category.Combat, 6002406);
   }

   private float getDistFromMouseSq(EntityLivingBase entity) {
      EntityPlayerSP player = AC.getMC().getPlayer();
      if(player != null && entity != null) {
         double[] moveToLoc = MoveUtils.getMoveToLoc(entity, 2 + LatencyUtils.getLatencyTicks());
         double hitY;
         if(entity.posY > player.posY + (double)player.getEyeHeight()) {
            hitY = entity.posY + (double)(AC.getRandom().nextFloat() / 5.0F);
         } else if(entity.posY + (double)entity.getEyeHeight() < player.posY + (double)player.getEyeHeight()) {
            hitY = entity.posY + (double)entity.getEyeHeight() - (double)(AC.getRandom().nextFloat() / 5.0F);
         } else {
            hitY = player.posY + (double)player.getEyeHeight() * 0.75D;
         }

         double diffX = moveToLoc[0] - player.posX;
         double diffY = hitY - (player.posY + (double)player.getEyeHeight());
         double diffZ = moveToLoc[2] - player.posZ;
         double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
         float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
         float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
         float[] neededRotations = new float[]{player.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - player.rotationYaw), player.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - player.rotationPitch)};
         float neededYaw = player.rotationYaw - neededRotations[0];
         float neededPitch = player.rotationPitch - neededRotations[1];
         return neededYaw * neededYaw + neededPitch * neededPitch;
      } else {
         return 1337.0F;
      }
   }

   private boolean canHit(EntityPlayerSP player, EntityLivingBase entity) {
      return entity.getDistanceSqToEntity(player) < 14.44D && entity.getHealth() > 0.0F;
   }

   private EntityLivingBase findTarget() {
      EntityPlayerSP player = AC.getMC().getPlayer();
      float nearest = Float.MAX_VALUE;
      EntityLivingBase nearestEntity = null;

      for(EntityLivingBase entity : this.getTargets()) {
         if(entity != null && this.canHit(player, entity)) {
            float distanceFromMouse = this.getDistFromMouseSq(entity);
            if(distanceFromMouse < nearest) {
               nearest = distanceFromMouse;
               nearestEntity = entity;
            }
         }
      }

      this.target = nearestEntity;
      return nearestEntity;
   }

   private ArrayList getTargets() {
      return (ArrayList)AC.getMC().getWorld().playerEntities.stream().filter((entityPlayer) -> {
         return !((EventIsBot)(new EventIsBot(entityPlayer)).fire()).isBot() && !ModuleTeam.isinTeam(entityPlayer) && !AC.getFriendManager().isFriend(entityPlayer.getName());
      }).collect(Collectors.toCollection(ArrayList::<init>));
   }

   private boolean shouldGiveUp(EntityLivingBase target) {
      return AC.getMC().getPlayer().getDistanceSqToEntity(target) > 25.0D || target.isDead;
   }

   public void onEvent(Event event) {
      if(event instanceof EventRender3D) {
         if(this.timer.hasMSPassed(16L)) {
            EntityPlayerSP player = AC.getMC().getPlayer();
            if(player == null) {
               return;
            }

            if(this.target == null) {
               this.target = this.findTarget();
               if(this.target == null) {
                  return;
               }
            } else if(this.shouldGiveUp(this.target)) {
               this.target = this.findTarget();
               if(this.target == null) {
                  return;
               }
            }

            if(this.getDistFromMouseSq(this.target) < 100.0F) {
               return;
            }

            double[] moveToLoc = MoveUtils.getMoveToLoc(this.target, 2 + LatencyUtils.getLatencyTicks());
            double hitY;
            if(this.target.posY > player.posY + (double)player.getEyeHeight()) {
               hitY = this.target.posY + (double)(AC.getRandom().nextFloat() / 5.0F);
            } else if(this.target.posY + (double)this.target.getEyeHeight() < player.posY + (double)player.getEyeHeight()) {
               hitY = this.target.posY + (double)this.target.getEyeHeight() - (double)(AC.getRandom().nextFloat() / 5.0F);
            } else {
               hitY = player.posY + (double)player.getEyeHeight() * 0.75D;
            }

            double diffX = moveToLoc[0] - player.posX;
            double diffY = hitY - (player.posY + (double)player.getEyeHeight());
            double diffZ = moveToLoc[2] - player.posZ;
            double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
            float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
            float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
            float[] neededRotations = new float[]{player.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - player.rotationYaw), MathHelper.wrapAngleTo180_float(pitch)};
            float d0 = 0.0F - player.rotationYaw;
            float d0y = neededRotations[0] + d0;
            boolean rotateRight = d0y > 0.0F;
            if(rotateRight) {
               neededRotations[0] = player.rotationYaw + Math.min(Math.abs(0.0F - d0y), Math.min(Math.abs(0.0F - d0y) / 2.0F, 30.0F));
            } else {
               neededRotations[0] = player.rotationYaw - Math.min(Math.abs(0.0F - d0y), Math.min(Math.abs(0.0F - d0y) / 2.0F, 30.0F));
            }

            player.prevRotationYaw = player.rotationYaw;
            player.prevRotationPitch = player.rotationPitch;
            player.rotationYaw = neededRotations[0];
            player.rotationPitch = neededRotations[1];
         }

         this.timer.reset();
      }

   }
}
