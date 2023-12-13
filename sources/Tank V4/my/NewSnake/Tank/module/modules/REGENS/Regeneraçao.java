package my.NewSnake.Tank.module.modules.REGENS;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.Event;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.UpdateEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

@Module.Mod
public class Regeneraçao extends Module {
   @Option.Op(
      min = 0.0D,
      max = 21.0D,
      increment = 1.0D
   )
   public double Health;
   @Option.Op(
      min = 0.0D,
      max = 40.0D,
      increment = 0.1D
   )
   public double PotionEffect;

   @EventTarget
   public void OnUpdate(UpdateEvent var1) {
      if (var1.getState().equals(Event.State.POST) && this != false) {
         Minecraft var10000 = mc;
         if ((double)Minecraft.thePlayer.getHealth() < this.Health) {
            var10000 = mc;
            if (Minecraft.thePlayer.getActivePotionEffect(Potion.regeneration) != null) {
               for(int var2 = 0; (double)var2 < this.PotionEffect; ++var2) {
                  ClientUtils.player().getActivePotionEffect(Potion.regeneration).deincrementDuration();
                  var10000 = mc;
                  Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
               }
            }
         }
      }

   }
}
