package de.violence.module.modules.RENDER;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;

public class NameProtect extends Module {
   private VSetting bOtherPlayers = new VSetting("Protect Other", this, false);

   public NameProtect() {
      super("NameProtect", Category.RENDER);
   }
}
