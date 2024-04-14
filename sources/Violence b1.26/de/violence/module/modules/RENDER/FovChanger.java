package de.violence.module.modules.RENDER;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;

public class FovChanger extends Module {
   private VSetting sFov = new VSetting("Fov", this, 0.0D, 3.0D, 0.0D, false);

   public FovChanger() {
      super("FovChanger", Category.RENDER);
   }
}
