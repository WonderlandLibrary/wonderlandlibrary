package cn.hanabi.modules.modules.movement.Speed;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.Wrapper;
import cn.hanabi.events.EventMove;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.combat.KillAura;
import cn.hanabi.modules.modules.combat.TargetStrafe;
import cn.hanabi.utils.PlayerUtil;
import net.minecraft.client.Minecraft;

@ObfuscationClass
public class Speed_GudHop {
   Minecraft mc = Minecraft.getMinecraft();

   public void onPre(EventPreMotion e) {
      KillAura killAura = (KillAura)ModManager.getModule(KillAura.class);
      TargetStrafe targetStrafe = (TargetStrafe)ModManager.getModule(TargetStrafe.class);
      if (this.mc.thePlayer.onGround && PlayerUtil.MovementInput() && !this.mc.thePlayer.isInWater()) {
         Wrapper.getTimer().timerSpeed = 1.0F;
         this.mc.thePlayer.jump();
      } else if (PlayerUtil.MovementInput() && !this.mc.thePlayer.isInWater() && targetStrafe.isStrafing((EventMove)null, KillAura.target, 1.0)) {
         PlayerUtil.setSpeed(1.0);
      }

      if (!PlayerUtil.MovementInput()) {
         this.mc.thePlayer.motionX = this.mc.thePlayer.motionZ = 0.0;
      }

   }
}
