package org.alphacentauri.modules;

import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventRenderString;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;

public class ModuleFakeName extends Module implements EventListener {
   private Property name = new Property(this, "Name", "L33T_H4X0R");

   public ModuleFakeName() {
      super("FakeName", "Changes your name", new String[]{"fakename"}, Module.Category.Misc, 11070720);
   }

   public void onEvent(Event event) {
      if(event instanceof EventRenderString) {
         if(AC.getMC().getWorld() == null) {
            return;
         }

         if(AC.getMC().session == null) {
            return;
         }

         ((EventRenderString)event).setText(((EventRenderString)event).getText().replace(AC.getMC().session.getUsername(), (CharSequence)this.name.value));
      }

   }
}
