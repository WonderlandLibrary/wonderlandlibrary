package my.NewSnake.Tank.module.modules.REGENS;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.Event;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.UpdateEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.potion.Potion;

@Module.Mod
public class Regeneration extends Module {
   @Option.Op(
      min = 0.0D,
      max = 40.0D,
      increment = 0.1D
   )
   public double Reg;
   @Option.Op(
      min = 0.0D,
      max = 60.0D,
      increment = 0.1D
   )
   public double Regpost;

   @EventTarget
   public void OnUpdate(UpdateEvent var1) {
      Minecraft var10000;
      if (this != false && this != false && this >= 0) {
         label62: {
            var10000 = mc;
            if (Minecraft.thePlayer.getActivePotionEffect(Potion.regeneration) == null) {
               var10000 = mc;
               if (!(Minecraft.thePlayer.getHealth() < 21.0F)) {
                  break label62;
               }
            }

            var10000 = mc;
            if (Minecraft.thePlayer.getActivePotionEffect(Potion.regeneration).getDuration() >= 0) {
               var10000 = mc;
               if (!(Minecraft.thePlayer.getHealth() < 21.0F)) {
                  break label62;
               }
            }

            for(int var2 = 0; (double)var2 < this.Reg; ++var2) {
               ClientUtils.player().getActivePotionEffect(Potion.regeneration).deincrementDuration();
               var10000 = mc;
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            }
         }
      }

      if (var1.getState().equals(Event.State.POST)) {
         new S06PacketUpdateHealth();
         if (this != false && this != false && this >= 0) {
            var10000 = mc;
            if (Minecraft.thePlayer.getActivePotionEffect(Potion.regeneration) == null) {
               var10000 = mc;
               if (!(Minecraft.thePlayer.getHealth() < 21.0F)) {
                  return;
               }
            }

            var10000 = mc;
            if (Minecraft.thePlayer.getActivePotionEffect(Potion.regeneration).getDuration() >= 0) {
               var10000 = mc;
               if (!(Minecraft.thePlayer.getHealth() < 21.0F)) {
                  return;
               }
            }

            for(int var3 = 0; (double)var3 < this.Regpost; ++var3) {
               ClientUtils.player().getActivePotionEffect(Potion.regeneration).deincrementDuration();
               var10000 = mc;
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            }
         }
      }

   }
}
