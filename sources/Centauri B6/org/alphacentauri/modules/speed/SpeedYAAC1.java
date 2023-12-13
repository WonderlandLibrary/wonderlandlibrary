package org.alphacentauri.modules.speed;

import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.util.BlockUtils;
import org.alphacentauri.management.util.MoveUtils;
import org.alphacentauri.modules.speed.Speed;

public class SpeedYAAC1 extends Speed {
   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         EntityPlayerSP player = AC.getMC().getPlayer();
         if(!player.hasMovementInput()) {
            return;
         }

         if(AC.getMC().gameSettings.keyBindSneak.isKeyDown()) {
            return;
         }

         if(player.isInLiquid() || BlockUtils.getBlock(-1.0F).getBlock().getMaterial().isLiquid()) {
            return;
         }

         if(player.isOnLadder()) {
            return;
         }

         if(BlockUtils.getBlock(-1.0F).getBlock().getMaterial() == Material.air && BlockUtils.getBlock(-2.0F).getBlock().getMaterial() == Material.air) {
            return;
         }

         if(player.onGround) {
            MoveUtils.setSpeed(0.5199999809265137D);
            player.motionY = 0.20000000298023224D;
         } else {
            MoveUtils.setSpeed(0.5199999809265137D);
            player.motionY = -9.0D;
         }
      }

   }
}
