package de.violence.module.modules.PLAYER;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.modules.COMBAT.Killaura;
import de.violence.module.ui.Category;
import java.util.Iterator;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.EnumFacing;

public class TntBlocker extends Module {
   private VSetting fuseTicks = new VSetting("Fuse Ticks", this, 0.0D, 20.0D, 0.0D, true);
   private boolean hasBlocked;

   public TntBlocker() {
      super("TntBlocker", Category.PLAYER);
   }

   public void onUpdate() {
      this.nameAddon = "Ticks: " + this.fuseTicks.getCurrent();
      super.onUpdate();
      boolean foundTnt = false;
      if(!Killaura.shouldAction) {
         Iterator var3 = this.mc.theWorld.loadedEntityList.iterator();

         while(var3.hasNext()) {
            Entity e = (Entity)var3.next();
            if(e instanceof EntityTNTPrimed) {
               EntityTNTPrimed entityTNTPrimed = (EntityTNTPrimed)e;
               if((double)this.mc.thePlayer.getDistanceToEntity(e) <= 4.0D) {
                  foundTnt = true;
                  if((double)entityTNTPrimed.fuse >= this.fuseTicks.getCurrent()) {
                     this.blockItem();
                  }
               }
            }
         }

         if(!foundTnt && this.hasBlocked) {
            this.unblockItem();
         }

      }
   }

   private void unblockItem() {
      this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, this.mc.thePlayer.getPosition(), EnumFacing.DOWN));
      ItemRenderer.isBlocking = false;
      this.hasBlocked = false;
   }

   private void blockItem() {
      if(this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
         ItemRenderer.isBlocking = true;
         this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.getPosition(), 0, this.mc.thePlayer.getCurrentEquippedItem(), 0.0F, 0.0F, 0.0F));
         this.hasBlocked = true;
      }

   }
}
