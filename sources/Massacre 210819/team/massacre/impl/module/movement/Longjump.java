package team.massacre.impl.module.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.impl.event.EventMove;
import team.massacre.impl.event.EventUpdate;

public class Longjump extends Module {
   public boolean hasJumped;
   public double speed;

   public Longjump() {
      super("Long Jump", 0, Category.MOVEMENT);
   }

   @Handler
   public void onUpdate(EventUpdate eventUpdate) {
      if ((double)this.mc.thePlayer.fallDistance > 0.4D) {
         this.toggle();
      }

      this.mc.thePlayer.setSprinting(false);
      double playerYaw = Math.toRadians((double)this.mc.thePlayer.rotationYaw);
      this.mc.thePlayer.motionX = 0.95D * -Math.sin(playerYaw);
      this.mc.thePlayer.motionZ = 0.95D * Math.cos(playerYaw);
      this.mc.timer.timerSpeed = 0.4F;
      if ((double)this.mc.thePlayer.fallDistance > 0.2D) {
         this.toggle();
      }

      if (this.mc.thePlayer.onGround && this.mc.thePlayer.isMoving()) {
         this.mc.thePlayer.jump();
         this.mc.timer.timerSpeed = 0.6F;
         this.mc.thePlayer.motionY = 0.6700000166893005D;
         this.mc.thePlayer.jumpMovementFactor = 0.22F;
      }

   }

   @Handler
   public void onMove(EventMove eventMove) {
   }

   public void onDisable() {
      super.onDisable();
      this.mc.timer.timerSpeed = 1.0F;
      EntityPlayerSP var10000 = this.mc.thePlayer;
      var10000.motionX *= 0.8D;
      var10000 = this.mc.thePlayer;
      var10000.motionZ *= 0.8D;
   }

   public void onEnable() {
      super.onEnable();
   }
}
