package de.violence.module.modules.SETTINGS;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;

public class Model extends Module {
   private VSetting rotateModel = new VSetting("Rotate Model", this, false);

   public Model() {
      super("Model", Category.SETTINGS);
   }
}
