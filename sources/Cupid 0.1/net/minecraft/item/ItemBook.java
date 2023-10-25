package net.minecraft.item;

public class ItemBook extends Item {
  public boolean isItemTool(ItemStack stack) {
    return (stack.stackSize == 1);
  }
  
  public int getItemEnchantability() {
    return 1;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */