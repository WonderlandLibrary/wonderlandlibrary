package de.violence.module.modules.SETTINGS;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;

public class TabGuiMod extends Module {
   private VSetting bToggled = new VSetting("Enabled", this, false);

   public TabGuiMod() {
      super("TabGui", Category.SETTINGS);
   }
}
