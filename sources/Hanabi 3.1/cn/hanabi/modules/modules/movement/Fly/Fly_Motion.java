package cn.hanabi.modules.modules.movement.Fly;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.injection.interfaces.IKeyBinding;
import cn.hanabi.utils.PlayerUtil;
import net.minecraft.client.Minecraft;

@ObfuscationClass
public class Fly_Motion {
   Minecraft mc = Minecraft.getMinecraft();

   public void onPre() {
      this.mc.thePlayer.motionY = 0.0;
      if (PlayerUtil.MovementInput()) {
         PlayerUtil.setSpeed((Double)Fly.timer.getValueState() * 0.5);
      } else {
         PlayerUtil.setSpeed(0.0);
      }

      if (((IKeyBinding)this.mc.gameSettings.keyBindSneak).getPress()) {
         --this.mc.thePlayer.motionY;
      } else if (((IKeyBinding)this.mc.gameSettings.keyBindJump).getPress()) {
         ++this.mc.thePlayer.motionY;
      }

   }
}
