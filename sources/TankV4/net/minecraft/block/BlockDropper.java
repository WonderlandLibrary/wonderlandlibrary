package net.minecraft.block;

import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockDropper extends BlockDispenser {
   private final IBehaviorDispenseItem dropBehavior = new BehaviorDefaultDispenseItem();

   protected void dispense(World var1, BlockPos var2) {
      BlockSourceImpl var3 = new BlockSourceImpl(var1, var2);
      TileEntityDispenser var4 = (TileEntityDispenser)var3.getBlockTileEntity();
      if (var4 != null) {
         int var5 = var4.getDispenseSlot();
         if (var5 < 0) {
            var1.playAuxSFX(1001, var2, 0);
         } else {
            ItemStack var6 = var4.getStackInSlot(var5);
            if (var6 != null) {
               EnumFacing var7 = (EnumFacing)var1.getBlockState(var2).getValue(FACING);
               BlockPos var8 = var2.offset(var7);
               IInventory var9 = TileEntityHopper.getInventoryAtPosition(var1, (double)var8.getX(), (double)var8.getY(), (double)var8.getZ());
               ItemStack var10;
               if (var9 == null) {
                  var10 = this.dropBehavior.dispense(var3, var6);
                  if (var10 != null && var10.stackSize <= 0) {
                     var10 = null;
                  }
               } else {
                  var10 = TileEntityHopper.putStackInInventoryAllSlots(var9, var6.copy().splitStack(1), var7.getOpposite());
                  if (var10 == null) {
                     var10 = var6.copy();
                     if (--var10.stackSize <= 0) {
                        var10 = null;
                     }
                  } else {
                     var10 = var6.copy();
                  }
               }

               var4.setInventorySlotContents(var5, var10);
            }
         }
      }

   }

   protected IBehaviorDispenseItem getBehavior(ItemStack var1) {
      return this.dropBehavior;
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityDropper();
   }
}
