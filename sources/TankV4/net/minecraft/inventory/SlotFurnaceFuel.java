package net.minecraft.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class SlotFurnaceFuel extends Slot {
   public int getItemStackLimit(ItemStack var1) {
      return var1 != false ? 1 : super.getItemStackLimit(var1);
   }

   public boolean isItemValid(ItemStack var1) {
      return TileEntityFurnace.isItemFuel(var1) || var1 == false;
   }

   public SlotFurnaceFuel(IInventory var1, int var2, int var3, int var4) {
      super(var1, var2, var3, var4);
   }
}
