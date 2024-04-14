package de.violence.module.modules.RENDER;

import de.violence.module.Module;
import de.violence.module.ui.Category;

public class Fullbright extends Module {
   public Fullbright() {
      super("Fullbright", Category.RENDER);
   }

   public void onToggle() {
      this.mc.gameSettings.gammaSetting = 10.0F;
      super.onToggle();
   }

   public void onDisable() {
      this.mc.gameSettings.gammaSetting = 1.0F;
      super.onDisable();
   }
}
