package net.minecraft.enchantment;

import java.util.Random;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentDurability extends Enchantment {
   public int getMinEnchantability(int var1) {
      return 5 + (var1 - 1) * 8;
   }

   public boolean canApply(ItemStack var1) {
      return var1.isItemStackDamageable() ? true : super.canApply(var1);
   }

   public int getMaxEnchantability(int var1) {
      return super.getMinEnchantability(var1) + 50;
   }

   public static boolean negateDamage(ItemStack var0, int var1, Random var2) {
      return var0.getItem() instanceof ItemArmor && var2.nextFloat() < 0.6F ? false : var2.nextInt(var1 + 1) > 0;
   }

   public int getMaxLevel() {
      return 3;
   }

   protected EnchantmentDurability(int var1, ResourceLocation var2, int var3) {
      super(var1, var2, var3, EnumEnchantmentType.BREAKABLE);
      this.setName("durability");
   }
}
