package net.minecraft.dispenser;

import net.minecraft.item.ItemStack;

public interface IBehaviorDispenseItem {
   IBehaviorDispenseItem itemDispenseBehaviorProvider = new IBehaviorDispenseItem() {
      public ItemStack dispense(IBlockSource var1, ItemStack var2) {
         return var2;
      }
   };

   ItemStack dispense(IBlockSource var1, ItemStack var2);
}
