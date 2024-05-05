package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorldNameable;

public abstract interface IInventory
  extends IWorldNameable
{
  public abstract int getSizeInventory();
  
  public abstract ItemStack getStackInSlot(int paramInt);
  
  public abstract ItemStack decrStackSize(int paramInt1, int paramInt2);
  
  public abstract ItemStack getStackInSlotOnClosing(int paramInt);
  
  public abstract void setInventorySlotContents(int paramInt, ItemStack paramItemStack);
  
  public abstract int getInventoryStackLimit();
  
  public abstract void markDirty();
  
  public abstract boolean isUseableByPlayer(EntityPlayer paramEntityPlayer);
  
  public abstract void openInventory(EntityPlayer paramEntityPlayer);
  
  public abstract void closeInventory(EntityPlayer paramEntityPlayer);
  
  public abstract boolean isItemValidForSlot(int paramInt, ItemStack paramItemStack);
  
  public abstract int getField(int paramInt);
  
  public abstract void setField(int paramInt1, int paramInt2);
  
  public abstract int getFieldCount();
  
  public abstract void clearInventory();
}
