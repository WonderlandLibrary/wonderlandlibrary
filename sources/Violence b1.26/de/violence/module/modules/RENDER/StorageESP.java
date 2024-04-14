package de.violence.module.modules.RENDER;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import java.util.Arrays;

public class StorageESP extends Module {
   private VSetting mMode = new VSetting("Mode", this, Arrays.asList(new String[]{"Glow", "Outline", "Inside"}), "Glow");

   public StorageESP() {
      super("StorageESP", Category.RENDER);
   }

   public void onUpdate() {
      this.nameAddon = this.mMode.getActiveMode();
      this.mc.gameSettings.ofFastRender = false;
      super.onUpdate();
   }
}
