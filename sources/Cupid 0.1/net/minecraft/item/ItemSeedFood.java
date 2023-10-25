package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemSeedFood extends ItemFood {
  private Block crops;
  
  private Block soilId;
  
  public ItemSeedFood(int healAmount, float saturation, Block crops, Block soil) {
    super(healAmount, saturation, false);
    this.crops = crops;
    this.soilId = soil;
  }
  
  public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (side != EnumFacing.UP)
      return false; 
    if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
      return false; 
    if (worldIn.getBlockState(pos).getBlock() == this.soilId && worldIn.isAirBlock(pos.up())) {
      worldIn.setBlockState(pos.up(), this.crops.getDefaultState());
      stack.stackSize--;
      return true;
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemSeedFood.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */