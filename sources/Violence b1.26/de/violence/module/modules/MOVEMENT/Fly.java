package de.violence.module.modules.MOVEMENT;

import de.violence.module.Module;
import de.violence.module.ui.Category;

public class Fly extends Module {
   public Fly() {
      super("Fly", Category.MOVEMENT);
   }

   public void onDisable() {
      this.mc.thePlayer.capabilities.isFlying = false;
      this.mc.thePlayer.capabilities.allowFlying = false;
      super.onDisable();
   }

   public void onUpdate() {
      this.mc.thePlayer.capabilities.isFlying = true;
      this.mc.thePlayer.capabilities.allowFlying = true;
      super.onUpdate();
   }
}
