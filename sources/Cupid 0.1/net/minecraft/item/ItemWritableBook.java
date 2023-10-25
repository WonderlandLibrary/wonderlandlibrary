package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemWritableBook extends Item {
  public ItemWritableBook() {
    setMaxStackSize(1);
  }
  
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    playerIn.displayGUIBook(itemStackIn);
    playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
    return itemStackIn;
  }
  
  public static boolean isNBTValid(NBTTagCompound nbt) {
    if (nbt == null)
      return false; 
    if (!nbt.hasKey("pages", 9))
      return false; 
    NBTTagList nbttaglist = nbt.getTagList("pages", 8);
    for (int i = 0; i < nbttaglist.tagCount(); i++) {
      String s = nbttaglist.getStringTagAt(i);
      if (s == null)
        return false; 
      if (s.length() > 32767)
        return false; 
    } 
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemWritableBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */