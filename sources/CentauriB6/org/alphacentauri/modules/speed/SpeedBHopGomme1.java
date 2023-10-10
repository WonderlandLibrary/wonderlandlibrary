package org.alphacentauri.modules.speed;

import net.minecraft.client.entity.EntityPlayerSP;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.util.MoveUtils;
import org.alphacentauri.modules.speed.Speed;

public class SpeedBHopGomme1 extends Speed {
   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         EntityPlayerSP player = AC.getMC().getPlayer();
         if(player.isSneaking() || player.isInLiquid()) {
            return;
         }

         if(player.hasMovementInput()) {
            if(player.onGround) {
               MoveUtils.setSpeed(0.5600000023841858D);
               player.motionY = 0.41999998688697815D;
            } else {
               MoveUtils.setSpeed((double)((float)Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ) * ((double)player.fallDistance > 0.4D?1.0F:1.03F)));
            }
         } else {
            player.motionX = 0.0D;
            player.motionZ = 0.0D;
         }
      }

   }
}
