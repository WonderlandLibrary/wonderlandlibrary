package org.alphacentauri.modules.speed;

import net.minecraft.client.entity.EntityPlayerSP;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.util.MoveUtils;
import org.alphacentauri.modules.speed.Speed;

public class SpeedGroundGomme1 extends Speed {
   int i1;
   int i2;
   int i3;
   int i4;
   double d1;
   double d2;
   double d3;
   double d4;

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         EntityPlayerSP player = AC.getMC().getPlayer();
         if(!player.hasMovementInput()) {
            this.i1 = 0;
         }

         if((double)player.fallDistance < 0.1D) {
            MoveUtils.setSpeed((double)Math.min(this.i1++, 10) * 0.05D);
            if(player.onGround) {
               player.motionY = 0.1D;
            }
         }
      }

   }
}
