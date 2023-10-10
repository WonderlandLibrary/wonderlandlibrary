package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockClay extends Block {
   public BlockClay() {
      super(Material.clay);
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public int quantityDropped(Random var1) {
      return 4;
   }

   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.clay_ball;
   }
}
