package de.violence.module.modules.MOVEMENT;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;

public class Glide extends Module {
   private VSetting sMotion = new VSetting("Motion", this, 0.0D, 5.0D, 0.0D, false);

   public Glide() {
      super("Glide", Category.MOVEMENT);
   }

   public void onUpdate() {
      this.mc.thePlayer.motionY = -this.sMotion.getCurrent() / 10.0D;
      super.onUpdate();
   }
}
