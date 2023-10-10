package my.NewSnake.Tank.module.modules.MOVEMENT;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.UpdateEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.Minecraft;

@Module.Mod
public class AutoMinerar extends Module {
   Minecraft mc = Minecraft.getMinecraft();

   @EventTarget
   private void onUpdate(UpdateEvent var1) {
      ClientUtils.mc().gameSettings.keyBindAttack.pressed = true;
   }

   public void disable() {
      ClientUtils.mc().gameSettings.keyBindForward.pressed = false;
      super.disable();
   }
}
