package my.NewSnake.Tank.module.modules.RENDER;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.UpdateEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@Module.Mod
public class FullBright extends Module {
   @EventTarget
   private void onUpdate(UpdateEvent var1) {
      if (mc.gameSettings.gammaSetting <= 2000.0F) {
         mc.gameSettings.gammaSetting = 2000.0F;
         Minecraft var10000 = mc;
         Minecraft.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 5200, 1));
      }

   }
}
