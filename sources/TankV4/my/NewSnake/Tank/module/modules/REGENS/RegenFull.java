package my.NewSnake.Tank.module.modules.REGENS;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.MoveEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

@Module.Mod
public class RegenFull extends Module {
   @Option.Op(
      min = 0.0D,
      max = 30.0D,
      increment = 0.1D
   )
   public double full;

   @EventTarget
   public void OnUpdate(MoveEvent var1) {
      if (this != false) {
         Minecraft var10000 = mc;
         if (!(Minecraft.thePlayer.getHealth() < 21.0F)) {
            var10000 = mc;
            if (Minecraft.thePlayer.getActivePotionEffect(Potion.regeneration) == null) {
               return;
            }
         }

         var10000 = mc;
         if (Minecraft.thePlayer.getActivePotionEffect(Potion.regeneration) == null) {
            var10000 = mc;
            if (Minecraft.thePlayer.getActivePotionEffect(Potion.regeneration).getDuration() >= 0) {
               var10000 = mc;
               if (Minecraft.thePlayer.getActivePotionEffect(Potion.regeneration).getAmplifier() >= 0) {
                  var10000 = mc;
                  if (!(Minecraft.thePlayer.getHealth() < 21.0F)) {
                     return;
                  }
               }
            }
         }

         var10000 = mc;
         if (Minecraft.thePlayer.getActivePotionEffect(Potion.regeneration) == null) {
            var10000 = mc;
            if (!(Minecraft.thePlayer.getHealth() < 21.0F)) {
               return;
            }
         }

         var10000 = mc;
         if (!(Minecraft.thePlayer.getHealth() < 21.0F)) {
            var10000 = mc;
            if (!(Minecraft.thePlayer.getAbsorptionAmount() < 1.0F)) {
               return;
            }
         }

         for(int var2 = 0; (double)var2 < this.full; ++var2) {
            ClientUtils.player().getActivePotionEffect(Potion.regeneration).deincrementDuration();
            var10000 = mc;
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
         }
      }

   }
}
