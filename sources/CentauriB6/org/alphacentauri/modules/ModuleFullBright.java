package org.alphacentauri.modules;

import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventGetBlockLight;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.modules.Module;

public class ModuleFullBright extends Module implements EventListener {
   public ModuleFullBright() {
      super("FullBright", "Night Vision", new String[]{"fullbright"}, Module.Category.Render, 23439);
   }

   public void setEnabledSilent(boolean enabled) {
      super.setEnabledSilent(enabled);
      AC.getMC().renderGlobal.loadRenderers();
   }

   public void onEvent(Event event) {
      if(event instanceof EventGetBlockLight) {
         ((EventGetBlockLight)event).setLight(15);
      }

   }
}
