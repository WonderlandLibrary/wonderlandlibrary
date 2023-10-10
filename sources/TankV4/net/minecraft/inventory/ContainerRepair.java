package net.minecraft.inventory;

import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContainerRepair extends Container {
   private int materialCost;
   private World theWorld;
   private static final Logger logger = LogManager.getLogger();
   private IInventory outputSlot;
   private final EntityPlayer thePlayer;
   private IInventory inputSlots;
   private String repairedItemName;
   public int maximumCost;
   private BlockPos selfPosition;

   public void onContainerClosed(EntityPlayer var1) {
      super.onContainerClosed(var1);
      if (!this.theWorld.isRemote) {
         for(int var2 = 0; var2 < this.inputSlots.getSizeInventory(); ++var2) {
            ItemStack var3 = this.inputSlots.removeStackFromSlot(var2);
            if (var3 != null) {
               var1.dropPlayerItemWithRandomChoice(var3, false);
            }
         }
      }

   }

   public ContainerRepair(InventoryPlayer var1, World var2, EntityPlayer var3) {
      this(var1, var2, BlockPos.ORIGIN, var3);
   }

   public void updateRepairOutput() {
      boolean var1 = false;
      boolean var2 = true;
      boolean var3 = true;
      boolean var4 = true;
      boolean var5 = true;
      boolean var6 = true;
      boolean var7 = true;
      ItemStack var8 = this.inputSlots.getStackInSlot(0);
      this.maximumCost = 1;
      int var9 = 0;
      byte var10 = 0;
      byte var11 = 0;
      if (var8 == null) {
         this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
         this.maximumCost = 0;
      } else {
         ItemStack var12 = var8.copy();
         ItemStack var13 = this.inputSlots.getStackInSlot(1);
         Map var14 = EnchantmentHelper.getEnchantments(var12);
         boolean var15 = false;
         int var26 = var10 + var8.getRepairCost() + (var13 == null ? 0 : var13.getRepairCost());
         this.materialCost = 0;
         int var16;
         if (var13 != null) {
            var15 = var13.getItem() == Items.enchanted_book && Items.enchanted_book.getEnchantments(var13).tagCount() > 0;
            int var17;
            int var18;
            if (var12.isItemStackDamageable() && var12.getItem().getIsRepairable(var8, var13)) {
               var16 = Math.min(var12.getItemDamage(), var12.getMaxDamage() / 4);
               if (var16 <= 0) {
                  this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
                  this.maximumCost = 0;
                  return;
               }

               for(var17 = 0; var16 > 0 && var17 < var13.stackSize; ++var17) {
                  var18 = var12.getItemDamage() - var16;
                  var12.setItemDamage(var18);
                  ++var9;
                  var16 = Math.min(var12.getItemDamage(), var12.getMaxDamage() / 4);
               }

               this.materialCost = var17;
            } else {
               if (!var15 && (var12.getItem() != var13.getItem() || !var12.isItemStackDamageable())) {
                  this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
                  this.maximumCost = 0;
                  return;
               }

               int var20;
               if (var12.isItemStackDamageable() && !var15) {
                  var16 = var8.getMaxDamage() - var8.getItemDamage();
                  var17 = var13.getMaxDamage() - var13.getItemDamage();
                  var18 = var17 + var12.getMaxDamage() * 12 / 100;
                  int var19 = var16 + var18;
                  var20 = var12.getMaxDamage() - var19;
                  if (var20 < 0) {
                     var20 = 0;
                  }

                  if (var20 < var12.getMetadata()) {
                     var12.setItemDamage(var20);
                     var9 += 2;
                  }
               }

               Map var27 = EnchantmentHelper.getEnchantments(var13);
               Iterator var28 = var27.keySet().iterator();

               label149:
               while(true) {
                  Enchantment var29;
                  do {
                     if (!var28.hasNext()) {
                        break label149;
                     }

                     var18 = (Integer)var28.next();
                     var29 = Enchantment.getEnchantmentById(var18);
                  } while(var29 == null);

                  var20 = var14.containsKey(var18) ? (Integer)var14.get(var18) : 0;
                  int var21 = (Integer)var27.get(var18);
                  int var22;
                  if (var20 == var21) {
                     ++var21;
                     var22 = var21;
                  } else {
                     var22 = Math.max(var21, var20);
                  }

                  var21 = var22;
                  boolean var23 = var29.canApply(var8);
                  if (this.thePlayer.capabilities.isCreativeMode || var8.getItem() == Items.enchanted_book) {
                     var23 = true;
                  }

                  Iterator var24 = var14.keySet().iterator();

                  int var25;
                  while(var24.hasNext()) {
                     var25 = (Integer)var24.next();
                     if (var25 != var18 && !var29.canApplyTogether(Enchantment.getEnchantmentById(var25))) {
                        var23 = false;
                        ++var9;
                     }
                  }

                  if (var23) {
                     if (var22 > var29.getMaxLevel()) {
                        var21 = var29.getMaxLevel();
                     }

                     var14.put(var18, var21);
                     var25 = 0;
                     switch(var29.getWeight()) {
                     case 1:
                        var25 = 8;
                        break;
                     case 2:
                        var25 = 4;
                     case 3:
                     case 4:
                     case 6:
                     case 7:
                     case 8:
                     case 9:
                     default:
                        break;
                     case 5:
                        var25 = 2;
                        break;
                     case 10:
                        var25 = 1;
                     }

                     if (var15) {
                        var25 = Math.max(1, var25 / 2);
                     }

                     var9 += var25 * var21;
                  }
               }
            }
         }

         if (StringUtils.isBlank(this.repairedItemName)) {
            if (var8.hasDisplayName()) {
               var11 = 1;
               var9 += var11;
               var12.clearCustomName();
            }
         } else if (!this.repairedItemName.equals(var8.getDisplayName())) {
            var11 = 1;
            var9 += var11;
            var12.setStackDisplayName(this.repairedItemName);
         }

         this.maximumCost = var26 + var9;
         if (var9 <= 0) {
            var12 = null;
         }

         if (var11 == var9 && var11 > 0 && this.maximumCost >= 40) {
            this.maximumCost = 39;
         }

         if (this.maximumCost >= 40 && !this.thePlayer.capabilities.isCreativeMode) {
            var12 = null;
         }

         if (var12 != null) {
            var16 = var12.getRepairCost();
            if (var13 != null && var16 < var13.getRepairCost()) {
               var16 = var13.getRepairCost();
            }

            var16 = var16 * 2 + 1;
            var12.setRepairCost(var16);
            EnchantmentHelper.setEnchantments(var14, var12);
         }

         this.outputSlot.setInventorySlotContents(0, var12);
         this.detectAndSendChanges();
      }

   }

   public void updateProgressBar(int var1, int var2) {
      if (var1 == 0) {
         this.maximumCost = var2;
      }

   }

   static IInventory access$0(ContainerRepair var0) {
      return var0.inputSlots;
   }

   public ContainerRepair(InventoryPlayer var1, World var2, BlockPos var3, EntityPlayer var4) {
      this.outputSlot = new InventoryCraftResult();
      this.inputSlots = new InventoryBasic(this, "Repair", true, 2) {
         final ContainerRepair this$0;

         public void markDirty() {
            super.markDirty();
            this.this$0.onCraftMatrixChanged(this);
         }

         {
            this.this$0 = var1;
         }
      };
      this.selfPosition = var3;
      this.theWorld = var2;
      this.thePlayer = var4;
      this.addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
      this.addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
      this.addSlotToContainer(new Slot(this, this.outputSlot, 2, 134, 47, var2, var3) {
         final ContainerRepair this$0;
         private final BlockPos val$blockPosIn;
         private final World val$worldIn;

         public void onPickupFromSlot(EntityPlayer var1, ItemStack var2) {
            if (!var1.capabilities.isCreativeMode) {
               var1.addExperienceLevel(-this.this$0.maximumCost);
            }

            ContainerRepair.access$0(this.this$0).setInventorySlotContents(0, (ItemStack)null);
            if (ContainerRepair.access$1(this.this$0) > 0) {
               ItemStack var3 = ContainerRepair.access$0(this.this$0).getStackInSlot(1);
               if (var3 != null && var3.stackSize > ContainerRepair.access$1(this.this$0)) {
                  var3.stackSize -= ContainerRepair.access$1(this.this$0);
                  ContainerRepair.access$0(this.this$0).setInventorySlotContents(1, var3);
               } else {
                  ContainerRepair.access$0(this.this$0).setInventorySlotContents(1, (ItemStack)null);
               }
            } else {
               ContainerRepair.access$0(this.this$0).setInventorySlotContents(1, (ItemStack)null);
            }

            this.this$0.maximumCost = 0;
            IBlockState var6 = this.val$worldIn.getBlockState(this.val$blockPosIn);
            if (!var1.capabilities.isCreativeMode && !this.val$worldIn.isRemote && var6.getBlock() == Blocks.anvil && var1.getRNG().nextFloat() < 0.12F) {
               int var4 = (Integer)var6.getValue(BlockAnvil.DAMAGE);
               ++var4;
               if (var4 > 2) {
                  this.val$worldIn.setBlockToAir(this.val$blockPosIn);
                  this.val$worldIn.playAuxSFX(1020, this.val$blockPosIn, 0);
               } else {
                  this.val$worldIn.setBlockState(this.val$blockPosIn, var6.withProperty(BlockAnvil.DAMAGE, var4), 2);
                  this.val$worldIn.playAuxSFX(1021, this.val$blockPosIn, 0);
               }
            } else if (!this.val$worldIn.isRemote) {
               this.val$worldIn.playAuxSFX(1021, this.val$blockPosIn, 0);
            }

         }

         public boolean canTakeStack(EntityPlayer var1) {
            return (var1.capabilities.isCreativeMode || var1.experienceLevel >= this.this$0.maximumCost) && this.this$0.maximumCost > 0 && this.getHasStack();
         }

         public boolean isItemValid(ItemStack var1) {
            return false;
         }

         {
            this.this$0 = var1;
            this.val$worldIn = var6;
            this.val$blockPosIn = var7;
         }
      });

      int var5;
      for(var5 = 0; var5 < 3; ++var5) {
         for(int var6 = 0; var6 < 9; ++var6) {
            this.addSlotToContainer(new Slot(var1, var6 + var5 * 9 + 9, 8 + var6 * 18, 84 + var5 * 18));
         }
      }

      for(var5 = 0; var5 < 9; ++var5) {
         this.addSlotToContainer(new Slot(var1, var5, 8 + var5 * 18, 142));
      }

   }

   public boolean canInteractWith(EntityPlayer var1) {
      return this.theWorld.getBlockState(this.selfPosition).getBlock() != Blocks.anvil ? false : var1.getDistanceSq((double)this.selfPosition.getX() + 0.5D, (double)this.selfPosition.getY() + 0.5D, (double)this.selfPosition.getZ() + 0.5D) <= 64.0D;
   }

   public void onCraftGuiOpened(ICrafting var1) {
      super.onCraftGuiOpened(var1);
      var1.sendProgressBarUpdate(this, 0, this.maximumCost);
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
            if (var2 >= 3 && var2 < 39 && !this.mergeItemStack(var5, 0, 2, false)) {
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

   public void updateItemName(String var1) {
      this.repairedItemName = var1;
      if (this.getSlot(2).getHasStack()) {
         ItemStack var2 = this.getSlot(2).getStack();
         if (StringUtils.isBlank(var1)) {
            var2.clearCustomName();
         } else {
            var2.setStackDisplayName(this.repairedItemName);
         }
      }

      this.updateRepairOutput();
   }

   public void onCraftMatrixChanged(IInventory var1) {
      super.onCraftMatrixChanged(var1);
      if (var1 == this.inputSlots) {
         this.updateRepairOutput();
      }

   }

   static int access$1(ContainerRepair var0) {
      return var0.materialCost;
   }
}
