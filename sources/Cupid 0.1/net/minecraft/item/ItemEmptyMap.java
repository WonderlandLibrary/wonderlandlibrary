package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapData;

public class ItemEmptyMap extends ItemMapBase {
  protected ItemEmptyMap() {
    setCreativeTab(CreativeTabs.tabMisc);
  }
  
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    ItemStack itemstack = new ItemStack(Items.filled_map, 1, worldIn.getUniqueDataId("map"));
    String s = "map_" + itemstack.getMetadata();
    MapData mapdata = new MapData(s);
    worldIn.setItemData(s, (WorldSavedData)mapdata);
    mapdata.scale = 0;
    mapdata.calculateMapCenter(playerIn.posX, playerIn.posZ, mapdata.scale);
    mapdata.dimension = (byte)worldIn.provider.getDimensionId();
    mapdata.markDirty();
    itemStackIn.stackSize--;
    if (itemStackIn.stackSize <= 0)
      return itemstack; 
    if (!playerIn.inventory.addItemStackToInventory(itemstack.copy()))
      playerIn.dropPlayerItemWithRandomChoice(itemstack, false); 
    playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
    return itemStackIn;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemEmptyMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */