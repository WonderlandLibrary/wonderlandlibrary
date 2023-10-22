package cn.hanabi.modules.modules.movement.Fly;

import cn.hanabi.events.EventMove;
import cn.hanabi.events.EventPacket;
import cn.hanabi.utils.MoveUtils;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Fly_NiggaBaipai {
   Minecraft mc = Minecraft.getMinecraft();
   TimeHelper timer = new TimeHelper();

   public void onMove(EventMove e) {
      e.setX(this.mc.thePlayer.motionX = 0.0);
      e.setY(this.mc.thePlayer.motionY = 0.0);
      e.setZ(this.mc.thePlayer.motionZ = 0.0);
      if (MoveUtils.isOnGround(0.01)) {
         this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.004 * Math.random(), this.mc.thePlayer.posZ);
         this.timer.reset();
      }

      if (PlayerUtil.isMoving2()) {
         if (this.timer.isDelayComplete(1250L)) {
            double playerYaw = Math.toRadians((double)this.mc.thePlayer.rotationYaw);
            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + (Double)Fly.timer.getValueState() * 1.0 * -Math.sin(playerYaw), this.mc.thePlayer.posY - 2.0, this.mc.thePlayer.posZ + (Double)Fly.timer.getValueState() * 1.0 * Math.cos(playerYaw));
            this.timer.reset();
         }
      } else {
         MoveUtils.setMotion(e, 0.0);
      }

   }

   public void onPacket(EventPacket event) {
      if (event.getPacket() instanceof C03PacketPlayer) {
      }

   }

   public void onDisable() {
   }
}
