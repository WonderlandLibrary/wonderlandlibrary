package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorldNameable;

public interface IInventory extends IWorldNameable {
   boolean isUseableByPlayer(EntityPlayer var1);

   ItemStack getStackInSlot(int var1);

   void clear();

   void openInventory(EntityPlayer var1);

   boolean isItemValidForSlot(int var1, ItemStack var2);

   ItemStack removeStackFromSlot(int var1);

   int getField(int var1);

   void markDirty();

   int getInventoryStackLimit();

   void setField(int var1, int var2);

   ItemStack decrStackSize(int var1, int var2);

   int getFieldCount();

   int getSizeInventory();

   void closeInventory(EntityPlayer var1);

   void setInventorySlotContents(int var1, ItemStack var2);
}
