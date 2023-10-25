package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemHoe extends Item {
  protected Item.ToolMaterial theToolMaterial;
  
  public ItemHoe(Item.ToolMaterial material) {
    this.theToolMaterial = material;
    this.maxStackSize = 1;
    setMaxDamage(material.getMaxUses());
    setCreativeTab(CreativeTabs.tabTools);
  }
  
  public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
      return false; 
    IBlockState iblockstate = worldIn.getBlockState(pos);
    Block block = iblockstate.getBlock();
    if (side != EnumFacing.DOWN && worldIn.getBlockState(pos.up()).getBlock().getMaterial() == Material.air) {
      if (block == Blocks.grass)
        return useHoe(stack, playerIn, worldIn, pos, Blocks.farmland.getDefaultState()); 
      if (block == Blocks.dirt)
        switch ((BlockDirt.DirtType)iblockstate.getValue((IProperty)BlockDirt.VARIANT)) {
          case DIRT:
            return useHoe(stack, playerIn, worldIn, pos, Blocks.farmland.getDefaultState());
          case COARSE_DIRT:
            return useHoe(stack, playerIn, worldIn, pos, Blocks.dirt.getDefaultState().withProperty((IProperty)BlockDirt.VARIANT, (Comparable)BlockDirt.DirtType.DIRT));
        }  
    } 
    return false;
  }
  
  protected boolean useHoe(ItemStack stack, EntityPlayer player, World worldIn, BlockPos target, IBlockState newState) {
    worldIn.playSoundEffect((target.getX() + 0.5F), (target.getY() + 0.5F), (target.getZ() + 0.5F), (newState.getBlock()).stepSound.getStepSound(), ((newState.getBlock()).stepSound.getVolume() + 1.0F) / 2.0F, (newState.getBlock()).stepSound.getFrequency() * 0.8F);
    if (worldIn.isRemote)
      return true; 
    worldIn.setBlockState(target, newState);
    stack.damageItem(1, (EntityLivingBase)player);
    return true;
  }
  
  public boolean isFull3D() {
    return true;
  }
  
  public String getMaterialName() {
    return this.theToolMaterial.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemHoe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */