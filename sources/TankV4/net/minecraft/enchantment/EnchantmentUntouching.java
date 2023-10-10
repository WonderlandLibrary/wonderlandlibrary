package net.minecraft.enchantment;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentUntouching extends Enchantment {
   public int getMinEnchantability(int var1) {
      return 15;
   }

   protected EnchantmentUntouching(int var1, ResourceLocation var2, int var3) {
      super(var1, var2, var3, EnumEnchantmentType.DIGGER);
      this.setName("untouching");
   }

   public int getMaxEnchantability(int var1) {
      return super.getMinEnchantability(var1) + 50;
   }

   public boolean canApplyTogether(Enchantment var1) {
      return super.canApplyTogether(var1) && var1.effectId != fortune.effectId;
   }

   public boolean canApply(ItemStack var1) {
      return var1.getItem() == Items.shears ? true : super.canApply(var1);
   }

   public int getMaxLevel() {
      return 1;
   }
}
