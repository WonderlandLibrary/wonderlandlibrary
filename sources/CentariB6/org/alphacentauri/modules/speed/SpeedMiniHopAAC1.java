package org.alphacentauri.modules.speed;

import net.minecraft.client.entity.EntityPlayerSP;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventSetback;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.util.MoveUtils;
import org.alphacentauri.management.util.Timer;
import org.alphacentauri.modules.speed.Speed;

public class SpeedMiniHopAAC1 extends Speed {
   private Timer setback = new Timer();

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         EntityPlayerSP player = AC.getMC().getPlayer();
         if(!player.hasMovementInput()) {
            player.motionX = player.motionZ = 0.0D;
            return;
         }

         if(player.onGround) {
            if(this.setback.hasMSPassed(200L)) {
               MoveUtils.setSpeed(0.7200000286102295D);
            }

            player.motionY = 0.20000000298023224D;
         } else {
            MoveUtils.setSpeed(Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ));
         }
      } else if(event instanceof EventSetback) {
         this.setback.reset();
      }

   }
}
