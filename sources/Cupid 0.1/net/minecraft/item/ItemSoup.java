package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.world.World;

public class ItemSoup extends ItemFood {
  public ItemSoup(int healAmount) {
    super(healAmount, false);
    setMaxStackSize(1);
  }
  
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
    super.onItemUseFinish(stack, worldIn, playerIn);
    return new ItemStack(Items.bowl);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemSoup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */