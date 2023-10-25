package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockBookshelf extends Block {
  public BlockBookshelf() {
    super(Material.wood);
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  public int quantityDropped(Random random) {
    return 3;
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.book;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockBookshelf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */