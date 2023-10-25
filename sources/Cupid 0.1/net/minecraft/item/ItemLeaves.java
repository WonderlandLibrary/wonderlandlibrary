package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;

public class ItemLeaves extends ItemBlock {
  private final BlockLeaves leaves;
  
  public ItemLeaves(BlockLeaves block) {
    super((Block)block);
    this.leaves = block;
    setMaxDamage(0);
    setHasSubtypes(true);
  }
  
  public int getMetadata(int damage) {
    return damage | 0x4;
  }
  
  public int getColorFromItemStack(ItemStack stack, int renderPass) {
    return this.leaves.getRenderColor(this.leaves.getStateFromMeta(stack.getMetadata()));
  }
  
  public String getUnlocalizedName(ItemStack stack) {
    return getUnlocalizedName() + "." + this.leaves.getWoodType(stack.getMetadata()).getUnlocalizedName();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemLeaves.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */