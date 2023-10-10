package my.NewSnake.Tank.module.modules.MOVEMENT;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.event.Event;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.UpdateEvent;
import net.minecraft.client.Minecraft;

@Module.Mod
public class Sprint extends Module {
   @EventTarget
   private void EventoSprint(UpdateEvent var1) {
      if (var1.getState().equals(Event.State.PRE)) {
         this.setSuffix("Normal");
         if (this == false) {
            Minecraft var10000 = mc;
            Minecraft.thePlayer.setSprinting(true);
         }
      }

   }
}
