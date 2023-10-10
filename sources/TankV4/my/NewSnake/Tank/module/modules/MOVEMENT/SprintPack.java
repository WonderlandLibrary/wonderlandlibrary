package my.NewSnake.Tank.module.modules.MOVEMENT;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.SprintEvent;
import my.NewSnake.event.events.TickEvent;
import my.NewSnake.utils.ClientUtils;

@Module.Mod(
   displayName = "Sprint Packet"
)
public class SprintPack extends Module {
   @Option.Op
   private boolean Direçao = true;
   @Option.Op
   private boolean Sprint = true;

   @EventTarget
   private void onUpdate(TickEvent var1) {
      if (ClientUtils.player() != null && this != false) {
         ClientUtils.player().setSprinting(true);
      }

   }

   @EventTarget
   private void onSprint(SprintEvent var1) {
      if (this != false) {
         var1.setSprinting(true);
      }

   }
}
