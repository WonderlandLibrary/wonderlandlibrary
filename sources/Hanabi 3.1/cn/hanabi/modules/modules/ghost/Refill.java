package cn.hanabi.modules.modules.ghost;

import cn.hanabi.events.EventPreMotion;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

public class Refill extends Mod {
   public Value delay = new Value("Refill", "Delay", 500.0, 0.0, 2000.0, 50.0);
   public Value Soup = new Value("Refill", "Soup", false);
   public Value Pot = new Value("Refill", "Pot", false);
   public Value onInv = new Value("Refill", "onInv", false);
   TimeHelper time = new TimeHelper();
   Item value;

   public Refill() {
      super("Refill", Category.GHOST);
   }

   public static boolean isHotbarFull() {
      for(int i = 0; i <= 36; ++i) {
         ItemStack itemstack = mc.thePlayer.inventory.getStackInSlot(i);
         if (itemstack == null) {
            return false;
         }
      }

      return true;
   }

   public static void refill(Item value) {
      for(int i = 9; i < 37; ++i) {
         ItemStack itemstack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
         if (itemstack != null && itemstack.getItem() == value) {
            mc.playerController.windowClick(0, i, 0, 1, mc.thePlayer);
            break;
         }
      }

   }

   @EventTarget
   public void onUpdate(EventPreMotion event) {
      if ((Boolean)this.Soup.getValue()) {
         this.value = Items.mushroom_stew;
      } else if ((Boolean)this.Pot.getValue()) {
         this.value = ItemPotion.getItemById(373);
      }

      this.refill();
   }

   private void refill() {
      if ((!(Boolean)this.onInv.getValue() || mc.currentScreen instanceof GuiInventory) && !isHotbarFull() && this.time.isDelayComplete((Double)this.delay.getValue())) {
         refill(this.value);
         this.time.reset();
      }

   }
}
