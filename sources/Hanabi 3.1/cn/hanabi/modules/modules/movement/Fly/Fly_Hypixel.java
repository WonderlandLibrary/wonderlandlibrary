package cn.hanabi.modules.modules.movement.Fly;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.events.EventMove;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.utils.TimeHelper;
import net.minecraft.client.Minecraft;

@ObfuscationClass
public class Fly_Hypixel {
   Minecraft mc = Minecraft.getMinecraft();
   TimeHelper timer = new TimeHelper();

   public void onPre(EventPreMotion e) {
      if (this.timer.isDelayComplete(850L)) {
         this.HClip(0.5);
         this.timer.reset();
      }

   }

   public void onMove(EventMove event) {
      event.setX(0.0);
      event.setY(0.0);
      event.setZ(0.0);
   }

   private void HClip(double horizontal) {
      double playerYaw = Math.toRadians((double)this.mc.thePlayer.rotationYaw);
      this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + horizontal * -Math.sin(playerYaw), this.mc.thePlayer.posY, this.mc.thePlayer.posZ + horizontal * Math.cos(playerYaw));
   }
}
