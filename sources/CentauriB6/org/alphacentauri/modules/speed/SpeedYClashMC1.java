package org.alphacentauri.modules.speed;

import net.minecraft.client.entity.EntityPlayerSP;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.util.MoveUtils;
import org.alphacentauri.modules.speed.Speed;

public class SpeedYClashMC1 extends Speed {
   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         EntityPlayerSP player = AC.getMC().getPlayer();
         if(player.isSneaking()) {
            return;
         }

         if(!player.hasMovementInput()) {
            player.motionX = 0.0D;
            player.motionZ = 0.0D;
            return;
         }

         if(player.onGround) {
            MoveUtils.setSpeed(0.5299999713897705D);
            player.motionY = 0.2D;
         } else {
            MoveUtils.setSpeed(0.3499999940395355D);
            player.motionY = -1.0D;
         }
      }

   }
}
