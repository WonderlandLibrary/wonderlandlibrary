package my.NewSnake.Tank.module.modules.REGENS;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.UpdateEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

@Module.Mod
public class RegenWhile extends Module {
   @Option.Op
   public boolean PotionEffect;
   @Option.Op(
      min = 0.0D,
      max = 40.0D,
      increment = 0.1D
   )
   public double While;

   @EventTarget
   public void OnUpdate(UpdateEvent var1) {
      if (this != false && this <= 0) {
         Minecraft var10000;
         if (!this.PotionEffect) {
            var10000 = mc;
            if (Minecraft.thePlayer.getActivePotionEffect(Potion.regeneration) == null) {
               return;
            }
         }

         var10000 = mc;
         if (!Minecraft.thePlayer.onGround) {
            var10000 = mc;
            if (!Minecraft.thePlayer.isCollidedVertically) {
               var10000 = mc;
               if (!(Minecraft.thePlayer.getHealth() < 21.0F)) {
                  return;
               }
            }
         }

         var10000 = mc;
         if (Minecraft.thePlayer.getHealth() < 21.0F) {
            for(int var2 = 0; (double)var2 < this.While; ++var2) {
               ClientUtils.player().getActivePotionEffect(Potion.regeneration).deincrementDuration();
               var10000 = mc;
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            }
         }
      }

   }
}
