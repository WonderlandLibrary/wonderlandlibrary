package my.NewSnake.Tank.module.modules.MOVEMENT;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.ItemSlowEvent;
import my.NewSnake.event.events.UpdateEvent;

@Module.Mod
public class NoSlowDown extends Module {
   @EventTarget(4)
   private void onUpdate(UpdateEvent param1) {
      // $FF: Couldn't be decompiled
   }

   @EventTarget
   private void onItemUse(ItemSlowEvent var1) {
      var1.setCancelled(true);
   }
}
