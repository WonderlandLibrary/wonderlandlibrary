package org.alphacentauri.modules;

import net.minecraft.client.multiplayer.WorldClient;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;

public class ModuleNoWeather extends Module implements EventListener {
   public ModuleNoWeather() {
      super("NoWeather", "Get rid of rain", new String[]{"noweather"}, Module.Category.Render, 5041271);
   }

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         WorldClient world = AC.getMC().getWorld();
         world.getWorldInfo().setRaining(true);
         world.setRainStrength(0.0F);
      }

   }
}
