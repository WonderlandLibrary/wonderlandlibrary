package org.alphacentauri.modules;

import net.minecraft.client.entity.EntityPlayerSP;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.util.MoveUtils;

public class ModuleStrafe extends Module implements EventListener {
   public ModuleStrafe() {
      super("Strafe", "Better movement", new String[]{"strafe"}, Module.Category.Movement, 4249244);
   }

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         EntityPlayerSP player = AC.getMC().getPlayer();
         MoveUtils.setSpeed(Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ));
      }

   }
}
