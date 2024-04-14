package de.violence.module.modules.PLAYER;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import de.violence.ui.Timings;
import java.util.Arrays;

public class NoRightClickDelay extends Module {
   private VSetting bDelayed = new VSetting("Delayed", this, false, Arrays.asList(new String[]{"Delay Start-NoRightClickDelay", "Delay Stop-NoRightClickDelay"}));
   private VSetting sDelay = new VSetting("Delay", this, 0.0D, 10.0D, 0.0D, true);
   private VSetting sDelayStart = new VSetting("Delay Start", this, 0.0D, 2000.0D, 0.0D, true);
   private VSetting sDelayStop = new VSetting("Delay Stop", this, 0.0D, 4000.0D, 0.0D, true);
   private Timings timings = new Timings();

   public NoRightClickDelay() {
      super("NoRightClickDelay", Category.PLAYER);
   }

   public void onDisable() {
      this.mc.rightClickDelayTimer = 4;
      super.onDisable();
   }

   public void onUpdate() {
      this.nameAddon = "Delay: " + this.bDelayed.getCurrent();
      if(this.bDelayed.isToggled() && this.timings.hasReached((long)this.sDelayStart.getCurrent())) {
         this.mc.rightClickDelayTimer = 4;
         if(this.timings.hasReached((long)this.sDelayStop.getCurrent())) {
            this.timings.resetTimings();
         }

      } else {
         this.mc.rightClickDelayTimer = (int)this.sDelay.getCurrent();
         super.onUpdate();
      }
   }
}
