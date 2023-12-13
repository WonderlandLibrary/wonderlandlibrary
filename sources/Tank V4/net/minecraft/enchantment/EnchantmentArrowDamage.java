package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentArrowDamage extends Enchantment {
   public int getMinEnchantability(int var1) {
      return 1 + (var1 - 1) * 10;
   }

   public int getMaxEnchantability(int var1) {
      return this.getMinEnchantability(var1) + 15;
   }

   public EnchantmentArrowDamage(int var1, ResourceLocation var2, int var3) {
      super(var1, var2, var3, EnumEnchantmentType.BOW);
      this.setName("arrowDamage");
   }

   public int getMaxLevel() {
      return 5;
   }
}
