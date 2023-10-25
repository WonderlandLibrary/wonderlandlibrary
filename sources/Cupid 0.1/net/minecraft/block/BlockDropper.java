package net.minecraft.block;

import net.minecraft.block.properties.IProperty;
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
  private final IBehaviorDispenseItem dropBehavior = (IBehaviorDispenseItem)new BehaviorDefaultDispenseItem();
  
  protected IBehaviorDispenseItem getBehavior(ItemStack stack) {
    return this.dropBehavior;
  }
  
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return (TileEntity)new TileEntityDropper();
  }
  
  protected void dispense(World worldIn, BlockPos pos) {
    BlockSourceImpl blocksourceimpl = new BlockSourceImpl(worldIn, pos);
    TileEntityDispenser tileentitydispenser = blocksourceimpl.<TileEntityDispenser>getBlockTileEntity();
    if (tileentitydispenser != null) {
      int i = tileentitydispenser.getDispenseSlot();
      if (i < 0) {
        worldIn.playAuxSFX(1001, pos, 0);
      } else {
        ItemStack itemstack = tileentitydispenser.getStackInSlot(i);
        if (itemstack != null) {
          ItemStack itemstack1;
          EnumFacing enumfacing = (EnumFacing)worldIn.getBlockState(pos).getValue((IProperty)FACING);
          BlockPos blockpos = pos.offset(enumfacing);
          IInventory iinventory = TileEntityHopper.getInventoryAtPosition(worldIn, blockpos.getX(), blockpos.getY(), blockpos.getZ());
          if (iinventory == null) {
            itemstack1 = this.dropBehavior.dispense(blocksourceimpl, itemstack);
            if (itemstack1 != null && itemstack1.stackSize <= 0)
              itemstack1 = null; 
          } else {
            itemstack1 = TileEntityHopper.putStackInInventoryAllSlots(iinventory, itemstack.copy().splitStack(1), enumfacing.getOpposite());
            if (itemstack1 == null) {
              itemstack1 = itemstack.copy();
              if (--itemstack1.stackSize <= 0)
                itemstack1 = null; 
            } else {
              itemstack1 = itemstack.copy();
            } 
          } 
          tileentitydispenser.setInventorySlotContents(i, itemstack1);
        } 
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockDropper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */