package de.violence.module.modules.RENDER;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import java.util.Arrays;

public class ESP extends Module {
   private VSetting bTeamColor = new VSetting("Team Color", this, false);
   private VSetting mMode = new VSetting("Mode", this, Arrays.asList(new String[]{"Glow", "Corners", "Outline", "Inside"}), "Glow", Arrays.asList(new String[]{"!Team Color-ESP-Corners"}));

   public ESP() {
      super("ESP", Category.RENDER);
   }

   public void onUpdate() {
      this.nameAddon = this.mMode.getActiveMode();
      this.mc.gameSettings.ofFastRender = false;
      super.onUpdate();
   }
}
