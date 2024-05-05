package net.minecraft.inventory;

import java.util.List;
import net.minecraft.item.ItemStack;

public abstract interface ICrafting
{
  public abstract void updateCraftingInventory(Container paramContainer, List paramList);
  
  public abstract void sendSlotContents(Container paramContainer, int paramInt, ItemStack paramItemStack);
  
  public abstract void sendProgressBarUpdate(Container paramContainer, int paramInt1, int paramInt2);
  
  public abstract void func_175173_a(Container paramContainer, IInventory paramIInventory);
}
