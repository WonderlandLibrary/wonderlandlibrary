package cn.hanabi.modules.modules.movement.Speed;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.utils.MoveUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

@ObfuscationClass
public class Speed_Mineplex {
   Minecraft mc = Minecraft.getMinecraft();
   private int boost = 0;
   private boolean jumped = false;

   public void onUpdate() {
      if (!MoveUtils.isMoving()) {
         this.mc.thePlayer.motionX = 0.0;
         this.mc.thePlayer.motionZ = 0.0;
      } else {
         if (this.jumped) {
            if (this.mc.thePlayer.onGround) {
               this.jumped = false;
               this.mc.thePlayer.motionX = 0.0;
               this.mc.thePlayer.motionZ = 0.0;
               return;
            }

            float boostPercent;
            if (this.boost == 0) {
               boostPercent = 1.75F;
            } else {
               boostPercent = 1.0F;
            }

            MoveUtils.strafe((double)(MoveUtils.getSpeed() * boostPercent));
            ++this.boost;
         } else if (this.mc.thePlayer.onGround) {
            this.boost = 0;
            this.mc.thePlayer.jump();
            this.jumped = true;
         }

         if ((double)this.mc.thePlayer.fallDistance > 1.5) {
            this.mc.thePlayer.jumpMovementFactor = 0.01F;
         } else if (this.mc.thePlayer.fallDistance > 0.0F) {
            this.mc.thePlayer.jumpMovementFactor = 0.035F;
            EntityPlayerSP var10000 = this.mc.thePlayer;
            var10000.motionY += 0.02;
         } else {
            this.mc.thePlayer.jumpMovementFactor = 0.025F;
         }

      }
   }
}
