package org.alphacentauri.modules;

import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;

public class ModuleTimer extends Module implements EventListener {
   private Property speed = new Property(this, "Speed", Float.valueOf(1.1F));

   public ModuleTimer() {
      super("Timer", "Makes your game faster", new String[]{"timer"}, Module.Category.Movement, 3569450);
   }

   public void setEnabledSilent(boolean enabled) {
      if(!enabled) {
         AC.getMC().timer.timerSpeed = 1.0F;
      }

      super.setEnabledSilent(enabled);
   }

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         AC.getMC().timer.timerSpeed = ((Float)this.speed.value).floatValue();
      }

   }
}
