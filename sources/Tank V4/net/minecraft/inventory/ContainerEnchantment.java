package net.minecraft.inventory;

import java.util.List;
import java.util.Random;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ContainerEnchantment extends Container {
   public int[] field_178151_h;
   private World worldPointer;
   private BlockPos position;
   private Random rand;
   public IInventory tableInventory;
   public int xpSeed;
   public int[] enchantLevels;

   private List func_178148_a(ItemStack var1, int var2, int var3) {
      this.rand.setSeed((long)(this.xpSeed + var2));
      List var4 = EnchantmentHelper.buildEnchantmentList(this.rand, var1, var3);
      if (var1.getItem() == Items.book && var4 != null && var4.size() > 1) {
         var4.remove(this.rand.nextInt(var4.size()));
      }

      return var4;
   }

   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack var3 = null;
      Slot var4 = (Slot)this.inventorySlots.get(var2);
      if (var4 != null && var4.getHasStack()) {
         ItemStack var5 = var4.getStack();
         var3 = var5.copy();
         if (var2 == 0) {
            if (!this.mergeItemStack(var5, 2, 38, true)) {
               return null;
            }
         } else if (var2 == 1) {
            if (!this.mergeItemStack(var5, 2, 38, true)) {
               return null;
            }
         } else if (var5.getItem() == Items.dye && EnumDyeColor.byDyeDamage(var5.getMetadata()) == EnumDyeColor.BLUE) {
            if (!this.mergeItemStack(var5, 1, 2, true)) {
               return null;
            }
         } else {
            if (((Slot)this.inventorySlots.get(0)).getHasStack() || !((Slot)this.inventorySlots.get(0)).isItemValid(var5)) {
               return null;
            }

            if (var5.hasTagCompound() && var5.stackSize == 1) {
               ((Slot)this.inventorySlots.get(0)).putStack(var5.copy());
               var5.stackSize = 0;
            } else if (var5.stackSize >= 1) {
               ((Slot)this.inventorySlots.get(0)).putStack(new ItemStack(var5.getItem(), 1, var5.getMetadata()));
               --var5.stackSize;
            }
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
      if (!this.worldPointer.isRemote) {
         for(int var2 = 0; var2 < this.tableInventory.getSizeInventory(); ++var2) {
            ItemStack var3 = this.tableInventory.removeStackFromSlot(var2);
            if (var3 != null) {
               var1.dropPlayerItemWithRandomChoice(var3, false);
            }
         }
      }

   }

   public void onCraftMatrixChanged(IInventory var1) {
      if (var1 == this.tableInventory) {
         ItemStack var2 = var1.getStackInSlot(0);
         int var3;
         if (var2 != null && var2.isItemEnchantable()) {
            if (!this.worldPointer.isRemote) {
               var3 = 0;

               int var4;
               for(var4 = -1; var4 <= 1; ++var4) {
                  for(int var5 = -1; var5 <= 1; ++var5) {
                     if ((var4 != 0 || var5 != 0) && this.worldPointer.isAirBlock(this.position.add(var5, 0, var4)) && this.worldPointer.isAirBlock(this.position.add(var5, 1, var4))) {
                        if (this.worldPointer.getBlockState(this.position.add(var5 * 2, 0, var4 * 2)).getBlock() == Blocks.bookshelf) {
                           ++var3;
                        }

                        if (this.worldPointer.getBlockState(this.position.add(var5 * 2, 1, var4 * 2)).getBlock() == Blocks.bookshelf) {
                           ++var3;
                        }

                        if (var5 != 0 && var4 != 0) {
                           if (this.worldPointer.getBlockState(this.position.add(var5 * 2, 0, var4)).getBlock() == Blocks.bookshelf) {
                              ++var3;
                           }

                           if (this.worldPointer.getBlockState(this.position.add(var5 * 2, 1, var4)).getBlock() == Blocks.bookshelf) {
                              ++var3;
                           }

                           if (this.worldPointer.getBlockState(this.position.add(var5, 0, var4 * 2)).getBlock() == Blocks.bookshelf) {
                              ++var3;
                           }

                           if (this.worldPointer.getBlockState(this.position.add(var5, 1, var4 * 2)).getBlock() == Blocks.bookshelf) {
                              ++var3;
                           }
                        }
                     }
                  }
               }

               this.rand.setSeed((long)this.xpSeed);

               for(var4 = 0; var4 < 3; ++var4) {
                  this.enchantLevels[var4] = EnchantmentHelper.calcItemStackEnchantability(this.rand, var4, var3, var2);
                  this.field_178151_h[var4] = -1;
                  if (this.enchantLevels[var4] < var4 + 1) {
                     this.enchantLevels[var4] = 0;
                  }
               }

               for(var4 = 0; var4 < 3; ++var4) {
                  if (this.enchantLevels[var4] > 0) {
                     List var7 = this.func_178148_a(var2, var4, this.enchantLevels[var4]);
                     if (var7 != null && !var7.isEmpty()) {
                        EnchantmentData var6 = (EnchantmentData)var7.get(this.rand.nextInt(var7.size()));
                        this.field_178151_h[var4] = var6.enchantmentobj.effectId | var6.enchantmentLevel << 8;
                     }
                  }
               }

               this.detectAndSendChanges();
            }
         } else {
            for(var3 = 0; var3 < 3; ++var3) {
               this.enchantLevels[var3] = 0;
               this.field_178151_h[var3] = -1;
            }
         }
      }

   }

   public void detectAndSendChanges() {
      super.detectAndSendChanges();

      for(int var1 = 0; var1 < this.crafters.size(); ++var1) {
         ICrafting var2 = (ICrafting)this.crafters.get(var1);
         var2.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
         var2.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
         var2.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
         var2.sendProgressBarUpdate(this, 3, this.xpSeed & -16);
         var2.sendProgressBarUpdate(this, 4, this.field_178151_h[0]);
         var2.sendProgressBarUpdate(this, 5, this.field_178151_h[1]);
         var2.sendProgressBarUpdate(this, 6, this.field_178151_h[2]);
      }

   }

   public boolean enchantItem(EntityPlayer var1, int var2) {
      ItemStack var3 = this.tableInventory.getStackInSlot(0);
      ItemStack var4 = this.tableInventory.getStackInSlot(1);
      int var5 = var2 + 1;
      if ((var4 == null || var4.stackSize < var5) && !var1.capabilities.isCreativeMode) {
         return false;
      } else if (this.enchantLevels[var2] <= 0 || var3 == null || (var1.experienceLevel < var5 || var1.experienceLevel < this.enchantLevels[var2]) && !var1.capabilities.isCreativeMode) {
         return false;
      } else {
         if (!this.worldPointer.isRemote) {
            List var6 = this.func_178148_a(var3, var2, this.enchantLevels[var2]);
            boolean var7 = var3.getItem() == Items.book;
            if (var6 != null) {
               var1.removeExperienceLevel(var5);
               if (var7) {
                  var3.setItem(Items.enchanted_book);
               }

               for(int var8 = 0; var8 < var6.size(); ++var8) {
                  EnchantmentData var9 = (EnchantmentData)var6.get(var8);
                  if (var7) {
                     Items.enchanted_book.addEnchantment(var3, var9);
                  } else {
                     var3.addEnchantment(var9.enchantmentobj, var9.enchantmentLevel);
                  }
               }

               if (!var1.capabilities.isCreativeMode) {
                  var4.stackSize -= var5;
                  if (var4.stackSize <= 0) {
                     this.tableInventory.setInventorySlotContents(1, (ItemStack)null);
                  }
               }

               var1.triggerAchievement(StatList.field_181739_W);
               this.tableInventory.markDirty();
               this.xpSeed = var1.getXPSeed();
               this.onCraftMatrixChanged(this.tableInventory);
            }
         }

         return true;
      }
   }

   public ContainerEnchantment(InventoryPlayer var1, World var2) {
      this(var1, var2, BlockPos.ORIGIN);
   }

   public int getLapisAmount() {
      ItemStack var1 = this.tableInventory.getStackInSlot(1);
      return var1 == null ? 0 : var1.stackSize;
   }

   public ContainerEnchantment(InventoryPlayer var1, World var2, BlockPos var3) {
      this.tableInventory = new InventoryBasic(this, "Enchant", true, 2) {
         final ContainerEnchantment this$0;

         {
            this.this$0 = var1;
         }

         public void markDirty() {
            super.markDirty();
            this.this$0.onCraftMatrixChanged(this);
         }

         public int getInventoryStackLimit() {
            return 64;
         }
      };
      this.rand = new Random();
      this.enchantLevels = new int[3];
      this.field_178151_h = new int[]{-1, -1, -1};
      this.worldPointer = var2;
      this.position = var3;
      this.xpSeed = var1.player.getXPSeed();
      this.addSlotToContainer(new Slot(this, this.tableInventory, 0, 15, 47) {
         final ContainerEnchantment this$0;

         public boolean isItemValid(ItemStack var1) {
            return true;
         }

         public int getSlotStackLimit() {
            return 1;
         }

         {
            this.this$0 = var1;
         }
      });
      this.addSlotToContainer(new Slot(this, this.tableInventory, 1, 35, 47) {
         final ContainerEnchantment this$0;

         {
            this.this$0 = var1;
         }

         public boolean isItemValid(ItemStack var1) {
            return var1.getItem() == Items.dye && EnumDyeColor.byDyeDamage(var1.getMetadata()) == EnumDyeColor.BLUE;
         }
      });

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

   public boolean canInteractWith(EntityPlayer var1) {
      return this.worldPointer.getBlockState(this.position).getBlock() != Blocks.enchanting_table ? false : var1.getDistanceSq((double)this.position.getX() + 0.5D, (double)this.position.getY() + 0.5D, (double)this.position.getZ() + 0.5D) <= 64.0D;
   }

   public void onCraftGuiOpened(ICrafting var1) {
      super.onCraftGuiOpened(var1);
      var1.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
      var1.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
      var1.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
      var1.sendProgressBarUpdate(this, 3, this.xpSeed & -16);
      var1.sendProgressBarUpdate(this, 4, this.field_178151_h[0]);
      var1.sendProgressBarUpdate(this, 5, this.field_178151_h[1]);
      var1.sendProgressBarUpdate(this, 6, this.field_178151_h[2]);
   }

   public void updateProgressBar(int var1, int var2) {
      if (var1 >= 0 && var1 <= 2) {
         this.enchantLevels[var1] = var2;
      } else if (var1 == 3) {
         this.xpSeed = var2;
      } else if (var1 >= 4 && var1 <= 6) {
         this.field_178151_h[var1 - 4] = var2;
      } else {
         super.updateProgressBar(var1, var2);
      }

   }
}
