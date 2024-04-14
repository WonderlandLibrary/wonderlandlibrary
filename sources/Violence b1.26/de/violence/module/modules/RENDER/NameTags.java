package de.violence.module.modules.RENDER;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;

public class NameTags extends Module {
   private VSetting sOpactiy = new VSetting("Opacity", this, 1.0D, 100.0D, 1.0D, true);
   private VSetting bHealth = new VSetting("Health", this, false);
   private VSetting bRange = new VSetting("Scaled of distance", this, true);

   public NameTags() {
      super("NameTags", Category.RENDER);
   }
}
