package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerMerchant extends Container {
   private final World theWorld;
   private InventoryMerchant merchantInventory;
   private IMerchant theMerchant;

   public void onContainerClosed(EntityPlayer var1) {
      super.onContainerClosed(var1);
      this.theMerchant.setCustomer((EntityPlayer)null);
      super.onContainerClosed(var1);
      if (!this.theWorld.isRemote) {
         ItemStack var2 = this.merchantInventory.removeStackFromSlot(0);
         if (var2 != null) {
            var1.dropPlayerItemWithRandomChoice(var2, false);
         }

         var2 = this.merchantInventory.removeStackFromSlot(1);
         if (var2 != null) {
            var1.dropPlayerItemWithRandomChoice(var2, false);
         }
      }

   }

   public ContainerMerchant(InventoryPlayer var1, IMerchant var2, World var3) {
      this.theMerchant = var2;
      this.theWorld = var3;
      this.merchantInventory = new InventoryMerchant(var1.player, var2);
      this.addSlotToContainer(new Slot(this.merchantInventory, 0, 36, 53));
      this.addSlotToContainer(new Slot(this.merchantInventory, 1, 62, 53));
      this.addSlotToContainer(new SlotMerchantResult(var1.player, var2, this.merchantInventory, 2, 120, 53));

      int var4;
      for(var4 = 0; var4 < 3; ++var4) {
         for(int var5 = 0; var5 < 9; ++var5) {
            this.addSlotToContainer(new Slot(var1, var5 + var4 * 9 + 9, 8 + var5 * 18, 84 + var4 * 18));
         }
      }

      for(var4 = 0; var4 < 9; ++var4) {
         this.addSlotToContainer(new Slot(var1, var4, 8 + var4 * 18, 142));
      }

   }

   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack var3 = null;
      Slot var4 = (Slot)this.inventorySlots.get(var2);
      if (var4 != null && var4.getHasStack()) {
         ItemStack var5 = var4.getStack();
         var3 = var5.copy();
         if (var2 == 2) {
            if (!this.mergeItemStack(var5, 3, 39, true)) {
               return null;
            }

            var4.onSlotChange(var5, var3);
         } else if (var2 != 0 && var2 != 1) {
            if (var2 >= 3 && var2 < 30) {
               if (!this.mergeItemStack(var5, 30, 39, false)) {
                  return null;
               }
            } else if (var2 >= 30 && var2 < 39 && !this.mergeItemStack(var5, 3, 30, false)) {
               return null;
            }
         } else if (!this.mergeItemStack(var5, 3, 39, false)) {
            return null;
         }

         if (var5.stackSize == 0) {
            var4.putStack((ItemStack)null);
         } else {
            var4.onSlotChanged();
         }

         if (var5.stackSize == var3.stackSize) {
            return null;
         }

         var4.onPickupFromSlot(var1, var5);
      }

      return var3;
   }

   public void onCraftGuiOpened(ICrafting var1) {
      super.onCraftGuiOpened(var1);
   }

   public void detectAndSendChanges() {
      super.detectAndSendChanges();
   }

   public void updateProgressBar(int var1, int var2) {
   }

   public boolean canInteractWith(EntityPlayer var1) {
      return this.theMerchant.getCustomer() == var1;
   }

   public void setCurrentRecipeIndex(int var1) {
      this.merchantInventory.setCurrentRecipeIndex(var1);
   }

   public void onCraftMatrixChanged(IInventory var1) {
      this.merchantInventory.resetRecipeAndSlots();
      super.onCraftMatrixChanged(var1);
   }

   public InventoryMerchant getMerchantInventory() {
      return this.merchantInventory;
   }
}
