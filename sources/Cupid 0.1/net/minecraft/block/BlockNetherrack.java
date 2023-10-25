package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;

public class BlockNetherrack extends Block {
  public BlockNetherrack() {
    super(Material.rock);
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  public MapColor getMapColor(IBlockState state) {
    return MapColor.netherrackColor;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockNetherrack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */