package org.alphacentauri.modules;

import net.minecraft.client.entity.EntityPlayerSP;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;

public class ModuleDolphin extends Module implements EventListener {
   public ModuleDolphin() {
      super("Dolphin", "Stay above water", new String[]{"dolphin"}, Module.Category.Movement, 7640852);
   }

   public void onEvent(Event event) {
      if(event instanceof EventTick && AC.getMC().getPlayer().isInWater() && !AC.getMC().gameSettings.keyBindSneak.isKeyDown()) {
         EntityPlayerSP var10000 = AC.getMC().getPlayer();
         var10000.motionY += 0.03999999910593033D;
      }

   }
}
