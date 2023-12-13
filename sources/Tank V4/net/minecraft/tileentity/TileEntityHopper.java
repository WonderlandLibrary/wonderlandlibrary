package net.minecraft.tileentity;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHopper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileEntityHopper extends TileEntityLockable implements IHopper, ITickable {
   private int transferCooldown = -1;
   private ItemStack[] inventory = new ItemStack[5];
   private String customName;

   public int getInventoryStackLimit() {
      return 64;
   }

   public double getYPos() {
      return (double)this.pos.getY() + 0.5D;
   }

   public void setCustomName(String var1) {
      this.customName = var1;
   }

   private static ItemStack insertStack(IInventory var0, ItemStack var1, int var2, EnumFacing var3) {
      ItemStack var4 = var0.getStackInSlot(var2);
      if (var3 != false) {
         boolean var5 = false;
         if (var4 == null) {
            var0.setInventorySlotContents(var2, var1);
            var1 = null;
            var5 = true;
         } else if (var1 != false) {
            int var6 = var1.getMaxStackSize() - var4.stackSize;
            int var7 = Math.min(var1.stackSize, var6);
            var1.stackSize -= var7;
            var4.stackSize += var7;
            var5 = var7 > 0;
         }

         if (var5) {
            if (var0 instanceof TileEntityHopper) {
               TileEntityHopper var8 = (TileEntityHopper)var0;
               if (var8 != false) {
                  var8.setTransferCooldown(8);
               }

               var0.markDirty();
            }

            var0.markDirty();
         }
      }

      return var1;
   }

   public static IInventory getHopperInventory(IHopper var0) {
      return getInventoryAtPosition(var0.getWorld(), var0.getXPos(), var0.getYPos() + 1.0D, var0.getZPos());
   }

   public int getSizeInventory() {
      return this.inventory.length;
   }

   public int getFieldCount() {
      return 0;
   }

   public void setInventorySlotContents(int var1, ItemStack var2) {
      this.inventory[var1] = var2;
      if (var2 != null && var2.stackSize > this.getInventoryStackLimit()) {
         var2.stackSize = this.getInventoryStackLimit();
      }

   }

   public ItemStack decrStackSize(int var1, int var2) {
      if (this.inventory[var1] != null) {
         ItemStack var3;
         if (this.inventory[var1].stackSize <= var2) {
            var3 = this.inventory[var1];
            this.inventory[var1] = null;
            return var3;
         } else {
            var3 = this.inventory[var1].splitStack(var2);
            if (this.inventory[var1].stackSize == 0) {
               this.inventory[var1] = null;
            }

            return var3;
         }
      } else {
         return null;
      }
   }

   public void setTransferCooldown(int var1) {
      this.transferCooldown = var1;
   }

   private boolean transferItemsOut() {
      IInventory var1 = this.getInventoryForHopperTransfer();
      if (var1 == null) {
         return false;
      } else {
         EnumFacing var2 = BlockHopper.getFacing(this.getBlockMetadata()).getOpposite();
         if (var2 != false) {
            return false;
         } else {
            for(int var3 = 0; var3 < this.getSizeInventory(); ++var3) {
               if (this.getStackInSlot(var3) != null) {
                  ItemStack var4 = this.getStackInSlot(var3).copy();
                  ItemStack var5 = putStackInInventoryAllSlots(var1, this.decrStackSize(var3, 1), var2);
                  if (var5 == null || var5.stackSize == 0) {
                     var1.markDirty();
                     return true;
                  }

                  this.setInventorySlotContents(var3, var4);
               }
            }

            return false;
         }
      }
   }

   public void closeInventory(EntityPlayer var1) {
   }

   private IInventory getInventoryForHopperTransfer() {
      EnumFacing var1 = BlockHopper.getFacing(this.getBlockMetadata());
      return getInventoryAtPosition(this.getWorld(), (double)(this.pos.getX() + var1.getFrontOffsetX()), (double)(this.pos.getY() + var1.getFrontOffsetY()), (double)(this.pos.getZ() + var1.getFrontOffsetZ()));
   }

   public void update() {
      if (this.worldObj != null && !this.worldObj.isRemote) {
         --this.transferCooldown;
         if (this > 0) {
            this.setTransferCooldown(0);
            this.updateHopper();
         }
      }

   }

   public int getField(int var1) {
      return 0;
   }

   public boolean updateHopper() {
      // $FF: Couldn't be decompiled
   }

   public ItemStack removeStackFromSlot(int var1) {
      if (this.inventory[var1] != null) {
         ItemStack var2 = this.inventory[var1];
         this.inventory[var1] = null;
         return var2;
      } else {
         return null;
      }
   }

   public boolean isItemValidForSlot(int var1, ItemStack var2) {
      return true;
   }

   public boolean isUseableByPlayer(EntityPlayer var1) {
      return this.worldObj.getTileEntity(this.pos) != this ? false : var1.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
   }

   public static List func_181556_a(World var0, double var1, double var3, double var5) {
      return var0.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(var1 - 0.5D, var3 - 0.5D, var5 - 0.5D, var1 + 0.5D, var3 + 0.5D, var5 + 0.5D), EntitySelectors.selectAnything);
   }

   public void writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(var1);
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.inventory.length; ++var3) {
         if (this.inventory[var3] != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setByte("Slot", (byte)var3);
            this.inventory[var3].writeToNBT(var4);
            var2.appendTag(var4);
         }
      }

      var1.setTag("Items", var2);
      var1.setInteger("TransferCooldown", this.transferCooldown);
      if (this != false) {
         var1.setString("CustomName", this.customName);
      }

   }

   public String getGuiID() {
      return "minecraft:hopper";
   }

   public static IInventory getInventoryAtPosition(World var0, double var1, double var3, double var5) {
      Object var7 = null;
      int var8 = MathHelper.floor_double(var1);
      int var9 = MathHelper.floor_double(var3);
      int var10 = MathHelper.floor_double(var5);
      BlockPos var11 = new BlockPos(var8, var9, var10);
      Block var12 = var0.getBlockState(var11).getBlock();
      if (var12.hasTileEntity()) {
         TileEntity var13 = var0.getTileEntity(var11);
         if (var13 instanceof IInventory) {
            var7 = (IInventory)var13;
            if (var7 instanceof TileEntityChest && var12 instanceof BlockChest) {
               var7 = ((BlockChest)var12).getLockableContainer(var0, var11);
            }
         }
      }

      if (var7 == null) {
         List var15 = var0.getEntitiesInAABBexcluding((Entity)null, new AxisAlignedBB(var1 - 0.5D, var3 - 0.5D, var5 - 0.5D, var1 + 0.5D, var3 + 0.5D, var5 + 0.5D), EntitySelectors.selectInventories);
         if (var15.size() > 0) {
            var7 = (IInventory)var15.get(var0.rand.nextInt(var15.size()));
         }
      }

      return (IInventory)var7;
   }

   public double getXPos() {
      return (double)this.pos.getX() + 0.5D;
   }

   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(var1);
      NBTTagList var2 = var1.getTagList("Items", 10);
      this.inventory = new ItemStack[this.getSizeInventory()];
      if (var1.hasKey("CustomName", 8)) {
         this.customName = var1.getString("CustomName");
      }

      this.transferCooldown = var1.getInteger("TransferCooldown");

      for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
         NBTTagCompound var4 = var2.getCompoundTagAt(var3);
         byte var5 = var4.getByte("Slot");
         if (var5 >= 0 && var5 < this.inventory.length) {
            this.inventory[var5] = ItemStack.loadItemStackFromNBT(var4);
         }
      }

   }

   public void setField(int var1, int var2) {
   }

   public void markDirty() {
      super.markDirty();
   }

   public ItemStack getStackInSlot(int var1) {
      return this.inventory[var1];
   }

   public double getZPos() {
      return (double)this.pos.getZ() + 0.5D;
   }

   public void clear() {
      for(int var1 = 0; var1 < this.inventory.length; ++var1) {
         this.inventory[var1] = null;
      }

   }

   public void openInventory(EntityPlayer var1) {
   }

   public static ItemStack putStackInInventoryAllSlots(IInventory var0, ItemStack var1, EnumFacing var2) {
      if (var0 instanceof ISidedInventory && var2 != null) {
         ISidedInventory var7 = (ISidedInventory)var0;
         int[] var8 = var7.getSlotsForFace(var2);

         for(int var5 = 0; var5 < var8.length && var1 != null && var1.stackSize > 0; ++var5) {
            var1 = insertStack(var0, var1, var8[var5], var2);
         }
      } else {
         int var3 = var0.getSizeInventory();

         for(int var4 = 0; var4 < var3 && var1 != null && var1.stackSize > 0; ++var4) {
            var1 = insertStack(var0, var1, var4, var2);
         }
      }

      if (var1 != null && var1.stackSize == 0) {
         var1 = null;
      }

      return var1;
   }

   public String getName() {
      return this != null ? this.customName : "container.hopper";
   }

   public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
      return new ContainerHopper(var1, this, var2);
   }
}
