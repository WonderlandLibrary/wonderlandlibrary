package net.minecraft.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public abstract interface ISidedInventory
  extends IInventory
{
  public abstract int[] getSlotsForFace(EnumFacing paramEnumFacing);
  
  public abstract boolean canInsertItem(int paramInt, ItemStack paramItemStack, EnumFacing paramEnumFacing);
  
  public abstract boolean canExtractItem(int paramInt, ItemStack paramItemStack, EnumFacing paramEnumFacing);
}
