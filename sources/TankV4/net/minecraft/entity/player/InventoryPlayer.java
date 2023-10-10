package net.minecraft.entity.player;

import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ReportedException;

public class InventoryPlayer implements IInventory {
   public ItemStack[] armorInventory = new ItemStack[4];
   public ItemStack[] mainInventory = new ItemStack[36];
   public EntityPlayer player;
   public int currentItem;
   public boolean inventoryChanged;
   private ItemStack itemStack;

   public String getName() {
      return "container.inventory";
   }

   private int storePartialItemStack(ItemStack var1) {
      Item var2 = var1.getItem();
      int var3 = var1.stackSize;
      int var4 = this.storeItemStack(var1);
      if (var4 < 0) {
         var4 = this.getFirstEmptyStack();
      }

      if (var4 < 0) {
         return var3;
      } else {
         if (this.mainInventory[var4] == null) {
            this.mainInventory[var4] = new ItemStack(var2, 0, var1.getMetadata());
            if (var1.hasTagCompound()) {
               this.mainInventory[var4].setTagCompound((NBTTagCompound)var1.getTagCompound().copy());
            }
         }

         int var5 = var3;
         if (var3 > this.mainInventory[var4].getMaxStackSize() - this.mainInventory[var4].stackSize) {
            var5 = this.mainInventory[var4].getMaxStackSize() - this.mainInventory[var4].stackSize;
         }

         if (var5 > this.getInventoryStackLimit() - this.mainInventory[var4].stackSize) {
            var5 = this.getInventoryStackLimit() - this.mainInventory[var4].stackSize;
         }

         if (var5 == 0) {
            return var3;
         } else {
            var3 -= var5;
            ItemStack var10000 = this.mainInventory[var4];
            var10000.stackSize += var5;
            this.mainInventory[var4].animationsToGo = 5;
            return var3;
         }
      }
   }

   public InventoryPlayer(EntityPlayer var1) {
      this.player = var1;
   }

   public void damageArmor(float var1) {
      var1 /= 4.0F;
      if (var1 < 1.0F) {
         var1 = 1.0F;
      }

      for(int var2 = 0; var2 < this.armorInventory.length; ++var2) {
         if (this.armorInventory[var2] != null && this.armorInventory[var2].getItem() instanceof ItemArmor) {
            this.armorInventory[var2].damageItem((int)var1, this.player);
            if (this.armorInventory[var2].stackSize == 0) {
               this.armorInventory[var2] = null;
            }
         }
      }

   }

   public NBTTagList writeToNBT(NBTTagList var1) {
      int var2;
      NBTTagCompound var3;
      for(var2 = 0; var2 < this.mainInventory.length; ++var2) {
         if (this.mainInventory[var2] != null) {
            var3 = new NBTTagCompound();
            var3.setByte("Slot", (byte)var2);
            this.mainInventory[var2].writeToNBT(var3);
            var1.appendTag(var3);
         }
      }

      for(var2 = 0; var2 < this.armorInventory.length; ++var2) {
         if (this.armorInventory[var2] != null) {
            var3 = new NBTTagCompound();
            var3.setByte("Slot", (byte)(var2 + 100));
            this.armorInventory[var2].writeToNBT(var3);
            var1.appendTag(var3);
         }
      }

      return var1;
   }

   public ItemStack getCurrentItem() {
      return this.currentItem < 9 && this.currentItem >= 0 ? this.mainInventory[this.currentItem] : null;
   }

   public boolean hasItem(Item var1) {
      int var2 = this.getInventorySlotContainItem(var1);
      return var2 >= 0;
   }

   public void readFromNBT(NBTTagList var1) {
      this.mainInventory = new ItemStack[36];
      this.armorInventory = new ItemStack[4];

      for(int var2 = 0; var2 < var1.tagCount(); ++var2) {
         NBTTagCompound var3 = var1.getCompoundTagAt(var2);
         int var4 = var3.getByte("Slot") & 255;
         ItemStack var5 = ItemStack.loadItemStackFromNBT(var3);
         if (var5 != null) {
            if (var4 >= 0 && var4 < this.mainInventory.length) {
               this.mainInventory[var4] = var5;
            }

            if (var4 >= 100 && var4 < this.armorInventory.length + 100) {
               this.armorInventory[var4 - 100] = var5;
            }
         }
      }

   }

   public ItemStack decrStackSize(int var1, int var2) {
      ItemStack[] var3 = this.mainInventory;
      if (var1 >= this.mainInventory.length) {
         var3 = this.armorInventory;
         var1 -= this.mainInventory.length;
      }

      if (var3[var1] != null) {
         ItemStack var4;
         if (var3[var1].stackSize <= var2) {
            var4 = var3[var1];
            var3[var1] = null;
            return var4;
         } else {
            var4 = var3[var1].splitStack(var2);
            if (var3[var1].stackSize == 0) {
               var3[var1] = null;
            }

            return var4;
         }
      } else {
         return null;
      }
   }

   private int getInventorySlotContainItem(Item var1) {
      for(int var2 = 0; var2 < this.mainInventory.length; ++var2) {
         if (this.mainInventory[var2] != null && this.mainInventory[var2].getItem() == var1) {
            return var2;
         }
      }

      return -1;
   }

   public ItemStack armorItemInSlot(int var1) {
      return this.armorInventory[var1];
   }

   public boolean isItemValidForSlot(int var1, ItemStack var2) {
      return true;
   }

   public float getStrVsBlock(Block var1) {
      float var2 = 1.0F;
      if (this.mainInventory[this.currentItem] != null) {
         var2 *= this.mainInventory[this.currentItem].getStrVsBlock(var1);
      }

      return var2;
   }

   public boolean hasCustomName() {
      return false;
   }

   public static int getHotbarSize() {
      return 9;
   }

   public int clearMatchingItems(Item var1, int var2, int var3, NBTTagCompound var4) {
      int var5 = 0;

      ItemStack var10000;
      int var6;
      ItemStack var7;
      int var8;
      for(var6 = 0; var6 < this.mainInventory.length; ++var6) {
         var7 = this.mainInventory[var6];
         if (var7 != null && (var1 == null || var7.getItem() == var1) && (var2 <= -1 || var7.getMetadata() == var2) && (var4 == null || NBTUtil.func_181123_a(var4, var7.getTagCompound(), true))) {
            var8 = var3 <= 0 ? var7.stackSize : Math.min(var3 - var5, var7.stackSize);
            var5 += var8;
            if (var3 != 0) {
               var10000 = this.mainInventory[var6];
               var10000.stackSize -= var8;
               if (this.mainInventory[var6].stackSize == 0) {
                  this.mainInventory[var6] = null;
               }

               if (var3 > 0 && var5 >= var3) {
                  return var5;
               }
            }
         }
      }

      for(var6 = 0; var6 < this.armorInventory.length; ++var6) {
         var7 = this.armorInventory[var6];
         if (var7 != null && (var1 == null || var7.getItem() == var1) && (var2 <= -1 || var7.getMetadata() == var2) && (var4 == null || NBTUtil.func_181123_a(var4, var7.getTagCompound(), false))) {
            var8 = var3 <= 0 ? var7.stackSize : Math.min(var3 - var5, var7.stackSize);
            var5 += var8;
            if (var3 != 0) {
               var10000 = this.armorInventory[var6];
               var10000.stackSize -= var8;
               if (this.armorInventory[var6].stackSize == 0) {
                  this.armorInventory[var6] = null;
               }

               if (var3 > 0 && var5 >= var3) {
                  return var5;
               }
            }
         }
      }

      if (this.itemStack != null) {
         if (var1 != null && this.itemStack.getItem() != var1) {
            return var5;
         }

         if (var2 > -1 && this.itemStack.getMetadata() != var2) {
            return var5;
         }

         if (var4 != null && !NBTUtil.func_181123_a(var4, this.itemStack.getTagCompound(), false)) {
            return var5;
         }

         var6 = var3 <= 0 ? this.itemStack.stackSize : Math.min(var3 - var5, this.itemStack.stackSize);
         var5 += var6;
         if (var3 != 0) {
            var10000 = this.itemStack;
            var10000.stackSize -= var6;
            if (this.itemStack.stackSize == 0) {
               this.itemStack = null;
            }

            if (var3 > 0 && var5 >= var3) {
               return var5;
            }
         }
      }

      return var5;
   }

   public void setField(int var1, int var2) {
   }

   private int getInventorySlotContainItemAndDamage(Item var1, int var2) {
      for(int var3 = 0; var3 < this.mainInventory.length; ++var3) {
         if (this.mainInventory[var3] != null && this.mainInventory[var3].getItem() == var1 && this.mainInventory[var3].getMetadata() == var2) {
            return var3;
         }
      }

      return -1;
   }

   public boolean isUseableByPlayer(EntityPlayer var1) {
      return this.player.isDead ? false : var1.getDistanceSqToEntity(this.player) <= 64.0D;
   }

   public boolean addItemStackToInventory(ItemStack var1) {
      if (var1 != null && var1.stackSize != 0 && var1.getItem() != null) {
         try {
            int var2;
            if (var1.isItemDamaged()) {
               var2 = this.getFirstEmptyStack();
               if (var2 >= 0) {
                  this.mainInventory[var2] = ItemStack.copyItemStack(var1);
                  this.mainInventory[var2].animationsToGo = 5;
                  var1.stackSize = 0;
                  return true;
               } else if (this.player.capabilities.isCreativeMode) {
                  var1.stackSize = 0;
                  return true;
               } else {
                  return false;
               }
            } else {
               do {
                  var2 = var1.stackSize;
                  var1.stackSize = this.storePartialItemStack(var1);
               } while(var1.stackSize > 0 && var1.stackSize < var2);

               if (var1.stackSize == var2 && this.player.capabilities.isCreativeMode) {
                  var1.stackSize = 0;
                  return true;
               } else {
                  return var1.stackSize < var2;
               }
            }
         } catch (Throwable var5) {
            CrashReport var3 = CrashReport.makeCrashReport(var5, "Adding item to inventory");
            CrashReportCategory var4 = var3.makeCategory("Item being added");
            var4.addCrashSection("Item ID", Item.getIdFromItem(var1.getItem()));
            var4.addCrashSection("Item data", var1.getMetadata());
            var4.addCrashSectionCallable("Item name", new Callable(this, var1) {
               private final ItemStack val$itemStackIn;
               final InventoryPlayer this$0;

               public Object call() throws Exception {
                  return this.call();
               }

               {
                  this.this$0 = var1;
                  this.val$itemStackIn = var2;
               }

               public String call() throws Exception {
                  return this.val$itemStackIn.getDisplayName();
               }
            });
            throw new ReportedException(var3);
         }
      } else {
         return false;
      }
   }

   public ItemStack getStackInSlot(int var1) {
      ItemStack[] var2 = this.mainInventory;
      if (var1 >= var2.length) {
         var1 -= var2.length;
         var2 = this.armorInventory;
      }

      return var2[var1];
   }

   public void setItemStack(ItemStack var1) {
      this.itemStack = var1;
   }

   public int getField(int var1) {
      return 0;
   }

   public void openInventory(EntityPlayer var1) {
   }

   public void clear() {
      int var1;
      for(var1 = 0; var1 < this.mainInventory.length; ++var1) {
         this.mainInventory[var1] = null;
      }

      for(var1 = 0; var1 < this.armorInventory.length; ++var1) {
         this.armorInventory[var1] = null;
      }

   }

   public void closeInventory(EntityPlayer var1) {
   }

   public void decrementAnimations() {
      for(int var1 = 0; var1 < this.mainInventory.length; ++var1) {
         if (this.mainInventory[var1] != null) {
            this.mainInventory[var1].updateAnimation(this.player.worldObj, this.player, var1, this.currentItem == var1);
         }
      }

   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public void dropAllItems() {
      int var1;
      for(var1 = 0; var1 < this.mainInventory.length; ++var1) {
         if (this.mainInventory[var1] != null) {
            this.player.dropItem(this.mainInventory[var1], true, false);
            this.mainInventory[var1] = null;
         }
      }

      for(var1 = 0; var1 < this.armorInventory.length; ++var1) {
         if (this.armorInventory[var1] != null) {
            this.player.dropItem(this.armorInventory[var1], true, false);
            this.armorInventory[var1] = null;
         }
      }

   }

   public int getFirstEmptyStack() {
      for(int var1 = 0; var1 < this.mainInventory.length; ++var1) {
         if (this.mainInventory[var1] == null) {
            return var1;
         }
      }

      return -1;
   }

   public int getTotalArmorValue() {
      int var1 = 0;

      for(int var2 = 0; var2 < this.armorInventory.length; ++var2) {
         if (this.armorInventory[var2] != null && this.armorInventory[var2].getItem() instanceof ItemArmor) {
            int var3 = ((ItemArmor)this.armorInventory[var2].getItem()).damageReduceAmount;
            var1 += var3;
         }
      }

      return var1;
   }

   public IChatComponent getDisplayName() {
      return (IChatComponent)(this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]));
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public void markDirty() {
      this.inventoryChanged = true;
   }

   public boolean consumeInventoryItem(Item var1) {
      int var2 = this.getInventorySlotContainItem(var1);
      if (var2 < 0) {
         return false;
      } else {
         if (--this.mainInventory[var2].stackSize <= 0) {
            this.mainInventory[var2] = null;
         }

         return true;
      }
   }

   public int getFieldCount() {
      return 0;
   }

   public void copyInventory(InventoryPlayer var1) {
      int var2;
      for(var2 = 0; var2 < this.mainInventory.length; ++var2) {
         this.mainInventory[var2] = ItemStack.copyItemStack(var1.mainInventory[var2]);
      }

      for(var2 = 0; var2 < this.armorInventory.length; ++var2) {
         this.armorInventory[var2] = ItemStack.copyItemStack(var1.armorInventory[var2]);
      }

      this.currentItem = var1.currentItem;
   }

   public void changeCurrentItem(int var1) {
      if (var1 > 0) {
         var1 = 1;
      }

      if (var1 < 0) {
         var1 = -1;
      }

      for(this.currentItem -= var1; this.currentItem < 0; this.currentItem += 9) {
      }

      while(this.currentItem >= 9) {
         this.currentItem -= 9;
      }

   }

   private int storeItemStack(ItemStack var1) {
      for(int var2 = 0; var2 < this.mainInventory.length; ++var2) {
         if (this.mainInventory[var2] != null && this.mainInventory[var2].getItem() == var1.getItem() && this.mainInventory[var2].isStackable() && this.mainInventory[var2].stackSize < this.mainInventory[var2].getMaxStackSize() && this.mainInventory[var2].stackSize < this.getInventoryStackLimit() && (!this.mainInventory[var2].getHasSubtypes() || this.mainInventory[var2].getMetadata() == var1.getMetadata()) && ItemStack.areItemStackTagsEqual(this.mainInventory[var2], var1)) {
            return var2;
         }
      }

      return -1;
   }

   public boolean hasItemStack(ItemStack var1) {
      int var2;
      for(var2 = 0; var2 < this.armorInventory.length; ++var2) {
         if (this.armorInventory[var2] != null && this.armorInventory[var2].isItemEqual(var1)) {
            return true;
         }
      }

      for(var2 = 0; var2 < this.mainInventory.length; ++var2) {
         if (this.mainInventory[var2] != null && this.mainInventory[var2].isItemEqual(var1)) {
            return true;
         }
      }

      return false;
   }

   public void setInventorySlotContents(int var1, ItemStack var2) {
      ItemStack[] var3 = this.mainInventory;
      if (var1 >= var3.length) {
         var1 -= var3.length;
         var3 = this.armorInventory;
      }

      var3[var1] = var2;
   }

   public int getSizeInventory() {
      return this.mainInventory.length + 4;
   }

   public ItemStack removeStackFromSlot(int var1) {
      ItemStack[] var2 = this.mainInventory;
      if (var1 >= this.mainInventory.length) {
         var2 = this.armorInventory;
         var1 -= this.mainInventory.length;
      }

      if (var2[var1] != null) {
         ItemStack var3 = var2[var1];
         var2[var1] = null;
         return var3;
      } else {
         return null;
      }
   }

   public void setCurrentItem(Item var1, int var2, boolean var3, boolean var4) {
      ItemStack var5 = this.getCurrentItem();
      int var6 = var3 ? this.getInventorySlotContainItemAndDamage(var1, var2) : this.getInventorySlotContainItem(var1);
      if (var6 >= 0 && var6 < 9) {
         this.currentItem = var6;
      } else if (var4 && var1 != null) {
         int var7 = this.getFirstEmptyStack();
         if (var7 >= 0 && var7 < 9) {
            this.currentItem = var7;
         }

         if (var5 == null || !var5.isItemEnchantable() || this.getInventorySlotContainItemAndDamage(var5.getItem(), var5.getItemDamage()) != this.currentItem) {
            int var8 = this.getInventorySlotContainItemAndDamage(var1, var2);
            int var9;
            if (var8 >= 0) {
               var9 = this.mainInventory[var8].stackSize;
               this.mainInventory[var8] = this.mainInventory[this.currentItem];
            } else {
               var9 = 1;
            }

            this.mainInventory[this.currentItem] = new ItemStack(var1, var9, var2);
         }
      }

   }

   public boolean canHeldItemHarvest(Block var1) {
      if (var1.getMaterial().isToolNotRequired()) {
         return true;
      } else {
         ItemStack var2 = this.getStackInSlot(this.currentItem);
         return var2 != null ? var2.canHarvestBlock(var1) : false;
      }
   }
}
