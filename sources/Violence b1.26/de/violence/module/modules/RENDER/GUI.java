package de.violence.module.modules.RENDER;

import de.violence.gui.ClickGui;
import de.violence.module.Module;
import de.violence.module.ui.Category;

public class GUI extends Module {
   public GUI() {
      super("GUI", Category.RENDER);
   }

   public void onEnable() {
      this.mc.displayGuiScreen(new ClickGui());
      super.onEnable();
      this.onToggle();
   }
}
