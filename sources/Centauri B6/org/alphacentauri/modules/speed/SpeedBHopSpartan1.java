package org.alphacentauri.modules.speed;

import net.minecraft.client.entity.EntityPlayerSP;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventSetback;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.util.MoveUtils;
import org.alphacentauri.modules.speed.Speed;

public class SpeedBHopSpartan1 extends Speed {
   private int setback;

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         EntityPlayerSP player = AC.getMC().getPlayer();
         if(player.isSneaking() || player.isInLiquid()) {
            return;
         }

         if(player.hasMovementInput()) {
            if(player.onGround) {
               if(Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ) < 0.2D && this.setback > 10) {
                  MoveUtils.setSpeed((double)(0.3F - AC.getRandom().nextFloat() / 100.0F));
               }

               player.jump();
            } else {
               MoveUtils.setSpeed((double)((float)Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ) * 1.011F));
            }
         } else {
            player.motionX = 0.0D;
            player.motionZ = 0.0D;
         }

         ++this.setback;
      } else if(event instanceof EventSetback) {
         this.setback = 0;
      }

   }
}
