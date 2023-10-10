package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ContainerPlayer extends Container {
   public boolean isLocalWorld;
   private final EntityPlayer thePlayer;
   public IInventory craftResult = new InventoryCraftResult();
   public InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);

   public boolean canInteractWith(EntityPlayer var1) {
      return true;
   }

   public boolean canMergeSlot(ItemStack var1, Slot var2) {
      return var2.inventory != this.craftResult && super.canMergeSlot(var1, var2);
   }

   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack var3 = null;
      Slot var4 = (Slot)this.inventorySlots.get(var2);
      if (var4 != null && var4.getHasStack()) {
         ItemStack var5 = var4.getStack();
         var3 = var5.copy();
         if (var2 == 0) {
            if (!this.mergeItemStack(var5, 9, 45, true)) {
               return null;
            }

            var4.onSlotChange(var5, var3);
         } else if (var2 >= 1 && var2 < 5) {
            if (!this.mergeItemStack(var5, 9, 45, false)) {
               return null;
            }
         } else if (var2 >= 5 && var2 < 9) {
            if (!this.mergeItemStack(var5, 9, 45, false)) {
               return null;
            }
         } else if (var3.getItem() instanceof ItemArmor && !((Slot)this.inventorySlots.get(5 + ((ItemArmor)var3.getItem()).armorType)).getHasStack()) {
            int var6 = 5 + ((ItemArmor)var3.getItem()).armorType;
            if (!this.mergeItemStack(var5, var6, var6 + 1, false)) {
               return null;
            }
         } else if (var2 >= 9 && var2 < 36) {
            if (!this.mergeItemStack(var5, 36, 45, false)) {
               return null;
            }
         } else if (var2 >= 36 && var2 < 45) {
            if (!this.mergeItemStack(var5, 9, 36, false)) {
               return null;
            }
         } else if (!this.mergeItemStack(var5, 9, 45, false)) {
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

   public void onContainerClosed(EntityPlayer var1) {
      super.onContainerClosed(var1);

      for(int var2 = 0; var2 < 4; ++var2) {
         ItemStack var3 = this.craftMatrix.removeStackFromSlot(var2);
         if (var3 != null) {
            var1.dropPlayerItemWithRandomChoice(var3, false);
         }
      }

      this.craftResult.setInventorySlotContents(0, (ItemStack)null);
   }

   public ContainerPlayer(InventoryPlayer var1, boolean var2, EntityPlayer var3) {
      this.isLocalWorld = var2;
      this.thePlayer = var3;
      this.addSlotToContainer(new SlotCrafting(var1.player, this.craftMatrix, this.craftResult, 0, 144, 36));

      int var4;
      int var5;
      for(var4 = 0; var4 < 2; ++var4) {
         for(var5 = 0; var5 < 2; ++var5) {
            this.addSlotToContainer(new Slot(this.craftMatrix, var5 + var4 * 2, 88 + var5 * 18, 26 + var4 * 18));
         }
      }

      for(var4 = 0; var4 < 4; ++var4) {
         this.addSlotToContainer(new Slot(this, var1, var1.getSizeInventory() - 1 - var4, 8, 8 + var4 * 18, var4) {
            private final int val$k_f;
            final ContainerPlayer this$0;

            public boolean isItemValid(ItemStack var1) {
               return var1 == null ? false : (var1.getItem() instanceof ItemArmor ? ((ItemArmor)var1.getItem()).armorType == this.val$k_f : (var1.getItem() != Item.getItemFromBlock(Blocks.pumpkin) && var1.getItem() != Items.skull ? false : this.val$k_f == 0));
            }

            {
               this.this$0 = var1;
               this.val$k_f = var6;
            }

            public int getSlotStackLimit() {
               return 1;
            }

            public String getSlotTexture() {
               return ItemArmor.EMPTY_SLOT_NAMES[this.val$k_f];
            }
         });
      }

      for(var4 = 0; var4 < 3; ++var4) {
         for(var5 = 0; var5 < 9; ++var5) {
            this.addSlotToContainer(new Slot(var1, var5 + (var4 + 1) * 9, 8 + var5 * 18, 84 + var4 * 18));
         }
      }

      for(var4 = 0; var4 < 9; ++var4) {
         this.addSlotToContainer(new Slot(var1, var4, 8 + var4 * 18, 142));
      }

      this.onCraftMatrixChanged(this.craftMatrix);
   }

   public void onCraftMatrixChanged(IInventory var1) {
      this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.thePlayer.worldObj));
   }
}
