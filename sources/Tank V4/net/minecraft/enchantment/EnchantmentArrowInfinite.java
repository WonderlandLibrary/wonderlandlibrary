package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentArrowInfinite extends Enchantment {
   public int getMinEnchantability(int var1) {
      return 20;
   }

   public EnchantmentArrowInfinite(int var1, ResourceLocation var2, int var3) {
      super(var1, var2, var3, EnumEnchantmentType.BOW);
      this.setName("arrowInfinite");
   }

   public int getMaxEnchantability(int var1) {
      return 50;
   }

   public int getMaxLevel() {
      return 1;
   }
}
