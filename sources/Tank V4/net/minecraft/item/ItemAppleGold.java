package net.minecraft.item;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemAppleGold extends ItemFood {
   public void getSubItems(Item var1, CreativeTabs var2, List var3) {
      var3.add(new ItemStack(var1, 1, 0));
      var3.add(new ItemStack(var1, 1, 1));
   }

   public boolean hasEffect(ItemStack var1) {
      return var1.getMetadata() > 0;
   }

   protected void onFoodEaten(ItemStack var1, World var2, EntityPlayer var3) {
      if (!var2.isRemote) {
         var3.addPotionEffect(new PotionEffect(Potion.absorption.id, 2400, 0));
      }

      if (var1.getMetadata() > 0) {
         if (!var2.isRemote) {
            var3.addPotionEffect(new PotionEffect(Potion.regeneration.id, 600, 4));
            var3.addPotionEffect(new PotionEffect(Potion.resistance.id, 6000, 0));
            var3.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 6000, 0));
         }
      } else {
         super.onFoodEaten(var1, var2, var3);
      }

   }

   public ItemAppleGold(int var1, float var2, boolean var3) {
      super(var1, var2, var3);
      this.setHasSubtypes(true);
   }

   public EnumRarity getRarity(ItemStack var1) {
      return var1.getMetadata() == 0 ? EnumRarity.RARE : EnumRarity.EPIC;
   }
}
