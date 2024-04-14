package de.violence.module.modules.MOVEMENT;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;

public class AirJump extends Module {
   private VSetting bPressKey = new VSetting("On Keypress", this, false);
   private boolean hasJumped;

   public AirJump() {
      super("AirJump", Category.MOVEMENT);
   }

   public void onUpdate() {
      if(this.mc.gameSettings.keyBindJump.pressed != this.hasJumped || !this.bPressKey.isToggled()) {
         this.mc.thePlayer.onGround = true;
      }

      this.hasJumped = this.mc.gameSettings.keyBindJump.pressed;
      super.onUpdate();
   }
}
