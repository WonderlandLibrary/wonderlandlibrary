package org.alphacentauri.modules.speed;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventMove;
import org.alphacentauri.management.events.EventSetback;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.util.MathUtils;
import org.alphacentauri.modules.speed.Speed;

public class SpeedY2NCP extends Speed {
   private int tState = 0;
   double y = 0.0D;
   public int state;
   public double moveSpeed;
   private double lastDist;
   private int cooldownHops;
   private boolean wasOnWater = false;

   public Block getBlock(AxisAlignedBB bb) {
      int y = (int)bb.minY;

      for(int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; ++x) {
         for(int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; ++z) {
            Block block = AC.getMC().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
            if(block != null) {
               return block;
            }
         }
      }

      return null;
   }

   public void onEvent(Event event) {
      if(event instanceof EventSetback) {
         this.cooldownHops = 2;
         this.state = 0;
      } else if(event instanceof EventTick) {
         if(AC.getMC().thePlayer.isInWater() || AC.getMC().thePlayer.isInLava()) {
            return;
         }

         if(AC.getMC().thePlayer.moveForward == 0.0F && AC.getMC().thePlayer.moveStrafing == 0.0F && AC.getMC().thePlayer.onGround) {
            this.cooldownHops = 2;
            this.moveSpeed *= 1.0800000429153442D;
            this.state = 2;
         }

         if(!AC.getMC().thePlayer.onGround && !AC.getMC().thePlayer.isCollidedHorizontally && AC.getMC().thePlayer.motionY > -0.5D) {
            AC.getMC().thePlayer.motionY = -0.5D;
         }

         double xDist = AC.getMC().thePlayer.posX - AC.getMC().thePlayer.prevPosX;
         double zDist = AC.getMC().thePlayer.posZ - AC.getMC().thePlayer.prevPosZ;
         this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
      } else if(event instanceof EventMove) {
         if(AC.getMC().thePlayer.isInWater() || AC.getMC().thePlayer.isInLava()) {
            this.cooldownHops = 2;
            return;
         }

         if(AC.getMC().thePlayer.isOnLadder() || AC.getMC().thePlayer.isEntityInsideOpaqueBlock() || AC.getMC().thePlayer.isCollidedHorizontally) {
            this.moveSpeed = 0.0D;
            this.wasOnWater = true;
            return;
         }

         if(this.wasOnWater) {
            this.moveSpeed = 0.0D;
            this.wasOnWater = false;
            return;
         }

         if(AC.getMC().thePlayer.moveForward == 0.0F && AC.getMC().thePlayer.moveStrafing == 0.0F) {
            return;
         }

         if(AC.getMC().thePlayer.onGround) {
            this.state = 2;
            AC.getMC().timer.timerSpeed = 1.0F;
         }

         if(MathUtils.round(AC.getMC().thePlayer.posY - (double)((int)AC.getMC().thePlayer.posY), 3) == MathUtils.round(0.138D, 3)) {
            EntityPlayerSP var10000 = AC.getMC().thePlayer;
            var10000.motionY -= 0.08D;
            ((EventMove)event).y -= 0.09316090325960147D;
            var10000 = AC.getMC().thePlayer;
            var10000.posY -= 0.09316090325960147D;
         }

         if(this.state != 1 || AC.getMC().thePlayer.moveForward == 0.0F && AC.getMC().thePlayer.moveStrafing == 0.0F) {
            if(this.state == 2) {
               this.state = 3;
               if(AC.getMC().thePlayer.moveForward != 0.0F || AC.getMC().thePlayer.moveStrafing != 0.0F) {
                  AC.getMC().thePlayer.motionY = 0.399399995803833D;
                  ((EventMove)event).y = 0.399399995803833D;
                  if(this.cooldownHops > 0) {
                     --this.cooldownHops;
                  }

                  this.moveSpeed *= 2.149D;
               }
            } else if(this.state == 3) {
               this.state = 4;
               double difference = 0.66D * (this.lastDist - this.getBaseMoveSpeed());
               this.moveSpeed = this.lastDist - difference;
            } else {
               if(AC.getMC().theWorld.getCollidingBoundingBoxes(AC.getMC().thePlayer, AC.getMC().thePlayer.boundingBox.offset(0.0D, AC.getMC().thePlayer.motionY, 0.0D)).size() > 0 || AC.getMC().thePlayer.isCollidedVertically) {
                  this.state = 1;
               }

               this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
            }
         } else {
            this.state = 2;
            this.moveSpeed = 1.35D * this.getBaseMoveSpeed() - 0.01D;
         }

         this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
         MovementInput movementInput = AC.getMC().thePlayer.movementInput;
         float forward = movementInput.moveForward;
         float strafe = movementInput.moveStrafe;
         float yaw = AC.getMC().thePlayer.rotationYaw;
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
         double var24 = (double)forward * this.moveSpeed * mx + (double)strafe * this.moveSpeed * mz;
         var24 = (double)forward * this.moveSpeed * mz - (double)strafe * this.moveSpeed * mx;
         if(this.cooldownHops == 0) {
            if(AC.getMC().thePlayer.isUsingItem() || AC.getMC().thePlayer.isBlocking()) {
               EntityPlayerSP var26 = AC.getMC().thePlayer;
               var26.motionX *= 0.399399995803833D;
               var26 = AC.getMC().thePlayer;
               var26.motionZ *= 0.399399995803833D;
            }

            ((EventMove)event).x = (double)forward * this.moveSpeed * mx + (double)strafe * this.moveSpeed * mz;
            ((EventMove)event).z = (double)forward * this.moveSpeed * mz - (double)strafe * this.moveSpeed * mx;
         }

         AC.getMC().thePlayer.stepHeight = 0.6F;
         if(forward == 0.0F && strafe == 0.0F) {
            ((EventMove)event).x = 0.0D;
            ((EventMove)event).z = 0.0D;
         } else {
            boolean collideCheck = false;
            if(AC.getMC().theWorld.getCollidingBoundingBoxes(AC.getMC().thePlayer, AC.getMC().thePlayer.boundingBox.expand(0.5D, 0.0D, 0.5D)).size() > 0) {
               collideCheck = true;
            }

            if(forward != 0.0F) {
               if(strafe >= 1.0F) {
                  float var28 = yaw + (float)(forward > 0.0F?-45:45);
                  strafe = 0.0F;
               } else if(strafe <= -1.0F) {
                  float var29 = yaw + (float)(forward > 0.0F?45:-45);
                  strafe = 0.0F;
               }

               if(forward > 0.0F) {
                  forward = 1.0F;
               } else if(forward < 0.0F) {
                  forward = -1.0F;
               }
            }
         }
      }

   }

   private boolean isColliding(AxisAlignedBB bb) {
      boolean colliding = false;

      for(AxisAlignedBB boundingBox : AC.getMC().theWorld.getCollidingBoundingBoxes(AC.getMC().thePlayer, bb)) {
         colliding = true;
      }

      if(this.getBlock(bb.offset(0.0D, -0.1D, 0.0D)) instanceof BlockAir) {
         colliding = true;
      }

      return colliding;
   }

   public int getGroundLevel() {
      for(int i = (int)Math.round(AC.getMC().thePlayer.posY + 0.49D); i > 0; --i) {
         AxisAlignedBB box = AC.getMC().thePlayer.boundingBox;
         box.minY = (double)(i - 1);
         box.maxY = (double)i;
         if(this.isColliding(box) && box.minY <= AC.getMC().thePlayer.posY) {
            return i;
         }
      }

      return -100000;
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
