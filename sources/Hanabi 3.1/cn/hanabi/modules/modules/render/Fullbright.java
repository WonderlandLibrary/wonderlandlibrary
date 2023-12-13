package cn.hanabi.modules.modules.render;

import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Fullbright extends Mod {
   private final Value mode = new Value("Fullbright", "Mode", 0);
   public float oldGammaSetting;

   public Fullbright() {
      super("Fullbright", Category.RENDER);
      this.mode.addValue("Gamma");
      this.mode.addValue("Potion");
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (this.mode.isCurrentMode("Gamma")) {
         this.setDisplayName("Gamma");
         this.oldGammaSetting = mc.gameSettings.gammaSetting;
         mc.gameSettings.gammaSetting = 1000.0F;
      }

      if (this.mode.isCurrentMode("Potion")) {
         this.setDisplayName("Potion");
         mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 4000, 1));
      }

   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      mc.gameSettings.gammaSetting = this.oldGammaSetting;
      super.onDisable();
   }
}
