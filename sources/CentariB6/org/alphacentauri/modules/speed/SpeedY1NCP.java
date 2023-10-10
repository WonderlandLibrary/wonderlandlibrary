package org.alphacentauri.modules.speed;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventMove;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.util.BlockUtils;
import org.alphacentauri.management.util.MathUtils;
import org.alphacentauri.management.util.Timer;
import org.alphacentauri.modules.speed.Speed;

public class SpeedY1NCP extends Speed {
   private double speedSonic = 10.0D;
   private int levelSonic = 1;
   private boolean disablingSonic;
   private double moveSpeedSonic = 16.0D;
   public static boolean canStepSonic;
   private double lastDistSonic;
   public static double yOffsetSonic;
   private boolean cancelSonic;
   private boolean speedTickSonic = false;
   private float speedTimerSonic = 1.1F;
   private int timerSonic;
   Timer setbackSonic = new Timer();

   public SpeedY1NCP() {
      Minecraft mc = AC.getMC();
      mc.timer.timerSpeed = 1.0F;
      this.speedTimerSonic = 1.0F;
      this.cancelSonic = false;
      this.speedTickSonic = true;
      this.moveSpeedSonic = mc.thePlayer == null?0.2873D:this.getStandartSonic();
      if(mc.thePlayer != null) {
         mc.thePlayer.onGround = true;
      }

   }

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         Minecraft mc = AC.getMC();
         if(BlockUtils.getBlock(-0.1F).getBlock().getMaterial().isLiquid()) {
            return;
         }

         mc.thePlayer.capabilities.isFlying = true;
         if(!mc.thePlayer.onGround) {
            mc.thePlayer.motionY = -4.0D;
         }

         double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
         double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
         this.lastDistSonic = Math.sqrt(xDist * xDist + zDist * zDist);
      } else if(event instanceof EventMove) {
         Minecraft mc = AC.getMC();
         mc.thePlayer.cameraPitch = 0.3F;
         if(!this.setbackSonic.hasMSPassed(450L)) {
            return;
         }

         if(BlockUtils.getBlock(-0.1F).getBlock().getMaterial().isLiquid()) {
            return;
         }

         if(mc.thePlayer.onGround) {
            this.levelSonic = 2;
         }

         if(MathUtils.round(mc.thePlayer.posY - (double)((int)mc.thePlayer.posY), 3) == MathUtils.round(0.138D, 3)) {
            mc.thePlayer.motionY -= 5.0D;
            ((EventMove)event).y -= 5.0D;
         }

         if(this.levelSonic != 1 || mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F) {
            if(this.levelSonic == 2) {
               this.moveSpeedSonic *= 0.9D;
               this.levelSonic = 3;
               if((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) && !mc.thePlayer.isCollidedHorizontally) {
                  mc.thePlayer.motionY = 0.399399995003034D;
                  ((EventMove)event).y = 0.399399995003034D;
                  this.moveSpeedSonic *= 2.385D;
                  mc.thePlayer.motionY = -5.0D;
                  this.speedTickSonic = false;
               }
            } else if(this.levelSonic == 3) {
               this.levelSonic = 4;
               double difference = 0.66D * (this.lastDistSonic - this.getStandartSonic());
               this.moveSpeedSonic = this.lastDistSonic - difference;
            } else if(mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) {
               this.levelSonic = 1;
            }
         } else {
            mc.thePlayer.motionY -= 5.0D;
            ((EventMove)event).y -= 5.0D;
            this.levelSonic = 2;
            this.moveSpeedSonic = 1.35D * this.getStandartSonic() - 0.01D;
         }

         this.moveSpeedSonic = Math.max(this.moveSpeedSonic, this.getStandartSonic());
         MovementInput movementInput = mc.thePlayer.movementInput;
         float forward = movementInput.moveForward;
         float strafe = movementInput.moveStrafe;
         float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
         if(forward == 0.0F && strafe == 0.0F) {
            ((EventMove)event).x = 0.0D;
            ((EventMove)event).z = 0.0D;
         } else if(forward != 0.0F) {
            if(strafe >= 1.0F) {
               yaw += (float)(forward > 0.0F?-45:45);
               strafe = 0.0F;
            } else if(strafe <= -1.0F) {
               yaw += (float)(forward > 0.0F?45:-45);
               strafe = 0.0F;
            }

            if(forward > 0.0F) {
               forward = 1.0F;
            } else if(forward < 0.0F) {
               forward = -1.0F;
            }
         }

         double mx = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
         double mz = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
         ((EventMove)event).x = (double)forward * this.moveSpeedSonic * mx + (double)strafe * this.moveSpeedSonic * mz;
         ((EventMove)event).z = (double)forward * this.moveSpeedSonic * mz - (double)strafe * this.moveSpeedSonic * mx;
         canStepSonic = true;
         if(forward == 0.0F && strafe == 0.0F) {
            ((EventMove)event).x = 0.0D;
            ((EventMove)event).z = 0.0D;
         }
      }

   }

   private double getStandartSonic() {
      Minecraft mc = AC.getMC();
      double baseSpeed = 0.2873D;
      if(mc.thePlayer == null) {
         return baseSpeed;
      } else {
         if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0D + 0.2D * (double)(amplifier + 1);
         }

         return baseSpeed;
      }
   }
}
