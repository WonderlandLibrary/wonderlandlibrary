package net.minecraft.util;

import java.util.Random;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

public class WeightedRandomFishable extends WeightedRandom.Item {
   private float maxDamagePercent;
   private boolean enchantable;
   private final ItemStack returnStack;

   public WeightedRandomFishable setEnchantable() {
      this.enchantable = true;
      return this;
   }

   public ItemStack getItemStack(Random var1) {
      ItemStack var2 = this.returnStack.copy();
      if (this.maxDamagePercent > 0.0F) {
         int var3 = (int)(this.maxDamagePercent * (float)this.returnStack.getMaxDamage());
         int var4 = var2.getMaxDamage() - var1.nextInt(var1.nextInt(var3) + 1);
         if (var4 > var3) {
            var4 = var3;
         }

         if (var4 < 1) {
            var4 = 1;
         }

         var2.setItemDamage(var4);
      }

      if (this.enchantable) {
         EnchantmentHelper.addRandomEnchantment(var1, var2, 30);
      }

      return var2;
   }

   public WeightedRandomFishable(ItemStack var1, int var2) {
      super(var2);
      this.returnStack = var1;
   }

   public WeightedRandomFishable setMaxDamagePercent(float var1) {
      this.maxDamagePercent = var1;
      return this;
   }
}
