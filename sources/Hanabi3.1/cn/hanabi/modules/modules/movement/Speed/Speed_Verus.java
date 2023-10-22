package cn.hanabi.modules.modules.movement.Speed;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.events.EventMove;
import cn.hanabi.utils.MoveUtils;
import net.minecraft.client.Minecraft;

@ObfuscationClass
public class Speed_Verus {
   Minecraft mc = Minecraft.getMinecraft();

   public void onMove(EventMove event) {
      if (!this.mc.thePlayer.isInLava() && !this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isOnLadder() && this.mc.thePlayer.ridingEntity == null && MoveUtils.isMoving()) {
         if (this.mc.thePlayer.onGround) {
            this.mc.thePlayer.jump();
            this.mc.thePlayer.motionY = 0.0;
            MoveUtils.strafe(0.6100000143051147);
            event.y = 0.41999998688698;
         }

         MoveUtils.strafe();
      }

   }
}
