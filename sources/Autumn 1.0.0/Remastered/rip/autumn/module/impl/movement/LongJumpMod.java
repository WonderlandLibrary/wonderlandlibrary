package rip.autumn.module.impl.movement;

import me.zane.basicbus.api.annotations.Listener;
import rip.autumn.annotations.Label;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.events.player.MoveEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Category;
import rip.autumn.utils.MoveUtil;

@Label("Long Jump")
@Category(ModuleCategory.MOVEMENT)
@Aliases({"longjump", "lj"})
public final class LongJumpMod extends Module {

   private double lastDif;
   private double moveSpeed;
   private int stage;
   private int groundTicks;

   public void onEnable() {
      this.lastDif = 0.0D;
      this.moveSpeed = 0.0D;
      this.stage = 0;
      this.groundTicks = 1;
   }


   @Listener(MoveEvent.class)
   public void onMove(MoveEvent event) {
      if (isEnabled()) {
         if (isMoving()) {
            switch(this.stage) {
            case 0:
            case 1:
               this.moveSpeed = 0.0D;
               break;
            case 2:
               if (getGround() && mc.thePlayer.isCollidedVertically) {
                  event.y = mc.thePlayer.motionY = MoveUtil.getJumpBoostModifier(0.3999999463558197D);
                  this.moveSpeed = MoveUtil.getBaseMoveSpeed() * 2.0D;
               }
               break;
            case 3:
               this.moveSpeed = MoveUtil.getBaseMoveSpeed() * 2.1489999294281006D;
               break;
            default:
               if (mc.thePlayer.motionY < 0.0D) {
                  mc.thePlayer.motionY *= 0.5D;
               }

               this.moveSpeed = this.lastDif - this.lastDif / 159.0D;
            }

            this.moveSpeed = Math.max(this.moveSpeed, MoveUtil.getBaseMoveSpeed());
            ++this.stage;
         }

         MoveUtil.setSpeed(this.moveSpeed);
      }
   }

   @Listener(MotionUpdateEvent.class)
   public void onMotionUpdate(MotionUpdateEvent event) {
      if (event.isPre()) {
         if (getGround() && mc.thePlayer.isCollidedVertically) {
            event.setPosY(event.getPosY() + 7.435E-4D);
         }

         double xDif = mc.thePlayer.posX - mc.thePlayer.prevPosX;
         double zDif = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
         this.lastDif = Math.sqrt(xDif * xDif + zDif * zDif);
         if (isMoving() && getGround() && mc.thePlayer.isCollidedVertically && this.stage > 2) {
            ++this.groundTicks;
         }

         if (this.groundTicks > 1) {
            this.toggle();
         }
      }
   }
}
