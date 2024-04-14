package de.violence.module.modules.OTHER;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;

public class Hitbox extends Module {
   private VSetting vSetting = new VSetting("Expand", this, 0.2D, 1.0D, 0.2D, false);

   public Hitbox() {
      super("Hitbox", Category.OTHER);
   }

   public void onUpdate() {
      this.nameAddon = "Expand: " + this.vSetting.getCurrent();
      super.onUpdate();
   }
}
