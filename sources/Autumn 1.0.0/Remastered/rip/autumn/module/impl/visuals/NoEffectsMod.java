package rip.autumn.module.impl.visuals;

import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.potion.Potion;
import rip.autumn.annotations.Label;
import rip.autumn.events.game.TickEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Category;

@Label("No Effects")
@Category(ModuleCategory.VISUALS)
@Aliases({"noeffects", "speedygonzales", "noblind"})
public final class NoEffectsMod extends Module {

   @Listener(TickEvent.class)
   public void onTick() {
      if (getPlayer().isPotionActive(Potion.blindness)) {
         mc.thePlayer.removePotionEffect(Potion.blindness.id);
      }

      if (getPlayer().isPotionActive(Potion.confusion)) {
         mc.thePlayer.removePotionEffect(Potion.confusion.id);
      }
   }

}
