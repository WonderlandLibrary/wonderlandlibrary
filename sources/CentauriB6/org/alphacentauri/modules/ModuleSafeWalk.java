package org.alphacentauri.modules;

import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.BlockUtils;

public class ModuleSafeWalk extends Module implements EventListener {
   private Property advSafeWalk = new Property(this, "AdvMode", Boolean.valueOf(true));
   private Property safeFallDist = new Property(this, "SafeFallDist", Double.valueOf(3.0D));
   private double lastOnGroundX;
   private double lastOnGroundY;
   private double lastOnGroundZ;
   private boolean lastOnGroundExists = false;

   public ModuleSafeWalk() {
      super("SafeWalk", "Dont walk off the edge", new String[]{"safewalk"}, Module.Category.Movement, 15070908);
   }

   public void onEvent(Event event) {
      if(((Boolean)this.advSafeWalk.value).booleanValue() && event instanceof EventTick) {
         EntityPlayerSP player = AC.getMC().getPlayer();
         if(player.onGround) {
            this.lastOnGroundExists = true;
            this.lastOnGroundX = player.lastTickPosX;
            this.lastOnGroundY = player.lastTickPosY;
            this.lastOnGroundZ = player.lastTickPosZ;
         }

         if(player.fallDistance == 0.0F) {
            return;
         }

         boolean setback = true;
         if(((Double)this.safeFallDist.value).doubleValue() < 0.0D) {
            this.safeFallDist.value = Double.valueOf(0.0D);
         }

         for(float dY = 0.0F; (double)dY > -((Double)this.safeFallDist.value).doubleValue(); dY -= 0.4F) {
            if(BlockUtils.getBlock(dY).getBlock().getMaterial() != Material.air) {
               setback = false;
               break;
            }
         }

         if(setback && this.lastOnGroundExists) {
            player.setPosition(this.lastOnGroundX, this.lastOnGroundY, this.lastOnGroundZ);
         }
      }

   }
}
