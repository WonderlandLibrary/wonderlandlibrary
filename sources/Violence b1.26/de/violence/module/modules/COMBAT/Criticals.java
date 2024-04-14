package de.violence.module.modules.COMBAT;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import java.util.Arrays;

public class Criticals extends Module {
   private VSetting crackSize = new VSetting("Crack Size", this, 1.0D, 10.0D, 1.0D, true);
   private VSetting criticalsMode = new VSetting("Mode", this, Arrays.asList(new String[]{"Packet", "Legit Jump", "Crack", "Minihop"}), "Packet", Arrays.asList(new String[]{"Crack Size-Criticals-Crack"}));

   public Criticals() {
      super("Criticals", Category.COMBAT);
   }

   public void onUpdate() {
      this.nameAddon = this.criticalsMode.getActiveMode();
      super.onUpdate();
   }
}
