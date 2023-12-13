package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class ItemEmptyMap extends ItemMapBase {
   protected ItemEmptyMap() {
      this.setCreativeTab(CreativeTabs.tabMisc);
   }

   public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
      ItemStack var4 = new ItemStack(Items.filled_map, 1, var2.getUniqueDataId("map"));
      String var5 = "map_" + var4.getMetadata();
      MapData var6 = new MapData(var5);
      var2.setItemData(var5, var6);
      var6.scale = 0;
      var6.calculateMapCenter(var3.posX, var3.posZ, var6.scale);
      var6.dimension = (byte)var2.provider.getDimensionId();
      var6.markDirty();
      --var1.stackSize;
      if (var1.stackSize <= 0) {
         return var4;
      } else {
         if (!var3.inventory.addItemStackToInventory(var4.copy())) {
            var3.dropPlayerItemWithRandomChoice(var4, false);
         }

         var3.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
         return var1;
      }
   }
}
