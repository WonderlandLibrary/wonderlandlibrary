package my.NewSnake.Tank.module.modules.WORLD;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.Event;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.PacketReceiveEvent;
import my.NewSnake.event.events.UpdateEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

@Module.Mod
public class Tempo extends Module {
   @Option.Op(
      min = 0.0D,
      max = 24000.0D,
      increment = 250.0D
   )
   private double Tempo = 9000.0D;

   @EventTarget
   private void onPacketRecieve(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof S03PacketTimeUpdate) {
         var1.setCancelled(true);
      }

   }

   @EventTarget
   private void onUpdate(UpdateEvent var1) {
      if (var1.getState() == Event.State.POST) {
         ClientUtils.world().setWorldTime((long)this.Tempo);
      }

   }
}
