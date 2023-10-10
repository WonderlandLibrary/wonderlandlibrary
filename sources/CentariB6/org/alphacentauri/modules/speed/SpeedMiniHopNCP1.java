package org.alphacentauri.modules.speed;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventMove;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.util.BlockUtils;
import org.alphacentauri.management.util.MathUtils;
import org.alphacentauri.modules.speed.Speed;

public class SpeedMiniHopNCP1 extends Speed {
   private double delay;
   private boolean speedTick;
   private int level = 1;
   private double moveSpeed = AC.getMC().getPlayer() == null?0.2873D:this.getBaseMoveSpeed();
   private double lastDist;

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         double xDist = AC.getMC().getPlayer().posX - AC.getMC().getPlayer().prevPosX;
         double zDist = AC.getMC().getPlayer().posZ - AC.getMC().getPlayer().prevPosZ;
         this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
      } else if(event instanceof EventMove) {
         this.onMove((EventMove)event);
      }

   }

   private double getBaseMoveSpeed() {
      double baseSpeed = 0.2873D;
      if(AC.getMC().getPlayer().isPotionActive(Potion.moveSpeed)) {
         int amplifier = AC.getMC().getPlayer().getActivePotionEffect(Potion.moveSpeed).getAmplifier();
         baseSpeed *= 1.0D + 0.2D * (double)(amplifier + 1);
      }

      return baseSpeed;
   }

   private void onMove(EventMove event) {
      MovementInput movementInput = AC.getMC().getPlayer().movementInput;
      float forward = movementInput.moveForward;
      float strafe = movementInput.moveStrafe;
      float yaw = AC.getMC().getPlayer().rotationYaw;
      if(forward == 0.0F && strafe == 0.0F) {
         event.x = 0.0D;
         event.z = 0.0D;
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
      if(AC.getMC().getPlayer().hasMovementInput() && !BlockUtils.isOnLiquid() && !AC.getMC().getPlayer().isInWater() && AC.getMC().getPlayer().isMoving()) {
         ++this.delay;
         if(AC.getMC().getPlayer().onGround && AC.getMC().getPlayer().isMoving() && this.delay > 8.0D) {
            this.level = 2;
         }

         if(BlockUtils.getBlock(-1.0F).getBlock().getMaterial() != Material.air) {
            if(MathUtils.round(AC.getMC().getPlayer().posY - (double)((int)AC.getMC().getPlayer().posY), 3) == MathUtils.round(0.4D, 3)) {
               EntityPlayerSP thePlayer = AC.getMC().getPlayer();
               double n = 0.31D;
               thePlayer.motionY = 0.31D;
               event.y = 0.31D;
            } else if(MathUtils.round(AC.getMC().getPlayer().posY - (double)((int)AC.getMC().getPlayer().posY), 3) == MathUtils.round(0.71D, 3)) {
               EntityPlayerSP thePlayer2 = AC.getMC().getPlayer();
               double n2 = 0.04D;
               thePlayer2.motionY = 0.04D;
               event.y = 0.04D;
            } else if(MathUtils.round(AC.getMC().getPlayer().posY - (double)((int)AC.getMC().getPlayer().posY), 3) == MathUtils.round(0.75D, 3)) {
               EntityPlayerSP thePlayer3 = AC.getMC().getPlayer();
               double n3 = -0.2D;
               thePlayer3.motionY = -0.2D;
               event.y = -0.2D;
            } else if(MathUtils.round(AC.getMC().getPlayer().posY - (double)((int)AC.getMC().getPlayer().posY), 3) == MathUtils.round(0.55D, 3)) {
               EntityPlayerSP thePlayer4 = AC.getMC().getPlayer();
               double n4 = -0.14D;
               thePlayer4.motionY = -0.14D;
               event.y = -0.14D;
            } else if(MathUtils.round(AC.getMC().getPlayer().posY - (double)((int)AC.getMC().getPlayer().posY), 3) == MathUtils.round(0.41D, 3)) {
               EntityPlayerSP thePlayer5 = AC.getMC().getPlayer();
               double n5 = -0.2D;
               thePlayer5.motionY = -0.2D;
               event.y = -0.2D;
            }
         }

         if(this.level == -1) {
            event.x *= 0.3D;
            event.z *= 0.3D;
         }

         if(this.level != 1 || AC.getMC().getPlayer().moveForward == 0.0F && AC.getMC().getPlayer().moveStrafing == 0.0F) {
            if(this.level == 2) {
               if(AC.getMC().getPlayer().isCollidedVertically) {
                  event.y = 0.4D;
               }

               this.moveSpeed *= 2.149D;
            } else if(this.level == 3) {
               double difference = 0.66D * (this.lastDist - this.getBaseMoveSpeed());
               this.moveSpeed = this.lastDist - difference;
            } else {
               List collidingList = AC.getMC().theWorld.getCollidingBoundingBoxes(AC.getMC().getPlayer(), AC.getMC().getPlayer().boundingBox.offset(0.0D, AC.getMC().getPlayer().motionY, 0.0D));
               if((collidingList.size() > 0 || AC.getMC().getPlayer().isCollidedVertically) && this.level > 0) {
                  this.level = AC.getMC().getPlayer().moveForward == 0.0F && AC.getMC().getPlayer().moveStrafing == 0.0F?0:1;
               }

               this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
            }
         } else {
            this.moveSpeed = 1.35D * this.getBaseMoveSpeed();
         }

         this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
         event.x = (double)forward * this.moveSpeed * mx + (double)strafe * this.moveSpeed * mz;
         event.z = (double)forward * this.moveSpeed * mz - (double)strafe * this.moveSpeed * mx;
         if(forward == 0.0F && strafe == 0.0F) {
            event.x = 0.0D;
            event.z = 0.0D;
         }

         ++this.level;
         this.speedTick = !this.speedTick;
      } else {
         this.level = -8;
         this.delay = -10.0D;
      }
   }
}
