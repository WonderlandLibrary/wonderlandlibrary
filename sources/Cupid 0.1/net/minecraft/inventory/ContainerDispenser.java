package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ContainerDispenser extends Container {
  private IInventory dispenserInventory;
  
  public ContainerDispenser(IInventory playerInventory, IInventory dispenserInventoryIn) {
    this.dispenserInventory = dispenserInventoryIn;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++)
        addSlotToContainer(new Slot(dispenserInventoryIn, j + i * 3, 62 + j * 18, 17 + i * 18)); 
    } 
    for (int k = 0; k < 3; k++) {
      for (int i1 = 0; i1 < 9; i1++)
        addSlotToContainer(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18)); 
    } 
    for (int l = 0; l < 9; l++)
      addSlotToContainer(new Slot(playerInventory, l, 8 + l * 18, 142)); 
  }
  
  public boolean canInteractWith(EntityPlayer playerIn) {
    return this.dispenserInventory.isUseableByPlayer(playerIn);
  }
  
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
    ItemStack itemstack = null;
    Slot slot = this.inventorySlots.get(index);
    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      if (index < 9) {
        if (!mergeItemStack(itemstack1, 9, 45, true))
          return null; 
      } else if (!mergeItemStack(itemstack1, 0, 9, false)) {
        return null;
      } 
      if (itemstack1.stackSize == 0) {
        slot.putStack((ItemStack)null);
      } else {
        slot.onSlotChanged();
      } 
      if (itemstack1.stackSize == itemstack.stackSize)
        return null; 
      slot.onPickupFromSlot(playerIn, itemstack1);
    } 
    return itemstack;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\inventory\ContainerDispenser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */