package de.violence.module.modules.PLAYER;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.modules.COMBAT.Killaura;
import de.violence.module.ui.Category;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.EnumFacing;

public class FastBow extends Module {
   private VSetting sTicks = new VSetting("Ticks to shoot", this, 0.0D, 25.0D, 0.0D, true);

   public FastBow() {
      super("FastBow", Category.PLAYER);
   }

   public void onUpdate() {
      this.nameAddon = "Ticks: " + this.sTicks.getCurrent();
      super.onUpdate();
      if(!Killaura.shouldAction) {
         if(this.mc.thePlayer.isUsingItem() && this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) {
            int useTicks = this.mc.thePlayer.getItemInUseDuration();
            if((double)useTicks >= this.sTicks.getCurrent()) {
               for(int i = 0; i < 20; ++i) {
                  this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
               }

               this.shootArrow();
            }
         }

      }
   }

   private void shootArrow() {
      this.mc.thePlayer.swingItem();
      this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, this.mc.thePlayer.getPosition(), EnumFacing.DOWN));
   }
}
