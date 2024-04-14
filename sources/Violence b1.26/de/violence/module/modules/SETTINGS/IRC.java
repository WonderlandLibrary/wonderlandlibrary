package de.violence.module.modules.SETTINGS;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;

public class IRC extends Module {
   private VSetting bEnabled = new VSetting("Enabled", this, false);
   private VSetting bAutoNick = new VSetting("Replace Nicknames", this, false);

   public IRC() {
      super("IRC", Category.SETTINGS);
   }
}
