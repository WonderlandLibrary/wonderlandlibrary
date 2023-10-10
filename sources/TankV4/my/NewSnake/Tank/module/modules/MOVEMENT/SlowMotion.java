package my.NewSnake.Tank.module.modules.MOVEMENT;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.UpdateEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;

@Module.Mod
public class SlowMotion extends Module {
   public void disable() {
      Timer.timerSpeed = 1.0F;
      ClientUtils.mc();
      Minecraft.thePlayer.landMovementFactor = 0.03F;
      ClientUtils.mc();
      Minecraft.thePlayer.jumpMovementFactor = 0.03F;
      super.disable();
   }

   @EventTarget
   private void onUpdate(UpdateEvent var1) {
      ClientUtils.mc();
      if (Minecraft.thePlayer.onGround) {
         ClientUtils.mc();
         float var10000 = Minecraft.thePlayer.moveForward;
      }

      Timer.timerSpeed = 0.5F;
   }
}
