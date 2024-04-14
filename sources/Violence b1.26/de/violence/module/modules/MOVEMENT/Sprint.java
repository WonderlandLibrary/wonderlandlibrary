package de.violence.module.modules.MOVEMENT;

import de.violence.module.Module;
import de.violence.module.ui.Category;

public class Sprint extends Module {
   public Sprint() {
      super("Sprint", Category.MOVEMENT);
   }

   public void onUpdate() {
      this.mc.gameSettings.keyBindSprint.pressed = true;
      super.onUpdate();
   }

   public void onDisable() {
      this.mc.gameSettings.keyBindSprint.pressed = false;
      super.onDisable();
   }
}
