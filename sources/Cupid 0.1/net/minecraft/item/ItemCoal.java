package net.minecraft.item;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;

public class ItemCoal extends Item {
  public ItemCoal() {
    setHasSubtypes(true);
    setMaxDamage(0);
    setCreativeTab(CreativeTabs.tabMaterials);
  }
  
  public String getUnlocalizedName(ItemStack stack) {
    return (stack.getMetadata() == 1) ? "item.charcoal" : "item.coal";
  }
  
  public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
    subItems.add(new ItemStack(itemIn, 1, 0));
    subItems.add(new ItemStack(itemIn, 1, 1));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemCoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */