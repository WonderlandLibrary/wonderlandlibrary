package net.minecraft.tileentity;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntityDispenser extends TileEntityLockable implements IInventory {
   private ItemStack[] stacks = new ItemStack[9];
   private static final Random RNG = new Random();
   protected String customName;

   public void openInventory(EntityPlayer var1) {
   }

   public int getFieldCount() {
      return 0;
   }

   public int getSizeInventory() {
      return 9;
   }

   public String getName() {
      return this != false ? this.customName : "container.dispenser";
   }

   public boolean isItemValidForSlot(int var1, ItemStack var2) {
      return true;
   }

   public void closeInventory(EntityPlayer var1) {
   }

   public void writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(var1);
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.stacks.length; ++var3) {
         if (this.stacks[var3] != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setByte("Slot", (byte)var3);
            this.stacks[var3].writeToNBT(var4);
            var2.appendTag(var4);
         }
      }

      var1.setTag("Items", var2);
      if (this != false) {
         var1.setString("CustomName", this.customName);
      }

   }

   public boolean isUseableByPlayer(EntityPlayer var1) {
      return this.worldObj.getTileEntity(this.pos) != this ? false : var1.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
   }

   public int addItemStack(ItemStack var1) {
      for(int var2 = 0; var2 < this.stacks.length; ++var2) {
         if (this.stacks[var2] == null || this.stacks[var2].getItem() == null) {
            this.setInventorySlotContents(var2, var1);
            return var2;
         }
      }

      return -1;
   }

   public void setInventorySlotContents(int var1, ItemStack var2) {
      this.stacks[var1] = var2;
      if (var2 != null && var2.stackSize > this.getInventoryStackLimit()) {
         var2.stackSize = this.getInventoryStackLimit();
      }

      this.markDirty();
   }

   public int getField(int var1) {
      return 0;
   }

   public ItemStack getStackInSlot(int var1) {
      return this.stacks[var1];
   }

   public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
      return new ContainerDispenser(var1, this);
   }

   public void setField(int var1, int var2) {
   }

   public ItemStack decrStackSize(int var1, int var2) {
      if (this.stacks[var1] != null) {
         ItemStack var3;
         if (this.stacks[var1].stackSize <= var2) {
            var3 = this.stacks[var1];
            this.stacks[var1] = null;
            this.markDirty();
            return var3;
         } else {
            var3 = this.stacks[var1].splitStack(var2);
            if (this.stacks[var1].stackSize == 0) {
               this.stacks[var1] = null;
            }

            this.markDirty();
            return var3;
         }
      } else {
         return null;
      }
   }

   public ItemStack removeStackFromSlot(int var1) {
      if (this.stacks[var1] != null) {
         ItemStack var2 = this.stacks[var1];
         this.stacks[var1] = null;
         return var2;
      } else {
         return null;
      }
   }

   public String getGuiID() {
      return "minecraft:dispenser";
   }

   public void setCustomName(String var1) {
      this.customName = var1;
   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public int getDispenseSlot() {
      int var1 = -1;
      int var2 = 1;

      for(int var3 = 0; var3 < this.stacks.length; ++var3) {
         if (this.stacks[var3] != null && RNG.nextInt(var2++) == 0) {
            var1 = var3;
         }
      }

      return var1;
   }

   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(var1);
      NBTTagList var2 = var1.getTagList("Items", 10);
      this.stacks = new ItemStack[this.getSizeInventory()];

      for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
         NBTTagCompound var4 = var2.getCompoundTagAt(var3);
         int var5 = var4.getByte("Slot") & 255;
         if (var5 >= 0 && var5 < this.stacks.length) {
            this.stacks[var5] = ItemStack.loadItemStackFromNBT(var4);
         }
      }

      if (var1.hasKey("CustomName", 8)) {
         this.customName = var1.getString("CustomName");
      }

   }

   public void clear() {
      for(int var1 = 0; var1 < this.stacks.length; ++var1) {
         this.stacks[var1] = null;
      }

   }
}
