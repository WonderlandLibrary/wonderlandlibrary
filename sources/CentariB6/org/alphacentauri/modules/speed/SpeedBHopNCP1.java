package org.alphacentauri.modules.speed;

import java.math.BigDecimal;
import java.math.RoundingMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventMove;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.modules.speed.Speed;

public class SpeedBHopNCP1 extends Speed {
   public static double speed;
   private int level = 1;
   public static double moveSpeed;
   public static boolean canStep;
   private double lastDist;
   private double[] values = new double[]{0.08D, 0.09316090325960147D, 1.35D, 2.149D, 0.66D};

   public static double round(double value, int places) {
      if(places < 0) {
         throw new IllegalArgumentException();
      } else {
         BigDecimal bd = new BigDecimal(value);
         bd = bd.setScale(places, RoundingMode.HALF_UP);
         return bd.doubleValue();
      }
   }

   public void onEvent(Event eventuncast) {
      if(eventuncast instanceof EventMove) {
         EventMove event = (EventMove)eventuncast;
         if(AC.getMC().getPlayer().isSneaking()) {
            return;
         }

         if(AC.getMC().thePlayer.onGround) {
            this.level = 2;
         }

         if(round(AC.getMC().thePlayer.posY - (double)((int)AC.getMC().thePlayer.posY), 3) == round(0.138D, 3)) {
            EntityPlayerSP var10000 = AC.getMC().thePlayer;
            var10000.motionY -= this.values[0];
            event.y -= this.values[1];
            var10000 = AC.getMC().thePlayer;
            var10000.posY -= this.values[1];
         }

         if(this.level != 1 || AC.getMC().thePlayer.moveForward == 0.0F && AC.getMC().thePlayer.moveStrafing == 0.0F) {
            if(this.level == 2) {
               this.level = 3;
               if(AC.getMC().thePlayer.moveForward != 0.0F || AC.getMC().thePlayer.moveStrafing != 0.0F) {
                  AC.getMC().thePlayer.motionY = 0.4D;
                  event.y = 0.4D;
                  moveSpeed *= this.values[3];
               }
            } else if(this.level == 3) {
               this.level = 4;
               double difference = this.values[4] * (this.lastDist - this.getBaseMoveSpeed());
               moveSpeed = this.lastDist - difference;
            } else {
               if(AC.getMC().thePlayer.onGround && (AC.getMC().theWorld.getCollidingBoundingBoxes(AC.getMC().thePlayer, AC.getMC().thePlayer.boundingBox.offset(0.0D, AC.getMC().thePlayer.motionY, 0.0D)).size() > 0 || AC.getMC().thePlayer.isCollidedVertically)) {
                  this.level = 1;
               }

               moveSpeed = this.lastDist - this.lastDist / 159.0D;
            }
         } else {
            this.level = 2;
            moveSpeed = this.values[2] * this.getBaseMoveSpeed() - 0.01D;
         }

         moveSpeed = Math.max(moveSpeed, this.getBaseMoveSpeed());
         MovementInput movementInput = AC.getMC().thePlayer.movementInput;
         float forward = movementInput.moveForward;
         float strafe = movementInput.moveStrafe;
         float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
         if(forward == 0.0F && strafe == 0.0F) {
            event.x = 0.0D;
            event.z = 0.0D;
         } else if(forward != 0.0F) {
            if(strafe > 0.0F) {
               yaw += (float)(forward > 0.0F?-45:45);
               strafe = 0.0F;
            } else if(strafe < 0.0F) {
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
         event.x = (double)forward * moveSpeed * mx + (double)strafe * moveSpeed * mz;
         event.z = (double)forward * moveSpeed * mz - (double)strafe * moveSpeed * mx;
         canStep = true;
         AC.getMC().thePlayer.stepHeight = 0.6F;
         if(forward == 0.0F && strafe == 0.0F) {
            event.x = 0.0D;
            event.z = 0.0D;
         }
      } else if(eventuncast instanceof EventTick) {
         double xDist = AC.getMC().thePlayer.posX - AC.getMC().thePlayer.prevPosX;
         double zDist = AC.getMC().thePlayer.posZ - AC.getMC().thePlayer.prevPosZ;
         this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
      }

   }

   private double getBaseMoveSpeed() {
      double baseSpeed = 0.2873D;
      if(AC.getMC().thePlayer.isPotionActive(Potion.moveSpeed)) {
         int amplifier = AC.getMC().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
         baseSpeed *= 1.0D + 0.2D * (double)(amplifier + 1);
      }

      return baseSpeed;
   }
}
