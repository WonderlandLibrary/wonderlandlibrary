package de.violence.module.modules.RENDER;

import de.violence.module.Module;
import de.violence.module.ui.Category;

public class ItemESP extends Module {
   public ItemESP() {
      super("ItemESP", Category.RENDER);
   }

   public void onUpdate() {
      this.mc.gameSettings.ofFastRender = false;
      super.onUpdate();
   }
}
