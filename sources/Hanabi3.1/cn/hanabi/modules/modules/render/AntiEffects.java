package cn.hanabi.modules.modules.render;

import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.potion.Potion;

public class AntiEffects extends Mod {
   public AntiEffects() {
      super("AntiEffects", Category.RENDER);
   }

   @EventTarget
   private void onUpdate(EventUpdate event) {
      if (mc.thePlayer.isPotionActive(Potion.blindness)) {
         mc.thePlayer.removePotionEffect(Potion.blindness.id);
      }

      if (mc.thePlayer.isPotionActive(Potion.confusion)) {
         mc.thePlayer.removePotionEffect(Potion.confusion.id);
      }

   }
}
