package net.minecraft.item;

public class ItemBook extends Item {
   public int getItemEnchantability() {
      return 1;
   }

   public boolean isItemTool(ItemStack var1) {
      return var1.stackSize == 1;
   }
}
