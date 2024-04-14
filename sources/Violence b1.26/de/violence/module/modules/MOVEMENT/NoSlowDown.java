package de.violence.module.modules.MOVEMENT;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import java.util.Arrays;

public class NoSlowDown extends Module {
   private VSetting slowDownMode = new VSetting("Mode", this, Arrays.asList(new String[]{"Vanilla", "AAC"}), "Vanilla");

   public NoSlowDown() {
      super("NoSlowDown", Category.MOVEMENT);
   }

   public void onUpdate() {
      this.nameAddon = this.slowDownMode.getActiveMode();
      super.onUpdate();
   }
}
