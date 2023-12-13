package org.alphacentauri.modules;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import org.alphacentauri.AC;
import org.alphacentauri.management.bypass.AntiCheat;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventStep;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.util.MoveUtils;
import org.alphacentauri.management.util.Timer;

public class ModuleStep extends Module implements EventListener {
   private boolean isStepping = false;
   private Timer lastStep = new Timer();

   public ModuleStep() {
      super("Step", "Step 1 block", new String[]{"step"}, Module.Category.Movement, 7048503);
   }

   public void setEnabledSilent(boolean enabled) {
      if(!enabled) {
         AC.getMC().getPlayer().stepHeight = 0.5F;
      }

      super.setEnabledSilent(enabled);
   }

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         if(this.isStepping) {
            return;
         }

         EntityPlayerSP player = AC.getMC().getPlayer();
         if(this.getBypass() == AntiCheat.Vanilla && !player.isSneaking()) {
            player.stepHeight = 9.9F;
         } else if(this.getBypass() != AntiCheat.NCP && this.getBypass() != AntiCheat.AAC && this.getBypass() != AntiCheat.Reflex && (this.getBypass() != AntiCheat.Vanilla || !player.isSneaking())) {
            if(player.onGround && player.isCollidedVertically && this.getBypass() == AntiCheat.Spartan && this.lastStep.hasMSPassed(250L)) {
               player.jump();
               this.lastStep.reset();
               this.isStepping = true;

               for(int i = 0; i < 4; ++i) {
                  player.onUpdate();
               }

               this.isStepping = false;
            }
         } else {
            player.stepHeight = 1.1F;
         }
      } else if(event instanceof EventStep && this.getBypass() != AntiCheat.Vanilla && this.getBypass() != AntiCheat.Spartan) {
         EntityPlayerSP player = AC.getMC().getPlayer();
         double stepAmount = ((EventStep)event).getStepAmount();
         if(stepAmount == 0.5D) {
            return;
         }

         if(stepAmount < 1.0D) {
            player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(player.posX, player.posY + 0.42D, player.posZ, false));
            player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(player.posX, player.posY + 0.75D, player.posZ, false));
            MoveUtils.tpRel(0.0D, 1.0D, 0.0D);
            return;
         }

         player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(player.posX, player.posY + 0.42D, player.posZ, false));
         player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(player.posX, player.posY + 0.75D, player.posZ, false));
      }

   }
}
