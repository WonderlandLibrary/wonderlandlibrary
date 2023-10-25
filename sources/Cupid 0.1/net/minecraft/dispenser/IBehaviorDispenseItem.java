package net.minecraft.dispenser;

import net.minecraft.item.ItemStack;

public interface IBehaviorDispenseItem {
  public static final IBehaviorDispenseItem itemDispenseBehaviorProvider = new IBehaviorDispenseItem() {
      public ItemStack dispense(IBlockSource source, ItemStack stack) {
        return stack;
      }
    };
  
  ItemStack dispense(IBlockSource paramIBlockSource, ItemStack paramItemStack);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\dispenser\IBehaviorDispenseItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */