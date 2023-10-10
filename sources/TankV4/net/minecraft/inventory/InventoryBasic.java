package net.minecraft.inventory;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class InventoryBasic implements IInventory {
   private String inventoryTitle;
   private int slotsCount;
   private ItemStack[] inventoryContents;
   private List field_70480_d;
   private boolean hasCustomName;

   public int getSizeInventory() {
      return this.slotsCount;
   }

   public void markDirty() {
      if (this.field_70480_d != null) {
         for(int var1 = 0; var1 < this.field_70480_d.size(); ++var1) {
            ((IInvBasic)this.field_70480_d.get(var1)).onInventoryChanged(this);
         }
      }

   }

   public boolean hasCustomName() {
      return this.hasCustomName;
   }

   public int getFieldCount() {
      return 0;
   }

   public boolean isUseableByPlayer(EntityPlayer var1) {
      return true;
   }

   public void func_110132_b(IInvBasic var1) {
      this.field_70480_d.remove(var1);
   }

   public ItemStack decrStackSize(int var1, int var2) {
      if (this.inventoryContents[var1] != null) {
         ItemStack var3;
         if (this.inventoryContents[var1].stackSize <= var2) {
            var3 = this.inventoryContents[var1];
            this.inventoryContents[var1] = null;
            this.markDirty();
            return var3;
         } else {
            var3 = this.inventoryContents[var1].splitStack(var2);
            if (this.inventoryContents[var1].stackSize == 0) {
               this.inventoryContents[var1] = null;
            }

            this.markDirty();
            return var3;
         }
      } else {
         return null;
      }
   }

   public void setInventorySlotContents(int var1, ItemStack var2) {
      this.inventoryContents[var1] = var2;
      if (var2 != null && var2.stackSize > this.getInventoryStackLimit()) {
         var2.stackSize = this.getInventoryStackLimit();
      }

      this.markDirty();
   }

   public ItemStack removeStackFromSlot(int var1) {
      if (this.inventoryContents[var1] != null) {
         ItemStack var2 = this.inventoryContents[var1];
         this.inventoryContents[var1] = null;
         return var2;
      } else {
         return null;
      }
   }

   public void openInventory(EntityPlayer var1) {
   }

   public ItemStack getStackInSlot(int var1) {
      return var1 >= 0 && var1 < this.inventoryContents.length ? this.inventoryContents[var1] : null;
   }

   public IChatComponent getDisplayName() {
      return (IChatComponent)(this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]));
   }

   public void func_110134_a(IInvBasic var1) {
      if (this.field_70480_d == null) {
         this.field_70480_d = Lists.newArrayList();
      }

      this.field_70480_d.add(var1);
   }

   public void closeInventory(EntityPlayer var1) {
   }

   public boolean isItemValidForSlot(int var1, ItemStack var2) {
      return true;
   }

   public void setField(int var1, int var2) {
   }

   public int getField(int var1) {
      return 0;
   }

   public void setCustomName(String var1) {
      this.hasCustomName = true;
      this.inventoryTitle = var1;
   }

   public String getName() {
      return this.inventoryTitle;
   }

   public void clear() {
      for(int var1 = 0; var1 < this.inventoryContents.length; ++var1) {
         this.inventoryContents[var1] = null;
      }

   }

   public ItemStack func_174894_a(ItemStack var1) {
      ItemStack var2 = var1.copy();

      for(int var3 = 0; var3 < this.slotsCount; ++var3) {
         ItemStack var4 = this.getStackInSlot(var3);
         if (var4 == null) {
            this.setInventorySlotContents(var3, var2);
            this.markDirty();
            return null;
         }

         if (ItemStack.areItemsEqual(var4, var2)) {
            int var5 = Math.min(this.getInventoryStackLimit(), var4.getMaxStackSize());
            int var6 = Math.min(var2.stackSize, var5 - var4.stackSize);
            if (var6 > 0) {
               var4.stackSize += var6;
               var2.stackSize -= var6;
               if (var2.stackSize <= 0) {
                  this.markDirty();
                  return null;
               }
            }
         }
      }

      if (var2.stackSize != var1.stackSize) {
         this.markDirty();
      }

      return var2;
   }

   public InventoryBasic(IChatComponent var1, int var2) {
      this(var1.getUnformattedText(), true, var2);
   }

   public InventoryBasic(String var1, boolean var2, int var3) {
      this.inventoryTitle = var1;
      this.hasCustomName = var2;
      this.slotsCount = var3;
      this.inventoryContents = new ItemStack[var3];
   }

   public int getInventoryStackLimit() {
      return 64;
   }
}
