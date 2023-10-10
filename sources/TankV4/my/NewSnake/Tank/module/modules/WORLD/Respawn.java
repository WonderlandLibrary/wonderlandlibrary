package my.NewSnake.Tank.module.modules.WORLD;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.event.Event;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.UpdateEvent;
import my.NewSnake.utils.ClientUtils;

@Module.Mod
public class Respawn extends Module {
   @EventTarget
   public void onUpdate(UpdateEvent var1) {
      if (var1.getState() == Event.State.POST && !ClientUtils.player().isEntityAlive()) {
         ClientUtils.player().respawnPlayer();
      }

   }
}
