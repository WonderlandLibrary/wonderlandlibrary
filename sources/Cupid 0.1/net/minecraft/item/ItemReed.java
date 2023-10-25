package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemReed extends Item {
  private Block block;
  
  public ItemReed(Block block) {
    this.block = block;
  }
  
  public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    Block block = iblockstate.getBlock();
    if (block == Blocks.snow_layer && ((Integer)iblockstate.getValue((IProperty)BlockSnow.LAYERS)).intValue() < 1) {
      side = EnumFacing.UP;
    } else if (!block.isReplaceable(worldIn, pos)) {
      pos = pos.offset(side);
    } 
    if (!playerIn.canPlayerEdit(pos, side, stack))
      return false; 
    if (stack.stackSize == 0)
      return false; 
    if (worldIn.canBlockBePlaced(this.block, pos, false, side, (Entity)null, stack)) {
      IBlockState iblockstate1 = this.block.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, 0, (EntityLivingBase)playerIn);
      if (worldIn.setBlockState(pos, iblockstate1, 3)) {
        iblockstate1 = worldIn.getBlockState(pos);
        if (iblockstate1.getBlock() == this.block) {
          ItemBlock.setTileEntityNBT(worldIn, playerIn, pos, stack);
          iblockstate1.getBlock().onBlockPlacedBy(worldIn, pos, iblockstate1, (EntityLivingBase)playerIn, stack);
        } 
        worldIn.playSoundEffect((pos.getX() + 0.5F), (pos.getY() + 0.5F), (pos.getZ() + 0.5F), this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getFrequency() * 0.8F);
        stack.stackSize--;
        return true;
      } 
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemReed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */