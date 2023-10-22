package cn.hanabi.events;

import cn.hanabi.injection.interfaces.IEntityPlayerSP;
import com.darkmagician6.eventapi.events.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class EventMove implements Event {
   public double x;
   public double y;
   public double z;
   private boolean onGround;

   public EventMove(double a, double b, double c) {
      this.x = a;
      this.y = b;
      this.z = c;
   }

   public double getX() {
      return this.x;
   }

   public void setX(double x) {
      this.x = x;
   }

   public double getY() {
      return this.y;
   }

   public void setY(double y) {
      this.y = y;
   }

   public double getZ() {
      return this.z;
   }

   public void setZ(double z) {
      this.z = z;
   }

   public boolean isOnGround() {
      return this.onGround;
   }

   public void setOnGround(boolean onGround) {
      this.onGround = onGround;
   }

   public void setSpeed(double speed) {
      float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
      double forward = (double)Minecraft.getMinecraft().thePlayer.movementInput.moveForward;
      double strafe = (double)Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe;
      if (forward == 0.0 && strafe == 0.0) {
         this.setX(0.0);
         this.setZ(0.0);
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
            } else {
               forward = -1.0;
            }
         }

         double cos = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
         double sin = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
         this.setX(forward * speed * cos + strafe * speed * sin);
         this.setZ(forward * speed * sin - strafe * speed * cos);
      }

   }

   public void setSpeed(double speed, float yaw, double strafe, double forward) {
      if (forward == 0.0 && strafe == 0.0) {
         this.setX(0.0);
         this.setZ(0.0);
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
            } else {
               forward = -1.0;
            }
         }

         double cos = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
         double sin = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
         this.setX(forward * speed * cos + strafe * speed * sin);
         this.setZ(forward * speed * sin - strafe * speed * cos);
      }

   }

   public double getMovementSpeed() {
      double baseSpeed = 0.2873;
      if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
         int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
         baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
      }

      return baseSpeed;
   }

   public void setMovementSpeed(double movementSpeed) {
      this.setX(-(Math.sin((double)((IEntityPlayerSP)Minecraft.getMinecraft().thePlayer).getDirection()) * Math.max(movementSpeed, this.getMovementSpeed())));
      this.setZ(Math.cos((double)((IEntityPlayerSP)Minecraft.getMinecraft().thePlayer).getDirection()) * Math.max(movementSpeed, this.getMovementSpeed()));
   }

   public double getMotionY(double mY) {
      if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump)) {
         mY += (double)(Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1;
      }

      return mY;
   }

   public double getLegitMotion() {
      return 0.41999998688697815;
   }
}
