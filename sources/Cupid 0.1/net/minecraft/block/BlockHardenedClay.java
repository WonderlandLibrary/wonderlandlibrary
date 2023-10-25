package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;

public class BlockHardenedClay extends Block {
  public BlockHardenedClay() {
    super(Material.rock);
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  public MapColor getMapColor(IBlockState state) {
    return MapColor.adobeColor;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockHardenedClay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */