package cn.hanabi.modules.modules.movement.LongJump;

import cn.hanabi.events.EventMove;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.MoveUtils;
import cn.hanabi.utils.PlayerUtil;
import net.minecraft.client.Minecraft;

public class LongJump_NonDMG {
   final Minecraft mc = Minecraft.getMinecraft();
   private int stage;
   private double speed;
   private double verticalSpeed;

   public void onMove(EventMove e) {
      if (MoveUtils.isOnGround(0.01) || this.stage > 0) {
         switch (this.stage) {
            case 0:
               this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.004 * Math.random(), this.mc.thePlayer.posZ);
               this.verticalSpeed = PlayerUtil.getBaseJumpHeight();
               this.speed = MoveUtils.getBaseMoveSpeed(0.2873, 0.1) * 2.149;
               break;
            case 1:
               this.speed *= 0.65;
         }

         e.setY(this.verticalSpeed);
         if (this.stage > 8) {
            this.speed *= 0.98;
            this.verticalSpeed -= 0.035;
         } else {
            this.speed *= 0.99;
            this.verticalSpeed *= 0.65;
         }

         ++this.stage;
         if (MoveUtils.isOnGround(0.01) && this.stage > 4) {
            ModManager.getModule("LongJump").set(false);
         }

         MoveUtils.setMotion(e, Math.max(MoveUtils.getBaseMoveSpeed(0.2873, 0.1), this.speed));
      }

   }

   public void onEnable() {
      this.stage = 0;
   }

   public void onDisable() {
      this.stage = 0;
   }
}
