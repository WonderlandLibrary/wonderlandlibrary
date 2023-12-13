package my.NewSnake.Tank.module.modules.MOVEMENT;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.UpdateEvent;
import net.minecraft.client.Minecraft;

@Module.Mod
public class Twerk extends Module {
   private int faithisthebest;

   @EventTarget
   private void onUpdate(UpdateEvent var1) {
      ++this.faithisthebest;
      if (this.faithisthebest >= 2) {
         Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed = !Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed;
         this.faithisthebest = 0;
      }

   }
}
