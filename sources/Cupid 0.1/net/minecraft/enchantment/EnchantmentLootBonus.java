package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentLootBonus extends Enchantment {
  protected EnchantmentLootBonus(int p_i45767_1_, ResourceLocation p_i45767_2_, int p_i45767_3_, EnumEnchantmentType p_i45767_4_) {
    super(p_i45767_1_, p_i45767_2_, p_i45767_3_, p_i45767_4_);
    if (p_i45767_4_ == EnumEnchantmentType.DIGGER) {
      setName("lootBonusDigger");
    } else if (p_i45767_4_ == EnumEnchantmentType.FISHING_ROD) {
      setName("lootBonusFishing");
    } else {
      setName("lootBonus");
    } 
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return 15 + (enchantmentLevel - 1) * 9;
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return super.getMinEnchantability(enchantmentLevel) + 50;
  }
  
  public int getMaxLevel() {
    return 3;
  }
  
  public boolean canApplyTogether(Enchantment ench) {
    return (super.canApplyTogether(ench) && ench.effectId != silkTouch.effectId);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\enchantment\EnchantmentLootBonus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */