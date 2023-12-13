package org.alphacentauri.modules;

import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.modules.Module;

public class ModuleDebug extends Module implements EventListener {
   public ModuleDebug() {
      super("Debug", "Debug module", new String[]{"debug"}, Module.Category.Misc, 16777215);
   }

   public void onEvent(Event event) {
   }
}
