package de.violence.module.modules.PLAYER;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.modules.COMBAT.Killaura;
import de.violence.module.ui.Category;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastEat extends Module {
   private VSetting sTicks = new VSetting("Ticks to eat", this, 0.0D, 25.0D, 0.0D, true);

   public FastEat() {
      super("FastEat", Category.PLAYER);
   }

   public void onUpdate() {
      this.nameAddon = "Ticks: " + this.sTicks.getCurrent();
      super.onUpdate();
      if(!Killaura.shouldAction) {
         if(this.mc.thePlayer.isUsingItem() && this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemFood) {
            int useTicks = this.mc.thePlayer.getItemInUseDuration();
            if((double)useTicks >= this.sTicks.getCurrent()) {
               this.eatItem();
            }
         }

      }
   }

   private void eatItem() {
      for(int i = 0; i < 15; ++i) {
         this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
      }

   }
}
