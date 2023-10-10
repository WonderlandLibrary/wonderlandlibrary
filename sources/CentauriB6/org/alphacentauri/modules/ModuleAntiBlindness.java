package org.alphacentauri.modules;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;

public class ModuleAntiBlindness extends Module implements EventListener {
   public ModuleAntiBlindness() {
      super("AntiBlindness", "Removes Blindness and Nausea", new String[]{"antiblindness", "antiblind"}, Module.Category.Render, 7756051);
   }

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         EntityPlayerSP player = AC.getMC().getPlayer();
         if(player.isPotionActive(Potion.blindness)) {
            player.removePotionEffect(Potion.blindness.id);
         }

         if(player.isPotionActive(Potion.confusion)) {
            player.removePotionEffect(Potion.confusion.id);
         }
      }

   }
}
