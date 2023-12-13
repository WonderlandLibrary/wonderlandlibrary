package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;

public class BlockNetherBrick extends Block {
   public MapColor getMapColor(IBlockState var1) {
      return MapColor.netherrackColor;
   }

   public BlockNetherBrick() {
      super(Material.rock);
      this.setCreativeTab(CreativeTabs.tabBlock);
   }
}
