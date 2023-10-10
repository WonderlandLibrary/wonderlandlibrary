package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorldNameable;

public interface IInventory extends IWorldNameable {
   void clear();

   int getField(int var1);

   void markDirty();

   void setField(int var1, int var2);

   int getFieldCount();

   void closeInventory(EntityPlayer var1);

   void setInventorySlotContents(int var1, ItemStack var2);

   int getSizeInventory();

   ItemStack getStackInSlot(int var1);

   void openInventory(EntityPlayer var1);

   ItemStack decrStackSize(int var1, int var2);

   int getInventoryStackLimit();

   boolean isItemValidForSlot(int var1, ItemStack var2);

   ItemStack removeStackFromSlot(int var1);

   boolean isUseableByPlayer(EntityPlayer var1);
}
