package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemPiston extends ItemBlock {
  public ItemPiston(Block block) {
    super(block);
  }
  
  public int getMetadata(int damage) {
    return 7;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemPiston.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */