package org.alphacentauri.modules;

import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventRender3D;
import org.alphacentauri.management.modules.Module;

public class ModuleNoBob extends Module implements EventListener {
   public ModuleNoBob() {
      super("NoBob", "Toggle View Bobbing", new String[]{"nobob"}, Module.Category.Misc, 3759448);
   }

   public void onEvent(Event event) {
      if(event instanceof EventRender3D) {
         AC.getMC().getPlayer().distanceWalkedModified = 0.0F;
      }

   }
}
