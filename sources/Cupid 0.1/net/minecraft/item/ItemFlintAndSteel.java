package net.minecraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemFlintAndSteel extends Item {
  public ItemFlintAndSteel() {
    this.maxStackSize = 1;
    setMaxDamage(64);
    setCreativeTab(CreativeTabs.tabTools);
  }
  
  public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
    pos = pos.offset(side);
    if (!playerIn.canPlayerEdit(pos, side, stack))
      return false; 
    if (worldIn.getBlockState(pos).getBlock().getMaterial() == Material.air) {
      worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "fire.ignite", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
      worldIn.setBlockState(pos, Blocks.fire.getDefaultState());
    } 
    stack.damageItem(1, (EntityLivingBase)playerIn);
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemFlintAndSteel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */