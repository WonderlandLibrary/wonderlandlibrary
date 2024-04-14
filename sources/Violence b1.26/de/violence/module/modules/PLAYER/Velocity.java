package de.violence.module.modules.PLAYER;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import java.util.Arrays;

public class Velocity extends Module {
   private VSetting bMotionX = new VSetting("Motion X", this, false, Arrays.asList(new String[]{"Reduce X-Velocity"}));
   private VSetting bMotionY = new VSetting("Motion Y", this, false, Arrays.asList(new String[]{"Reduce Y-Velocity"}));
   private VSetting bMotionZ = new VSetting("Motion Z", this, false, Arrays.asList(new String[]{"Reduce Z-Velocity"}));
   private VSetting sXReducer = new VSetting("Reduce X", this, 0.0D, 100.0D, 0.0D, false);
   private VSetting sYReducer = new VSetting("Reduce Y", this, 0.0D, 100.0D, 0.0D, false);
   private VSetting sZReducer = new VSetting("Reduce Z", this, 0.0D, 100.0D, 0.0D, false);
   private VSetting sAACBoost = new VSetting("Boost Speed", this, 0.0D, 5.0D, 0.0D, false);
   private VSetting bBoost = new VSetting("Boost", this, false, Arrays.asList(new String[]{"Boost Speed-Velocity", "!Move to last Pos-Velocity"}));
   private VSetting bMoveToLastPos = new VSetting("Move to last Pos", this, false, Arrays.asList(new String[]{"Boost-Velocity"}));
   private VSetting sExtraBoost = new VSetting("Extra Boost", this, 0.0D, 0.5D, 0.0D, false);
   private VSetting bUltraBoost = new VSetting("Ultra Boost", this, false);
   private VSetting mMode = new VSetting("Mode", this, Arrays.asList(new String[]{"Custom", "AAC", "AAC Push Plus"}), "Custom", Arrays.asList(new String[]{"Motion X-Velocity-Custom", "Motion Y-Velocity-Custom", "Motion Z-Velocity-Custom", "Boost-Velocity-AAC", "Extra Boost-Velocity-AAC Push Plus", "Ultra Boost-Velocity-AAC Push Plus"}));

   public Velocity() {
      super("Velocity", Category.PLAYER);
   }

   public void onUpdate() {
      this.nameAddon = this.mMode.getActiveMode();
      super.onUpdate();
   }
}
