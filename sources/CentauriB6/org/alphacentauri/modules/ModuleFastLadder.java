package org.alphacentauri.modules;

import net.minecraft.block.BlockLadder;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import org.alphacentauri.AC;
import org.alphacentauri.management.bypass.AntiCheat;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.BlockUtils;

public class ModuleFastLadder extends Module implements EventListener {
   private Property speedMultiplierNCP = new Property(this, "SpeedMultiplierNCP", Float.valueOf(2.4F));

   public ModuleFastLadder() {
      super("FastLadder", "Climb ladders faster", new String[]{"fastladder"}, Module.Category.Movement, 5589967);
   }

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         EntityPlayerSP player = AC.getMC().getPlayer();
         if(this.getBypass() != AntiCheat.NCP && this.getBypass() != AntiCheat.Reflex) {
            if(this.getBypass() == AntiCheat.AAC) {
               if(BlockUtils.getBlockInfront(0.5F).getBlock() instanceof BlockLadder && (player.isCollidedHorizontally || !player.isEndOfLadder() && player.isOnLadder()) && !player.isSneaking() && !player.isOnLadder()) {
                  player.motionY = 0.5D;
               }
            } else if(this.getBypass() == AntiCheat.Vanilla) {
               if(player.onGround) {
                  return;
               }

               int counter = 0;

               while(player.isOnLadder() && player.motionY != 0.0D && (player.isAboveLadder() || player.motionY >= 0.0D)) {
                  player.setPosition(player.posX, player.posY + (double)(player.motionY > 0.0D?1:-1), player.posZ);
                  ++counter;
                  if(counter == 9) {
                     player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(player.posX, player.posY, player.posZ, true));
                     counter = 0;
                  }
               }
            }
         } else if(player.motionY > 0.0D && player.isOnLadder() && player.hasMovementInput()) {
            player.motionY *= (double)((Float)this.speedMultiplierNCP.value).floatValue();
         }
      }

   }
}
