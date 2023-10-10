package my.NewSnake.Tank.module.modules.MOVEMENT;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.MoveEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.util.Timer;

@Module.Mod(
   displayName = "Fast Ladder"
)
public class FastLadder extends Module {
   private static final double MAX_LADDER_SPEED = 0.287299999999994D;

   @EventTarget
   private void onMove(MoveEvent var1) {
      Timer var10000 = ClientUtils.mc().timer;
      Timer.timerSpeed = 1.0F;
      if (var1.getY() > 0.0D && ClientUtils.player().isOnLadder()) {
         var1.setY(0.287299999999994D);
      }

   }
}
