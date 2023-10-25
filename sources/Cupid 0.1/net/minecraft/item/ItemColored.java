package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemColored extends ItemBlock {
  private final Block coloredBlock;
  
  private String[] subtypeNames;
  
  public ItemColored(Block block, boolean hasSubtypes) {
    super(block);
    this.coloredBlock = block;
    if (hasSubtypes) {
      setMaxDamage(0);
      setHasSubtypes(true);
    } 
  }
  
  public int getColorFromItemStack(ItemStack stack, int renderPass) {
    return this.coloredBlock.getRenderColor(this.coloredBlock.getStateFromMeta(stack.getMetadata()));
  }
  
  public int getMetadata(int damage) {
    return damage;
  }
  
  public ItemColored setSubtypeNames(String[] names) {
    this.subtypeNames = names;
    return this;
  }
  
  public String getUnlocalizedName(ItemStack stack) {
    if (this.subtypeNames == null)
      return super.getUnlocalizedName(stack); 
    int i = stack.getMetadata();
    return (i >= 0 && i < this.subtypeNames.length) ? (super.getUnlocalizedName(stack) + "." + this.subtypeNames[i]) : super.getUnlocalizedName(stack);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemColored.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */